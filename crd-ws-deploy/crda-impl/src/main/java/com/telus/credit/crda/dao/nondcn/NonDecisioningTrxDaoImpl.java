/*package com.telus.credit.crda.dao.nondcn;

import com.telus.credit.crda.dao.base.BaseAssessmentDaoImpl;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


*//**
 * User: T828927
 * Date: 07/07/13
 *//*
public class NonDecisioningTrxDaoImpl
        extends BaseAssessmentDaoImpl 
        implements NonDecisioningTrxDao{
	@Override
    public void storeCompleteNonDecisioningTrx(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails)
            {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        String userID = (auditInfo != null ? auditInfo.getUserId() : null);
        String originatorApplicationID = (auditInfo != null ? auditInfo.getOriginatorApplicationId() : null);

        LogUtil.infolog("create car");
        long dbCARID = createCreditAssessmentRequestTrx(aCreditAssessmentRequest,
                auditInfo, EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING);
        aCreditAssessmentTransactionDetails.setCreditAssessmentID(dbCARID);
        LogUtil.infolog("Create comment");
        //this.createCreditAssessmentComment(aCreditAssessmentRequest, auditInfo, null, null);
        LogUtil.traceCalllog("dao.createCreditAssessmentComment");
        createCreditAssessmentComment(aCreditAssessmentTransactionDetails.getCreditAssessmentID()
                , userID,
                originatorApplicationID,
                aCreditAssessmentRequest.getCommentTxt());

        LogUtil.infolog("update car");
        updateCreditAssessmentRequestStatus(
                aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
                (auditInfo != null ? auditInfo.getUserId() : null),
                EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);
    }
}
*/