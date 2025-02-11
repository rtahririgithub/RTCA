package com.telus.credit.crda.asmtclassification.dscn;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
/*    Unmerge Customer B from Customer A
	1-Customer B gets new credit profile
	2-Customer A keeps the current profile 

	Batch calls eCrda for Customer B. Providing the new credit value in creditProfileData.creditWorthiness in the input request .(Credit Value is defaulted to "R")
	eCrda sets new credit value from the credit value from existing credit worthiness in input request.
	eCrda calls Fico to retrieve corresponding changes to other credit assessment result attributes such as Product Set Qualification.
	eCrda Create a credit assessment transaction with transaction type "Override Assessment" and subtype "Unmerged Credit Profile"
	Batch caller app, Update the Credit Value, together with affected credit assessment result attributes returned from FICO, as well as credit assessment date and ID, in the Credit Profile
*/
public class UnmergedOverrideAssessmet extends OverrideAssessment {
    public UnmergedOverrideAssessmet(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }

    @Override
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest aOverrideCreditAssessmentBaseRequest=
        		(com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest)creditAssessmentRequest;
        String newCreditValueCd= "";
        if (aOverrideCreditAssessmentBaseRequest!= null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData()!=null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness()!=null){
         	newCreditValueCd=aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness().getCreditValueCd();
        }
        aOverrideCreditAssessmentBaseRequest.setNewCreditValueCd(newCreditValueCd);
         return super.performDecisioningEngineCreditAssessment(aOverrideCreditAssessmentBaseRequest);
    }
    
    @Override
    public void validateCreditAssessmentRequest(
            CreditAssessmentRequest aCreditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException {
    	super.validateCreditAssessmentRequest(aCreditAssessmentRequest);

        com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest aOverrideCreditAssessmentBaseRequest=
    		(com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest)aCreditAssessmentRequest;    	
        
        // mandatory fields validation 
        CrdaValidationUtility.validateMandatoryCreditCustomerInfoFields(aOverrideCreditAssessmentBaseRequest.getCustomerID(), 
        		aOverrideCreditAssessmentBaseRequest.getCreditCustomerInfo());
        
        // mandatory field CreditProfileData.CreditWorthiness.CreditValueCd
        String creditWorthinnessCreditValueCd= "";
        if (aOverrideCreditAssessmentBaseRequest!= null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData()!=null &&
        		aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness()!=null){
        	creditWorthinnessCreditValueCd=aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness().getCreditValueCd();
        }
    	
        if (creditWorthinnessCreditValueCd.trim().equals("") ){
            StringBuffer extramsg = new StringBuffer("");
            extramsg.append("[" + "for Customer ID : " + aCreditAssessmentRequest.getCustomerID() + "]");
                throw new EnterpriseCreditAssessmentPolicyException(
                        EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_VALUE_CODE_STR + extramsg,
                        EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_VALUE_CODE);        	
        }     
        
    	//  fields's value validation if provided and not empty
        CrdaValidationUtility.validate_CrdPrflID_CrdPRflStatusCd_CrdWorthiness_CustID_FieldsValues(aOverrideCreditAssessmentBaseRequest.getCustomerID(), aOverrideCreditAssessmentBaseRequest.getCreditProfileData(), m_creditProfileBr);

        
    }
}
