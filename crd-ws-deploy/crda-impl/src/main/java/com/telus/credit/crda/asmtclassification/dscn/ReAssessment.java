package com.telus.credit.crda.asmtclassification.dscn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.dao.mgmt.CreditAssessmentDataMgmtDao;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;


public class ReAssessment extends DecisioningAssessment {
    public ReAssessment(
            DecisioningEngineAdapter aDecisioningEngineAdapter,
            DecisioningDao aDecisioningDao) {
        super(aDecisioningEngineAdapter, aDecisioningDao);
    }
    CreditAssessmentDataMgmtDao m_creditAssessmentDataMgmtDao;

    public void setCreditAssessmentDataMgmtDao(
			CreditAssessmentDataMgmtDao mCreditAssessmentDataMgmtDao) {
		m_creditAssessmentDataMgmtDao = mCreditAssessmentDataMgmtDao;
	}


	@Override
    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return super.performCreditAssessment(creditAssessmentRequest, auditInfo);
    }


    @Override
    CreditAssessmentResultWrapper performDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        CreditAssessmentResultWrapper decisioningCreditAssessmentResult;
        
        ExistingCustomerCreditAssessmentRequest aExistingCustomerCreditAssessmentRequest = (ExistingCustomerCreditAssessmentRequest) creditAssessmentRequest;
      //get existing BureauResultData
        if(  !isBureauResultDataEmpty(aExistingCustomerCreditAssessmentRequest.getBureauResultData() ) ) {    	
	        if(aExistingCustomerCreditAssessmentRequest!= null 
	        		&& aExistingCustomerCreditAssessmentRequest.getCreditProfileData()!= null 
	        ){
	        	//get existing BureauResultData regardless if reportIndicator is true or not.
		        CreditBureauResult  aCreditBureauResult= prepPerformDecisioningEngineCreditAssessment(aExistingCustomerCreditAssessmentRequest.getCustomerID());
		        aExistingCustomerCreditAssessmentRequest.setBureauResultData(aCreditBureauResult);
	        }
        }
        decisioningCreditAssessmentResult = m_decisioningEngineAdapter.performCreditAssessment(aExistingCustomerCreditAssessmentRequest);
        return decisioningCreditAssessmentResult;
    }
     private boolean isBureauResultDataEmpty(CreditBureauResult obj) {
         if( obj!= null){  
        	 if( obj.getAdjudicationResult()!= null){ 
        		 return true;
        	 }
         }
	return false;	
	}


	public CreditBureauResult prepPerformDecisioningEngineCreditAssessment(long customerID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException{
    	 return m_creditAssessmentDataMgmtDao.getCreditBureauTransResultByCustID(customerID);
    } 
     @Override

     public void validateCreditAssessmentRequest(CreditAssessmentRequest aCreditAssessmentRequest)
     throws EnterpriseCreditAssessmentPolicyException {
 		FullCreditAssessmentRequest fullCreditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
 		
 		// mandatory fields validation
 		CrdaValidationUtility.validateMandatoryCreditCustomerInfoFields(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditCustomerInfo()); 
 		CrdaValidationUtility.validateMandatoryCreditProfile_CreditWorthiness_Fields( fullCreditAssessmentRequest.getCustomerID(),fullCreditAssessmentRequest.getCreditProfileData());
 		
 		//  fields's value validation 
 		CrdaValidationUtility.validate_CrdPrflID_CrdPRflStatusCd_CrdWorthiness_CustID_FieldsValues(fullCreditAssessmentRequest.getCustomerID(), fullCreditAssessmentRequest.getCreditProfileData(), m_creditProfileBr);

     }
}