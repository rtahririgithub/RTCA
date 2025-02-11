package com.telus.credit.crda.asmtclassification.dscn;


import java.util.Date;

import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.asmtclassification.Assessment;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.factory.AssessmentEvaluationProcessorFactory;
import com.telus.credit.crda.factory.PostDecisioningProcesserFactory;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.CreditProfileBr;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;



public class DecisioningAssessment extends Assessment {
    public DecisioningEngineAdapter m_decisioningEngineAdapter;
    public DecisioningDao m_decisioningDao;
    public PostDecisioningProcessor m_postDecisioningProcesser;
    public AssessmentEvaluationProcessor m_assessmentEvaluationProcessor;

    public CreditProfileBr m_creditProfileBr;


    public boolean m_failOverIndicator = true;
    public DozerBeanMapper m_mapper;

    public void setMapper(DozerBeanMapper aMapper) {
        this.m_mapper = aMapper;
    }

    public DecisioningAssessment(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        this.m_decisioningEngineAdapter = aDecisioningEngineAdapter;
        this.m_decisioningDao = aDecisioningDao;

    }

    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        validateCreditAssessmentRequest(creditAssessmentRequest);
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails = new CreditAssessmentTransactionDetails();
        aCreditAssessmentTransactionDetails.setMapper(m_mapper);
        
        populateCrdAsmtTrxDetailsCommonAtrributes(creditAssessmentRequest,auditInfo, aCreditAssessmentTransactionDetails);

        LogUtil.infolog("-->call Fico ");
        CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper = performDecisioningEngineCreditAssessment(creditAssessmentRequest);
       
        //populate aCreditAssessmentTransactionDetails with Fico result 
        aCreditAssessmentTransactionDetails.setCreditDecisioningResult(decisioningCreditAssessmentResultWrapper);
        //current date
        aCreditAssessmentTransactionDetails.setCreditAssessmentDate(new Date());

        aCreditAssessmentTransactionDetails.setCreditAssessmentStatus(EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING);
          //************perform the post Decision-ing processor  tasks      
        
        //call Factory  to create proper postDecisioningProcesser
        m_postDecisioningProcesser = createPostDecisioningProcessor(creditAssessmentRequest, decisioningCreditAssessmentResultWrapper);
        m_postDecisioningProcesser.setDecisioningDao(m_decisioningDao);


        //*************perform the post Decisioning evaluation tasks
        String postDecisioningProcessResult = m_postDecisioningProcesser.performPostDecisioning(
                creditAssessmentRequest,
                auditInfo,
                decisioningCreditAssessmentResultWrapper,
                aCreditAssessmentTransactionDetails,
                isFailOverIndicator());

        //analyse postDecisioningProcessResult and perform the post Decisioning business rule 
        m_assessmentEvaluationProcessor =
                (AssessmentEvaluationProcessorFactory.create(postDecisioningProcessResult));
        m_assessmentEvaluationProcessor.setDecisioningDao(m_decisioningDao);
        m_assessmentEvaluationProcessor.setDecisioningEngineAdapter(m_decisioningEngineAdapter);
        try {
            decisioningCreditAssessmentResultWrapper = m_assessmentEvaluationProcessor.performAssessmentEvaluation(
                    creditAssessmentRequest,
                    auditInfo,
                    decisioningCreditAssessmentResultWrapper,
                    aCreditAssessmentTransactionDetails,
                    isFailOverIndicator());
        } catch (Throwable e) { 
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", creditAssessmentRequest.getCustomerID(), e);
        }
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatus(EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);
        aCreditAssessmentTransactionDetails.setCreditAssessmentDataSourceCd(InternalRules.determineCreditAssessmentDataSourceCd(aCreditAssessmentTransactionDetails));
 
        LogUtil.infolog("->Decisiong Assessment Returning the CreditAssessmentTransactionResult.");
        aCreditAssessmentTransactionDetails.setCreditDecisioningResult(decisioningCreditAssessmentResultWrapper);
        return (CreditAssessmentTransactionResult) aCreditAssessmentTransactionDetails;
    }

    //Subclasses can override this method
	public PostDecisioningProcessor createPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper) {
		return m_postDecisioningProcesser =
                PostDecisioningProcesserFactory.createPostDecisioningProcessor(
                        creditAssessmentRequest,
                        decisioningCreditAssessmentResultWrapper
                );
	}

	private void populateCrdAsmtTrxDetailsCommonAtrributes(
			CreditAssessmentRequest creditAssessmentRequest,
			AuditInfo auditInfo,
			CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails) {
		aCreditAssessmentTransactionDetails.setCustomerID(creditAssessmentRequest.getCustomerID());
        aCreditAssessmentTransactionDetails.setUserID((auditInfo != null ? auditInfo.getUserId() : null));
        aCreditAssessmentTransactionDetails.setCreditAssessmentTypeCd(creditAssessmentRequest.getCreditAssessmentTypeCd());
        aCreditAssessmentTransactionDetails.setCreditAssessmentSubTypeCd(creditAssessmentRequest.getCreditAssessmentSubTypeCd());
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatus(EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING);
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatusReasonCd(EnterpriseCreditAssessmentConsts.EMPTY_STR);
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatusDate(new Date());
        aCreditAssessmentTransactionDetails.setChannelID(creditAssessmentRequest.getChannelID());       
        aCreditAssessmentTransactionDetails.setCommentTxt(creditAssessmentRequest.getCommentTxt());
	}


    //Subclasses can override this method
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        CreditAssessmentResultWrapper decisioningCreditAssessmentResult = m_decisioningEngineAdapter.performCreditAssessment(creditAssessmentRequest);
        return decisioningCreditAssessmentResult;
    }


    /*
     * 		subclasses can override and add to this method to do specific validation.
     * */
    public void validateCreditAssessmentRequest(CreditAssessmentRequest aCreditAssessmentRequest)
            throws EnterpriseCreditAssessmentPolicyException {
    	//common validation
    /*
    	FullCreditAssessmentRequest fullCreditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;

        CrdaUtility.validateMandatoryCreditCustomerInfoFields(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditCustomerInfo()); 
        CrdaUtility.validateMandatoryCreditProfileFields( fullCreditAssessmentRequest.getCustomerID(),fullCreditAssessmentRequest.getCreditProfileData());
        CrdaUtility.validateCreditProfileData(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditProfileData(), m_creditProfileBr);
      */  
    }

    //setters
    public boolean isFailOverIndicator() {
        return m_failOverIndicator;
    }

    public void setFailOverIndicator(boolean failOverIndicator) {
        this.m_failOverIndicator = failOverIndicator;
    }

    public void setCreditProfileBr(CreditProfileBr mCreditProfileBr) {
        m_creditProfileBr = mCreditProfileBr;
    }

    public void setDecisioningDao(DecisioningDao aDecisioningDao) {
        this.m_decisioningDao = aDecisioningDao;
    }

    public void setDecisioningEngineAdapter(DecisioningEngineAdapter aDecisioningEngineAdapter) {
        m_decisioningEngineAdapter = aDecisioningEngineAdapter;
    }
}

