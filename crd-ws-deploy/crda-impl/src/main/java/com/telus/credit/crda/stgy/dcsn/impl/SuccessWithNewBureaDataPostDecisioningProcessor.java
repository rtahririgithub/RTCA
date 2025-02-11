package com.telus.credit.crda.stgy.dcsn.impl;


import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public class SuccessWithNewBureaDataPostDecisioningProcessor implements
        PostDecisioningProcessor {

    public DecisioningDao m_decisioningDao;

    @Override
    public void setDecisioningDao(DecisioningDao aDecisioningDao) {
        m_decisioningDao = aDecisioningDao;
    }


    @Override
    /*
     * */
    public String performPostDecisioning(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo anAuditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException,
            EnterpriseCreditAssessmentPolicyException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        String userID = (anAuditInfo != null ? anAuditInfo.getUserId() : null);
        String originatorApplicationID = (anAuditInfo != null ? anAuditInfo.getOriginatorApplicationId() : null);

        try {
            //store a single decision-ing trx:including  decision-ing trx, fico output 
            m_decisioningDao.storeSingleSecondCompletedDecisioningTrx(
                    creditAssessmentRequest,
                    anAuditInfo,
                    decisioningCreditAssessmentResultWrapper,
                    aCreditAssessmentTransactionDetails,
                    failOverIndicator);
            

            
            //update car to mark the CAR as completed.
            LogUtil.traceCalllog("dao.updateCreditAssessmentRequestStatus");
            m_decisioningDao.updateCreditAssessmentRequestStatus(aCreditAssessmentTransactionDetails.getCreditAssessmentID(), userID, EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);

            //create comment
            LogUtil.traceCalllog("dao.createCreditAssessmentComment");
            String commentTxt = InternalRules.lookupCommentByAsmtTypeSubtype(creditAssessmentRequest,aCreditAssessmentTransactionDetails);
            aCreditAssessmentTransactionDetails.setCommentTxt(commentTxt);  
            
            m_decisioningDao.createCreditAssessmentComment(
            			aCreditAssessmentTransactionDetails.getCreditAssessmentID(), 
            			userID, originatorApplicationID, 
            			aCreditAssessmentTransactionDetails.getCommentTxt());
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "customer id", creditAssessmentRequest.getCustomerID(), e);
        }

        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);

        //return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS;
        return returnPostProcessorResult();

    }

    public String returnPostProcessorResult() {
        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);
        return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS;
    }
}