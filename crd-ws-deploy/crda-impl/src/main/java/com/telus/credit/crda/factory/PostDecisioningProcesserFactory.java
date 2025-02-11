package com.telus.credit.crda.factory;

import java.util.List;

import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.stgy.dcsn.PostDecisioningProcessor;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.domain.common.ProductCategoryQualification;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
public class PostDecisioningProcesserFactory extends BaseAssessmentFactory {

	public static PostDecisioningProcessor createPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper) {
		return   PostDecisioningProcesserFactory.createPostDecisioningProcessor(
                creditAssessmentRequest,
                decisioningCreditAssessmentResultWrapper,
                false
        );
	}
	
	public static PostDecisioningProcessor createPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
			boolean cVUDTrialRun) {
		
		LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread()
				.getStackTrace()[1].getMethodName(), Thread.currentThread()
				.getStackTrace()[1].getClassName()));

		PostDecisioningProcessor postDecisioningProcessor;
		String assessmentType = creditAssessmentRequest.getCreditAssessmentTypeCd();
		String assessmentSubType = creditAssessmentRequest.getCreditAssessmentSubTypeCd();


		String resultCd = (decisioningCreditAssessmentResultWrapper.getAssessmentResultCd()== null)?"":decisioningCreditAssessmentResultWrapper.getAssessmentResultCd().trim();
		String reasonCd = (decisioningCreditAssessmentResultWrapper.getAssessmentResultReasonCd()== null)?"":decisioningCreditAssessmentResultWrapper.getAssessmentResultReasonCd().trim();

		postDecisioningProcessor = getPostDecisioningProcessor( 
				creditAssessmentRequest,assessmentType, assessmentSubType,
				resultCd, reasonCd, decisioningCreditAssessmentResultWrapper, 
				true,
				cVUDTrialRun);//boolean cVUDTrialRun

		LogUtil.infolog(" Returning  PostDecisioningProcesserFactory PostDecisioningProcessor =  "
				+ postDecisioningProcessor.getClass().getName());
		return postDecisioningProcessor;
	}

	private static PostDecisioningProcessor getPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			String assessmentType,
			String assessmentSubType, 
			String resultCd, 
			String reasonCd,
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper, 
			boolean firstFicocall,
			boolean cVUDTrialRun) {
		
		PostDecisioningProcessor postDecisioningProcessor;
		if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS.equalsIgnoreCase(resultCd)) { 
			if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_SUCCESS.equalsIgnoreCase(reasonCd)) {
				if(isValidCreditAssessmentResult(decisioningCreditAssessmentResultWrapper,creditAssessmentRequest)){
					if (cVUDTrialRun &&
							EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT.equalsIgnoreCase(assessmentType)
							&& EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD.equalsIgnoreCase(assessmentSubType)
							){
						postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessNoChangeDisregardPostDecisioningProcesser");
					}
					else{
						if(decisioningCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy()!=null 
								&& decisioningCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy().getUnifiedCreditAssessmentIndicator()!=null 
								) {
							postDecisioningProcessor = getPostDecisioningProcessorBean("UnifiedCreditSuccessPostDecisioningProcessor"); 
						}
						else {							
							postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessPostDecisioningProcessor"); 
						}
					}
				}else{
					postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedPostDecisioningProcessor");
				}           		
			}else 
			{
				if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_NOCHANGE.equalsIgnoreCase(reasonCd)) {
					if(isValidCreditAssessmentResult(decisioningCreditAssessmentResultWrapper,creditAssessmentRequest)){
						if (EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT.equalsIgnoreCase(assessmentType)
								&& EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD.equalsIgnoreCase(assessmentSubType)) {
							postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessNoChangeDisregardPostDecisioningProcesser");
						} else {
							postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessNoChangePostDecisioningProcessor");                      
						}
					}else{
						postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedPostDecisioningProcessor");
					}
				} else {
					if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_BUREAU_REQUIRED.equalsIgnoreCase(reasonCd)) {
						postDecisioningProcessor = getPostDecisioningProcessorBean("BureauDataRequiredPostDecisioningProcessor");

					} else {
						//SUCCESS resultCd and Unsupported reasonCd
						postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedPostDecisioningProcessor");
			            LogUtil.errorlog(
			        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE + 
			        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG + 
			        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
			        			"AssessmentResultCd = "  + resultCd +
			        			"AssessmentResultReasonCd = "  + reasonCd			        			
			        			);
					}
				}
			}
		} else {
			if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR.equalsIgnoreCase(resultCd) 
					||"".equalsIgnoreCase(resultCd) 
					||  null== resultCd
			){
				postDecisioningProcessor = getPostDecisioningProcessorBean("FailedPostDecisioningProcessor");
			} else {
				postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedPostDecisioningProcessor");
			}
            LogUtil.errorlog(
        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE + 
        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG + 
        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
        			"AssessmentResultCd = "  + resultCd +
        			"AssessmentResultReasonCd = "  + reasonCd			        			
        			);			
		}
		return postDecisioningProcessor;
	}

	/*
	 *
	 * On any subsequent calls to Decision-ing Engine (Fico) , Fico is expected
	 * to return a successful result/result reason code. Any other value will be
	 * considered as failed Decision-ing.
	 */
	public static PostDecisioningProcessor create_On_SubsequentDecisioning(
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentResultWrapper aCreditAssessmentResultWrapper) {
		LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
		// default
		PostDecisioningProcessor postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedPostDecisioningProcessor");
		String assessmentType = creditAssessmentRequest.getCreditAssessmentTypeCd();
		String assessmentSubType = creditAssessmentRequest.getCreditAssessmentSubTypeCd();
		String resultCd = (aCreditAssessmentResultWrapper.getAssessmentResultCd()== null)?"":aCreditAssessmentResultWrapper.getAssessmentResultCd().trim();
		String reasonCd = (aCreditAssessmentResultWrapper.getAssessmentResultReasonCd()== null)?"":aCreditAssessmentResultWrapper.getAssessmentResultReasonCd().trim();        

		postDecisioningProcessor = getOn_SubsequentDecisioningPostDecisioningProcessor(
					creditAssessmentRequest,assessmentType, assessmentSubType,resultCd, 
					reasonCd,aCreditAssessmentResultWrapper,false);

		LogUtil.infolog(" PostDecisioningProcesserFactory Returning    " + postDecisioningProcessor.getClass().getName());
		return postDecisioningProcessor;
	}
 
 
	private static PostDecisioningProcessor getPostDecisioningProcessorBean(String beanId) {
		return (PostDecisioningProcessor) m_ApplicationContext.getBean(beanId);
	}   
	private static boolean isValidCreditAssessmentResult(
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper
			,CreditAssessmentRequest creditAssessmentRequest) {
			boolean valid=true;
			String invalidReasonMeg="";
	       	
			String aCreditValueCd = decisioningCreditAssessmentResultWrapper.getCreditValueCd();
	        String aDecisionCd = decisioningCreditAssessmentResultWrapper.getDecisionCd();
	        ProductCategoryQualification aProductCategoryQualification =decisioningCreditAssessmentResultWrapper.getProductCategoryQualification();
	        
	        if (aCreditValueCd== null || aCreditValueCd.trim().equals("")){
	        	valid=false;
	        	invalidReasonMeg = invalidReasonMeg + " [ Decisioning Result :  CreditValueCd = " + aCreditValueCd + "] " ;
	        }
	        if (aDecisionCd== null || aDecisionCd.trim().equals("")){
	        	valid=false;
	        	invalidReasonMeg = invalidReasonMeg + " [ Decisioning Result :  DecisionCd = " + aDecisionCd + "] " ;
	        }	
	        if (aProductCategoryQualification== null || aProductCategoryQualification.getProductCategoryList()== null ){
	        	valid=false;
	        }	 
	        List<ProductCategory> aProductCategoryList = aProductCategoryQualification.getProductCategoryList();
	        for (ProductCategory productCategory : aProductCategoryList) {
		        if (productCategory.getCategoryCd()== null || productCategory.getCategoryCd().trim().equals("")){
		        	valid=false;
		        	invalidReasonMeg = invalidReasonMeg + " [ Decisioning Result :  productCategory.CategoryCd = " + productCategory.getCategoryCd() + "] " ;
		        }				
			}
	        if(!valid){
	            LogUtil.errorlog(
	        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_RESULT_VALIDATION_WARNING_CODE + 
	        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_RESULT_VALIDATION_WARNING_MSG + 
	        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
	        			invalidReasonMeg 
	        			);
	        }
		return valid;
	}

	private static PostDecisioningProcessor getOn_SubsequentDecisioningPostDecisioningProcessor(
			CreditAssessmentRequest creditAssessmentRequest,
			String assessmentType, 
			String assessmentSubType, 
			String resultCd, 
			String reasonCd, 
			CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper, 
			boolean firstcall2) {
		PostDecisioningProcessor postDecisioningProcessor;

		if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS.equalsIgnoreCase(resultCd)) { 
			if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_SUCCESS.equalsIgnoreCase(reasonCd)) {
				if(isValidCreditAssessmentResult(decisioningCreditAssessmentResultWrapper,creditAssessmentRequest)){
						postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessWithNewBureaDataPostDecisioningProcessor");
				}else{
					postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedWithNewBureaDataPostDecisioningProcessor");
				}
			} else {
				if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_NOCHANGE.equalsIgnoreCase(reasonCd)) {       
					if(isValidCreditAssessmentResult(decisioningCreditAssessmentResultWrapper,creditAssessmentRequest)){	
						if (EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT.equalsIgnoreCase(assessmentType)
								&& EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD.equalsIgnoreCase(assessmentSubType)) {
							postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessWithNewBureaDataNoChangeDisregardPostDecisioningProcesser");
						} else {
							postDecisioningProcessor = getPostDecisioningProcessorBean("SuccessWithNewBureaDataNoChangePostDecisioningProcesser");                       
						}
					}else{
						postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedWithNewBureaDataPostDecisioningProcessor");
			            LogUtil.errorlog(
			        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE + 
			        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG + 
			        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
			        			"AssessmentResultCd = "  + resultCd +
			        			"AssessmentResultReasonCd = "  + reasonCd			        			
			        			);						
					}
				} else {
					if (EnterpriseCreditAssessmentConsts.DECISIONING_REASON_CD_BUREAU_REQUIRED.equalsIgnoreCase(reasonCd)) {
						postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedWithNewBureaDataPostDecisioningProcessor");
					} else {
						//SUCCESS resultCd and Unsupported reasonCd
						postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedWithNewBureaDataPostDecisioningProcessor");
					}
		            LogUtil.errorlog(
		        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE + 
		        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG + 
		        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
		        			"AssessmentResultCd = "  + resultCd +
		        			"AssessmentResultReasonCd = "  + reasonCd			        			
		        			);
				}
			}
		} else {
			if (EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_ERROR.equalsIgnoreCase(resultCd) ||
					"".equalsIgnoreCase(resultCd) || 
					null== resultCd
			){
				postDecisioningProcessor = getPostDecisioningProcessorBean("FailedPostDecisioningProcessor");
			} else {
				postDecisioningProcessor = getPostDecisioningProcessorBean("NotSupportedWithNewBureaDataPostDecisioningProcessor");
			}
            LogUtil.errorlog(
        			" Warning Code:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE + 
        			" Warning Message:" + EnterpriseCreditAssessmentExceptionCodes.FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG + 
        			" CustomerID="  + creditAssessmentRequest.getCustomerID()+ ". "+ 
        			"AssessmentResultCd = "  + resultCd +
        			"AssessmentResultReasonCd = "  + reasonCd			        			
        			);
		}
		return postDecisioningProcessor;
	}



}