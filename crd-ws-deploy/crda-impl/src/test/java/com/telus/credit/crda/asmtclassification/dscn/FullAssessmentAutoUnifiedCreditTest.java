package com.telus.credit.crda.asmtclassification.dscn;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.util.EcrdaTestHelper;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.EnvUtil;
import com.telus.credit.crda.util.ReferencePDSValidateUnit;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.crda.webservice.handler.CrdaClientSoapHandler;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.CreditDecision;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.common.DriverLicense;
import com.telus.credit.domain.common.PersonName;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.IndicatorType;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.crda.WlnAdjudicationResult;
import com.telus.credit.domain.crda.WlsAccountFinancialHistory;
import com.telus.credit.domain.crda.WlsAccountInfo;
import com.telus.credit.domain.crda.WlsCreditBureauResult;
import com.telus.credit.domain.crda.WlsCreditWorthinessData;
import com.telus.credit.domain.crda.WlsMatchedAccount;
import com.telus.credit.domain.crda.WlsUnifiedCreditSearchResult;
import com.telus.credit.domain.crda.WlsWarningHistory;
import com.telus.credit.domain.creditprofile.ConsumerCreditProfile;
import com.telus.credit.domain.creditprofile.CreditWorthiness;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;
import com.telus.framework.config.ConfigContext;
import com.telus.framework.crypto.EncryptionUtil;

public class FullAssessmentAutoUnifiedCreditTest extends TestCase {

	private Log log;
	DozerBeanMapper m_mapper;

	EnterpriseCreditAssessmentServiceImpl pojo;
	
	protected void setUp() throws Exception {
		try {
			//log.info( "all" + Thread.getContextClassLoader().Resource("log4j.xml") );
			super.setUp();
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			//log.info("System.getProperties():" + System.getProperties() );
			EnvUtil.setupTestEnv();		
			
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
			pojo = (EnterpriseCreditAssessmentServiceImpl)m_ApplicationContext.getBean("enterpriseCreditAssessmentServiceTarget");
			log = LogFactory.getLog(FullAssessmentAutoUnifiedCreditTest.class);
			log.info("pojo is " + pojo );
			com.telus.framework.crypto.impl.pilot.PilotCryptoImpl cryptoImpl = (com.telus.framework.crypto.impl.pilot.PilotCryptoImpl)m_ApplicationContext.getBean("cryptoImpl");
			m_mapper =  (DozerBeanMapper)m_ApplicationContext.getBean("EcrdaDozerBeanMapper");
			//EnvDetailUtil
			log.info(EncryptionUtil.encrypt("blabla"));
			log.info("ConfigContext.getApplicationId: " + ConfigContext.getApplicationId() );
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();			
		}
	}
	
	@Test
	public void testsetup() throws Exception {
		setUp();
	}	
	
	@Test
	public void testPing() throws Exception {
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(new DefaultResourceLoader());
		Resource[] resources = patternResolver.getResources("classpath:crda-decisionengine-spring.xml");

		for(Resource resource : resources) {
		   System.out.println("****" + resource.getDescription());
		   System.out.println("****" + resource.getInputStream().available());
		   //readAllBytes(), StandardCharsets.UTF_8)
		   
		}
		log.info(pojo.ping());
	}
	
	@Test
	public void testGetCreditAssessment() throws Exception {
		//8571171
		CreditAssessmentDetails aCreditAssessmentDetails =
					pojo.getCreditAssessment(9795506, EcrdaTestHelper.createAudiInfo());
		GetCreditAssessmentResponse a = new GetCreditAssessmentResponse();
		
		a.setCreditAssessmentDetails(aCreditAssessmentDetails);
		
		log.info( EcrdaTestHelper.convertToXml(a) );
	}
	
	@Test
	public void test_getCreditBureauDocument() throws Exception {
		//this does not work from local
		//pojo.getCreditBureauDocument("79323578", EcrdaTestHelper.createAudiInfo());
	}

	@Test
	public void test_singleFileFullAssessmentAutoUnifiedCredit2() throws Exception {
		String dir = "src/test/resources/data/FullASmt/";
        String responseFolder = dir + "response/";
        String xmlFile = "tmp_UC_FULL_AUTO.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT_MinData.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT.xml";
        PerformCreditAssessment aPerformCreditAssessment = EcrdaTestHelper.createPerformCreditAssessmentRequest(dir + xmlFile);
	}
	@Test
	public void test_singleFileFullAssessmentAutoUnifiedCredit() throws Exception {
		String dir = "src/test/resources/data/FullASmt/";
        String responseFolder = dir + "response/";
        String xmlFile = "tmp_UC_FULL_AUTO.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT_MinData.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT.xml";
        PerformCreditAssessment aPerformCreditAssessment = EcrdaTestHelper.createPerformCreditAssessmentRequest(dir + xmlFile);
        
        //inject any data here:
        int custID = 841382;
        long cpID = 2304345590L;
        aPerformCreditAssessment.getCreditAssessmentRequest().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditCustomerInfo().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditProfileData().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditProfileData().setCreditProfileID(cpID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).setLineOfBusiness("WIRELINE");
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(
				aPerformCreditAssessment.getCreditAssessmentRequest(), 
				aPerformCreditAssessment.getAuditInfo());
		
		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		aPerformCreditAssessmentResponse.setCreditAssessmentTransactionResult(aCreditAssessmentTrxResult);
		log.info("Response: " + EcrdaTestHelper.convertToXml(aPerformCreditAssessmentResponse));
		EcrdaTestHelper.writeToFile(responseFolder + xmlFile, aPerformCreditAssessmentResponse);
		//log.info("Response created: " + responseFolder + xmlFile);
	}
	
	@Test
	public void test_singleFile_BasedOnFICOinput() throws Exception  {
		String dir = "src/test/resources/data/FullASmt/NewCustomerCreditAssessmentRequest/";
        String responseFolder = dir + "response/";
        String xmlFile = "tmp_fico_input.xml";    
        if (!new File(responseFolder).exists()) {
			new File(responseFolder).mkdirs();
		}
        processTestCase(new File(dir + xmlFile), dir, responseFolder);
	}
	
	@Test
	public void test_AllFiles_BasedOnSVC_RequestNew() throws Exception  {
        //test directory setup: 
		// cp -rf ../crda-decisionengine/src/test/resources/data/SVC_Request/newCustomerCreditAssessmentRequest 
		// to src/test/resources/data/FullASmt/newCustomerCreditAssessmentRequest/
		String dir = "src/test/resources/data/FullASmt/NewCustomerCreditAssessmentRequest/";
        String responseFolder = dir + "response/";
        File directory = new File(dir);
        File[] fList = directory.listFiles();
		if (!new File(responseFolder).exists()) {
			new File(responseFolder).mkdirs();
		} else {
			EcrdaTestHelper.delete(new File(responseFolder));
			if (!new File(responseFolder).exists()) {
				new File(responseFolder).mkdirs();
			}
		}

        int i=0;
        int stopCount = 100;
        stopCount=3;
        stopCount = fList.length + 3;
        for (File file : fList){
            if (!file.isDirectory() && file.getName().endsWith(".xml")) {
            	processTestCase(file, dir, responseFolder);
            }
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}
	
	
	private void processTestCase(File file, String descEnginedir, String responseFolder) {
		String filename=file.getName();
    	String filenameFullPath = file.getAbsolutePath(); 
		log.info("Input filename FullPath: " + filenameFullPath);
		try {
			String nodeName = "NewCustomerCreditAssessmentRequest";

			NewCustomerCreditAssessmentRequest m_ficoCreditAssessmentRequest = new NewCustomerCreditAssessmentRequest();
			m_ficoCreditAssessmentRequest = (NewCustomerCreditAssessmentRequest) EcrdaTestHelper
					.createRequestCommon(filenameFullPath, nodeName, m_ficoCreditAssessmentRequest.getClass());
			PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = testPerformCreditAssessment(
					m_ficoCreditAssessmentRequest, responseFolder + filename + ".input");
			EcrdaTestHelper.writeToFile(responseFolder + filename, aPerformCreditAssessmentResponse);
			log.info("Response created: " + responseFolder + filename);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private PerformCreditAssessmentResponse testPerformCreditAssessment(NewCustomerCreditAssessmentRequest m_ficoCreditAssessmentRequest, String resFolderFilename) throws JAXBException, Throwable{
    	//log.info("NewCustomerCreditAssessmentRequest: " + m_ficoCreditAssessmentRequest);
    	
    	FullCreditAssessmentRequest aFullCreditAssessmentRequest = new FullCreditAssessmentRequest();
    	m_ficoCreditAssessmentRequest.getCreditProfileData().setUnderLegalCareCd("N");
    	
        m_mapper.map(m_ficoCreditAssessmentRequest, aFullCreditAssessmentRequest);
        log.info("mapping done!");
        
        int custID = 841382;
        long cpID = 2304345590L;
        aFullCreditAssessmentRequest.setCustomerID(custID);
        aFullCreditAssessmentRequest.getCreditCustomerInfo().setCustomerID(custID);
        aFullCreditAssessmentRequest.getCreditProfileData().setCustomerID(custID);
        aFullCreditAssessmentRequest.getCreditProfileData().setCreditProfileID(cpID);
        //aFullCreditAssessmentRequest.getCreditProfileData().getCreditWorthiness().setCustomerID(custID);
        aFullCreditAssessmentRequest.setApplicationID("1102");
        aFullCreditAssessmentRequest.getCreditCustomerInfo().setCustomerSubTypeCd("I");
        aFullCreditAssessmentRequest.getCreditProfileData().setCreditProfileStatusCd("A");
        //aFullCreditAssessmentRequest.getCreditProfileData().getUnderLegalCareCode();
        aFullCreditAssessmentRequest.setLineOfBusiness("WIRELINE");
        
    	PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
    	AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("pei123");
		auditInfo.setOriginatorApplicationId("11223344");
		performCreditAssessment.setAuditInfo(auditInfo);
    	
    	performCreditAssessment.setCreditAssessmentRequest(aFullCreditAssessmentRequest);
    	//log.info("performCreditAssessment:" + performCreditAssessment);
    	
        log.info("performCreditAssessment printed to :" + resFolderFilename);
        EcrdaTestHelper.writeToFile( resFolderFilename, performCreditAssessment);
    	
    	String requestStr = XMLUtility.convertObjectToXml(performCreditAssessment);
    	PerformCreditAssessment xmlPerformCreditAssessment = new PerformCreditAssessment();
    	xmlPerformCreditAssessment = (PerformCreditAssessment) EcrdaTestHelper.createRequestFromString(requestStr.replaceAll("[\\n\\t\\r]", ""), "PerformCreditAssessment", PerformCreditAssessment.class);
    	
		CreditAssessmentRequest creditAssessmentRequest = xmlPerformCreditAssessment.getCreditAssessmentRequest();
		
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(creditAssessmentRequest, auditInfo );
		
		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		aPerformCreditAssessmentResponse.setCreditAssessmentTransactionResult(aCreditAssessmentTrxResult);
		return aPerformCreditAssessmentResponse;

	}
	
    @Test
	public void testPerformCreditAssessment_02() throws JAXBException, Throwable{
   	        
    	PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
    	AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("pei123");
		auditInfo.setOriginatorApplicationId("11223344");
		performCreditAssessment.setAuditInfo(auditInfo);
    	
    	performCreditAssessment.setCreditAssessmentRequest(buildFullCreditAssessmentRequest());

    	log.info("performCreditAssessment start:");
    	log.info(XMLUtility.convertObjectToXml(performCreditAssessment) );
    	log.info("performCreditAssessment end:");
    	
		CreditAssessmentRequest creditAssessmentRequest = performCreditAssessment.getCreditAssessmentRequest();

		//CreditAssessmentResultWrapper aCreditAssessmentResult = m_ReAssessment.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(creditAssessmentRequest, auditInfo );		
		CreditAssessmentTransactionResult nCreditAssessmentTransactionResult = new CreditAssessmentTransactionResult();
		m_mapper.map(aCreditAssessmentTrxResult, nCreditAssessmentTransactionResult);
		log.info( "output: " + XMLUtility.convertObjectToXml(nCreditAssessmentTransactionResult) );
    }
    
    private FullCreditAssessmentRequest buildFullCreditAssessmentRequest() {
    	FullCreditAssessmentRequest aFullCreditAssessmentRequest = new FullCreditAssessmentRequest();
    	
    	aFullCreditAssessmentRequest.setCreditAssessmentTypeCd("FULL_ASSESSMENT");
    	aFullCreditAssessmentRequest.setCreditAssessmentSubTypeCd("AUTO_ASSESSMENT");
    	aFullCreditAssessmentRequest.setCustomerID(841382);
    	aFullCreditAssessmentRequest.setApplicationID("1102");
    	aFullCreditAssessmentRequest.setLineOfBusiness("WIRELINE");
    	
    	CreditCustomerInfo creditCustomerInfo = new CreditCustomerInfo();
    	aFullCreditAssessmentRequest.setCreditCustomerInfo(creditCustomerInfo);
    	
    	creditCustomerInfo.setCustomerID(841382);
    	creditCustomerInfo.setCustomerCreationDate(new Date());
    	creditCustomerInfo.setCustomerMasterSourceID(1L);
    	creditCustomerInfo.setCustomerStatusCd("A");
    	PersonName aPersonName = new PersonName();
    	creditCustomerInfo.setPersonName(aPersonName);
    	aPersonName.setFirstName("fname");
    	aPersonName.setLastName("lname");
    	creditCustomerInfo.setEmployeeInd(new Boolean(false));
    	creditCustomerInfo.setCustomerTypeCd("R");
    	creditCustomerInfo.setCustomerSubTypeCd("I");
    	
    	ConsumerCreditProfile consumerCreditProfile = new ConsumerCreditProfile();
    	aFullCreditAssessmentRequest.setCreditProfileData(consumerCreditProfile);
    	CreditWorthiness aCreditWorthiness = new CreditWorthiness();
    	aCreditWorthiness.setCustomerID(841382);//??
    	consumerCreditProfile.setCreditWorthiness(aCreditWorthiness);
    	consumerCreditProfile.setApplicationProvinceCd("AB");
    	consumerCreditProfile.setCreditProfileID(2304345590L);
    	consumerCreditProfile.setCustomerID(841382);
    	consumerCreditProfile.setCreditProfileStatusCd("A");
    	CreditIdentification aCreditIdentification = new CreditIdentification();
    	consumerCreditProfile.setCreditIdentification(aCreditIdentification);
    	DriverLicense aDriverLicense = new DriverLicense();
    	aDriverLicense.setDriverLicenseNum("DLPEI1212121");
    	aDriverLicense.setExpiryDate(new Date());
    	aDriverLicense.setProvinceCd("AB");
    	
    	aCreditIdentification.setDriverLicense(aDriverLicense);
    	aCreditIdentification.setSin("123123123");
    	
    	WlsUnifiedCreditSearchResult aWlsUnifiedCreditSearchResult = new WlsUnifiedCreditSearchResult();
    	aFullCreditAssessmentRequest.setUnifiedCreditSearchResult(aWlsUnifiedCreditSearchResult);
    	List<String> dataInquiryErrorCodeList = new ArrayList();
    	dataInquiryErrorCodeList.add("dataInquiryErrorCd");
    	aWlsUnifiedCreditSearchResult.setDataInquiryErrorCodeList(dataInquiryErrorCodeList);
    	aWlsUnifiedCreditSearchResult.setLineOfBusiness("WIRELESS");
    	
    	WlsMatchedAccount aWlsMatchedAccount = new WlsMatchedAccount();
    	aWlsUnifiedCreditSearchResult.setMatchedAccount(aWlsMatchedAccount);
    	WlsAccountInfo aWlsAccountInfo = new WlsAccountInfo();
    	aWlsMatchedAccount.setAccountData(aWlsAccountInfo);
    	aWlsAccountInfo.setAccountCreationDate(new Date());
    	aWlsAccountInfo.setAccountStatus("A");
    	aWlsAccountInfo.setBillingAccountNumber(112211221122L);
    	aWlsAccountInfo.setAccountSubType("I");
    	aWlsAccountInfo.setAccountType("R");
    	
    	WlsAccountFinancialHistory aWlsAccountFinancialHistory = new WlsAccountFinancialHistory();
    	aWlsMatchedAccount.setAccountFinancialHistory(aWlsAccountFinancialHistory);
    	
    	WlsCreditBureauResult aWlsCreditBureauResult = new WlsCreditBureauResult();
    	aWlsMatchedAccount.setCreditBureauResult(aWlsCreditBureauResult);
    	aWlsCreditBureauResult.setCreationDate(new Date());
    	aWlsCreditBureauResult.setErrorCd("Er");
    	aWlsCreditBureauResult.setReportSourceCd("ReportSourceCd");
    	
    	WlnAdjudicationResult aWlnAdjudicationResult = new WlnAdjudicationResult();
    	aWlsCreditBureauResult.setAdjudicationResult(aWlnAdjudicationResult);
    	CreditDecision aCreditDecision = new CreditDecision();
    	aWlnAdjudicationResult.setCreditDecision(aCreditDecision);
    	aCreditDecision.setCreditDecisionCd("DSC");
    	aCreditDecision.setCreditDecisionMessage("A UC wls desc code.");
    	
    	WlsCreditWorthinessData aWlsCreditWorthinessData = new WlsCreditWorthinessData();
    	aWlsMatchedAccount.setCreditWorthinessData(aWlsCreditWorthinessData);
    	aWlsCreditWorthinessData.setCreditClassCd("BAD");
    	aWlsCreditWorthinessData.setRiskLevelDecisionCd("wlsRLDC");
    	aWlsCreditWorthinessData.setRiskLevelNumber(10);
    	
    	WlsMatchedAccount.TotalSubscribers aTotalSubscribers = new WlsMatchedAccount.TotalSubscribers();
    	aWlsMatchedAccount.setTotalSubscribers(aTotalSubscribers);
    	
    	List<WlsWarningHistory> aWlsWarningHistoryList = new ArrayList();
    	WlsWarningHistory aWlsWarningHistory = new WlsWarningHistory();
    	aWlsWarningHistory.setWarningCategoryCd("catCD");
    	aWlsWarningHistory.setWarningCd("warCD");
    	aWlsWarningHistory.setWarningStatusCd("wsc");
    	aWlsWarningHistory.setWarningTypeCd("unassigned");
    	
    	aWlsWarningHistoryList.add(aWlsWarningHistory);
    	aWlsMatchedAccount.setWarningHistoryList(aWlsWarningHistoryList);
    	
    	IndicatorType aIndicatorType = new IndicatorType();
    	aIndicatorType.setIndicator(new Boolean(false));
    	aIndicatorType.setReasonCd("some reason");
    	aWlsUnifiedCreditSearchResult.setMatchFound(aIndicatorType);
    	
    	aWlsUnifiedCreditSearchResult.setUnifiedCreditDormantInd(false);

    	return aFullCreditAssessmentRequest;
    }
    
    @Test
	public void testReferencePDSUtils() throws Throwable{
        try {
			List<ReferencePDSValidateUnit> validationArray = new ArrayList<ReferencePDSValidateUnit>();

			String creditAssessmentTypeCd = "FULL_ASSESSMENT";
			validationArray.add(new ReferencePDSValidateUnit(creditAssessmentTypeCd,
			        EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_TYP,
			        true,
			        "Credit Assessment Type",
			        EnterpriseCreditAssessmentExceptionCodes.ASMT_TYPE_VALIDATION_EXCEPTION));

			String ucComment = InternalRules.getUnifiedCreditCommentText("true", "UCDORMT1", 4488448L);
			
			log.info("ucComment: " + ucComment);
			//log.info("getView:" + ReferencePdsAccess.getView( "category", "EN" ));
			//log.info("is_UC_DORMANT_FLAG_ON" + refPDSAdapter.is_UC_DORMANT_FLAG_ON());
			log.info("verifyRefPDSInitializedSucessfully is successful!");
			log.info("getTablesLoaded(): " + ReferencePdsAccess.getTablesLoaded());
	 
		} catch (Exception e) {
	 
			e.printStackTrace();
		}		
		
	}
}
