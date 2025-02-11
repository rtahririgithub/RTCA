package com.telus.credit.crda.stgy.eval.impl;


import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.adapter.CreditGatewayAdapter;
import com.telus.credit.crda.adapter.DecisioningEngineAdapter;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.factory.AssessmentEvaluationProcessorFactory;
import com.telus.credit.crda.factory.PostDecisioningProcesserFactory;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.crda.UnifiedCreditAssessmentResult;
import com.telus.credit.domain.crda.WlsUnifiedCreditSearchResult;
import com.telus.credit.domain.ent.AuditInfo;


public class BureauDataRequiredAssessmentEvaluationProcessor implements AssessmentEvaluationProcessor {
    //@Autowired
    public DozerBeanMapper m_mapper;

    public void setMapper(DozerBeanMapper aMapper) {
        this.m_mapper = aMapper;
    }


    //@Autowired
    public CreditGatewayAdapter m_creditGatewayAdapter;


    public void setCreditGatewayAdapter(
            CreditGatewayAdapter aCreditGatewayAdapter) {
        this.m_creditGatewayAdapter = aCreditGatewayAdapter;
    }


    public DecisioningEngineAdapter m_decisioningEngineAdapter;

    public void setDecisioningEngineAdapter(
            DecisioningEngineAdapter aDecisioningEngineAdapter) {
        m_decisioningEngineAdapter = aDecisioningEngineAdapter;
    }


    // decisioningDao is set based on the assessment classification in the com.telus.credit.crda.asmtclassification.dscn.DecisioningAssessment
    public DecisioningDao m_decisioningDao = null;

    @Override
    public void setDecisioningDao(
            DecisioningDao aDecisioningDao) {
        m_decisioningDao = aDecisioningDao;

    }


    @Override
    /*
     * performAssessmentEvaluation when fico requires to get external bureua data
     * 
     * */
    public CreditAssessmentResultWrapper performAssessmentEvaluation(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo, CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails creditAssessmentTransactionDetails,
            boolean failOverIndicator) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));


        //call cgw
        ConsumerCreditReportResponse consumerCreditReportResponse = m_creditGatewayAdapter.pullConsumerCreditReport(aCreditAssessmentRequest, auditInfo, failOverIndicator, creditAssessmentTransactionDetails);

        //store cgw response
        try {
            long mDbCreditBureauTransactionId = m_decisioningDao.storeCreditBureauResult(
                    consumerCreditReportResponse,
                    creditAssessmentTransactionDetails.getCreditAssessmentID(),
                    aCreditAssessmentRequest,
                    auditInfo);
            creditAssessmentTransactionDetails.setDbCreditBureauTransactionId(mDbCreditBureauTransactionId);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }

        //dozer mapping eCrda consumerCreditReportResponse to fico  CreditAssessmentRequest
        m_mapper.map(consumerCreditReportResponse, creditAssessmentTransactionDetails);

        LogUtil.infolog("-->capture Fico UC Comment to trx output before second ivokation");
        addUcComment(aCreditAssessmentRequest, decisioningCreditAssessmentResultWrapper, creditAssessmentTransactionDetails);
        
        LogUtil.infolog("-->call Fico again ");
        CreditAssessmentResultWrapper secondCreditAssessmentResult = m_decisioningEngineAdapter.performCreditAssessment(aCreditAssessmentRequest, consumerCreditReportResponse);
        creditAssessmentTransactionDetails.setCreditDecisioningResult(secondCreditAssessmentResult);
        
        LogUtil.infolog("-->Create proper postDecisioningProcesser");
        //Create/call proper postDecisioningProcesser to treat the fico result (secondCreditAssessmentResult)
        PostDecisioningProcessor postDecisioningProcesser =
                PostDecisioningProcesserFactory.create_On_SubsequentDecisioning(
                        aCreditAssessmentRequest,
                        secondCreditAssessmentResult
                );
        postDecisioningProcesser.setDecisioningDao(m_decisioningDao);
        LogUtil.infolog("-->perform the post Decisioning tasks");
        String postDecisioningProcessResult = postDecisioningProcesser.performPostDecisioning(
                aCreditAssessmentRequest,
                auditInfo,
                secondCreditAssessmentResult,
                creditAssessmentTransactionDetails,
                failOverIndicator);

 
        
        
        //call perform assessment eval
        LogUtil.infolog("-->analyse the secondpost Decisioning result");
        
        //analyse postDecisioningProcessResult and perform the post Decisioning business rule 
         AssessmentEvaluationProcessor m_assessmentEvaluationProcessor;
        m_assessmentEvaluationProcessor =(AssessmentEvaluationProcessorFactory.create(postDecisioningProcessResult));
        m_assessmentEvaluationProcessor.setDecisioningDao(m_decisioningDao);
        m_assessmentEvaluationProcessor.setDecisioningEngineAdapter(m_decisioningEngineAdapter);
       
        decisioningCreditAssessmentResultWrapper = m_assessmentEvaluationProcessor.performAssessmentEvaluation(
        		 aCreditAssessmentRequest,
                    auditInfo,
                    secondCreditAssessmentResult,
                    creditAssessmentTransactionDetails,
                    true);        
        
/*        if (
                postDecisioningProcessResult.equalsIgnoreCase(EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS)
                ) {
            return secondCreditAssessmentResult;
        } else {
            if (
                    postDecisioningProcessResult.equalsIgnoreCase(EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_BUREAU_REQUIRED)
                    ) {
   		     StringBuffer extramsg  = new StringBuffer ("");
		     extramsg.append("[" +"for Customer ID : "+ aCreditAssessmentRequest.getCustomerID()+ "]");
             	throw new EnterpriseCreditAssessmentPolicyException(	           			
    					EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION_STR 
    					+ extramsg + ": Decisioning result reason code =" + postDecisioningProcessResult
    					,EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION
    					);                             
            } else {
                if (postDecisioningProcessResult.equalsIgnoreCase(EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR)
                		) {
         		     StringBuffer extramsg  = new StringBuffer ("");
        		     extramsg.append("[" +"for Customer ID : "+ aCreditAssessmentRequest.getCustomerID()+ "]");
                     	throw new EnterpriseCreditAssessmentPolicyException(	           			
            					EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION_STR 
            					+ extramsg + ": Decisioning result reason code =" + postDecisioningProcessResult
            					,EnterpriseCreditAssessmentExceptionCodes.DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION
            					);                      
                }
            }
        }*/
        return decisioningCreditAssessmentResultWrapper;

    }

    private void addUcComment(CreditAssessmentRequest creditAssessmentRequest, 
    		CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper, 
    		CreditAssessmentTransactionResult aCreditAssessmentTransactionDetails) 
    				throws EnterpriseCreditAssessmentServiceException {
        //Unified Credit Comment
        com.fico.telus.blaze.creditCommon.UnifiedCreditAssessmentResult ficoUnifiedCreditAssessmentResult = decisioningCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy();
        //Add to the comment WLS account BAN
        Long ban = null;
        if( ((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult() != null &&
        		(WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult() != null &&
        		((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount() != null &&
        				((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount().getAccountData() != null ) {
        	ban = ((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount().getAccountData().getBillingAccountNumber();
        }
        String ucCommentTxt = "";
        if( ficoUnifiedCreditAssessmentResult!= null && (
        		!(ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getIndicator()==null ||
        		ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getReasonCd()==null)) ) {
			ucCommentTxt = InternalRules.getUnifiedCreditCommentText(
					ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getIndicator().toString(), 
							ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getReasonCd(),
							ban ); 
        }
        
        //Mapping and adding comments from UnifiedCreditReulst to CAR Result obj
        UnifiedCreditAssessmentResult aUnifiedCreditAssessmentResult = new UnifiedCreditAssessmentResult();            
        aUnifiedCreditAssessmentResult.setDiaryComment(ucCommentTxt);
        
        aCreditAssessmentTransactionDetails.setUnifiedCreditAssessmentResult(aUnifiedCreditAssessmentResult);

    }

}
