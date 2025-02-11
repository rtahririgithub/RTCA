package com.telus.credit.crda.dao.mgmt;


import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.ent.AuditInfo;

import java.util.HashMap;
import java.util.List;

public interface CreditAssessmentDataMgmtDao {

    public CreditAssessmentDetails getCreditAssessment(long creditAssessmentID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public List<CreditAssessmentTransaction> searchCreditAssessments(
            SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
            AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

 
    public void voidCreditAssessment(long creditAssessmentID, String voidReasonCode, AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public CreditAssessmentTransaction getBaseCreditAssessment(long creditAssessmentId)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public CreditBureauResult getCreditBureauTransResultByCustID(long customerId) 
    		throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

	public String getDocumentumPathId(String creditBureauDocumentID)
			throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;;
 
	public HashMap<String, Object> getCreditBureauMap(String creditBureauDocumentID)
			throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;;
}
