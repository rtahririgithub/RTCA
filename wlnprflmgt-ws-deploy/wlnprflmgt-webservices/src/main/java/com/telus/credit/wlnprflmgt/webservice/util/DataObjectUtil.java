package com.telus.credit.wlnprflmgt.webservice.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.math.BigDecimal;

import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;
import com.telus.credit.wlnprflmgt.domain.CreditWorthiness;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditDecision;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditIdentification;
import com.telus.credit.wlnprflmgt.domain.credit.common.DriverLicense;
import com.telus.credit.wlnprflmgt.domain.credit.common.Passport;
import com.telus.credit.wlnprflmgt.domain.credit.common.ProvincialIdCard;
import com.telus.credit.wlnprflmgt.domain.credit.common.HealthCard;
import com.telus.credit.wlnprflmgt.domain.customer.common.ProvinceCode;
import com.telus.credit.wlnprflmgt.domain.credit.common.PersonalInfo;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditCardCode;
import com.telus.credit.wlnprflmgt.domain.CreditProfileSearchResult;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditProfileData;
//import com.telus.credit.wlnprflmgt.domain.credit.common.CustomerGuarantor;

import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.domain.CreditAttribute;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.math.Money;


public class DataObjectUtil {
	  
	/*
	 * <p>
	 * <b>Description </b>Copies data from CreditProfileData to CreditProfile
	 * </p>
	 * 
	 * @param CreditProfileData
	 * @param CreditProfile
	 */
	public static void copyToCreditProfile(CreditProfileData creditProfileData, CreditProfile creditProfile){
		
	  if (creditProfileData.getFormatCd() != null) 
		  creditProfile.setFormat(creditProfileData.getFormatCd());
	  if (creditProfileData.getMethodCd() != null)
		  creditProfile.setMethod(creditProfileData.getMethodCd());
	  if (creditProfileData.getCreditProfileStatusCd() != null)
		  creditProfile.setStatus(creditProfileData.getCreditProfileStatusCd()); 
	  creditProfile.setBypassMatchIndicator(
			  creditProfileData.isBypassMatchIndicator() != null ? creditProfileData.isBypassMatchIndicator() : false);
	  creditProfile.setApplicationProvinceCd(creditProfileData.getApplicationProvinceCd());
	  creditProfile.setComment(creditProfileData.getCommentTxt());
	  
	  
	  //creditIdentification
	  if (creditProfileData.getCreditIdentification()!=null)
	      {  
		     List<CreditIDCard> creditIDCardList=new ArrayList<CreditIDCard>();
		     //CreditIDCard[] creditIDCardArray=creditProfile.getCreditIDCards();
		     copyToCreditIDCards(creditProfileData.getCreditIdentification(),creditIDCardList);
		     creditProfile.setCreditIDCards(creditIDCardList.toArray(new CreditIDCard[0]));
	      }
	  
	  //creditAddress
	  if (creditProfileData.getCreditAddress()!=null)
	   {
		com.telus.credit.domain.CreditAddress creditAddress=
			  new com.telus.credit.domain.CreditAddress();

		  //required
		  creditAddress.setAddressLineOne(creditProfileData.getCreditAddress().getAddressLineOne());
		  creditAddress.setAddressLineTwo(creditProfileData.getCreditAddress().getAddressLineTwo());
		  creditAddress.setAddressLineThree(creditProfileData.getCreditAddress().getAddressLineThree());
		  creditAddress.setAddressLineFour(creditProfileData.getCreditAddress().getAddressLineFour());
		  creditAddress.setAddressLineFive(creditProfileData.getCreditAddress().getAddressLineFive());
		  creditAddress.setAddressTypeCode(creditProfileData.getCreditAddress().getAddressTypeCd());
		  //required
		  creditAddress.setCity(creditProfileData.getCreditAddress().getCityName());
		  //required
		  //creditAddress.setProvince(ProvinceCode.fromValue(creditProfile.getCreditAddress().getProvinceCode()));
		  creditAddress.setProvinceCode(creditProfileData.getCreditAddress().getProvinceCd());
		  //required
		  creditAddress.setPostalCode(creditProfileData.getCreditAddress().getPostalCd());
		  //required
		  creditAddress.setCountryCode(creditProfileData.getCreditAddress().getCountryCd());
		  creditAddress.setType(creditProfileData.getCreditAddress().getCreditAddressTypeCd());
		  
		  creditProfile.setCreditAddress(creditAddress);
	    }
	  
	  //personalInfo
	  if (creditProfileData.getPersonalInfo()!=null)
	  {
		  creditProfile.setBirthdate(creditProfileData.getPersonalInfo().getBirthDate());
		  creditProfile.setCreditCheckConsentCode(creditProfileData.getPersonalInfo().getCreditCheckConsentCd());
		  creditProfile.setEmploymentStatusCode(creditProfileData.getPersonalInfo().getEmploymentStatusCd());
		  creditProfile.setResidencyCode(creditProfileData.getPersonalInfo().getResidencyCd());
		  creditProfile.setUnderLegalCareCode(creditProfileData.getPersonalInfo().getUnderLegalCareCd());
		  //RTCA 1.5
		  creditProfile.setProvinceOfCurrentResidenceCd(creditProfileData.getPersonalInfo().getProvinceOfCurrentResidenceCd());
	  }
	          	  
	  //creditCardCode
	  if (creditProfileData.getCreditCardCd()!=null)
	  {
		  creditProfile.setPrimaryCreditCardCode(creditProfileData.getCreditCardCd().getPrimaryCreditCardCd());
		  creditProfile.setSecondaryCreditCardCode(creditProfileData.getCreditCardCd().getSecondaryCreditCardCd());
	  }
	  
	  //CustomerGuarantor
     if (creditProfileData.getCustomerGuarantor()!=null){
	       com.telus.credit.domain.CustomerGuarantor guarantor=
		           new com.telus.credit.domain.CustomerGuarantor();
	       
	       guarantor.setGuarantorCustomerId(DataConvertUtil.LongToInt(creditProfileData.getCustomerGuarantor().getGuarantorCustomerID()));
	       guarantor.setGuarantorFullName(creditProfileData.getCustomerGuarantor().getGuarantorFullName());
	       guarantor.setExpiryDate(creditProfileData.getCustomerGuarantor().getExpiryDate());
	       guarantor.setGuaranteedAmount(
	    		   creditProfileData.getCustomerGuarantor().getGuaranteedAmt() != null ? 
	    				   new Money(creditProfileData.getCustomerGuarantor().getGuaranteedAmt().doubleValue()) : null);
	       guarantor.setComment(creditProfileData.getCustomerGuarantor().getCommentTxt());
	       guarantor.setGuarantorPhoneNumber(creditProfileData.getCustomerGuarantor().getGuarantorPhoneNum());
	       guarantor.setReferenceNumber(creditProfileData.getCustomerGuarantor().getReferenceNum());
	       
	       creditProfile.setCustomerGuarantor(guarantor);
       }
	}
	
	/*
	 * <p>
	 * <b>Description </b>Copies data from CreditProfile to CreditProfileData
	 * </p>
	 * 
	 * @param CreditProfile
	 * @param CreditProfileData
	 */
	public static void copyToCreditProfileData(CreditProfile creditProfile, CreditProfileData creditProfileData){
		
   	  
		creditProfileData.setBusinessLastUpdateTimestamp
   	        (creditProfile.getBusinessLastUpdateTimestamp());
		creditProfileData.setCreditProfileID(creditProfile.get_id());
		creditProfileData.setFormatCd(creditProfile.getFormat());
		creditProfileData.setMethodCd(creditProfile.getMethod());
		creditProfileData.setCreditProfileStatusCd(creditProfile.getStatus());
		creditProfileData.setBypassMatchIndicator(creditProfile.isBypassMatchIndicator());
		creditProfileData.setApplicationProvinceCd(creditProfile.getApplicationProvinceCd());
		creditProfileData.setCommentTxt(creditProfile.getComment());
   	  
   	  //creditIdentification
   	  if (creditProfile.getCreditIDCards()!=null)
   	      {  
   		     CreditIdentification creditIds=new CreditIdentification();
   		     CreditIDCard[] creditIDCardArray=creditProfile.getCreditIDCards();
   		     for (int i=0; i< creditIDCardArray.length; i++)
   		     {   
   		    	 //DL
   		    	 if (CreditIDCard.DRIVERS_LICENSE_KEY.equalsIgnoreCase(creditIDCardArray[i].getIdTypeCode()))
   		    	   {
   		    		   DriverLicense dl=new DriverLicense();
   		    		   dl.setDriverLicenseNum(EncryptionUtil.decrypt(creditIDCardArray[i].getIdNumber()));
   		    		   //dl.setProvinceCode(ProvinceCode.fromValue(creditIDCardArray[i].getProvinceCode()));
   		    		   dl.setProvinceCd(creditIDCardArray[i].getProvinceCode());
   		    		   creditIds.setDriverLicense(dl);
   		         	   }
   		    	 //SIN
   		    	 if (CreditIDCard.SIN_KEY.equalsIgnoreCase(creditIDCardArray[i].getIdTypeCode()))
 		    	       {
 		    		       creditIds.setSin(EncryptionUtil.decrypt(creditIDCardArray[i].getIdNumber()));
 		         	       }
   		    	 
   		    	//HealthCard
   		    	 if (CreditIDCard.HEALTH_CARE_NUMBER_KEY.equalsIgnoreCase(creditIDCardArray[i].getIdTypeCode()))
   		    	   {
   		    		     HealthCard hc = new HealthCard();
   		    		     hc.setHealthCardNum(EncryptionUtil.decrypt(creditIDCardArray[i].getIdNumber()));
   		    		     //hc.setProvinceCode(ProvinceCode.fromValue(creditIDCardArray[i].getProvinceCode()));
   		    		     hc.setProvinceCd(creditIDCardArray[i].getProvinceCode());
   		    		     creditIds.setHealthCard(hc);
   		    	      }
   		    	 
   		    	//ProvincialIdCard
   		    	 if (CreditIDCard.PROVINCIAL_ID_KEY.equalsIgnoreCase(creditIDCardArray[i].getIdTypeCode()))
 		    	       {
   		    		     ProvincialIdCard  pic = new ProvincialIdCard();
   		    		     pic.setProvincialIdNum(EncryptionUtil.decrypt(creditIDCardArray[i].getIdNumber()));
   		    		     pic.setProvinceCd(creditIDCardArray[i].getProvinceCode());
 		    		     creditIds.setProvincialIdCard(pic);
 		    	         }
   		    	 
   		    	//Passport
   		    	 if (CreditIDCard.PASSPORT_NUMBER_KEY.equalsIgnoreCase(creditIDCardArray[i].getIdTypeCode()))
		    	       {
   		    		     Passport pp = new Passport();
   		    		     pp.setPassportNum(EncryptionUtil.decrypt(creditIDCardArray[i].getIdNumber()));
   		    		     pp.setCountryCd(creditIDCardArray[i].getCountryCode());
		    		     creditIds.setPassport(pp);
		    	         }	 
   		      }      		     
   		    creditProfileData.setCreditIdentification(creditIds);
   	      }
   	  

   	  
   	  
   	  //creditAddress
   	  if (creditProfile.getCreditAddress()!=null)
   	   {
   		com.telus.credit.wlnprflmgt.domain.credit.common.CreditAddress creditAddress=
   			  new com.telus.credit.wlnprflmgt.domain.credit.common.CreditAddress();

   		  //required
   		  creditAddress.setAddressLineOne(creditProfile.getCreditAddress().getAddressLineOne());
   		  creditAddress.setAddressLineTwo(creditProfile.getCreditAddress().getAddressLineTwo());
   		  creditAddress.setAddressLineThree(creditProfile.getCreditAddress().getAddressLineThree());
   		  creditAddress.setAddressLineFour(creditProfile.getCreditAddress().getAddressLineFour());
   		  creditAddress.setAddressLineFive(creditProfile.getCreditAddress().getAddressLineFive());
   		  creditAddress.setAddressTypeCd(creditProfile.getCreditAddress().getAddressTypeCode());
   		  //required
   		  creditAddress.setCityName(creditProfile.getCreditAddress().getCity());
   		  //required
   		  //creditAddress.setProvince(ProvinceCode.fromValue(creditProfile.getCreditAddress().getProvinceCode()));
   		  creditAddress.setProvinceCd(creditProfile.getCreditAddress().getProvinceCode());
   		  //required
   		  creditAddress.setPostalCd(creditProfile.getCreditAddress().getPostalCode());
   		  //required
   		  creditAddress.setCountryCd(creditProfile.getCreditAddress().getCountryCode());
   		  creditAddress.setCreditAddressTypeCd(creditProfile.getCreditAddress().getType());
   		  
   		  creditProfileData.setCreditAddress(creditAddress);
   	    }
   	  
   	  //personalInfo
   	  PersonalInfo pInfo=new PersonalInfo();
   	  pInfo.setBirthDate(creditProfile.getBirthdate());
   	  pInfo.setCreditCheckConsentCd(creditProfile.getCreditCheckConsentCode());
   	  pInfo.setEmploymentStatusCd(creditProfile.getEmploymentStatusCode());
   	  pInfo.setResidencyCd(creditProfile.getResidencyCode());
   	  pInfo.setUnderLegalCareCd(creditProfile.getUnderLegalCareCode());
   	  pInfo.setProvinceOfCurrentResidenceCd(creditProfile.getProvinceOfCurrentResidenceCd());
   	  
   	  creditProfileData.setPersonalInfo(pInfo);
   	          	  
   	  //creditCardCode
   	  CreditCardCode ccCode=new CreditCardCode();
   	  ccCode.setPrimaryCreditCardCd(creditProfile.getPrimaryCreditCardCode());
   	  ccCode.setSecondaryCreditCardCd(creditProfile.getSecondaryCreditCardCode());
   	  creditProfileData.setCreditCardCd(ccCode);
   	  
   	  //CustomerGuarantor
     if (creditProfile.getCustomerGuarantor()!=null){
    	 com.telus.credit.wlnprflmgt.domain.credit.common.CustomerGuarantor guarantor=
    		 new com.telus.credit.wlnprflmgt.domain.credit.common.CustomerGuarantor();
    	 guarantor.setId(creditProfile.getCustomerGuarantor().get_id());
    	 guarantor.setGuarantorCreditProfileID(creditProfile.getCustomerGuarantor().getGuarantorCreditProfileId());
    	 guarantor.setGuarantorCustomerID(creditProfile.getCustomerGuarantor().getGuarantorCustomerId());
    	 guarantor.setGuarantorFullName(creditProfile.getCustomerGuarantor().getGuarantorFullName());
    	 guarantor.setExpiryDate(creditProfile.getCustomerGuarantor().getExpiryDate());
    	 if (creditProfile.getCustomerGuarantor().getGuaranteedAmount()!=null)
    	  {
    		 guarantor.setGuaranteedAmt(new BigDecimal(String.valueOf(creditProfile.getCustomerGuarantor().getGuaranteedAmount().doubleValue()))); 
    	    }
    	 
    	 guarantor.setCommentTxt(creditProfile.getCustomerGuarantor().getComment());
    	 guarantor.setGuarantorPhoneNum(creditProfile.getCustomerGuarantor().getGuarantorPhoneNumber());
    	 guarantor.setReferenceNum(creditProfile.getCustomerGuarantor().getReferenceNumber());
    	 
    	 creditProfileData.setCustomerGuarantor(guarantor);
       }
   	  
     }
	
	/**
	 * 
	 * <p>
	 * <b>Description </b> Copies CreditIdentification to CreditIDCard
	 * </p>
	 * 
	 * @param  CreditIdentification
	 */
    public static void copyToCreditIDCards(CreditIdentification creditIdentification, List<CreditIDCard> creditIDCardList )
    {
    	//List<CreditIDCard> creditIDCardList=new ArrayList<CreditIDCard>();
    	CreditIDCard anIdCard;
    	
    	if (!"".equals(creditIdentification.getSin()) && (creditIdentification.getSin()!=null))
    	 {   
    		anIdCard=new CreditIDCard();
    		anIdCard.setIdTypeCode(CreditIDCard.SIN_KEY);
    		anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getSin()));
    		creditIDCardList.add(anIdCard);
    	  }
    	
    	if (creditIdentification.getDriverLicense()!=null)
    	{
        	if (!"".equals(creditIdentification.getDriverLicense().getDriverLicenseNum()) 
        	&& (creditIdentification.getDriverLicense().getDriverLicenseNum() !=null))
        	 {
        		anIdCard=new CreditIDCard();
        		anIdCard.setIdTypeCode(CreditIDCard.DRIVERS_LICENSE_KEY);
        		anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getDriverLicense().getDriverLicenseNum()));
        		anIdCard.setProvinceCode(creditIdentification.getDriverLicense().getProvinceCd());
        		creditIDCardList.add(anIdCard);
        	   }
    	}
    	
    	if (creditIdentification.getHealthCard()!=null)
    	{
        	if (!"".equals(creditIdentification.getHealthCard().getHealthCardNum()) 
                	&& (creditIdentification.getHealthCard().getHealthCardNum() !=null))
        	 {
        		anIdCard=new CreditIDCard();
        		anIdCard.setIdTypeCode(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
        		anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getHealthCard().getHealthCardNum()));
        		anIdCard.setProvinceCode(creditIdentification.getHealthCard().getProvinceCd());
        		creditIDCardList.add(anIdCard);
        	   }
    	}
    	
    	
    	if (creditIdentification.getPassport()!=null)
    	{
        	if (!"".equals(creditIdentification.getPassport().getPassportNum()) 
                	&& (creditIdentification.getPassport().getPassportNum() !=null))
        	 {
        		anIdCard=new CreditIDCard();
        		anIdCard.setIdTypeCode(CreditIDCard.PASSPORT_NUMBER_KEY);
        		anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getPassport().getPassportNum()));
        		anIdCard.setCountryCode(creditIdentification.getPassport().getCountryCd());
        		creditIDCardList.add(anIdCard);
        	   }
    	}
    	
    	if (creditIdentification.getProvincialIdCard()!=null)
    	{
        	if (!"".equals(creditIdentification.getProvincialIdCard().getProvincialIdNum()) 
                	&& (creditIdentification.getProvincialIdCard().getProvincialIdNum() !=null))
        	 {
        		anIdCard=new CreditIDCard();
        		anIdCard.setIdTypeCode(CreditIDCard.PROVINCIAL_ID_KEY);
        		anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getProvincialIdCard().getProvincialIdNum()));
        		anIdCard.setProvinceCode(creditIdentification.getProvincialIdCard().getProvinceCd());
        		creditIDCardList.add(anIdCard);
        	   }
    	}
    	
    	//return ((CreditIDCard[])creditIDCardList.toArray(new CreditIDCard[0])); 
    	//return creditIDCardList.toArray(new CreditIDCard[0]);
    }
    
    /**
	 * 
	 * <p>
	 * <b>Description </b> Copies Credit value from credit profile to CreditWorthiness
	 * </p>
	 * 
	 * @param  CreditProfile
	 * @param  CreditWorthiness
	 */
    public static void copyToCreditWorthiness(CreditProfile creditProfile,CreditWorthiness creditWorthiness){
     	  
     	  if (creditProfile.getCreditValue()!=null)
     	   {  
     		 CreditValue creditValue= creditProfile.getCreditValue();
     		 
     		 //credit value info
     		 creditWorthiness.setCreditValueCd(creditValue.getCreditValueCode());
     		 
     		 //Risk Level Num
     		 creditWorthiness.setCreditRiskLevel(creditValue.getRiskLevelNum());
     		 
     		 //fraud object
     		 creditWorthiness.setFraudIndicatorCd(creditValue.getFraudIndicatorCd());
     		 if ( creditValue.getFraudMessageCodeList() != null
     		      && creditValue.getFraudMessageCodeList().size() > 0 ) {
     		     creditWorthiness.getFraudMessageCdList().addAll( creditValue.getFraudMessageCodeList() );
     		 }
     		 //creditWorthiness.(creditValue.getFraudMessageCodeList());
     
     		 
     		 //credit assessment info
     		 creditWorthiness.setCreditAssessmentId(creditValue.getCarId());
     		 creditWorthiness.setAssessmentMessageCd(creditValue.getAssessmentMsgCd());
     		 creditWorthiness.setCreditAssessmentTypeCd(creditValue.getCreditAssessmentTypeCd());
     		 creditWorthiness.setCreditAssessmentSubTypeCd(creditValue.getCreditAssessmentSubTypeCd());
     		 creditWorthiness.setCreditAssessmentCreationDate(creditValue.getCreditAssessmentCreationDate());
     		 
     		// creditDecision and CreditBureauReportInd is not set when we do not have carID
     		/* removed in RTCA 1.5
     		 if (creditValue.getCarId() != null) {
     			//credit bureau report ind
	     		// creditWorthiness.setCreditBureauReportInd(creditValue.getCreditBureauReportInd());
	     		  
	     		//credit decision
	      		 CreditDecision creditDecision = new CreditDecision();
	      		 creditDecision.setCreditDecisionCd(creditValue.getCreditDecisionCd());
	      		 creditDecision.setCreditDecisionMessage(creditValue.getCreditDecisionMsgTxt());
	      		 creditWorthiness.setCreditDecision(creditDecision);
     		 }
     		 */
     		     		 
     		 //credit class, removed from credit_common_1.1 in RTCA 1.5
     		 //creditWorthiness.setCreditClass(creditValue.getCreditClassCd());
     		 
     		 //credit score,removed from credit_common_1.1 in RTCA 1.5
     		 //creditWorthiness.setCreditScore(creditValue.getCreditScoreNumber());

     		 //RTCA 1.5 new attributes
     		 creditWorthiness.setDecisionCd(creditValue.getDecisionCd());
     		 creditWorthiness.setAssessmentMessageCd(creditValue.getAssessmentMsgCd());
     		 
     		//Product Set Qualifications
     		com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategoryQualification productCategoryQualification
     		 =new com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategoryQualification();
     		
     		com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategory
     		destProductCatgory=null;
     		
     		List<com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategory> 
				destProductCategoryList=new ArrayList<com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategory>();
			 
     		//null check for missing product list in credit worthiness
     		if ((creditValue.getProductCatQualification() !=null) && (creditValue.getProductCatQualification().getProductCategoryList()!=null))
     		{
     			//List<com.telus.credit.domain.ProductCategory> srcProductCategoryList 
     				//= new ArrayList<com.telus.credit.domain.ProductCategory>();
				List<com.telus.credit.domain.ProductCategory> srcProductCategoryList=DataConvertUtil.castList
				    (com.telus.credit.domain.ProductCategory.class, creditValue.getProductCatQualification().getProductCategoryList());
				if ((srcProductCategoryList!=null) &&(srcProductCategoryList.size()!=0))
     			{
     				Iterator<com.telus.credit.domain.ProductCategory> it=srcProductCategoryList.iterator();
     				while (it.hasNext())
     				 {
     					destProductCatgory=new com.telus.credit.wlnprflmgt.domain.credit.common.ProductCategory();
     					com.telus.credit.domain.ProductCategory srcProducCategory=
     						(com.telus.credit.domain.ProductCategory)it.next();
     					destProductCatgory.setCategoryCd(srcProducCategory.getCreditApprovedProductCategoryCd());
     					destProductCatgory.setQualified(srcProducCategory.getProductQualificationIndicator());
     					
     					//destProductCategoryList.add(destProductCatgory);
     					productCategoryQualification.getProductCategoryList().add(destProductCatgory);
     				  }
     			   }
				productCategoryQualification.setBoltOnInd(creditValue.getProductCatQualification().getBoltOn());
				creditWorthiness.setProductCategoryQualification(productCategoryQualification);
     		 }
 
      }
    }
    
    /**
	 * 
	 * <p>
	 * <b>Description </b> Copies Credit Attributes from credit profile to CreditAttribute
	 * </p>
	 * 
	 * @param  CreditProfile
	 * @param  CreditAttributeList
	 */
    public static void copyToCreditAttribtes(CreditProfile creditProfile, List<CreditAttribute> creditAttributeList)
    {
    	 
		 CreditAttribute creditAttribute = null;
	 	 if ((creditProfile.getProvinceOfCurrentResidenceCd() != null) && (!"".equals(creditProfile.getProvinceOfCurrentResidenceCd())))
	 		   
	 	    {  
	 		   creditAttribute = new CreditAttribute();
	 		   //creditAttribute.setCreditProfileId(creditProfileId);
	 		   creditAttribute.setAttributeCode(CreditAttribute.CURRENT_PROVINCE_RESIDENCY_CODE);
	 		   creditAttribute.setAttributeValue(creditProfile.getProvinceOfCurrentResidenceCd());
	 		   creditAttributeList.add(creditAttribute);
	 	       }
	 	   
	 	 if (creditProfile.getCreditValueEffectiveDate() != null)
			   
		    {  
			   creditAttribute = new CreditAttribute();
			   //creditAttribute.setCreditProfileId(creditProfileId);
			   creditAttribute.setAttributeCode(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE);
			   creditAttribute.setAttributeValue(DataConvertUtil.convertDateToString(creditProfile.getCreditValueEffectiveDate()));
			   creditAttributeList.add(creditAttribute);
		       }
     }
    
    
    /**
  	 * 
  	 * <p>
  	 * <b>Description </b> Copies Credit Attributes to ConsumerCreditProfile
  	 * </p>
  	 * 
  	 * @param  CreditAttributeList
  	 * @param  ConsumerCreditProfile
  	 */
      public static void copyToConsumerCreditProfile(List<CreditAttribute> creditAttributeList,ConsumerCreditProfile consumerCreditProfile)
      {
      	 
    	CreditAttribute creditAttribute = null;
    	if (!creditAttributeList.isEmpty())
      	{
      	  for (Iterator it = creditAttributeList.iterator(); it.hasNext();) 
      	  {
      		  creditAttribute = (CreditAttribute) it.next();
      		  
      		  if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_REPORT_INDICATOR))
      			  consumerCreditProfile.setReportIndicator(DataConvertUtil.convertStringToBoolean(creditAttribute.getAttributeValue()));
      	  	  
      		  if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE))
      			  consumerCreditProfile.setCreditValueEffectiveDate(DataConvertUtil.convertStringToDate(creditAttribute.getAttributeValue()));
      		  /*
      		  if (creditAttribute.getAttributeCode().equals(CreditAttribute.CURRENT_PROVINCE_RESIDENCY_CODE))
      		     {
      			   PersonalInfo pInfo=consumerCreditProfile.getPersonalInfo();
      			   pInfo.setProvinceOfCurrentResidenceCd(creditAttribute.getAttributeValue());
      			   consumerCreditProfile.setPersonalInfo(pInfo);
      		       }
      		       */
      		  
      		  if (creditAttribute.getAttributeCode().equals(CreditAttribute.FIRST_ASSESSMENT_DATE))
      			  consumerCreditProfile.setFirstCreditAssessmentDate(DataConvertUtil.convertStringToDate(creditAttribute.getAttributeValue()));
      		  
      		  if (creditAttribute.getAttributeCode().equals(CreditAttribute.LATEST_ASSESSMENT_DATE))
      			  consumerCreditProfile.setLatestCreditAssessmentDate(DataConvertUtil.convertStringToDate(creditAttribute.getAttributeValue()));  	 
         	   } //end loop
           }//end if
       }
	   
	   /**
       * 
       * @param creditIdentification
       * @param creditIDCardList
       */
      
	   
	   public static void copyToCreditIDCardswithoutEncryption(CreditIdentification creditIdentification, List<CreditIDCard> creditIDCardList )
      {
      	//List<CreditIDCard> creditIDCardList=new ArrayList<CreditIDCard>();
      	CreditIDCard anIdCard;
      	
      	if (!"".equals(creditIdentification.getSin()) && (creditIdentification.getSin()!=null))
      	 {   
      		anIdCard=new CreditIDCard();
      		anIdCard.setIdTypeCode(CreditIDCard.SIN_KEY);
      		anIdCard.setIdNumber((creditIdentification.getSin()));
      		creditIDCardList.add(anIdCard);
      	  }
      	
      	if (creditIdentification.getDriverLicense()!=null)
      	{
          	if (!"".equals(creditIdentification.getDriverLicense().getDriverLicenseNum()) 
          	&& (creditIdentification.getDriverLicense().getDriverLicenseNum() !=null))
          	 {
          		anIdCard=new CreditIDCard();
          		anIdCard.setIdTypeCode(CreditIDCard.DRIVERS_LICENSE_KEY);
          		anIdCard.setIdNumber((creditIdentification.getDriverLicense().getDriverLicenseNum()));
          		anIdCard.setProvinceCode(creditIdentification.getDriverLicense().getProvinceCd());
          		creditIDCardList.add(anIdCard);
          	   }
      	}
      	
      	if (creditIdentification.getHealthCard()!=null)
      	{
          	if (!"".equals(creditIdentification.getHealthCard().getHealthCardNum()) 
                  	&& (creditIdentification.getHealthCard().getHealthCardNum() !=null))
          	 {
          		anIdCard=new CreditIDCard();
          		anIdCard.setIdTypeCode(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
          		anIdCard.setIdNumber(creditIdentification.getHealthCard().getHealthCardNum());
          		anIdCard.setProvinceCode(creditIdentification.getHealthCard().getProvinceCd());
          		creditIDCardList.add(anIdCard);
          	   }
      	}
     	
      	if (creditIdentification.getPassport()!=null)
      	{
          	if (!"".equals(creditIdentification.getPassport().getPassportNum()) 
                  	&& (creditIdentification.getPassport().getPassportNum() !=null))
          	 {
          		anIdCard=new CreditIDCard();
          		anIdCard.setIdTypeCode(CreditIDCard.PASSPORT_NUMBER_KEY);
          		anIdCard.setIdNumber(creditIdentification.getPassport().getPassportNum());
          		anIdCard.setCountryCode(creditIdentification.getPassport().getCountryCd());
          		creditIDCardList.add(anIdCard);
          	   }
      	}
      	
      	if (creditIdentification.getProvincialIdCard()!=null)
      	{
          	if (!"".equals(creditIdentification.getProvincialIdCard().getProvincialIdNum()) 
                  	&& (creditIdentification.getProvincialIdCard().getProvincialIdNum() !=null))
          	 {
          		anIdCard=new CreditIDCard();
          		anIdCard.setIdTypeCode(CreditIDCard.PROVINCIAL_ID_KEY);
          		anIdCard.setIdNumber(creditIdentification.getProvincialIdCard().getProvincialIdNum());
          		anIdCard.setProvinceCode(creditIdentification.getProvincialIdCard().getProvinceCd());
          		creditIDCardList.add(anIdCard);
          	   }
      	  }
      
      }
    
    
	
}
