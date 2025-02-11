package com.telus.credit.crda.asmtclassification.nondcsn;


import com.telus.credit.crda.dao.nondcn.NonDecisioningTrxDao;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;


public class AuditBureauConsentAssessment extends NonDecisioningAssessment {
    public AuditBureauConsentAssessment(
            NonDecisioningTrxDao aOverrideDao) {
        super(aOverrideDao);

    }

    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performCreditAssessment(creditAssessmentRequest, auditInfo);
    }
}