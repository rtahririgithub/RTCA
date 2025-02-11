package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;

public class UnifiedCreditFullAssessmentAuto extends FullAssessment {
    public UnifiedCreditFullAssessmentAuto(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }

    @Override
    //Overridden Just for logging
    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
         return super.performCreditAssessment(creditAssessmentRequest, auditInfo);
    }

    @Override
        //Overridden Just for logging
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
    }
}
