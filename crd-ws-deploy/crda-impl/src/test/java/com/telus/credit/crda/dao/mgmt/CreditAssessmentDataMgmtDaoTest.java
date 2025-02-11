/*package com.telus.credit.crda.dao.mgmt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.dao.dcn.DecisiongDaoImpReAssessment;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CompareObjectsUtil;
import com.telus.credit.crda.util.EcrdaTestHelper;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.crdgw.domain.BaseReportResponse;
import com.telus.credit.crdgw.domain.PullConsumerCreditReport;
import com.telus.credit.domain.collection.CustomerCollectionData;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.common.CreditProfileData;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.CreditDecisioningExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.framework.test.TestUtil;


@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/test-telus-crd-crda-impl-spring.xml")

@RunWith(TelusJUnitClassRunner.class)
//@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/test-dao-spring.xml") 

public class CreditAssessmentDataMgmtDaoTest  {
	    @Autowired
	    public  DozerBeanMapper m_mapper;
	  @Autowired 
	  CreditAssessmentDataMgmtDao m_CreditAssessmentDataMgmtDao;
	  long crdasmtId = 1142;//with bureua report
		  //1262;
	  @Autowired 
	  DecisiongDaoImpReAssessment m_DecisiongDaoImpReAssessment;

	 @Autowired
	 DecisiongDaoImpReAssessment m_DecisioningDaoImpCommon;
	  
	////////@Test
	public void testGetCreditAssessment() throws Throwable {
		System.out.println("************* getCreditAssessment ********");
		CreditAssessmentDetails aCreditAssessmentDetails = m_CreditAssessmentDataMgmtDao.getCreditAssessment(crdasmtId);
		assertNotNull("CreditAssessmentDetails result is null", aCreditAssessmentDetails);
		
		//prep data by calling performCreditasmt using an existing customer with all data 
		//common
		//validateCommonCrdAmtAttributes()
		aCreditAssessmentDetails.getCustomerID();
		aCreditAssessmentDetails.getChannelID();
		aCreditAssessmentDetails.getCommentTxt();
		aCreditAssessmentDetails.getCreditAssessmentDataSourceCd();
		aCreditAssessmentDetails.getCreditAssessmentDate();
		aCreditAssessmentDetails.getCreditAssessmentID();
		aCreditAssessmentDetails.getCreditAssessmentStatus();
		aCreditAssessmentDetails.getCreditAssessmentStatusDate();
		aCreditAssessmentDetails.getCreditAssessmentStatusReasonCd();
		aCreditAssessmentDetails.getCreditAssessmentSubTypeCd();
		aCreditAssessmentDetails.getCreditAssessmentTypeCd();
		aCreditAssessmentDetails.getUserID();
		
		
		//testGetCreditDecisioningInput ( input : col, deposit,....
		aCreditAssessmentDetails.getCreditDecisioningInput();
		
		//testGetCreditDecisioningResult
		aCreditAssessmentDetails.getCreditDecisioningResult();


		
		
		;
		

		
	}
	
	

	
	////////@Test
	public void testGetCreditAssessment_AllCreditBureauData()
			throws Throwable {
		//prep data use an exist CARID
		String requestFilename =TestFiles.FULL_ASSESSMENT_REOPEN_ASSESSMENT;
		PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(requestFilename);
		
		//expire older CreditBureauTransaction
		m_DecisioningDaoImpCommon.expireCreditBureauTransaction(
					crdasmtId, 
					EnterpriseCreditAssessmentConsts.CREDIT_BUREAU_TRN_STATUS_VOID, 
					XMLUtility.createAuditInfo());
		
		//store data 
		String aConsumerCreditReportResponse_filename=TestFiles.CGW_pullConsumerCreditReportResponse;
		PullConsumerCreditReport aPullConsumerCreditReportResponse = XMLUtility.createPullConsumerCreditReportRequest(aConsumerCreditReportResponse_filename);    
		m_DecisioningDaoImpCommon.storeCreditBureauResult(consumerCreditReportResponse, dbCARID, creditAssessmentRequest, auditInfo)(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest(), 
				crdasmtId, 
				performCreditAssessment.getCreditAssessmentRequest(), 
				XMLUtility.createAuditInfo());

		//TEST
		//***************************** 
		CreditAssessmentDetails getCreditAssessmentRslt = m_CreditAssessmentDataMgmtDao.getCreditAssessment(crdasmtId);
		assertNotNull("CreditAssessmentDetails result is null", getCreditAssessmentRslt);

		
		//test CreditBureauResul
		//verifyCreditBureauResult(aPullConsumerCreditReportResponse,getCreditAssessmentRslt);


		//test CreditBureauDataResultDocumentList
		 //verifyCreditBureauDataResultDocumentList(aPullConsumerCreditReportResponse, getCreditAssessmentRslt);
		
		
	 
		assertFalse("ChannelID" , !getCreditAssessmentRslt.getChannelID().equalsIgnoreCase(performCreditAssessment.getCreditAssessmentRequest().getChannelID()) );
		assertFalse("getCommentTxt" , getCreditAssessmentRslt.getCustomerID()!=(performCreditAssessment.getCreditAssessmentRequest().getCustomerID()) );
		assertFalse("getCommentTxt" , getCreditAssessmentRslt.getCreditAssessmentTypeCd().equalsIgnoreCase(performCreditAssessment.getCreditAssessmentRequest().getCreditAssessmentTypeCd()) );
		assertFalse("getCommentTxt" , getCreditAssessmentRslt.getUserID().equalsIgnoreCase(performCreditAssessment.getAuditInfo().getUserId() ));


		
		assertFalse("getCreditBureauReportSourceCd" ,getCreditAssessmentRslt.getCreditBureauReportSourceCd().equalsIgnoreCase(aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getReportSourceCd()));
		assertFalse("getCreditClass" ,getCreditAssessmentRslt.getCreditClass().equalsIgnoreCase(
					aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getCreditClass()));
		assertFalse("getCreditDecisionCd" ,getCreditAssessmentRslt.getCreditDecisionCd().getCreditDecisionCd().equalsIgnoreCase(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getCreditDecision().getCreditDecisionCd()));
		assertFalse("getCreditDecisionMessage" ,getCreditAssessmentRslt.getCreditDecisionCd().getCreditDecisionMessage().equalsIgnoreCase(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getCreditDecision().getCreditDecisionMessage()));
	
		assertFalse("getCreditScoreCd" ,getCreditAssessmentRslt.getCreditScoreCd().equalsIgnoreCase(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getCreditScoreCd()));
		assertFalse("getCreditScoreTypeCd" ,getCreditAssessmentRslt.getCreditScoreTypeCd().equalsIgnoreCase(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getCreditScoreTypeCd()));

		assertFalse("getDepositAmt" ,getCreditAssessmentRslt.getDepositAmt()!=(
				aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getCreditBureauResult().getAdjudicationResult().getDepositAmt()));

		

        boolean aCreditBureauReportInd = InternalRules.getCreditBureauReportInd( aPullConsumerCreditReportResponse.getConsumerCreditReportRequest().getReportDocument() );
		assertFalse("isCreditBureauReportInd" ,getCreditAssessmentRslt.isCreditBureauReportInd()!=aCreditBureauReportInd);
	}
	////////@Test
	public void testGetCreditAssessment_Common()
	throws Throwable {
				getCreditAssessmentRslt.getCreditAssessmentDate();
		getCreditAssessmentRslt.getCreditAssessmentID();
		getCreditAssessmentRslt.getCreditAssessmentStatus();
		getCreditAssessmentRslt.getCreditAssessmentStatusDate();
		getCreditAssessmentRslt.getCreditAssessmentStatusReasonCd();		
		
	}
	////@Test
	public void testGetExistingCustomerCreditAssessment_CreditDecisioningInput_CreditDecisioningResult()
	throws Throwable {
		//prep data use an exist CARID
		String requestFilename =TestFiles.FULL_ASSESSMENT_REOPEN_ASSESSMENT;
		PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(requestFilename);
		AuditInfo aeAudiInfo = performCreditAssessment.getAuditInfo();
		
			
		//create FicoPerformCreditAssessmentResponse from XML test data 
		String FicoPerformCreditAssessmentResponsefilename=TestFiles.FicoperformCreditAssessmentResponse;
		com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse aFicoPerformCreditAssessmentResponse = XMLUtility.createFicoPerformCreditAssessmentRequest(FicoPerformCreditAssessmentResponsefilename);    
		CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails = new CreditAssessmentTransactionDetails();
		//load db
		//m_CreditAssessmentDataMgmtDao.voidCreditAssessment(crdasmtId, "REPLACED", EcrdaTestHelper.createAudiInfo());
        long dbCARID = m_DecisiongDaoImpReAssessment.createCreditAssessmentRequestTrx(
        		performCreditAssessment.getCreditAssessmentRequest(),
        		aeAudiInfo,
                EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING
        );
        aCreditAssessmentTransactionDetails.setCreditAssessmentID(dbCARID);		

        CreditAssessmentResultWrapper aCreditAssessmentResultWrapper = new  CreditAssessmentResultWrapper();
        m_mapper.map(aFicoPerformCreditAssessmentResponse.getCreditAssessmentResult(), aCreditAssessmentResultWrapper);
        
        m_DecisiongDaoImpReAssessment.storeSingleCompletedDecisioningTrx(
				performCreditAssessment.getCreditAssessmentRequest(), 
				aeAudiInfo, 
				aCreditAssessmentResultWrapper, 
				aCreditAssessmentTransactionDetails, 
				true);
		
        //update car
        LogUtil.traceCalllog("dao.updateCreditAssessmentRequestStatus");
        m_DecisiongDaoImpReAssessment.updateCreditAssessmentRequestStatus(
                aCreditAssessmentTransactionDetails.getCreditAssessmentID(), aeAudiInfo.getUserId(), EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);

        //create comment
        LogUtil.traceCalllog("dao.createCreditAssessmentComment");
        m_DecisiongDaoImpReAssessment.createCreditAssessmentComment(
        			aCreditAssessmentTransactionDetails.getCreditAssessmentID(), 
        			aeAudiInfo.getUserId(), aeAudiInfo.getOriginatorApplicationId(), 
        			aCreditAssessmentTransactionDetails.getCommentTxt());
		 System.out.println("crdasmtid=" + aCreditAssessmentTransactionDetails.getCreditAssessmentID());

		//TEST
		CreditAssessmentDetails getCreditAsmt_CreditAssessmentDetails = m_CreditAssessmentDataMgmtDao.getCreditAssessment(aCreditAssessmentTransactionDetails.getCreditAssessmentID());
		CreditDecisioningExistingCustomerCreditAssessmentRequest rsl_ExistingCustomerCreditAssessmentRequest = (CreditDecisioningExistingCustomerCreditAssessmentRequest) getCreditAsmt_CreditAssessmentDetails.getCreditDecisioningInput();
		ExistingCustomerCreditAssessmentRequest input_ExistingCustomerCreditAssessmentRequest =(ExistingCustomerCreditAssessmentRequest) performCreditAssessment.getCreditAssessmentRequest();
		
		//compare performCreditAssessment.getCreditAssessmentRequest with aExistingCustomerCreditAssessmentRequest
 		ArrayList filterNameExistingCustomerCreditAssessmentRequestList = new ArrayList();
		 filterNameExistingCustomerCreditAssessmentRequestList.add("commentTxt");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("customerGuarantor");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("formatCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("businessLastUpdateTimestamp");
		 
		 filterNameExistingCustomerCreditAssessmentRequestList.add("birthDate");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("addressTypeCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("creditAddressTypeCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("provincialIdCard");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("passport");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("healthCard");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("expiryDate");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("creditAssessmentCreationDate");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("creditAssessmentSubTypeCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("creditAssessmentTypeCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("fraudMessageCdList");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("productCategoryQualification");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("assessmentMessageCd");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("creditAssessmentId");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("customerID");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("telecomContactList");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("customerCreationDate");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("title");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("nameSuffix");
		 filterNameExistingCustomerCreditAssessmentRequestList.add("reportIndicator");
		 
		 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRsltExistingCustomerCreditAssessmentRequest  = CompareObjectsUtil.traversingCompare(
				 input_ExistingCustomerCreditAssessmentRequest, 
				 rsl_ExistingCustomerCreditAssessmentRequest,
				 filterNameExistingCustomerCreditAssessmentRequestList);
		 //CompareObjectsUtil.printCompareResult(CompRsltExistingCustomerCreditAssessmentRequest); 
		 
		 
		 
 		 
 
 	 CreditAssessmentResult rslt_MappedFicoCreditAssessmentResultFromDB = getCreditAsmt_CreditAssessmentDetails.getCreditDecisioningResult();
	 ArrayList filterNameCreditAssessmentRequestList = new ArrayList();
	 filterNameCreditAssessmentRequestList.add("assessmentResultReasonCd");
		 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRsltCreditAssessmentResult  = CompareObjectsUtil.traversingCompare(
				 aFicoPerformCreditAssessmentResponse.getCreditAssessmentResult(),
				 rslt_MappedFicoCreditAssessmentResultFromDB,
				 filterNameCreditAssessmentRequestList);
		 
		 CompareObjectsUtil.printCompareResult(CompRsltCreditAssessmentResult);  
		 
		 
		  CreditProfileData rslt_CreditProfileDataFromDB=	 ((CreditDecisioningExistingCustomerCreditAssessmentRequest) getCreditAsmt_CreditAssessmentDetails.getCreditDecisioningInput()).getCreditProfileData() ;
		  CreditProfileData input_CreditProfileData =	 ((ExistingCustomerCreditAssessmentRequest) performCreditAssessment.getCreditAssessmentRequest()).getCreditProfileData() ;
			 ArrayList filterNameCreditProfileDataList = new ArrayList();
			 filterNameCreditProfileDataList.add("commentTxt");
			 filterNameCreditProfileDataList.add("customerGuarantor");
			 filterNameCreditProfileDataList.add("formatCd");
			 filterNameCreditProfileDataList.add("businessLastUpdateTimestamp");
			 filterNameCreditProfileDataList.add("birthDate");
			 filterNameCreditProfileDataList.add("addressTypeCd");
			 filterNameCreditProfileDataList.add("creditAddressTypeCd");
			 filterNameCreditProfileDataList.add("passport");
			 filterNameCreditProfileDataList.add("healthCard");
			 filterNameCreditProfileDataList.add("expiryDate");
			 filterNameCreditProfileDataList.add("reportIndicator");
			 filterNameCreditProfileDataList.add("creditAssessmentCreationDate");
			 filterNameCreditProfileDataList.add("creditAssessmentSubTypeCd");
			 filterNameCreditProfileDataList.add("creditAssessmentTypeCd");
			 filterNameCreditProfileDataList.add("fraudMessageCdList");
			 filterNameCreditProfileDataList.add("productCategoryQualification");
			 filterNameCreditProfileDataList.add("creditAssessmentId");
			 filterNameCreditProfileDataList.add("provincialIdCard");
			 filterNameCreditProfileDataList.add("assessmentMessageCd");
	 			 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CreditProfileDataResult  = CompareObjectsUtil.traversingCompare(
					 input_CreditProfileData,
					 rslt_CreditProfileDataFromDB,
					 filterNameCreditProfileDataList);
			 CompareObjectsUtil.printCompareResult(CreditProfileDataResult);	 	
			 
			 
 			 ArrayList filterNameCustomerCollectionDataList = new ArrayList();
			 CustomerCollectionData rslt_CustomerCollectionDataFromDB=	 ((CreditDecisioningExistingCustomerCreditAssessmentRequest) getCreditAsmt_CreditAssessmentDetails.getCreditDecisioningInput()).getCustomerCollectionData() ;
			 CustomerCollectionData input_CustomerCollectionData =	 ((ExistingCustomerCreditAssessmentRequest) performCreditAssessment.getCreditAssessmentRequest()).getCustomerCollectionData() ;
			 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CustomerCollectionDataResult  = CompareObjectsUtil.traversingCompare(
					 input_CustomerCollectionData,
					 rslt_CustomerCollectionDataFromDB,
					 filterNameCustomerCollectionDataList);
			 CompareObjectsUtil.printCompareResult(CustomerCollectionDataResult); 
			 
		
	}

	private void verifyCreditBureauDataResultDocumentList(
			com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse aPullConsumerCreditReportResponse,
			CreditAssessmentDetails getCreditAssessmentRslt) {
		BaseReportResponse.ReportDocument input_ConsumerCreditReportResponseReportDocument= aPullConsumerCreditReportResponse.getConsumerCreditReportResponse().getReportDocument(); //bureauReportDocument
		CreditBureauDocument inputBureauReportDocument = input_ConsumerCreditReportResponseReportDocument.getBureauReportDocument();
		CreditBureauDocument inputPrintImageDocument = input_ConsumerCreditReportResponseReportDocument.getPrintImageDocument();
		
		List<CreditBureauDocument> rslt_CreditBureauDataResultDocumentList = getCreditAssessmentRslt.getCreditBureauDataResultDocumentList();
		for (CreditBureauDocument rslt_creditBureauDocument : rslt_CreditBureauDataResultDocumentList) {
			if( (rslt_creditBureauDocument.getDocumentType()).equalsIgnoreCase("TXT") ){
				//TODO compare   inputBureauReportDocument to creditBureauDocument.getDocumentType()
				System.out.println("");
			}else{
				//TODO compare   inputPrintImageDocument to creditBureauDocument.getDocumentType()
				System.out.println("");
			}
		}
	}
	private void verifyCreditBureauResult(
			com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse aPullConsumerCreditReportResponse,
			CreditAssessmentDetails getCreditAssessmentRslt) {
		CreditBureauResult input_CreditBureauResult= aPullConsumerCreditReportResponse.getConsumerCreditReportResponse().getCreditBureauResult();
		CreditBureauResult rslt_CreditBureauResult = getCreditAssessmentRslt.getCreditBureauDataResult();
		//compare aPullConsumerCreditReportResponse and rslt_CreditBureauResult
		
		 ArrayList filterNameList = new ArrayList();
		 filterNameList.add("id");
    	 filterNameList.add("bureauReportStatusDate");
    	 filterNameList.add("creationDate");
		 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRsltList  = CompareObjectsUtil.traversingCompare(
				 input_CreditBureauResult, 
				 rslt_CreditBureauResult,
				 filterNameList);
		 CompareObjectsUtil.printCompareResult(CompRsltList);
	}

	////////@Test
	public void testGetBaseCreditAssessment() throws Exception {
		CreditAssessmentTransaction aCreditAssessmentDetails = m_CreditAssessmentDataMgmtDao.getBaseCreditAssessment(63);
		assertNotNull("CreditAssessmentDetails result is null", aCreditAssessmentDetails);
		TestUtil.dump(aCreditAssessmentDetails);		
 
	} 
	// //////@Test
	public void testSearchCreditAssessments() throws Exception {
		long customerID=22189607;
		SearchCreditAssessmentsRequestCriteria asearchCreditAssessmentsRequestCriteria = new SearchCreditAssessmentsRequestCriteria();
		//Calendar c = Calendar.getInstance(); 
		//c.setTime(new Date()); 
		//c.add(Calendar.DATE, -1);
		//Date aDayBefore = c.getTime();
		//asearchCreditAssessmentsRequestCriteria.setAssesssmentFromDate(aDayBefore);
		//asearchCreditAssessmentsRequestCriteria.setAssesssmentToDate(creditAssessmentTransaction.getCreditAssessmentDate());
		//asearchCreditAssessmentsRequestCriteria.setCreditAssessmentStatus(creditAssessmentTransaction.getCreditAssessmentStatus());
		//asearchCreditAssessmentsRequestCriteria.setCreditAssessmentSubTypeCd(creditAssessmentTransaction.getCreditAssessmentSubTypeCd());
		//asearchCreditAssessmentsRequestCriteria.setCreditAssessmentTypeCd(creditAssessmentTransaction.getCreditAssessmentTypeCd());
		asearchCreditAssessmentsRequestCriteria.setCustomerID(customerID);
		List<CreditAssessmentTransaction> aCreditAssessmentTransactionList = 
					m_CreditAssessmentDataMgmtDao.searchCreditAssessments(asearchCreditAssessmentsRequestCriteria, EcrdaTestHelper.createAudiInfo());
		
		assertNotNull("Search Credit Assessment List returned empty list",aCreditAssessmentTransactionList);
		assertFalse("Search Credit Assessment List returned empty list", aCreditAssessmentTransactionList.size()!=0);
		for (CreditAssessmentTransaction creditAssessmentTransaction2 : aCreditAssessmentTransactionList) {
			System.out.println("************* creditAssessmentTransaction result ********");
			TestUtil.dump(creditAssessmentTransaction2);
		}
	}
	////////@Test
	public void testVoidCreditAssessment() throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
		m_CreditAssessmentDataMgmtDao.voidCreditAssessment(63, "REPLACED", EcrdaTestHelper.createAudiInfo());

	}

	//getCreditBureauTransResult
	@Test
	public void testGetCreditBureauTransResult() throws Throwable {
		long custid=119036752;
		System.out.println("custid= " + custid);
		CreditBureauResult aCreditBureauResult = m_CreditAssessmentDataMgmtDao.getCreditBureauTransResultByCustID(custid);
		TestUtil.dump(aCreditBureauResult);
		assertNotNull("aCreditBureauResult result is null", aCreditBureauResult);
	}
	public void testGetCreditBureauTransResult2() throws Throwable {
		
		//prep data use an exist CARID
		String requestFilename =TestFiles.FULL_ASSESSMENT_REOPEN_ASSESSMENT.xml";
		PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(requestFilename);
		
		//expire older CreditBureauTransaction
		m_DecisioningDaoImpCommon.expireCreditBureauTransaction(
					crdasmtId, 
					EnterpriseCreditAssessmentConsts.CREDIT_BUREAU_TRN_STATUS_VOID, 
					XMLUtility.createAuditInfo());
		//TODO to properly test get bureua , call fico first to prep data 
		// perform crd asmt , 
		//store data 
		String aConsumerCreditReportResponse_filename=TestFiles.CGW/pullConsumerCreditReportResponse.xml";
		com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse aPullConsumerCreditReportResponse = XMLUtility.createPullConsumerCreditReportResponse(aConsumerCreditReportResponse_filename);    
		m_DecisioningDaoImpCommon.storeCreditBureauResult(
				aPullConsumerCreditReportResponse.getConsumerCreditReportResponse(), 
				crdasmtId, 
				performCreditAssessment.getCreditAssessmentRequest(), 
				XMLUtility.createAuditInfo());

		long custid=performCreditAssessment.getCreditAssessmentRequest().getCustomerID();
		long custid=22189607;
		System.out.println("custid= " + custid);
		CreditBureauResult aCreditBureauResult = m_CreditAssessmentDataMgmtDao.getCreditBureauTransResultByCustID(custid);
		assertNotNull("aCreditBureauResult result is null", aCreditBureauResult);
		ArrayList filterName_CreditBureauResult_List = new ArrayList();
		filterName_CreditBureauResult_List.add("bureauReportStatusDate");
		filterName_CreditBureauResult_List.add("creationDate");
		filterName_CreditBureauResult_List.add("fraudWarningList");
		 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRslt_CreditBureauResult_  = CompareObjectsUtil.traversingCompare(
				 aPullConsumerCreditReportResponse.getConsumerCreditReportResponse().getCreditBureauResult(), 
				 aCreditBureauResult,
				 filterName_CreditBureauResult_List);
		 CompareObjectsUtil.printCompareResult(CompRslt_CreditBureauResult_); 
		//TestUtil.dump(aCreditBureauResult);		
 
		 List<FraudWarning> fraudWarning_input =aPullConsumerCreditReportResponse.getConsumerCreditReportResponse().getCreditBureauResult().getFraudWarningList();
		 List<FraudWarning> fraudWarning_rslt =aCreditBureauResult.getFraudWarningList();
		 ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRslt_FraudWarning  = CompareObjectsUtil.traversingCompare(
				 fraudWarning_input, 
				 fraudWarning_rslt,
				 filterName_CreditBureauResult_List);
		 CompareObjectsUtil.printCompareResult(CompRslt_FraudWarning); 

	}



}
*/