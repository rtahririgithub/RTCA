package com.telus.credit.crda.stgy.dcsn;


import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;


public interface PostDecisioningProcessor {

    String performPostDecisioning(CreditAssessmentRequest creditAssessmentRequest,
                                  AuditInfo auditInfo,
                                  CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
                                  CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
                                  boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException;

    void setDecisioningDao(DecisioningDao aDecisioningDao);


}