package com.telus.credit.crda.webservice.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.credit.crda.adapter.DocumentumSvcAdapter;
import com.telus.credit.crda.asmtclassification.Assessment;
import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.dao.mgmt.CreditAssessmentDataMgmtDao;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.factory.AssessmentFactory;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.PingUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.repo.dao.CreditDocSearch;
import com.telus.credit.repo.dao.CreditRepoDao;
import com.telus.credit.repo.domain.CreditDoc;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.EnterpriseCreditAssessmentServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;



public class EnterpriseCreditAssessmentServiceImpl implements EnterpriseCreditAssessmentServicePortType {
    @Autowired
    protected CreditAssessmentDataMgmtDao m_creditAssessmentDataMgmtDao;
	@Autowired 
	protected PingUtil m_PingUtil;	

	public static final Log log = LogFactory.getLog(EnterpriseCreditAssessmentServiceImpl.class);

    public void setCreditAssessmentDataMgmtDao(
            CreditAssessmentDataMgmtDao aCreditAssessmentDataMgmtDao) {
        this.m_creditAssessmentDataMgmtDao = aCreditAssessmentDataMgmtDao;
    }

    //@Autowired
    public DocumentumSvcAdapter m_documentumSvcAdapter;

    public void setDocumentumSvcAdapter(
            DocumentumSvcAdapter aDocumentumSvcAdapter) {
        this.m_documentumSvcAdapter = aDocumentumSvcAdapter;
    }
    
    @Autowired 
    protected CreditRepoDao m_creditRepoDao;

    public void setCreditRepo(CreditRepoDao aCreditRepoDao) {
		this.m_creditRepoDao = aCreditRepoDao;
	}

	@Override
    public CreditAssessmentDetails getCreditAssessment(long creditAssessmentID,
                                                       AuditInfo auditInfo) throws PolicyException, ServiceException {
    	
    	//CrdaUtility.print_GetCreditAssessment_Request(creditAssessmentID, auditInfo);

        String operationName = "getCreditAssessment";
        CreditAssessmentDetails result = null;
        try {
            long startTime = System.nanoTime();

            CrdaValidationUtility.validateGetCreditAssessment(creditAssessmentID, auditInfo);

            result = m_creditAssessmentDataMgmtDao.getCreditAssessment(creditAssessmentID);
            LogUtil.infolog("IGNORE : Total execution time to call " + LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()) + "  in millis: " + (System.nanoTime() - startTime) / 1000000);

        } catch (EnterpriseCreditAssessmentPolicyException e) {
        	log.error(e);
            throw CreditAssessmentExceptionFactory.createPolicyException(operationName, "Exception: " + e.getClass().getName() + "for credit asssessment id: "
                    + creditAssessmentID, e);
        }catch (Throwable e) {
        	log.error(e);
            throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for credit asssessment id: "
                    + creditAssessmentID, e);
        }
        return result;

    }

    @Override
    public CreditBureauDocument getCreditBureauDocument(
            String creditBureauDocumentID, AuditInfo auditInfo)
            throws PolicyException, ServiceException {
        String operationName = "getCreditBureauDocument for creditBureauDocumentID" + creditBureauDocumentID;
        long startTime = System.nanoTime();
        //
        try {
        	CrdaValidationUtility.validateGetCreditBureauDocument(creditBureauDocumentID, auditInfo);
        } catch (EnterpriseCreditAssessmentPolicyException e1) {

            throw CreditAssessmentExceptionFactory.createPolicyException(
                    operationName,//op name
                    "Exception: " + this.getClass().getName() + " validategetCreditBureauDocumentInput",
                    e1);
        }
        CreditBureauDocument creditBureauDocument= null;
        String documentPathId = null;
        try {
	        	documentPathId = m_creditAssessmentDataMgmtDao.getDocumentumPathId( creditBureauDocumentID );
        

        } catch (EnterpriseCreditAssessmentPolicyException e) {
        	// if documentPathId is not found it means the document is not in documentum, search in credit repo
        	if (e.getErrorCode().equals(EnterpriseCreditAssessmentExceptionCodes.OBJECT_NOT_FOUND_EXCEPTION)) {
				try { 
					HashMap<String, Object> creditBureauTransMap = m_creditAssessmentDataMgmtDao.getCreditBureauMap(creditBureauDocumentID);
					creditBureauDocument = this.getDocumentFromCreditRepo(creditBureauTransMap);
				} catch (EnterpriseCreditAssessmentPolicyException e1) {
					throw CreditAssessmentExceptionFactory.createPolicyException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
		                    + creditBureauDocumentID, e1);
				} catch (EnterpriseCreditAssessmentServiceException e1) {
					throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
		                    + creditBureauDocumentID, e1);
				} catch (Throwable e1) {
		            throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
		                    + creditBureauDocumentID, e1);
		        }
        		
        	} else {
	            throw CreditAssessmentExceptionFactory.createPolicyException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
	                    + creditBureauDocumentID, e);
        	}
        } catch (EnterpriseCreditAssessmentServiceException e) {
            throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
                    + creditBureauDocumentID, e);
        } catch (Throwable e) {
            throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
                    + creditBureauDocumentID, e);
        }
        
        if (documentPathId != null) {
	        try {
				creditBureauDocument = m_documentumSvcAdapter.getDocumentumDocuemnt(documentPathId, auditInfo);
			} catch (EnterpriseCreditAssessmentPolicyException e) {
				throw CreditAssessmentExceptionFactory.createPolicyException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
	                    + creditBureauDocumentID, e);
			} catch (EnterpriseCreditAssessmentServiceException e) {
				throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
	                    + creditBureauDocumentID, e);
	        } catch (Throwable e) {
	            throw CreditAssessmentExceptionFactory.createServiceException(operationName, "Exception: " + e.getClass().getName() + "for creditBureauDocumentID: "
	                    + creditBureauDocumentID, e);
	        }
        }

        LogUtil.infolog("IGNORE : Total execution time to call " + LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()) + "  in millis: " + (System.nanoTime() - startTime) / 1000000);

        return creditBureauDocument;
    }

    public String ping() throws PolicyException, ServiceException {
		String aPingResponse = "Success. Version v2_0";
		try{
			aPingResponse = m_PingUtil.getpingResultTxt();						
		}catch(Throwable e){
			log.error(e.getMessage(), e);
			String stacktraceMsg ="[" +  LogUtil.getInstance().getOnelineStackTrace(e) + "]";
			aPingResponse = stacktraceMsg;
		}
		return aPingResponse;
	}

    
    @Override
    public List<CreditAssessmentTransaction> searchCreditAssessmentList(
            SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
            AuditInfo auditInfo) throws PolicyException, ServiceException {
    	//CrdaUtility.print_aSearchCreditAssessmentList_Request(searchCreditAssessmentsRequestCriteria, auditInfo);
        try {
            long startTime = System.nanoTime();
            CrdaValidationUtility.validateSearchCreditAssessments(searchCreditAssessmentsRequestCriteria, auditInfo);


            List<CreditAssessmentTransaction> aCreditAssessmentTransactionList = m_creditAssessmentDataMgmtDao.searchCreditAssessments(searchCreditAssessmentsRequestCriteria, auditInfo);

            LogUtil.infolog("IGNORE : Total execution time to call " + LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()) + "  in millis: " + (System.nanoTime() - startTime) / 1000000);

            return aCreditAssessmentTransactionList;
        } catch (EnterpriseCreditAssessmentPolicyException e1) {

            throw CreditAssessmentExceptionFactory.createPolicyException(
                    "searchCreditAssessments",//op name
                    "Exception: " + this.getClass().getName() + "validatesearchCreditAssessmentsInput",
                    e1);
        } catch (Throwable e) {
            throw CreditAssessmentExceptionFactory.createServiceException(
                    "searchCreditAssessments", "Exception: " + this.getClass().getName(), e);
        }

    }



    @Override
    public void voidCreditAssessment(long creditAssessmentID,
                                     String voidReasonCd, AuditInfo auditInfo) throws PolicyException, ServiceException {
    	
    	//CrdaUtility.print_VoidCreditAssessment_Request(creditAssessmentID, voidReasonCd,auditInfo);

        long startTime = System.nanoTime();
        try {
            m_creditAssessmentDataMgmtDao.voidCreditAssessment(creditAssessmentID, voidReasonCd, auditInfo);
        } catch (EnterpriseCreditAssessmentPolicyException e1) {
            throw CreditAssessmentExceptionFactory.createPolicyException(
                    "voidCreditAssessment",//op name
                    "Exception: " + this.getClass().getName() + "validatevoidCreditAssessmentInput",
                    e1);
        } catch (Throwable e) {
            throw CreditAssessmentExceptionFactory.createServiceException(
                    "searchCreditAssessments", "Exception: " + this.getClass().getName(), e);
        }
        LogUtil.infolog("IGNORE : Total execution time to call " + LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()) + "  in millis: " + (System.nanoTime() - startTime) / 1000000);


    }
 

    @Override

    public CreditAssessmentTransactionResult performCreditAssessment(
            CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
            throws PolicyException, ServiceException {

        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        long startTime = System.nanoTime();
        try {
        	CrdaValidationUtility.validatePerformCreditAssessmentInput(creditAssessmentRequest, auditInfo);
        } catch (EnterpriseCreditAssessmentPolicyException e1) {
            throw CreditAssessmentExceptionFactory.createPolicyException(
                    "performCreditAssessment",//op name
                    "Exception: " + this.getClass().getName() + "validatePerformCreditAssessmentInput",
                    e1);
        }catch (EnterpriseCreditAssessmentServiceException e) {
        	throw CreditAssessmentExceptionFactory.createServiceException(
                    "performCreditAssessment",//op name
                    "Exception: " + this.getClass().getName() + "validatePerformCreditAssessmentInput",
                    e);
			}

        CreditAssessmentTransactionResult creditAssessmentTransactionResult = null;
        try {

            //get the proper asmt class
            Assessment assessment = AssessmentFactory.createCreditAssessment(creditAssessmentRequest);
            
            //get proper comment based on type/subtype
            creditAssessmentRequest.setCommentTxt(InternalRules.lookupCommentByAsmtTypeSubtype(creditAssessmentRequest));
            
            //call the performCreditAssessment operation
            creditAssessmentTransactionResult = assessment.performCreditAssessment(creditAssessmentRequest, auditInfo);

        } catch (EnterpriseCreditAssessmentPolicyException e) {
            throw CreditAssessmentExceptionFactory.createPolicyException(
                    "performCreditAssessment", "Exception: " + this.getClass().getName(), e);
        } catch (EnterpriseCreditAssessmentServiceException e) {
            throw CreditAssessmentExceptionFactory.createServiceException(
                    "performCreditAssessment", "Exception: " + this.getClass().getName(), e);
        } catch (Throwable e) {
            throw CreditAssessmentExceptionFactory.createServiceException(
                    "performCreditAssessment", "Exception: " + this.getClass().getName(), e);
        }
        LogUtil.infolog("IGNORE : Total execution time to call " + LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()) + "  in millis: " + (System.nanoTime() - startTime) / 1000000);
        return creditAssessmentTransactionResult;
    }

    public PingUtil getPingUtil() {
		return m_PingUtil;
	}
	public void setPingUtil(PingUtil m_PingUtil) {
		this.m_PingUtil = m_PingUtil;
	}
	
	private CreditBureauDocument getDocumentFromCreditRepo(HashMap<String, Object> creditBureauTransMap) 
			throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
		log.debug("getDocumentFromCreditRepo method...");
		CreditDocSearch creditDocSearch = new CreditDocSearch();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date)creditBureauTransMap.get("createTs"));
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);

    	String docType = (String)creditBureauTransMap.get("docType");
		creditDocSearch.setPartitionKey(cal.getTime());
		creditDocSearch.setExternalResourceId((Long)creditBureauTransMap.get("carId"));
		creditDocSearch.setExternalResourceTypeCode(CreditDoc.EXT_RSC_TYPE_CAR_ID);
		if (docType.equals(CreditAssessmentDaoConstants.REPORT_DOC_TYPE_TXT)) {
			creditDocSearch.setDocType(CreditDoc.DOC_TYP_CONS_CRD_RPT_IMG);
		} else {
			creditDocSearch.setDocType(CreditDoc.DOC_TYP_CONS_CRD_RPT_RAW);
		}
		
		CreditBureauDocument creditBureauDoc = new CreditBureauDocument();
		
		try {
			List<CreditDoc> l_doc = m_creditRepoDao.searchForCrditDocuments(creditDocSearch,true);
			if (!l_doc.isEmpty()) {
				CreditDoc doc = l_doc.get(0);
				log.debug("Document Content: " + new String(doc.getDocContent()));
				creditBureauDoc.setDocumentContent(doc.getDocContent());
				creditBureauDoc.setDocumentType(doc.getDocType());
			}
		} catch (Throwable e) {
			CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , "CAR  id", (Long)creditBureauTransMap.get("carId"), e);
		} 
		return creditBureauDoc;
	}
}  