package com.telus.credit.crda.factory;

import java.util.HashMap;
import java.util.Map;

import com.telus.credit.crda.asmtclassification.Assessment;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;

public class AssessmentFactory extends BaseAssessmentFactory {

    // private static ApplicationContext m_ApplicationContext;
    private static Map<String, String> m_AssessmentBeanMap = new HashMap<String, String>();
    private static final String UNIFIED_CREDIT_FULL_ASSESSMENT_AUTO = "FULL_ASSESSMENT_UC_FULL_ASMT_AUTO";

    public static Assessment createCreditAssessment(
            CreditAssessmentRequest creditAssessmentRequest)
            throws PolicyException, ServiceException, EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        String type = (creditAssessmentRequest.getCreditAssessmentTypeCd() == null) ? "" : creditAssessmentRequest.getCreditAssessmentTypeCd().trim();
        String subtype = (creditAssessmentRequest.getCreditAssessmentSubTypeCd() == null) ? "" : creditAssessmentRequest.getCreditAssessmentSubTypeCd().trim();
        String springAsmtID = type + "_" + subtype;

        //commented out due to an subtype of MANUAL_OVERRIDE will always call Fico(change of crd value or fraud ind.) 
        //springAsmtID = checkChangedCreditValue(creditAssessmentRequest, type,subtype, springAsmtID);
        
        //UnifiedCredit: calling internal rules to decide if a UC processor should be used
        if( InternalRules.isUnifiecCreditRuleApplies(springAsmtID) ) {
        	//FULL get dormant flag
        	 
    		if(((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult() != null &&
    				!((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult().isUnifiedCreditDormantInd() ) {
    			springAsmtID = UNIFIED_CREDIT_FULL_ASSESSMENT_AUTO;
    		}
        }
        
        Assessment assessment = (Assessment)m_ApplicationContext.getBean(springAsmtID);
        	//lookupAssessment(springAsmtID);
        LogUtil.infolog("AssessmentFactory Returning assessment class :" + assessment.getClass().getName());

        return assessment;
    }
/*
	private static String checkChangedCreditValue(
			CreditAssessmentRequest creditAssessmentRequest, String type,
			String subtype, String springAsmtID) {
		if (
				EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT.equalsIgnoreCase(type)
                        &&
                        EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MANUAL_OVRD.equalsIgnoreCase(subtype)
                ) {
            OverrideCreditAssessmentRequest overrideCreditAssessmentRequest = (OverrideCreditAssessmentRequest) creditAssessmentRequest;
            
            String newcrdvalu = (overrideCreditAssessmentRequest.getNewCreditValueCd() == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR: overrideCreditAssessmentRequest.getNewCreditValueCd().trim();
            String currentcrdvalu = overrideCreditAssessmentRequest.getCreditProfileData().getCreditWorthiness().getCreditValueCd();          
            currentcrdvalu = (currentcrdvalu == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR: currentcrdvalu.trim();
            
            
            // Has credit value changed ? 
            if (!EnterpriseCreditAssessmentConsts.EMPTY_STR.equals(newcrdvalu)&& !newcrdvalu.equalsIgnoreCase(currentcrdvalu)) {
                springAsmtID = type + "_"+ subtype+ "_"+ EnterpriseCreditAssessmentConsts.DECISIONING_IS_REQUIRED;
            }
        }
		return springAsmtID;
	}

    private static Assessment lookupAssessment(String springAsmtID)
            throws EnterpriseCreditAssessmentPolicyException,
            EnterpriseCreditAssessmentServiceException {
        if (m_AssessmentBeanMap.size() == 0) {
            throw new EnterpriseCreditAssessmentPolicyException(
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR
                            + "[" + springAsmtID + "]",
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);
        } else {
            String beanName = m_AssessmentBeanMap.get(springAsmtID);
            if (beanName == null) {
                throw new EnterpriseCreditAssessmentPolicyException(
                        EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR
                                + "[" + springAsmtID + "]",
                        EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);
            }
            Assessment bean = (Assessment) m_ApplicationContext
                    .getBean(beanName);
            return bean;
        }
    }

    static Assessment lookupAssessment(String type, String subtype,
                                       String requireDecisioningInd)
            throws EnterpriseCreditAssessmentPolicyException,
            EnterpriseCreditAssessmentServiceException {
        String asmtId = type + "_" + subtype + "_" + requireDecisioningInd;
        if (m_AssessmentBeanMap.size() == 0) {
            throw new EnterpriseCreditAssessmentPolicyException(
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR
                            + "[" + type + "_" + subtype + "]",
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);
        } else {
            String beanName = m_AssessmentBeanMap.get(asmtId);
            if (beanName == null) {
                throw new EnterpriseCreditAssessmentPolicyException(
                        EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR
                                + "[" + type + "," + subtype + "]",
                        EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);
            }
            Assessment bean = (Assessment) m_ApplicationContext
                    .getBean(beanName);
            return bean;
        }
    }

    public static Map<String, String> getAllAssessment() throws Exception {
        if (m_AssessmentBeanMap.size() == 0) {
            throw new EnterpriseCreditAssessmentPolicyException(
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR,
                    EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);

        } else {
            return m_AssessmentBeanMap;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        super.setApplicationContext(applicationContext);
        // m_ApplicationContext = applicationContext;

        Map<String, Assessment> processorMap = m_ApplicationContext.getBeansOfType(Assessment.class);
        if (processorMap.isEmpty()) {
            throw new Error(EnterpriseCreditAssessmentExceptionCodes.ASMT_FACTORY_INVALID_ASMT_EXCEPTION);
        }
        Set<Entry<String, Assessment>> processorEntrySet = processorMap
                .entrySet();
        Iterator<Entry<String, Assessment>> iterator = processorEntrySet
                .iterator();
        while (iterator.hasNext()) {
            Entry<String, Assessment> entry = iterator.next();
            m_AssessmentBeanMap.put(entry.getValue().getAsmtTypeSubType(),
                    entry.getKey());

        }
    }
*/}