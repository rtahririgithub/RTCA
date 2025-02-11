package com.telus.credit.crda.factory;

import com.telus.credit.crda.stgy.eval.AssessmentEvaluationProcessor;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;

public class AssessmentEvaluationProcessorFactory extends BaseAssessmentFactory {

    public static AssessmentEvaluationProcessor create(
            String postDecisioningProcessResult) {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        // default
        AssessmentEvaluationProcessor assessmentEvaluationProcessor = getAssessmentEvaluationProcessorBean("FailedAssessmentEvaluationProcessor");
        
        
        if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS.equalsIgnoreCase(postDecisioningProcessResult)) {
            assessmentEvaluationProcessor = getAssessmentEvaluationProcessorBean("SuccessAssessmentEvaluationProcessor"); 
        } else {
            if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_BUREAU_REQUIRED.equalsIgnoreCase(postDecisioningProcessResult)) {
                assessmentEvaluationProcessor = getAssessmentEvaluationProcessorBean("BureauDataRequiredAssessmentEvaluationProcessor");
            } else {
                if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR.equalsIgnoreCase(postDecisioningProcessResult )
                		) {
                    assessmentEvaluationProcessor = getAssessmentEvaluationProcessorBean("FailedAssessmentEvaluationProcessor");
                }else {
                	if(EnterpriseCreditAssessmentConsts.ASSESSMENT_RESILIENCE.equalsIgnoreCase(postDecisioningProcessResult)){
                		assessmentEvaluationProcessor = getAssessmentEvaluationProcessorBean("ResilienceAssessmentEvaluationProcessor");
                	}
                }
            }
        }
        LogUtil.infolog("AssessmentEvaluationProcessorFactory Returning assessmentEvaluationProcessor = " + assessmentEvaluationProcessor.getClass().getName());
        return assessmentEvaluationProcessor;
    }

    private static AssessmentEvaluationProcessor getAssessmentEvaluationProcessorBean(String beanId) {
        return (AssessmentEvaluationProcessor) m_ApplicationContext.getBean(beanId);
    }
}
