package com.telus.crd.assessment.batch;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.mail.MailException;

// RTCA1.6, using the CollectionSummarizationService and CollectionBillingAccountData defined in crd-batch
//import com.telus.collections.treatment.service.dto.CollectionBillingAccountData;
//import com.telus.collections.treatment.service.CollectionSummarizationService;

import com.telus.crd.assessment.util.CollectionSummarizationService;

import com.telus.crd.assessment.batch.domain.AbstractBillingAccountRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountAgencyRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountCollectionRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountDepositRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauDtlRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileFraudRecord;
import com.telus.crd.assessment.batch.domain.CustomerRecord;
import com.telus.crd.assessment.batch.dto.CollectionBillingAccountData;
import com.telus.crd.assessment.util.TemplateUtil;
import com.telus.credit.domain.CreditValue;
//import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.common.AdjudicationResult;
import com.telus.credit.domain.common.BureauInformation;
import com.telus.credit.domain.common.CreditAddress;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.common.CreditCardCode;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.CreditDecision;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.common.DriverLicense;
import com.telus.credit.domain.common.FraudWarning;
import com.telus.credit.domain.common.HealthCard;
import com.telus.credit.domain.common.Passport;
import com.telus.credit.domain.common.ProvincialIdCard;
import com.telus.credit.domain.common.PersonName;
import com.telus.credit.domain.common.PersonalInfo;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.domain.common.RiskIndicator;
import com.telus.credit.domain.common.ProductCategoryQualification;
import com.telus.credit.domain.common.ScoreCardAttribute;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.formletters.framework.batch.io.LineRecordWriter;
import com.telus.framework.config.ConfigContext;
import com.telus.framework.mail.MailBean;
import com.telus.framework.mail.MailSender;
import com.telus.framework.security.SecurityContext;
import com.telus.credit.domain.deposit.DepositItem;
import com.telus.credit.domain.deposit.DepositItemList;
//import com.telus.credit.domain.deposit.AccountReceivableDepositDataList;
//import com.telus.credit.domain.deposit.AccountReceivableDepositDetail;
//import com.telus.tmi.xmlschema.xsd.customer.basetypes.creditdeposittypes_v1.AccountReceivableDepositDataList;
//import com.telus.tmi.xmlschema.xsd.customer.basetypes.creditdeposittypes_v1.AccountReceivableDepositDetail;
//import com.telus.tmi.xmlschema.xsd.customer.customer.wirelinecollectionmanagementtypes_v1.CustomerCollectionData;
//import com.telus.tmi.xmlschema.xsd.customer.customer.wirelinecreditprofilemanagementtypes_v2.ConsumerCreditProfile;
//import com.telus.tmi.xmlschema.xsd.customer.customer.wirelinecreditprofilemanagementtypes_v2.CreditWorthiness;

// this class is in crda-wsdl-4.0.0-SNAPSHOT.jar
import com.telus.credit.domain.collection.CustomerCollectionData;
//Note: in RTCA1.5, there are two CustomerCollectionData, another one is in 
//      com.telus.collections.treatment.service.dto.CustomerCollectionData (not use in RTCA1.6)

import com.telus.credit.domain.creditprofile.CreditWorthiness;
import com.telus.credit.domain.creditprofile.ConsumerCreditProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.dto.AdditionalCollectionData;
import com.telus.credit.util.ThreadLocalUtil;

public class MonthlyUpDownModuleHelper
{
	private static final Log s_log = LogFactory.getLog(MonthlyUpDownModuleHelper.class);
	
    // spring injected
    private String m_emailTo;
    private String m_emailFrom;
    private String m_emailSubject;
    private String m_emailBody;
    private final int RISK_IND_NO_HIT_THIN_FILE = 1;
    private final int RISK_IND_TRUE_THIN_FILE = 2;
    private final int RISK_IND_HIGH_RISK_THIN_FILE = 3;
    private final int RISK_IND_TEMP_SIN = 4;
    private final int RISK_IND_UNDER_AGE = 5;
    private final int RISK_IND_BANKRUPTCY = 6;
    private final int RISK_IND_PRIMARY_RISK = 7;
    private final int RISK_IND_SECONDARY_RISK = 8;
    private final int RISK_IND_WRITEOFF_RISK = 9;
    private static final String S_CRDA_BATCH_USER ="CRDA-BATCH";
    private final String BLANK = "";
    //private static final String RESULT_REASON_CODE = "NO_CHANGE";
    @Autowired
    @Qualifier("errorReportWriter")
    private LineRecordWriter m_errorReportWriter;

    @Autowired
    private CollectionSummarizationService m_collectionSummarizationSvc;

    private String getDataSourceId() {
    	// Credit Assessment inref pds has no subscriptions, temperary using creditassementbatch app id
    	return (String.valueOf(SecurityContext.getSystemSourceId() <= 0 ? "1497" : SecurityContext.getSystemSourceId()));
    }
    public AuditInfo newAuditInfo()
    {
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId(SecurityContext.getPrincipal().getName());
        auditInfo.setOriginatorApplicationId(getDataSourceId());
        return auditInfo;
    }
    /////
    public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo  newPfdAuditInfo()
    {
    	com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo auditInfo = new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo();
        auditInfo.setUserId(SecurityContext.getPrincipal().getName());
        auditInfo.setOriginatorApplicationId(getDataSourceId());
        return auditInfo;
    }
    public CreditAssessmentTransaction newCrdTransactionBulk(CreditValue cv, long customerId)
    {
    	CreditAssessmentTransaction trans = new CreditAssessmentTransaction();
    
    	//trans.setChannelID(cv.);
    	trans.setCommentTxt(cv.getComment());
    	//trans.setCreditAssessmentDataSourceCd(cv.);
    	//trans.setCreditAssessmentDate(cv.);
    	if(cv.getCarId() !=null)
    	    trans.setCreditAssessmentID(cv.getCarId());
    	
    	//trans.setCreditAssessmentStatus(cv.);
    	//trans.setCreditAssessmentStatusDate(result.getCreditAssessmentStatusDate());
    	//trans.setCreditAssessmentStatusReasonCd(cv.);
    	trans.setCreditAssessmentSubTypeCd(cv.getCreditAssessmentSubTypeCd());
    	trans.setCreditAssessmentTypeCd(cv.getCreditAssessmentTypeCd());
    	trans.setCreditBureauReportInd(cv.getCreditBureauReportInd());
    	//trans.setCreditBureauReportSourceCd(cv.g);
    	trans.setCreditClass(cv.getCreditClassCd());
        com.telus.credit.wlnprfldmgt.domain.CreditDecision dsc = new   com.telus.credit.wlnprfldmgt.domain.CreditDecision();
    	
    	
    	dsc.setCreditDecisionCd(cv.getCreditDecisionCd());    		   
    	dsc.setCreditDecisionMessage(cv.getCreditDecisionMsgTxt());
    	trans.setCreditDecisionCd(dsc);
    	
    	com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult aResult = new com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult();
    	
    		
    	aResult.setAssessmentMessageCd(cv.getAssessmentMsgCd());
    		//aResult.setAssessmentResultCd(cv.);
    		//aResult.setAssessmentResultReasonCd(cv.);
    	List<com.telus.credit.wlnprfldmgt.domain.BureauInformation> burList =  new ArrayList<com.telus.credit.wlnprfldmgt.domain.BureauInformation>();
    		/*if(cv..getBureauInformationList() != null)
    		{
    			
    		    for(BureauInformation br  : re.getBureauInformationList() )
    		    {
    		    	com.telus.credit.wlnprfldmgt.domain.BureauInformation bre = new com.telus.credit.wlnprfldmgt.domain.BureauInformation();
    		    	if(br != null)
    		    	{
    		    	    bre.setBureauCd(br.getBureauCd());
    		    	    bre.setBureauPriority(br.getBureauPriority());
    		    	    bre.setBureauType(br.getBureauType());
    		    	}
    		    	burList.add(bre);
    		    }
    		*/	
    		
    	
    		//aResult.setBureauInformationList(burList);
    	aResult.setCreditValueCd(cv.getCreditValueCode());
    	aResult.setDecisionCd(cv.getCreditDecisionCd());
    	if(cv.getDepositAmount() != null){
    		BigDecimal dm = new BigDecimal(cv.getDepositAmount().doubleValue());
    		aResult.setDepositAmt(dm);
    		trans.setDepositAmt(dm);
    	}
    	
    	aResult.setFraudIndicatorCd(cv.getFraudIndicatorCd());
    	aResult.setFraudMessageCdList(cv.getFraudMessageCodeList());
    	com.telus.credit.wlnprfldmgt.domain.ProductCategoryQualification prdq = new com.telus.credit.wlnprfldmgt.domain.ProductCategoryQualification();
    	List<com.telus.credit.wlnprfldmgt.domain.ProductCategory> prList = new ArrayList<com.telus.credit.wlnprfldmgt.domain.ProductCategory>(); 
    	if(cv.getProductCatQualification()!= null)
    	{
    		prdq.setBoltOnInd(cv.getProductCatQualification().getBoltOn());
    		List<com.telus.credit.domain.ProductCategory> prctList = cv.getProductCatQualification().getProductCategoryList();
    		for(com.telus.credit.domain.ProductCategory pr  : prctList )
    	    {
    		    com.telus.credit.wlnprfldmgt.domain.ProductCategory prg = new com.telus.credit.wlnprfldmgt.domain.ProductCategory();
    		    if(pr != null)
    		    {
    		    	 prg.setCategoryCd(pr.getCreditApprovedProductCategoryCd());
    		    	 prg.setQualified(pr.getProductQualificationIndicator());
    		    }
    		    prList.add(prg);
    		}
    		prdq.setProductCategoryList(prList);
    	}
    	aResult.setProductCategoryQualification(prdq );
    	
    	trans.setCreditDecisioningResult(aResult);
        
    	trans.setCreditScoreCd(cv.getCreditScoreNumber());
    	trans.setCreditScoreTypeCd(cv.getCreditValueDetailTypeCd());
    	trans.setCustomerID(customerId);
    	trans.setUserID(cv.getCreateUserId());
    	return trans;
    } 
    public CreditAssessmentTransaction newCrdTransaction(CreditAssessmentTransactionResult result)
    {
    	CreditAssessmentTransaction trans = new CreditAssessmentTransaction();
    	trans.setChannelID(result.getChannelID());
    	trans.setCommentTxt(result.getCommentTxt());
    	trans.setCreditAssessmentDataSourceCd(result.getCreditAssessmentDataSourceCd());
    	trans.setCreditAssessmentDate(result.getCreditAssessmentDate());
    	trans.setCreditAssessmentID(result.getCreditAssessmentID());
    	trans.setCreditAssessmentStatus(result.getCreditAssessmentStatus());
    	trans.setCreditAssessmentStatusDate(result.getCreditAssessmentStatusDate());
    	trans.setCreditAssessmentStatusReasonCd(result.getCreditAssessmentStatusReasonCd());
    	trans.setCreditAssessmentSubTypeCd(result.getCreditAssessmentSubTypeCd());
    	trans.setCreditAssessmentTypeCd(result.getCreditAssessmentTypeCd());
    	trans.setCreditBureauReportInd(result.isCreditBureauReportInd());
    	trans.setCreditBureauReportSourceCd(result.getCreditBureauReportSourceCd());
    	trans.setCreditClass(result.getCreditClass());
        com.telus.credit.wlnprfldmgt.domain.CreditDecision dsc = new   com.telus.credit.wlnprfldmgt.domain.CreditDecision();
    	
    	//TODO credit decision message
    	if(result.getCreditDecisionCd() !=null)
    	{
    		dsc.setCreditDecisionCd(result.getCreditDecisionCd().getCreditDecisionCd());    		   
    	    dsc.setCreditDecisionMessage(result.getCreditDecisionCd().getCreditDecisionMessage());
    	  
    	}
    	trans.setCreditDecisionCd(dsc);
    	com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult aResult = new com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult();
    	CreditAssessmentResult re =result.getCreditDecisioningResult(); //.getCreditDecisioningResult();
    	if (re != null)
    	{
    		aResult.setAssessmentMessageCd(re.getAssessmentMessageCd());
    		aResult.setAssessmentResultCd(re.getAssessmentResultCd());
    		aResult.setAssessmentResultReasonCd(re.getAssessmentResultReasonCd());
    		List<com.telus.credit.wlnprfldmgt.domain.BureauInformation> burList =  new ArrayList<com.telus.credit.wlnprfldmgt.domain.BureauInformation>();
    		if(re.getBureauInformationList() != null)
    		{
    			
    		    for(BureauInformation br  : re.getBureauInformationList() )
    		    {
    		    	com.telus.credit.wlnprfldmgt.domain.BureauInformation bre = new com.telus.credit.wlnprfldmgt.domain.BureauInformation();
    		    	if(br != null)
    		    	{
    		    	    bre.setBureauCd(br.getBureauCd());
    		    	    bre.setBureauPriority(br.getBureauPriority());
    		    	    bre.setBureauType(br.getBureauType());
    		    	}
    		    	burList.add(bre);
    		    }
    			
    		}
    	
    		aResult.setBureauInformationList(burList);
    		aResult.setCreditValueCd(re.getCreditValueCd());
    		aResult.setDecisionCd(re.getDecisionCd());
    		aResult.setDepositAmt(re.getDepositAmt());
    		aResult.setFraudIndicatorCd(re.getFraudIndicatorCd());
    		aResult.setFraudMessageCdList(re.getFraudMessageCdList());
    		com.telus.credit.wlnprfldmgt.domain.ProductCategoryQualification prdq = new com.telus.credit.wlnprfldmgt.domain.ProductCategoryQualification();
    		List<com.telus.credit.wlnprfldmgt.domain.ProductCategory> prList = new ArrayList<com.telus.credit.wlnprfldmgt.domain.ProductCategory>(); 
    		if(re.getProductCategoryQualification() != null)
    		{
    			prdq.setBoltOnInd(re.getProductCategoryQualification().isBoltOnInd());
    		    for(ProductCategory pr  : re.getProductCategoryQualification().getProductCategoryList() )
    		    {
    		    	com.telus.credit.wlnprfldmgt.domain.ProductCategory prg = new com.telus.credit.wlnprfldmgt.domain.ProductCategory();
    		    	if(pr != null)
    		    	{
    		    	    prg.setCategoryCd(pr.getCategoryCd());
    		    	    prg.setQualified(pr.isQualified());
    		    	}
    		    	prList.add(prg);
    		    }
    			prdq.setProductCategoryList(prList);
    		}
    		aResult.setProductCategoryQualification(prdq);
    	}
    	trans.setCreditDecisioningResult(aResult);
        
    	trans.setCreditScoreCd(result.getCreditScoreCd());
    	trans.setCreditScoreTypeCd(result.getCreditScoreTypeCd());
    	trans.setCustomerID(result.getCustomerID());
    	trans.setDepositAmt(result.getDepositAmt());
    	trans.setUserID(result.getUserID());
    	return trans;
    } 
    ///////


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest
    //-------------------------------------------------------------------------
    public CreditAssessmentRequest newCreditAssessmentRequest(MonthlyUpDownRecordCollector collector)
    {
        CustomerRecord data = getSingleRecord(collector.customers);
        ExistingCustomerCreditAssessmentRequest car = new ExistingCustomerCreditAssessmentRequest();
        
        if(data != null)
        {
        // CreditAssessmentRequest
            car.setApplicationID(getDataSourceId());
        // N/A: car.setChannelID(null);
            car.setCommentTxt("Automated Credit Assessment Completed: " + new Date());
            car.setCreditAssessmentSubTypeCd("MONTHLY_CVUD");
            car.setCreditAssessmentTypeCd("FULL_ASSESSMENT");
            car.setCustomerID(data.getCustomerId());
            car.setLineOfBusiness("WIRELINE");

        // FullCreditAssessmentRequest
            car.setCreditCustomerInfo(newCreditCustomerInfo(collector));
            car.setCreditProfileData(newConsumerCreditProfile(collector));

        // ExistingCustomerCreditAssessmentRequest
            car.setDepositItemList(newAccountReceivableDepositDataList(collector));      
            car.setBureauResultData(newBureauResultData(collector));
               
            car.setCustomerCollectionData(newCustomerCollectionData(collector));
        }
        return car;
    }


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest/CreditCustomerInfo
    //-------------------------------------------------------------------------
    private CreditCustomerInfo newCreditCustomerInfo(MonthlyUpDownRecordCollector collector)
    {
        CustomerRecord record = getSingleRecord(collector.customers);

        CreditCustomerInfo creditCustomerInfo = new CreditCustomerInfo();
        if(record != null)
        {
            creditCustomerInfo.setCustomerCreationDate(record.getCreateDate());
            creditCustomerInfo.setCustomerID(record.getCustomerId());
            creditCustomerInfo.setCustomerMasterSourceID(record.getCustomerMasterSourceId());
            creditCustomerInfo.setCustomerStatusCd(record.getCustomerStatusCode());
            creditCustomerInfo.setCustomerSubTypeCd(record.getCustomerSubType());
            creditCustomerInfo.setCustomerTypeCd(record.getCustomerType());
            if(record.getEmployeeIndicator() != null)
                creditCustomerInfo.setEmployeeInd(record.getEmployeeIndicator());
            creditCustomerInfo.setPersonName(newCreditCustomerInfoPersonName(collector));
        // N/A: creditCustomerInfo.setPhoneNumberList(null);
            creditCustomerInfo.setRevenueSegmentCd(record.getRevenueSegmentCode());
        }
        return creditCustomerInfo;
    }


    private PersonName newCreditCustomerInfoPersonName(MonthlyUpDownRecordCollector collector)
    {
        CustomerRecord record = getSingleRecord(collector.customers);
        
        PersonName personName = new PersonName();
        if(record != null)
        {
            personName.setFirstName(record.getFirstName());
            personName.setLastName(record.getLastName());
            personName.setMiddleName(record.getMiddleName());
            personName.setNameSuffix(record.getNameSuffix());
            personName.setTitle(record.getTitle());
        }
        return personName;
    }


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest/CreditProfileData
    //-------------------------------------------------------------------------
    private ConsumerCreditProfile newConsumerCreditProfile(MonthlyUpDownRecordCollector collector)
    {
        CustomerCreditProfileRecord record = getSingleRecord(collector.customerCreditProfiles);

        ConsumerCreditProfile consumerCreditProfile = new ConsumerCreditProfile();

        // CreditProfileData
        if(record != null)
        {
            consumerCreditProfile.setApplicationProvinceCd(trimExtraSpace(record.getApplicationProvinceCode()));
            consumerCreditProfile.setBusinessLastUpdateTimestamp(record.getBusLastUpdateTs());
            if(record.getBypassMatchIndicator() != null)
                consumerCreditProfile.setBypassMatchIndicator(record.getBypassMatchIndicator());
            consumerCreditProfile.setCommentTxt(trimExtraSpace(record.getCommentText()));
            consumerCreditProfile.setCreditAddress(newCreditAddress(record));
            consumerCreditProfile.setCreditCardCd(newCreditCardCd(record));
            consumerCreditProfile.setCreditIdentification(newCreditIdentification(record));
            consumerCreditProfile.setCreditProfileID(record.getCreditProfileId());
            consumerCreditProfile.setCreditProfileStatusCd(trimExtraSpace(record.getCproflStatusCd()));
            // N/A: consumerCreditProfile.setCustomerGuarantor(newCustomerGuarantor(collector));
            consumerCreditProfile.setCustomerID(record.getCustomerId());
            consumerCreditProfile.setFormatCd(trimExtraSpace(record.getCproflFormatCd()));
            //consumerCreditProfile.setMethodCd(trimExtraSpace(record.getPopulatemethodCd()));
            //for batch is is BP, not the value from DB.
            consumerCreditProfile.setMethodCd("BP");
            
            consumerCreditProfile.setPersonalInfo(newPersonalInfo(record));

            // ConsumerCreditProfile
            consumerCreditProfile.setCreditWorthiness(newCreditWorthiness(collector));
          //  if(record.getCreditValueEffectiveDate() != null)
            consumerCreditProfile.setCreditValueEffectiveDate(record.getCreditValueEffectiveDate());
           // if(record.getFirstCreditAssessmentDate() != null)
            consumerCreditProfile.setFirstCreditAssessmentDate(record.getFirstCreditAssessmentDate());
           // if(record.getLatestCreditAssessmentDate() != null)
            consumerCreditProfile.setLatestCreditAssessmentDate(record.getLatestCreditAssessmentDate());
            String reportInd = record.getCredProfAttrValueReportInd();
            
            if(reportInd !=null && !BLANK.equalsIgnoreCase(reportInd.trim()))
            {
            	if("Y".equalsIgnoreCase(reportInd.trim()))
            	{
                    consumerCreditProfile.setReportIndicator(true);  
                }else
                {
            	    consumerCreditProfile.setReportIndicator(false);
                }
            }
        }
        return consumerCreditProfile;
    }


    private String trimExtraSpace(String input) {
		return ( ( input != null && input.trim().length() > 0 ) ? input.trim() : null );
	}
    
	private CreditAddress newCreditAddress(CustomerCreditProfileRecord record)
    {
        //CustomerCreditProfileRecord record = getSingleRecord(collector.customerCreditProfiles);

        CreditAddress creditAddress = new CreditAddress();
        creditAddress.setAddressLineOne(trimExtraSpace(record.getAddressLine1()));
        creditAddress.setAddressLineTwo(trimExtraSpace(record.getAddressLine2()));
        creditAddress.setAddressLineThree(trimExtraSpace(record.getAddressLine3()));
        creditAddress.setAddressLineFour(trimExtraSpace(record.getAddressLine4()));
        creditAddress.setAddressLineFive(trimExtraSpace(record.getAddressLine5()));
        creditAddress.setAddressTypeCd(null);  //TODO investigate this field is needed
        creditAddress.setCityName(trimExtraSpace(record.getMunicNm()));
        creditAddress.setCountryCd(trimExtraSpace(record.getCountryCode()));
        creditAddress.setCreditAddressTypeCd(trimExtraSpace(record.getCproflAddressTypeCd()));  //TODO investigate if this is needed
        creditAddress.setPostalCd(trimExtraSpace(record.getPostalZipCd()));
        creditAddress.setProvinceCd(trimExtraSpace(record.getProvinceCd()));
        return creditAddress;
    }


    private CreditCardCode newCreditCardCd(CustomerCreditProfileRecord record)
    {
    	CreditCardCode credCd = new CreditCardCode();
    	if(record != null)
    	{
    	    credCd.setPrimaryCreditCardCd(trimExtraSpace(record.getPrimCreditCardTypeCd()));
    	    credCd.setSecondaryCreditCardCd(trimExtraSpace(record.getSecondCreditCardTypeCd()));
    	}
    	return credCd;
    }


    private CreditIdentification newCreditIdentification(CustomerCreditProfileRecord record)
    {
    	CreditIdentification credId = new CreditIdentification();
    	String dlValue = trimExtraSpace(record.getDlNumber());
    	if ( dlValue!=null) {
    		DriverLicense dl = new DriverLicense();
    		dl.setDriverLicenseNum(dlValue);
    		dl.setProvinceCd(trimExtraSpace(record.getDlProvinceCd()));
    		//set driverlicense
    		credId.setDriverLicense(dl);
    	}
    	String healthCardValue = trimExtraSpace(record.getHealthCardNumber());
    	if ( healthCardValue != null ) {
    		HealthCard hlcd = new HealthCard();
    		hlcd.setHealthCardNum(healthCardValue);
    		hlcd.setProvinceCd(null); //TODO investigate if this is needed.
    		//set health card
    		credId.setHealthCard(hlcd);
    	}
    	
    	String passportNumberVal = trimExtraSpace(record.getPassportNumber());
    	if ( passportNumberVal != null ) {
    		Passport passpt = new Passport();
    		passpt.setPassportNum(passportNumberVal);
    		passpt.setCountryCd(trimExtraSpace(record.getPassportCountry()));
    		//set passport
    		credId.setPassport(passpt);
    	}
    	
    	String provinceVal = trimExtraSpace( record.getProvinceIdNumer() );
    	if ( provinceVal != null ) {
    		ProvincialIdCard prvCd = new ProvincialIdCard();
    		prvCd.setProvincialIdNum(provinceVal);
    		prvCd.setProvinceCd(trimExtraSpace(record.getProvinceIdCd()));
    		//set province id
    		credId.setProvincialIdCard(prvCd);
    	}
    	
    	//set sin
    	credId.setSin(trimExtraSpace(record.getSinNumber()));
        return credId;
    }


    private CreditWorthiness newCreditWorthiness(MonthlyUpDownRecordCollector collector)
    {
        CustomerCreditProfileRecord record = getSingleRecord(collector.customerCreditProfiles);
        CreditWorthiness creditWorthiness = new CreditWorthiness();
        if(record != null)
        {
            
            creditWorthiness.setAssessmentMessageCd(trimExtraSpace(record.getAssessmentMessageCode()));
            if(record.getFirstCreditAssessmentDate() != null)
                creditWorthiness.setCreditAssessmentCreationDate(record.getFirstCreditAssessmentDate()); //TODO investigate 
            if(record.getCreditAssessReqId() != null)
                creditWorthiness.setCreditAssessmentId(record.getCreditAssessReqId());
        // creditWorthiness.setCreditAssessmentSubTypeCd(record.); //will set in the request
        // creditWorthiness.setCreditAssessmentTypeCd(null);  // will set in the request
        // creditWorthiness.s
            creditWorthiness.setCreditValueCd(trimExtraSpace(record.getCreditValueCode()));
            creditWorthiness.setCustomerID(record.getCustomerId());
            creditWorthiness.setDecisionCd(trimExtraSpace(record.getDecisionCode()));
            creditWorthiness.setFraudIndicatorCd(trimExtraSpace(record.getFraudIndicatorCode())); 
        //creditWorthiness.setFraudIndicatorCd(record.getFraudIndicatorCode());
        //creditWorthiness.setFraudMessageCdList(newFraudCdList(collector));  // do not need to set according to Gurb
            creditWorthiness.setProductCategoryQualification(newProductCategoryQualification(record)); 
        }
        creditWorthiness.setFraudMessageCdList(newFraudCdList(collector));
        return creditWorthiness;
    }

    private ProductCategoryQualification newProductCategoryQualification(CustomerCreditProfileRecord record)
    {
    	ProductCategoryQualification prq = new ProductCategoryQualification();
        if(record != null && record.getBypassMatchIndicator() != null)
        {
            prq.setBoltOnInd(record.getBypassMatchIndicator().booleanValue()); //.setBoltOn(record.getBypassMatchIndicator());
            prq.setProductCategoryList(null); // TODO investigate if this is needed. may not need
        }
        return prq;
    }

    private PersonalInfo newPersonalInfo(CustomerCreditProfileRecord record)
    {
       // CustomerCreditProfileRecord record = getSingleRecord(collector.customerCreditProfiles);
   
        PersonalInfo personalInfo = new PersonalInfo();
        if(record != null)
        {
            personalInfo.setBirthDate(record.getBirthDate());
            personalInfo.setCreditCheckConsentCd(trimExtraSpace(record.getCreditCheckConsentCode()));
            personalInfo.setEmploymentStatusCd(trimExtraSpace(record.getEmploymentStatusCode()));
            personalInfo.setProvinceOfCurrentResidenceCd(trimExtraSpace(record.getProvinceOfCurrentResidence()));
            personalInfo.setResidencyCd(trimExtraSpace(record.getResidencyCode()));
            personalInfo.setUnderLegalCareCd(trimExtraSpace(record.getUnderLegalCareCode()));
        }
        return personalInfo;
    }


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest/AccountReceivableDepositDataList
    //-------------------------------------------------------------------------
    private DepositItemList newAccountReceivableDepositDataList(MonthlyUpDownRecordCollector collector)
    {
        DepositItemList accountReceivableDepositDataList = new DepositItemList();
        if(collector.billingAccountDeposits !=null)
        for( BillingAccountDepositRecord record : getMultipleRecordsIgnoreNull(collector.billingAccountDeposits) )
        {
        	if(record != null)
        	{
                DepositItem accountReceivableDepositDetail = new DepositItem();
                accountReceivableDepositDetail.setAccountID(record.getBillingAccountNum());
                accountReceivableDepositDetail.setCancelDate(record.getCancelDate());
                if(record.getCancelAmount() != null)
                    accountReceivableDepositDetail.setCancelledAmount(record.getCancelAmount().doubleValue());
                accountReceivableDepositDetail.setCancelReasonCd(record.getCancelReasonCode());
                accountReceivableDepositDetail.setCancelReasonTxt(null); //.setCancelReasonCd(value)setCancelReasonDescriptionTxt(null); TOD make sure this is not needed
                if(record.getDepositId()!= null)
                {
                    accountReceivableDepositDetail.setDepositID(record.getDepositId());
                }
                    //accountReceivableDepositDetail.setDepositDesignationID(value); //TODO investigate this one
                accountReceivableDepositDetail.setDueDate(record.getDueDate());
                if(record.getInterestAmount() != null)
                    accountReceivableDepositDetail.setInterestAmount(record.getInterestAmount().doubleValue());
                //accountReceivableDepositDetail.s.setOrderID(record.getOrderId()); //TODO not order id
                if(record.getPaidAmount() != null)
                    accountReceivableDepositDetail.setPaidAmount(record.getPaidAmount().doubleValue());
                accountReceivableDepositDetail.setPaidDate(record.getPaidDate());
                if(record.getReleaseAmount() != null)
                    accountReceivableDepositDetail.setReleasedAmount(record.getReleaseAmount().doubleValue());
                accountReceivableDepositDetail.setReleaseDate(record.getReleaseDate());
                accountReceivableDepositDetail.setReleaseMethodCd(record.getReleaseMethodCode());
               // accountReceivableDepositDetail.setReleaseMethodDescriptionTxt(null);
                accountReceivableDepositDetail.setReleaseReasonCd(record.getRequestReasonCode());
                //accountReceivableDepositDetail.setReleaseReasonDescriptionTxt(null);
                if(record.getRequestAmount() != null)
                    accountReceivableDepositDetail.setRequestAmount(record.getRequestAmount().doubleValue());
                accountReceivableDepositDetail.setRequestDate(record.getRequestDate());
                accountReceivableDepositDetail.setRequestReasonCd(record.getRequestReasonCode());
                //accountReceivableDepositDetail.setRequestReasonDescriptionTxt(null);
                accountReceivableDepositDataList.getDepositItem().add(accountReceivableDepositDetail);
        	}
        }

        return accountReceivableDepositDataList;
    } 


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest/BureauResultData
    //-------------------------------------------------------------------------
    private CreditBureauResult newBureauResultData(MonthlyUpDownRecordCollector collector)
    {
        CustomerCreditBureauRecord record = getSingleRecordIgnoreNull(collector.customerCreditBureaus);

       // List<CustomerCreditBureauDtlRecord> list = new ArrayList<CustomerCreditBureauDtlRecord>();

        CreditBureauResult creditBureauResult = new CreditBureauResult();
        if(record != null)
        {
            creditBureauResult.setAdjudicationResult(newAdjudicationResult(collector));
            creditBureauResult.setBureauReportStatusCd(trimExtraSpace(record.getBureauReportStatusCd()));
            creditBureauResult.setBureauReportStatusDate(record.getBureauReportStatusDate());
            creditBureauResult.setCreationDate(record.getCreateDate());
            creditBureauResult.setErrorCd(trimExtraSpace(record.getErrorCode()));
            creditBureauResult.setFraudWarningList(newFraudWarningList(collector));
            creditBureauResult.setId(record.getBureauTranId());
            creditBureauResult.setPersonName(newCreditBureauResultPersonName(record));
            creditBureauResult.setReportSourceCd(trimExtraSpace(record.getReportSourceCode()));
            creditBureauResult.setReportType(trimExtraSpace(record.getReportType()));
            creditBureauResult.setRiskIndicator(newRiskIndicator(collector));
        //creditBureauResult.s set score list
        }
        return creditBureauResult;
    }


    private PersonName newCreditBureauResultPersonName(CustomerCreditBureauRecord record)
    {
        PersonName personName = new PersonName();
        if(record != null)
        {
            personName.setFirstName(trimExtraSpace(record.getFirstName()));
            personName.setLastName(trimExtraSpace(record.getLastName()));
            personName.setMiddleName(trimExtraSpace(record.getMiddleName()));
            //personName.setNameSuffix(null); //TODO name suffix.
            //personName.setTitle(null);    //TODO title
        }
        return personName;
    }


    private RiskIndicator newRiskIndicator(MonthlyUpDownRecordCollector collector)
    {
    	
        RiskIndicator riskIndicator = new RiskIndicator();
  	
    	List<CustomerCreditBureauDtlRecord> list = getMultipleRecordsIgnoreNull(collector.customerCreditBureausDtl);
        
    	for( Integer riskind : collector.riskInds )
        {
    		if(riskind != null)
    		{
    		    CustomerCreditBureauDtlRecord record = getSingleRiskRecord(list,riskind);
        	    if(record != null)
        	    {
    		        if(riskind == RISK_IND_NO_HIT_THIN_FILE   )
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setNoHitThinFileInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setNoHitThinFileInd(false);
        		        }
        		
        	        } else if( riskind == RISK_IND_TRUE_THIN_FILE)
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setTrueThinFileInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setTrueThinFileInd(false);
        		        }
        		
        	        } else if(riskind == RISK_IND_HIGH_RISK_THIN_FILE)
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setHighRiskThinFileInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setHighRiskThinFileInd(false);
        		        }
        		
        	        } else if(riskind == RISK_IND_TEMP_SIN)
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setTempSINInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setTempSINInd(false);
        		        }
        	        } else if(riskind == RISK_IND_UNDER_AGE)
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setUnderAgeInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			       riskIndicator.setUnderAgeInd(false);
        		        }
        	        } else if(riskind == RISK_IND_BANKRUPTCY)
        	        {
        		        if("Y".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setBankcrupcyInd(true);
        		        } else if("N".equalsIgnoreCase(trimExtraSpace(record.getRiskIndValue())))
        		        {
        			        riskIndicator.setBankcrupcyInd(false);
        		        }
        	        } else if(riskind == RISK_IND_PRIMARY_RISK)
        	        {
        		        riskIndicator.setPrimaryRiskInd(trimExtraSpace(record.getRiskIndValue()));
        	        } else if(riskind == RISK_IND_SECONDARY_RISK )
        	        {
        		        riskIndicator.setSecondaryRiskInd(trimExtraSpace(record.getRiskIndValue()));
        	        } else if(riskind == RISK_IND_WRITEOFF_RISK)
        	        {
        		        riskIndicator.setWriteOffInd(trimExtraSpace(record.getRiskIndValue()));
        	        }
        	    }
    		}
        }
        
        return riskIndicator;
    }


    private AdjudicationResult newAdjudicationResult( MonthlyUpDownRecordCollector collector)
    {
    	CustomerCreditBureauRecord record = getSingleRecordIgnoreNull(collector.customerCreditBureaus);
    	
    		
        AdjudicationResult adjudicationResult = new AdjudicationResult();
        if(record !=null)
        {
            adjudicationResult.setCreditClass(record.getAdjudicationClass());
            CreditDecision dsc = new CreditDecision();
            dsc.setCreditDecisionCd(trimExtraSpace(record.getAdjudicationDecisionCd()));
            dsc.setCreditDecisionMessage(trimExtraSpace(record.getAdjudicationDecisionMsg()));
            adjudicationResult.setCreditDecision(dsc);
            adjudicationResult.setCreditLimitAmt(record.getAdjudicationLimitAmount());
            adjudicationResult.setCreditScoreCd(trimExtraSpace(record.getAdjudicationScoreTxt())); //TODO should this be score cd?
            adjudicationResult.setCreditScoreTypeCd(trimExtraSpace(record.getAdjudicationScoreType()));
            adjudicationResult.setDepositAmt(record.getAdjudicationDepositAmt());
        
            adjudicationResult.setScoreCardAttributeList(newScoreList(collector));
        }
        return adjudicationResult;
    }

    private List<ScoreCardAttribute> newScoreList(MonthlyUpDownRecordCollector collector)
    {
    	List<ScoreCardAttribute> attlist = new ArrayList<ScoreCardAttribute>();
    	
    	List<CustomerCreditBureauDtlRecord> list = getMultipleRecordsIgnoreNull(collector.customerCreditBureausDtl);
        String blank = "";
    	for( String scnm : collector.scoreNms )
        {
        	if(scnm !=null && !blank.equalsIgnoreCase(scnm.trim()))
        	{
        		CustomerCreditBureauDtlRecord record = getSingleScoreRecord(list,scnm);
        		if(record != null)
        		{
        		    ScoreCardAttribute satt = new ScoreCardAttribute();
        	        satt.setScoreName(trimExtraSpace(scnm));
        	        satt.setScoreValue(trimExtraSpace(record.getScoreText()));
        	        attlist.add(satt);
        		}
        	}
        }
        return attlist;
    }
    
    private List<FraudWarning> newFraudWarningList(MonthlyUpDownRecordCollector collector)
    {
    	List<FraudWarning> attlist = new ArrayList<FraudWarning>();
    	
    	List<CustomerCreditBureauDtlRecord> list = getMultipleRecordsIgnoreNull(collector.customerCreditBureausDtl);
        String blank = "";
    	for( String frnm : collector.fraudNms )
        {
        	if(frnm !=null && !blank.equalsIgnoreCase(frnm.trim()))
        	{
        		CustomerCreditBureauDtlRecord record = getSingleFraudRecord(list,frnm);
        		if(record !=null)
        		{
        		    FraudWarning fr = new FraudWarning();
        	        fr.setFraudCd(trimExtraSpace(record.getFraudCd()));
        	        fr.setFraudMessage(trimExtraSpace(record.getFraudMsg()));
        	        fr.setFraudType(trimExtraSpace(record.getFraudType()));
        	        attlist.add(fr);
        		}
        	}
        }
        return attlist;
    }
    private List<String> newFraudCdList(MonthlyUpDownRecordCollector collector)
    {
    	
    	List<String> attlist = new ArrayList<String>();
    	
    	//List<CustomerCreditProfileFraudRecord> list = getMultipleRecordsIgnoreNull(collector.customerCreditProfilesFraud);
    	
    	String blank = "";
    	for( String frcd : collector.prFraudCds )
        {
    		
        	if(frcd !=null && !blank.equalsIgnoreCase(frcd.trim()))
        	{
        		
        		attlist.add(trimExtraSpace(frcd));
        	}
        }
        return attlist;
    }

    private CreditDecision newCreditDecision(MonthlyUpDownRecordCollector collector)
    {
        // no data in extracts
        return null;
    }


    //-------------------------------------------------------------------------
    // ExistingCustomerCreditAssessmentRequest/CustomerCollectionData
    // Return CustomerCollectionData in crda-wsdl-4.0.0-SNAPSHOT.jar
    //-------------------------------------------------------------------------
    private CustomerCollectionData newCustomerCollectionData(MonthlyUpDownRecordCollector collector)
    {
        List<CollectionBillingAccountData> billingDataList = newCollectionBillingAccountData(collector);
        
        // RTCA1.6 - CVUD, do not use this CustomerCollectionData from TCM
        //com.telus.collections.treatment.service.dto.CustomerCollectionData data = m_collectionSummarizationSvc.summarizeCollectionDataByCustomer(billingDataList);

        // Use the CustomerCollectionData defined in cdra-batch
        com.telus.crd.assessment.batch.dto.CustomerCollectionData crdCustomerCollectionData = m_collectionSummarizationSvc.summarizeCollectionDataByCustomer(billingDataList);
                
        /* Note: this is com.telus.credit.domain.collection.CustomerCollectionData in crda-wsdl-4.0.0-SNAPSHOT.jar */
        CustomerCollectionData customerCollectionData = new CustomerCollectionData();
        if (crdCustomerCollectionData != null)
        {
            customerCollectionData.setAccountsInAgencyBalance(crdCustomerCollectionData.getAccountsInAgencyBalance());
            customerCollectionData.setActiveAccountsCollectionInd(crdCustomerCollectionData.isCollectionInd());  //TODO check if this one is really not in the request.
            customerCollectionData.setCollectionScore(crdCustomerCollectionData.getCollectionScore());
            customerCollectionData.setInvoluntaryCancelledAccounts(crdCustomerCollectionData.getInvoluntaryCancelledAccounts());
            customerCollectionData.setInvoluntaryCancelledAccountsBalance(crdCustomerCollectionData.getInvoluntaryCancelledAccountsBalance());
            customerCollectionData.setLatestAgencyAssignmentDate(crdCustomerCollectionData.getLatestAgencyAssignmentDate());
            customerCollectionData.setLatestCollectionEndDate(crdCustomerCollectionData.getLatestCollectionEndDate());
            customerCollectionData.setLatestCollectionStartDate(crdCustomerCollectionData.getLatestCollectionStartDate());
            customerCollectionData.setLatestInvoluntaryCancelledDate(crdCustomerCollectionData.getLatestInvoluntaryCancelledDate());
            customerCollectionData.setNumberOfAccountsInAgency(crdCustomerCollectionData.getNumberOfAccountsInAgency());
            customerCollectionData.setNumberOfNSFCheques(crdCustomerCollectionData.getNumberOfNSFCheques());
        }
        
        return customerCollectionData;
    }


    private List<CollectionBillingAccountData> newCollectionBillingAccountData(MonthlyUpDownRecordCollector collector)
    {
        List<CollectionBillingAccountData> list = new ArrayList<CollectionBillingAccountData>();

        for( long billingAccountNum : collector.billingAccountNums )
        {
            BillingAccountRecord account = getSingleRecord(collector.billingAccounts, billingAccountNum);
            BillingAccountAgencyRecord agency = getSingleRecordIngoreNull(collector.billingAccountAgencies, billingAccountNum);
            BillingAccountCollectionRecord collection = getSingleRecordIngoreNull(collector.billingAccountCollections, billingAccountNum);
            CollectionBillingAccountData collectionBillingAccountData = new CollectionBillingAccountData();

            if( account != null )
            {
                collectionBillingAccountData.setAccountStatus(account.getBillingAccountStatus());
                collectionBillingAccountData.setAccountStatusDate(account.getBillingAccountStatusDate());
                collectionBillingAccountData.setAccountBalance(account.getTotalAmount());
                                
                //RTCA1.6 - CVUD setStartServiceDate()
                collectionBillingAccountData.setStartServiceDate(account.getStartServiceDate());
                //s_log.debug("RTCA1.6-CVUD getStartServiceDate = " + account.getStartServiceDate());
             }

            //collectionBillingAccountData.setAccountBalance(null);

            if( agency != null )
            {
                collectionBillingAccountData.setAgencyAssignmentAmount(agency.getAgencyAmount());
                collectionBillingAccountData.setAgencyAssignmentCode(agency.getAgencyCode());
                collectionBillingAccountData.setAgencyAssignmentDate(agency.getAgencyDate());
               // collectionBillingAccountData.s  //TODO do we need agency status?
            }

            if( collection != null )
            {            	  	
                collectionBillingAccountData.setCollectionEndDate(collection.getCollectionsEndDate());
                collectionBillingAccountData.setCollectionIndicator(collection.getCollectionsIndicator());
                collectionBillingAccountData.setCollectionScore(collection.getCollectionsScore());
                collectionBillingAccountData.setCollectionStartDate(collection.getCollectionsStartDate());
                collectionBillingAccountData.setCollectionStatus(collection.getCollectionsStatus());
                collectionBillingAccountData.setInvoluntaryCeasedIndicator(collection.getInvoluntaryCeasedIndicator());
                collectionBillingAccountData.setNumberOfNSFCheques(collection.getNumberOfNSFCheques());
                collectionBillingAccountData.setCollectionScoreDate(collection.getScoreDate());
                
                // RTCA1.6 - CVUD
                collectionBillingAccountData.setBillingAccountNum(billingAccountNum);
                collectionBillingAccountData.setCollectionSegment(collection.getCollectionSegment());
                collectionBillingAccountData.setDelinquencyCycle(collection.getDelinquencyCycle());
                collectionBillingAccountData.setScorecardID(collection.getScorecardID());
            }

            list.add(collectionBillingAccountData);
        }

        return list;
    }


    //-------------------------------------------------------------------------
    private <T> T getSingleRecord(List<T> list)
    {
        if( list == null )
        {
            throw new RuntimeException("list cannot be null.");
        }

        if( list.isEmpty() )
        {
            throw new RuntimeException("list cannot be empty.");
        }

        if( list.size() != 1 )
        {
            throw new RuntimeException("list cannot have multiple entries.");
        }

        return list.get(0);
    }
    private <T> T getSingleRecordIgnoreNull(List<T> list)
    {
        if( list != null && list.size() >0 )
        {
           
            return list.get(0);
        } 
        return null;
    }


    private <T extends AbstractBillingAccountRecord> T getSingleRecord(List<T> list, long billingAccountNum)
    {
        if( list == null )
        {
            throw new RuntimeException("list cannot be null.");
        }

        for( T item : list )
        {
            if( item !=null  && item.getBillingAccountNum() == billingAccountNum )
            {
                return item;
            }
        }

        return null;
    }
    private <T extends AbstractBillingAccountRecord> T getSingleRecordIngoreNull(List<T> list, long billingAccountNum)
    {
        if( list != null )
        {

            for( T item : list )
            {
                if( item !=null  && item.getBillingAccountNum() == billingAccountNum )
                {                	
                    return item;
                }
            }
        }

        return null;
    }

    private CustomerCreditBureauDtlRecord getSingleScoreRecord(List<CustomerCreditBureauDtlRecord> list, String nm)
    {
        if(list != null)
        {
            for( CustomerCreditBureauDtlRecord item : list )
            {
                if(item !=null && item.getScoreName() != null && nm.equalsIgnoreCase(item.getScoreName()) )
                {
                    return item;
                }
            }
        }

        return null;
    }
    private CustomerCreditBureauDtlRecord getSingleRiskRecord(List<CustomerCreditBureauDtlRecord> list, int nm)
    {
      
        if(list != null)
        {
            for( CustomerCreditBureauDtlRecord item : list )
            {
        	
                if( item != null && item.getRiskIndType() != null && nm==item.getRiskIndType().intValue())
                {
            	          	
            	    //System.out.println("help: item.getRiskIndType():" + item.getRiskIndType());
            	
            	    return item;
            	
                }
            }
        }

        return null;
    }

    private CustomerCreditBureauDtlRecord getSingleFraudRecord(List<CustomerCreditBureauDtlRecord> list, String nm)
    {     
        if(list != null)
        {
            for( CustomerCreditBureauDtlRecord item : list )
            {
                if(item !=null && item.getFraudCd() != null && nm.equalsIgnoreCase(item.getFraudCd()) )
                {
                    return item;
                }
            }
        }

        return null;
    }
    
   /* private CustomerCreditProfileFraudRecord getSingleFraudCdRecord(List<CustomerCreditProfileFraudRecord> list, String nm)
    {

        if(list != null)
        {
            for( CustomerCreditProfileFraudRecord item : list )
            {
                if(item !=null && item.getFraudCd() != null && nm.equalsIgnoreCase(item.getFraudCd()) )
                {
            	    System.out.println("TEST helper Fraud Cd:" + item.getFraudCd() );
                    return item;
                }
            }
        }

        return null;
    }*/

    private <T> List<T> getMultipleRecords(List<T> list)
    {
        if( list == null )
        {
            throw new RuntimeException("list cannot be null.");
        }

        return list;
    }
    private <T> List<T> getMultipleRecordsIgnoreNull(List<T> list)
    {
        /*if( list == null )
        {
            throw new RuntimeException("list cannot be null.");
        }*/

        return list;
    }


    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    public void sendEmailSummaryReport(StandardEvaluationContext context) throws MailException, MessagingException, IOException
    {
        MailBean mailBean = new MailBean();

        try
        {
            StringTokenizer tokenizer = new StringTokenizer(m_emailTo, ",", false);
            while( tokenizer.hasMoreTokens() )
            {
                mailBean.addMailTo(tokenizer.nextToken());
            }
        }
        catch( AddressException e )
        {
            throw new MessagingException("Error while assigning To email address: " + m_emailTo, e);
        }

        try
        {
            mailBean.setMailFrom(m_emailFrom);
        }
        catch( AddressException e )
        {
            throw new MessagingException("Error while assigning From email address: " + m_emailFrom, e);
        }

        mailBean.setSubject(m_emailSubject);
        mailBean.setHtmlMsg(TemplateUtil.merge(m_emailBody, context));

        if( m_errorReportWriter.getOutputRecordCount() > 0 )
        {
            if( isProductionEnv() )
            {
                mailBean.setImportance(MailBean.HIGH_IMPORTANCE);
            }

            String file = m_errorReportWriter.getOutputFile().getAbsolutePath();
            
            try
            {
                mailBean.addFileAttachment(file);
            }
            catch( IOException e )
            {
                throw new IOException("Error while attaching error report: " + file, e);
            }
        }

        MailSender.send(mailBean);
    }


    //-------------------------------------------------------------------------
    public static boolean isProductionEnv()
    {
        return "PR".equals(ConfigContext.getProperty("metadata/envId"));
    }
    

    //-------------------------------------------------------------------------
    // Spring Injected
    //-------------------------------------------------------------------------
    public void setEmailTo(String emailTo)
    {
        m_emailTo = emailTo;
    }


    public void setEmailFrom(String emailFrom)
    {
        m_emailFrom = emailFrom;
    }


    public void setEmailSubject(String emailSubject)
    {
        m_emailSubject = emailSubject;
    }


    public void setEmailBody(String emailBody)
    {
        m_emailBody = emailBody;
    }
}
