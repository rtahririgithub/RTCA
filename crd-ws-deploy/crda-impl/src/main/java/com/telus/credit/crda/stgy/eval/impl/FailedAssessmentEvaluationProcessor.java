package com.telus.credit.crda.stgy.eval.impl;


import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public class FailedAssessmentEvaluationProcessor implements
        AssessmentEvaluationProcessor {


    DecisioningDao m_decisioningDao = null;
    public DecisioningEngineAdapter m_decisioningEngineAdapter;

    @Override
    public void setDecisioningDao(
            DecisioningDao aDecisioningDao) {
        m_decisioningDao = aDecisioningDao;

    }

    @Override
    public CreditAssessmentResultWrapper performAssessmentEvaluation(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails creditAssessmentTransactionDetails,
            boolean failOverIndicator) throws EnterpriseCreditAssessmentPolicyException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        StringBuffer extramsg  = new StringBuffer ("");
	     extramsg.append("[" +"for Customer ID : "+ creditAssessmentRequest.getCustomerID()+ "]");
        	throw new EnterpriseCreditAssessmentPolicyException(	           			
					EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASMT_RESULT_CD_EXCEPTION_STR 
					+ extramsg + 
					": Decisioning Assessment Result Cd =" + decisioningCreditAssessmentResultWrapper.getAssessmentResultCd() +
					" Decisioning Assessment Result Reason Cd =" + decisioningCreditAssessmentResultWrapper.getAssessmentResultReasonCd()   +
					" Decisioning Assessment Message  Cd =" + decisioningCreditAssessmentResultWrapper.getAssessmentMessageCd()
					,EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASMT_RESULT_CD_EXCEPTION
					); 
    }

    @Override
    public void setDecisioningEngineAdapter(
            DecisioningEngineAdapter aDecisioningEngineAdapter) {
        m_decisioningEngineAdapter = aDecisioningEngineAdapter;
    }
}
