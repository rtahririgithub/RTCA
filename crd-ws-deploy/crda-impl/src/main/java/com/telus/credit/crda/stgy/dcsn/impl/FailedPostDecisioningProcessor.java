package com.telus.credit.crda.stgy.dcsn.impl;


import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public class FailedPostDecisioningProcessor
        extends DisregardDecisioningRsltPostDecisioningProcesser {

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
        super.performPostDecisioning(creditAssessmentRequest, auditInfo, decisioningCreditAssessmentResultWrapper, aCreditAssessmentTransactionDetails, failOverIndicator);
        return returnPostProcessorResult();
    }

    @Override
    public String returnPostProcessorResult() {
        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR);
        return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR; 
    }

}