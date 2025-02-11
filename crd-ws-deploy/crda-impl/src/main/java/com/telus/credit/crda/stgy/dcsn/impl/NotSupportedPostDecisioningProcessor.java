package com.telus.credit.crda.stgy.dcsn.impl;


import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public class NotSupportedPostDecisioningProcessor extends SuccessPostDecisioningProcessor {
    @Override
    public String performPostDecisioning(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException,
            EnterpriseCreditAssessmentPolicyException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performPostDecisioning(creditAssessmentRequest, auditInfo, decisioningCreditAssessmentResultWrapper, aCreditAssessmentTransactionDetails, failOverIndicator);
    }
    @Override
    public String returnPostProcessorResult() {
        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.ASSESSMENT_RESILIENCE);
    
        return EnterpriseCreditAssessmentConsts.ASSESSMENT_RESILIENCE; 
    }
}
