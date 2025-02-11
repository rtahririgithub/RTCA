package com.telus.credit.crda.domain;

import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;

public class CreditAssessmentTransactionDetails extends
        CreditAssessmentTransactionResult {
    long m_dbStgCreditDecisionTrnId;
    long m_dbInternalCreditDecisionTransactionId;
    long m_dbInternalCreditDecisionTransactionStatusId;
    long m_dbCreditBureauTransactionId;
    //public CreditAssessmentResultWrapper m_creditAssessmentResultWrapper;
    public DozerBeanMapper m_mapper;
    
    

	public void setMapper(DozerBeanMapper mMapper) {
		m_mapper = mMapper;
	}

	public void setDbStgCreditDecisionTrnId(long mDbStgCreditDecisionTrnId) {
        m_dbStgCreditDecisionTrnId = mDbStgCreditDecisionTrnId;
    }

    public long getDbStgCreditDecisionTrnId() {
        return m_dbStgCreditDecisionTrnId;
    }

    public void setDbInternalCreditDecisionTransactionId(
            long mDbInternalCreditDecisionTransactionId) {
        m_dbInternalCreditDecisionTransactionId = mDbInternalCreditDecisionTransactionId;
    }

    public long getDbInternalCreditDecisionTransactionId() {
        return m_dbInternalCreditDecisionTransactionId;
    }

    public void setDbCreditBureauTransactionId(long mDbCreditBureauTransactionId) {
        m_dbCreditBureauTransactionId = mDbCreditBureauTransactionId;
    }

    public long getDbCreditBureauTransactionId() {
        return m_dbCreditBureauTransactionId;
    }

    public void setDbInternalCreditDecisionTransactionStatusId(
            long intCrdtDcnTrnStatId) {
        m_dbInternalCreditDecisionTransactionStatusId = intCrdtDcnTrnStatId;
    }

    public long getDbInternalCreditDecisionTransactionStatusId() {
        return m_dbInternalCreditDecisionTransactionStatusId;
    }
}
