package com.telus.credit.crda.asmtclassification.nondcsn;


import com.telus.credit.crda.dao.nondcn.NonDecisioningTrxDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;

/*
 * DepositDowngradeOverrideAssessmet will return a CreditAssessmentResult with some default values.
 * the returned CreditAssessmentResult will not store in db as credit decision-ing result as there were no Fico call. 
 * */
public class DepositDowngradeOverrideAssessmet extends NonDecisioningAssessment {

    public DepositDowngradeOverrideAssessmet(
            NonDecisioningTrxDao aOverrideDao) {
        super(aOverrideDao);

    }
    /*
     * down grade the customer's creditclaue to “D”  when deposit invoice was canceled .
     * */
    @Override
    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
       LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
       CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails= (CreditAssessmentTransactionDetails)super.performCreditAssessment(creditAssessmentRequest, auditInfo);
       
       return aCreditAssessmentTransactionDetails;
    }
    
    
    @Override
    CreditAssessmentResultWrapper performNonDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
       
        com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest aOverrideCreditAssessmentBaseRequest=(com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest)creditAssessmentRequest;        
        //Override Credit value to D in CreditAssessmentBaseRequest in order to save it as input in DB
        aOverrideCreditAssessmentBaseRequest.setNewCreditValueCd("D");
        
        //Use the CreditWorthiness from input as CreditAssessmentResult with CreditValue updated to D and assessmentMessageCd updated to REGCA04
        com.telus.credit.domain.creditprofile.CreditWorthiness sourceCreditWorthiness= aOverrideCreditAssessmentBaseRequest.getCreditProfileData().getCreditWorthiness();
        
        CreditAssessmentResultWrapper aCreditAssessmentResultWrapper = new CreditAssessmentResultWrapper();         
         	aCreditAssessmentResultWrapper.setAssessmentMessageCd("REGCA04");         	
	        aCreditAssessmentResultWrapper.setAssessmentResultCd(EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);  
	        aCreditAssessmentResultWrapper.setAssessmentResultReasonCd(EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_SUCCESS);
	        aCreditAssessmentResultWrapper.setCreditValueCd("D");
	        //rest from the previous credit worthiness
	        aCreditAssessmentResultWrapper.setDecisionCd(sourceCreditWorthiness.getDecisionCd());
	        aCreditAssessmentResultWrapper.setFraudIndicatorCd(sourceCreditWorthiness.getFraudIndicatorCd());
	        aCreditAssessmentResultWrapper.setFraudMessageCdList(sourceCreditWorthiness.getFraudMessageCdList()); 
	        aCreditAssessmentResultWrapper.setProductCategoryQualification(sourceCreditWorthiness.getProductCategoryQualification());
    
        return aCreditAssessmentResultWrapper;
    }
}
 