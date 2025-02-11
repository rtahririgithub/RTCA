package com.telus.credit.crda.stgy.dcsn.impl;

import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public class DisregardDecisioningRsltPostDecisioningProcesser implements
		PostDecisioningProcessor {
	public DecisioningDao m_decisioningDao;

	@Override
	public void setDecisioningDao(DecisioningDao aDecisioningDao) {
		m_decisioningDao = aDecisioningDao;
	}

	@Override
	public String performPostDecisioning(
			CreditAssessmentRequest creditAssessmentRequest,
			AuditInfo auditInfo,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
			CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
			boolean failOverIndicator)
			throws EnterpriseCreditAssessmentServiceException,
			EnterpriseCreditAssessmentPolicyException {
		LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread()
				.getStackTrace()[1].getMethodName(), Thread.currentThread()
				.getStackTrace()[1].getClassName()));
		// DO NOTHING
		LogUtil.infolog("-->no DB trx");
        return returnPostProcessorResult();
		    }

		
	    public String returnPostProcessorResult() {
	        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);
	        return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS; //ASSESSMENT_RESULT_CODE_ERROR
	    }
}