package com.telus.credit.crda.dao.dcn;


import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;

import com.telus.credit.domain.ent.AuditInfo;



public class DecisiongDaoImpOverrideAssessment
        extends DecisioningDaoImpCommon
        //implements DecisioningDao 
{

    public void storeDecisioningEngineInput(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.infolog("dao.store fico input as Attribute values ");
        try {
            storeOverrideDecisioningAttributes(creditAssessmentRequest, auditInfo, aCreditAssessmentTransactionDetails.getCreditAssessmentID());
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", creditAssessmentRequest.getCustomerID(), e);
        }

    }

}