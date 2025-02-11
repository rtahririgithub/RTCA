package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;

/*
 * When user  "cancel"  an order , the system shall cancel the deposit invoice, revert credit value and un-book due date.
 * Before reverting  credit value, the CancelDepositInvOverrideAssessmet will perform a re-assessment in FICO.
 * Fico will lookup Deposit Table to get original TELUS decision code and credit value.
 * */
public class CancelDepositInvOverrideAssessmet extends OverrideAssessment {

    public CancelDepositInvOverrideAssessmet(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }

    //Overridden Just for logging
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
    }
    @Override
    /*
     * validate Mandatory field creditWorthinessData.decisionCode
     * */
    public void validateCreditAssessmentRequest(
            CreditAssessmentRequest aCreditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException {
    	super.validateCreditAssessmentRequest(aCreditAssessmentRequest);

        com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest aOverrideCreditAssessmentBaseRequest=
    		(com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest)aCreditAssessmentRequest;    	
        
        //commented out to support existing customer with no decision code.
/*        String aDecisionCd= "";
        if (aOverrideCreditAssessmentBaseRequest!= null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData()!=null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness()!=null){
        	aDecisionCd=aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness().getDecisionCd();
        }
    	
        if (aDecisionCd.trim().equals("") ){
            StringBuffer extramsg = new StringBuffer("");
            extramsg.append("[" + "for Customer ID : " + aCreditAssessmentRequest.getCustomerID() + "]");
                throw new EnterpriseCreditAssessmentPolicyException(
                        EnterpriseCreditAssessmentExceptionCodes.INVALID_DECISION_CODE_STR + extramsg,
                        EnterpriseCreditAssessmentExceptionCodes.INVALID_DECISION_CODE);        	
        } */   	

    	//  fields's value validation if provided and not empty
        CrdaValidationUtility.validate_CrdPrflID_CrdPRflStatusCd_CrdWorthiness_CustID_FieldsValues(aCreditAssessmentRequest.getCustomerID(), aOverrideCreditAssessmentBaseRequest.getCreditProfileData(), m_creditProfileBr);

    }
}