package com.telus.credit.crda.asmtclassification.dscn;

/**
 * User: T828927
 * Date: 10/07/13
 */


import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;

public class FullAssessment extends DecisioningAssessment {


    public FullAssessment(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }

    //Overridden Just for loggingAuditInfo
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
    }
    
    @Override
    /*
     * Validation for all type FULL_ASSESSMENT	and subtypes AUTO_ASSESSMENT and GET_BUREAU_DATA
     * */
    public void validateCreditAssessmentRequest(CreditAssessmentRequest aCreditAssessmentRequest)
    throws EnterpriseCreditAssessmentPolicyException {
		FullCreditAssessmentRequest fullCreditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
		
		// mandatory fields validation
		CrdaValidationUtility.validateMandatoryCreditCustomerInfoFields(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditCustomerInfo()); 
		CrdaValidationUtility.validateMandatoryCreditProfile_CreditAddress_Fields( fullCreditAssessmentRequest.getCustomerID(),fullCreditAssessmentRequest.getCreditProfileData());
		
		//  fields's value validation 
		CrdaValidationUtility.validateCreditProfileFieldsValues(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditProfileData(), m_creditProfileBr);
		
    }
}