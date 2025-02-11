package com.telus.credit.crda.adapter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;
import com.fico.telus.rtca.blaze.RuleServicesBean;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.AnyXMLConverter;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.util.ThreadLocalUtil;

import com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest;



public class DecisioningEngineAdapter {
    private static final Log log = LogFactory.getLog(DecisioningEngineAdapter.class);
    public DozerBeanMapper m_mapper;
    public void setMapper(DozerBeanMapper m_mapper) {
        this.m_mapper = m_mapper;
    }

    
    RuleServicesBean m_ruleServicesBean;

    public void setRuleServicesBean(RuleServicesBean aRuleServicesBean) {
        m_ruleServicesBean = aRuleServicesBean;
    }

    public com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest m_ficoCreditAssessmentRequest = null;


    public void setFicoCreditAssessmentRequest(com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest aFicoCreditAssessmentRequest) {
        m_ficoCreditAssessmentRequest = aFicoCreditAssessmentRequest;
    }

    public CreditAssessmentResultWrapper performCreditAssessment(CreditAssessmentRequest aCreditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest m_ficoCreditAssessmentRequest = createFicoCreditAssessmentRequest(aCreditAssessmentRequest);
        m_mapper.map(aCreditAssessmentRequest, m_ficoCreditAssessmentRequest);
        
        log.info("FICO_Call input_ficoCreditAssessmentRequest: " + AnyXMLConverter.convertToXml(m_ficoCreditAssessmentRequest) );
        
        //Added by Alan for RTCA 1.6 
        
        if ( ThreadLocalUtil.getAdditionalCollectionData()!=null 
        		&& EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT.equals(aCreditAssessmentRequest.getCreditAssessmentTypeCd()) 
        		&& EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD.equals(aCreditAssessmentRequest.getCreditAssessmentSubTypeCd())
        	) 
        {
        	((MonthlyUDCreditAssessmentRequest)m_ficoCreditAssessmentRequest).getCollectionData().setScoreCardID(ThreadLocalUtil.getAdditionalCollectionData().getScorecardID());
        	((MonthlyUDCreditAssessmentRequest)m_ficoCreditAssessmentRequest).getCollectionData().setDelinquencyCycle(ThreadLocalUtil.getAdditionalCollectionData().getDelinquencyCycle());
        	((MonthlyUDCreditAssessmentRequest)m_ficoCreditAssessmentRequest).getCollectionData().setCollectionSegment(ThreadLocalUtil.getAdditionalCollectionData().getCollectionSegment());      	
        }
        
        //
        
        //call fico
        com.fico.telus.blaze.creditCommon.CreditAssessmentResult ficoCreditAssessmentResult = new com.fico.telus.blaze.creditCommon.CreditAssessmentResult();
        
        //
        try {
        	long startTime = System.nanoTime();
            ficoCreditAssessmentResult = m_ruleServicesBean.performCreditAssessment(aCreditAssessmentRequest.getCustomerID(), m_ficoCreditAssessmentRequest);

            log.info("IGNORE : Total execution time to call m_ruleServicesBean.performCreditAssessment in millis: " + (System.nanoTime() - startTime) / 1000000);
            log.info("FICO_Call output_ficoCreditAssessmentResult:" + aCreditAssessmentRequest.getCustomerID() + " " + AnyXMLConverter.convertToXml(ficoCreditAssessmentResult));
        	
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer ID ", aCreditAssessmentRequest.getCustomerID(), e);
        } 
        
        //Added by Alan for RTCA 1.6
        if ( ficoCreditAssessmentResult.getCreditRiskLevel()!=null){
        	ThreadLocalUtil.setRiskLevelNum(ficoCreditAssessmentResult.getCreditRiskLevel());
        	if (log.isDebugEnabled())
        		log.debug("RiskLevelNum is " + ficoCreditAssessmentResult.getCreditRiskLevel());
        }
        //
        
        CreditAssessmentResultWrapper aCreditAssessmentResultWrapper = new CreditAssessmentResultWrapper();
        m_mapper.map(ficoCreditAssessmentResult, aCreditAssessmentResultWrapper);
        
        if (ficoCreditAssessmentResult.getUnifiedCreditAssessmentResult() != null) {
        	aCreditAssessmentResultWrapper.setUnifiedCreditAssessmentResultCopy(ficoCreditAssessmentResult.getUnifiedCreditAssessmentResult());
        }        
         return aCreditAssessmentResultWrapper;
    }

    private com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest createFicoCreditAssessmentRequest(
			CreditAssessmentRequest aCreditAssessmentRequest) {
	com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest aFicoCreditAssessmentRequest=null;
    	String asmttype = aCreditAssessmentRequest.getCreditAssessmentTypeCd();
    	String asmtSubtype = aCreditAssessmentRequest.getCreditAssessmentSubTypeCd();
    	//FULL_ASSESSMENT_AUTO_ASSESSMENT
        if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT)) {
        	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest();
        } else {
        	//FULL_ASSESSMENT_GET_BUREAU_DATA
        	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_GET_BUREAU_DATA)) {
            	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.ManualInquiryCreditAssessmentRequest();
            } else {
            	//FULL_ASSESSMENT_MANUAL_ASSESSMENT
            	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MANUAL_ASSESSMENT)) {
                	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest();
                }
            	else{
            		//FULL_ASSESSMENT_MONTHLY_CVUD
                	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD)) {
                    	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest();
                    }    
                	else{
                		//FULL_ASSESSMENT_NEW_ACC_ASSESSMENT
                    	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_NEW_ACC_ASSESSMENT)) {
                        	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest();
                        }    
                    	else{
                    		//FULL_ASSESSMENT_REOPEN_ASSESSMENT
                        	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_REOPEN_ASSESSMENT)) {
                            	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest();
                            }    
                        	else{
                        		//OVRD_ASSESSMENT_MANUAL_OVERRIDE
                            	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MANUAL_OVRD)) {
                                	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest();
                                }    
                            	else{
                            		//OVRD_ASSESSMENT_UNMERGED
                                	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_UNMERGED)) {
                                    	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest();
                                    }    
                                	else{
                                		//OVRD_ASSESSMENT_CANCEL_DEPOSIT_INV
                                    	if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT) && asmtSubtype.equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_CANCEL_DEPOSIT_INV)) {
                                        	aFicoCreditAssessmentRequest = new com.fico.telus.blaze.creditAsessment.CancelDepositCreditAssessmentRequest();
                                        }    
                                	}
                            	}	
                        	}
                    	}
                	}
            	}
            	
            }
        }
		return aFicoCreditAssessmentRequest;
	}

	public CreditAssessmentResultWrapper performCreditAssessment(
            CreditAssessmentRequest aCreditAssessmentRequest, ConsumerCreditReportResponse consumerCreditReportResponse) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest m_ficoCreditAssessmentRequest = createFicoCreditAssessmentRequest(aCreditAssessmentRequest);

        //dozer mapping eCrda aCreditAssessmentRequest to fico  CreditAssessmentRequest
        m_mapper.map(aCreditAssessmentRequest, m_ficoCreditAssessmentRequest);

        //dozer mapping eCrda consumerCreditReportResponse(CreditBureuaResult to fico  CreditAssessmentRequest
        m_mapper.map(consumerCreditReportResponse, m_ficoCreditAssessmentRequest);

        log.info( "FICO_Call input_ficoCreditAssessmentRequest: " + AnyXMLConverter.convertToXml(m_ficoCreditAssessmentRequest) );

        com.fico.telus.blaze.creditCommon.CreditAssessmentResult ficoCreditAssessmentResult = new com.fico.telus.blaze.creditCommon.CreditAssessmentResult();
        try {
            long startTime = System.nanoTime();

            ficoCreditAssessmentResult = m_ruleServicesBean.performCreditAssessment(aCreditAssessmentRequest.getCustomerID(), m_ficoCreditAssessmentRequest);

            log.info("IGNORE : Total execution time to call m_ruleServicesBean.performCreditAssessmentin millis: " + (System.nanoTime() - startTime) / 1000000);
            log.info("FICO_Call output_ficoCreditAssessmentResult for CustomerID: " + aCreditAssessmentRequest.getCustomerID() + " " + AnyXMLConverter.convertToXml(ficoCreditAssessmentResult));

        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer ID ", aCreditAssessmentRequest.getCustomerID(), e);
        }
        

        CreditAssessmentResultWrapper aCreditAssessmentResultWrapper = new CreditAssessmentResultWrapper();
        m_mapper.map(ficoCreditAssessmentResult, aCreditAssessmentResultWrapper);
         return aCreditAssessmentResultWrapper;
    }

	public String ping() {
		String msg = "[ficoPing: ";
		if(m_ruleServicesBean!=null) {
			try {
				msg += m_ruleServicesBean.getRulesServer().ping();
			} catch (Exception e) {
				msg += e.getMessage();
				log.error(e.getMessage(), e);
			}
		} else {
			msg += "null";
		}
		return  msg += "]";

	}

}
