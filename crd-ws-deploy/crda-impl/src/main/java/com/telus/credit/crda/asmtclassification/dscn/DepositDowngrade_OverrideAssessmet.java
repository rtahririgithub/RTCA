/*package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;

public class DepositDowngrade_OverrideAssessmet extends OverrideAssessment {
    public DepositDowngrade_OverrideAssessmet(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }
 
    @Override
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest aOverrideCreditAssessmentBaseRequest=(com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest)creditAssessmentRequest;
        
        aOverrideCreditAssessmentBaseRequest.setNewCreditValueCd("D");
         return super.performDecisioningEngineCreditAssessment(aOverrideCreditAssessmentBaseRequest);
    }
}
*/