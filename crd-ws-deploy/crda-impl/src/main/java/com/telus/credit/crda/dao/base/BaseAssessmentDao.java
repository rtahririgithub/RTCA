package com.telus.credit.crda.dao.base;


import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public interface BaseAssessmentDao {


    long createCreditAssessmentRequestTrx(CreditAssessmentRequest creditAssessmentRequest,
                                          AuditInfo auditInfo,
                                          String creditAssessmentStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    void createCreditAssessmentComment(long dbCarID, String userID, String originatorApplicationId, String commentTxt) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public long updateCreditAssessmentRequestStatus(
            long cARID,
            String userId, String carStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    ;


    void expireCreditAssessmentRequestStatus(long creditAssessmentRequestId,
                                             String userId) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    long createCreditAssessmentRequestStatus(long creditAssessmentRequestId,
                                             String userId, String carStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    long createCreditAssessmentRequestActivity(long aCARID, String userId,
                                               String activityTypeCd, long carStatusId, String carActivityReasonCd)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    void expireCreditBureauTransaction(long creditAssessmentID, String status,
                                       AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

/*    public List<CreditAssessmentTransaction> searchCreditAssessments(
            SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
			AuditInfo auditInfo) throws  EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;
*/
}
