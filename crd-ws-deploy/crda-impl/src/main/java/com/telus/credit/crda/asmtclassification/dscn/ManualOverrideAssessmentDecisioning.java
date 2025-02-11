package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.OverrideCreditAssessmentRequest;

public class ManualOverrideAssessmentDecisioning extends OverrideAssessment {

    public ManualOverrideAssessmentDecisioning(DecisioningEngineAdapter aDecisioningEngineAdapter, DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }


    @Override
        //Overridden Just for logging
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
    }

    /*
     * Validation for this specific assessment
     * Value of CreditValueCd is validated at the parent OverrideAssessment as it is comment among all OverrideAssessment subtypes
     * Validate values of newCreditValueCd and newFraudIndictorCd if provided.
     * */
    @Override
    public void validateCreditAssessmentRequest(
            CreditAssessmentRequest aCreditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException {
        super.validateCreditAssessmentRequest(aCreditAssessmentRequest);

        OverrideCreditAssessmentRequest overrideCreditAssessmentRequest = (OverrideCreditAssessmentRequest) aCreditAssessmentRequest;
        
        // mandatory fields validation 
        CrdaValidationUtility.validateMandatoryCreditCustomerInfoFields(overrideCreditAssessmentRequest.getCustomerID(), 
        									  				  overrideCreditAssessmentRequest.getCreditCustomerInfo());
      //crd val mandatory and crd value and fraudind value validation if provided and not empty
        CrdaValidationUtility.validate_new_CrdValue_FraudInd(aCreditAssessmentRequest,overrideCreditAssessmentRequest);   

        //value validation if provided and not empty
        CrdaValidationUtility.validate_CrdPrflID_CrdPRflStatusCd_CrdWorthiness_CustID_FieldsValues(overrideCreditAssessmentRequest.getCustomerID(), overrideCreditAssessmentRequest.getCreditProfileData(), m_creditProfileBr);

    }


	

}