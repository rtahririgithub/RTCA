package com.telus.credit.crda.asmtclassification;


import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;


public abstract class Assessment {

    abstract public CreditAssessmentTransactionResult performCreditAssessment(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;

    public String getAssessmentClassName() {
        return this.getClass().getSimpleName();
    }

    public String m_AsmtTypeSubType;

    public void setAsmtTypeSubType(String m_AsmtTypeSubType) {
        this.m_AsmtTypeSubType = m_AsmtTypeSubType;
    }

    public String getAsmtTypeSubType() {
        return this.m_AsmtTypeSubType;
    }


}

