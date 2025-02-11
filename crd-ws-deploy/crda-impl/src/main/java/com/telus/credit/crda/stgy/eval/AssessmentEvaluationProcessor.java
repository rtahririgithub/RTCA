package com.telus.credit.crda.stgy.eval;


import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public interface AssessmentEvaluationProcessor {

    public CreditAssessmentResultWrapper performAssessmentEvaluation(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails creditAssessmentTransactionDetails,
            boolean failOverIndicator
    ) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public void setDecisioningDao(
            DecisioningDao aDecisioningDao);

    public void setDecisioningEngineAdapter(
            DecisioningEngineAdapter mDecisioningEngineAdapter);
}
