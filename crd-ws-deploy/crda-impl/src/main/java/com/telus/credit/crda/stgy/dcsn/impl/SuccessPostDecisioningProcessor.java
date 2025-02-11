package com.telus.credit.crda.stgy.dcsn.impl;


import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


/**
 * Implements an algorithm using the Strategy interface.
 */

public class SuccessPostDecisioningProcessor implements PostDecisioningProcessor {
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
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        String userID = (auditInfo != null ? auditInfo.getUserId() : null);
        String originatorApplicationID = (auditInfo != null ? auditInfo.getOriginatorApplicationId() : null);
        if (m_decisioningDao == null)
            throw new EnterpriseCreditAssessmentServiceException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + " |" + Thread.currentThread().getStackTrace()[1].getClassName() +
                            " customer id=" +
                            creditAssessmentRequest.getCustomerID() + " m_decisioningDao = " + m_decisioningDao
                    ,
                    EnterpriseCreditAssessmentExceptionCodes.ERROR_DAO_INIT);
        try {
            //create car
            long dbCARID = m_decisioningDao.createCreditAssessmentRequestTrx(
                    creditAssessmentRequest,
                    auditInfo,
                    EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING
            );
            aCreditAssessmentTransactionDetails.setCreditAssessmentID(dbCARID);


            //store a single decision-ing trx:including  decision-ing trx, fico input/output
            m_decisioningDao.storeSingleCompletedDecisioningTrx(
                    creditAssessmentRequest,
                    auditInfo,
                    decisioningCreditAssessmentResultWrapper,
                    aCreditAssessmentTransactionDetails,
                    failOverIndicator);


            //update car
            LogUtil.traceCalllog("dao.updateCreditAssessmentRequestStatus");
            m_decisioningDao.updateCreditAssessmentRequestStatus(
                    aCreditAssessmentTransactionDetails.getCreditAssessmentID(), userID, EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);

            //create comment
            String commentTxt = InternalRules.lookupCommentByAsmtTypeSubtype(creditAssessmentRequest,aCreditAssessmentTransactionDetails);
            aCreditAssessmentTransactionDetails.setCommentTxt(commentTxt);
            
            LogUtil.traceCalllog("dao.createCreditAssessmentComment");
            m_decisioningDao.createCreditAssessmentComment(
								            		aCreditAssessmentTransactionDetails.getCreditAssessmentID(), 
								            		userID, originatorApplicationID, 
								            		aCreditAssessmentTransactionDetails.getCommentTxt());

            

        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "customer id", creditAssessmentRequest.getCustomerID(), e);
        }

        return returnPostProcessorResult();
    }

    public String returnPostProcessorResult() {
        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);
        return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS;
    }


}