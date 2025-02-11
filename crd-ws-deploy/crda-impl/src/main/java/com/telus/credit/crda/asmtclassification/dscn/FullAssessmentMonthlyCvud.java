package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.factory.PostDecisioningProcesserFactory;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;

public class FullAssessmentMonthlyCvud extends ReAssessment {
	public boolean m_cVUDTrialRun;
    public boolean iscVUDTrialRun() {
		return m_cVUDTrialRun;
	}
	public void setcVUDTrialRun(boolean cVUDTrialRun) {
		this.m_cVUDTrialRun = cVUDTrialRun;
	}


	public FullAssessmentMonthlyCvud(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao); 
    }


    /**
     * @url element://model:project::telus-crd-crda-impl/design:view:::Vam8ynkhjuep240-dpazir
     */

    @Override

    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        CreditAssessmentTransactionResult aCreditAssessmentTransactionResult = super.performCreditAssessment(creditAssessmentRequest, auditInfo);
        return aCreditAssessmentTransactionResult;
    }

    @Override
        //Overridden Just for logging
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
    }
    
    //Overridden  to support trial run
	public PostDecisioningProcessor createPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper) {
		return m_postDecisioningProcesser =
                PostDecisioningProcesserFactory.createPostDecisioningProcessor(
                        creditAssessmentRequest,
                        decisioningCreditAssessmentResultWrapper,
                        iscVUDTrialRun()
                );
	}
}
