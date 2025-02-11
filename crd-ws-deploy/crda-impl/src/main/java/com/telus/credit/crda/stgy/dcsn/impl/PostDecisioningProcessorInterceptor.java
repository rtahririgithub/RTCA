package com.telus.credit.crda.stgy.dcsn.impl;

import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public class PostDecisioningProcessorInterceptor {

    public PostDecisioningProcessorInterceptor() {
        super();
    }

    public long createCreditAssessmentRequestTrxWithInterceptor(
            DecisioningDao m_decisioningDao,
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo, String status,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        //create car
        long dbCARID = m_decisioningDao.createCreditAssessmentRequestTrx(
                creditAssessmentRequest,
                auditInfo,
                EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING
        );
        return dbCARID;
    }

}