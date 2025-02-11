package com.telus.credit.crda.stgy.eval.impl;


import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public class SuccessAssessmentEvaluationProcessor implements
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
            boolean failOverIndicator) {
        LogUtil.traceCalllog( LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        LogUtil.infolog("-->Return creditAssessmentResult");
        return decisioningCreditAssessmentResultWrapper;

    }

    @Override
    public void setDecisioningEngineAdapter(
            DecisioningEngineAdapter aDecisioningEngineAdapter) {
        m_decisioningEngineAdapter = aDecisioningEngineAdapter;
    }
}
