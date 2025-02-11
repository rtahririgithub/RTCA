package com.telus.credit.crda.stgy.eval.impl;


import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

public class ResilienceAssessmentEvaluationProcessor implements
        AssessmentEvaluationProcessor {
    public DozerBeanMapper m_mapper;

    public void setMapper(DozerBeanMapper aMapper) {
        this.m_mapper = aMapper;
    }
    DecisioningDao m_decisioningDao = null;
    public DecisioningEngineAdapter m_decisioningEngineAdapter;

    @Override
    public void setDecisioningDao(
            DecisioningDao aDecisioningDao) {
        m_decisioningDao = aDecisioningDao;

    }

    @Override
    public CreditAssessmentResultWrapper performAssessmentEvaluation(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails creditAssessmentTransactionDetails,
            boolean failOverIndicator) {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        LogUtil.infolog("-->Return  creditAssessmentResult");
       
        //apply Resilience solution by using the credit worthiness from input as credit decisioning result
/*	    com.fico.telus.blaze.creditCommon.CreditAssessmentResult   aFicoCreditAssessmentResult = new com.fico.telus.blaze.creditCommon.CreditAssessmentResult();
        m_mapper.map(creditAssessmentRequest, aFicoCreditAssessmentResult);
        
        //Indicating the result is as the result of Resilience solution by setting AssessmentMessageCd=ASMT_BYPASSED
        aFicoCreditAssessmentResult.setAssessmentMessageCd(EnterpriseCreditAssessmentConsts.DECISIONING_RESILIENCE_ASMT_MSG_CD);
        
        //populate AssessmentResultCd and AssessmentResultReasonCd with the fico values that resulted the Resilience solution.
        if (decisioningCreditAssessmentResultWrapper!=null){
	        aFicoCreditAssessmentResult.setAssessmentResultCd(decisioningCreditAssessmentResultWrapper.getAssessmentResultCd());
	        aFicoCreditAssessmentResult.setAssessmentResultReasonCd(decisioningCreditAssessmentResultWrapper.getAssessmentResultReasonCd());
	        }
        decisioningCreditAssessmentResultWrapper= new  CreditAssessmentResultWrapper();
        m_mapper.map(aFicoCreditAssessmentResult, decisioningCreditAssessmentResultWrapper);*/

        LogUtil.errorlog(
    			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.RESILIENCE_WARNING_CODE + 
    			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.RESILIENCE_WARNING_MSG + 
    			" CustomerID=" + creditAssessmentRequest.getCustomerID()+ ". ");
        return decisioningCreditAssessmentResultWrapper;

    }

    @Override
    public void setDecisioningEngineAdapter(
            DecisioningEngineAdapter aDecisioningEngineAdapter) {
        m_decisioningEngineAdapter = aDecisioningEngineAdapter;
    }
}
