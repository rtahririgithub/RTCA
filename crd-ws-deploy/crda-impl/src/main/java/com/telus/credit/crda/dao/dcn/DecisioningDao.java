package com.telus.credit.crda.dao.dcn;

import com.telus.credit.crda.dao.base.BaseAssessmentDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public interface DecisioningDao extends BaseAssessmentDao {


    void storeDecisioningEngineInput(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    void storeSingleCompletedDecisioningTrx(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo aAuditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails, boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException;

    void createCreditBureauTransactionDetails(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo, long creditBureauTransactionId,
            ConsumerCreditReportResponse consumerCreditReportResponse)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    long createCreditBureauTransaction(
            ConsumerCreditReportResponse consumerCreditReportResponse,
            long dbCARID, CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo) throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException;

    void storeDecisioningEngineResult(long dbCarID,
                                      long dbStgCreditDecisionTrnId,
                                      long dbInternalCreditDecisionTransactionId,
                                      long dbCreditBureauTransactionId,
                                      CreditAssessmentRequest creditAssessmentRequest,
                                      AuditInfo auditInfo, CreditAssessmentResultWrapper aCreditAssessmentResultWrapper)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;


    long createInternalCreditDecisionTransactionResult(
            long dbStgCreditDecisionTrnId,
            long dbInternalCreditDecisionTransactionId,
            long dbCreditBureauTransactionId,
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo, CreditAssessmentResultWrapper aCreditAssessmentResultWrapper)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    long createInternalCreditDecisionTransactionResultDetails(
            long dbInt_crdt_dcsn_trn_rslt_Id,
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo, CreditAssessmentResultWrapper aCreditAssessmentResultWrapper)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    long storeCreditBureauResult(
            ConsumerCreditReportResponse consumerCreditReportResponse,
            long dbCARID, CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo) throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException;

    public void storeSingleSecondCompletedDecisioningTrx(CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo aAuditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
			throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException ;
}
