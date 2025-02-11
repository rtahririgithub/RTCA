package com.telus.credit.crda.util.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crdgw.domain.BaseReportResponse.ReportDocument;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.erm.referenceods.domain.ReferenceMessageDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

public class InternalRules {

	public static boolean getCreditBureauReportInd(ReportDocument aReportDocument){
		// check whether there is a BureauReportDocument or PrintImageDocument no matter which bureau it is coming from.
		if ( 					
				aReportDocument!= null 
				&&
				(
					//is there a BureauReportDocument?
					(
						aReportDocument.getBureauReportDocument() != null &&
						aReportDocument.getBureauReportDocument().getDocumentPath()!= null &&
						!aReportDocument.getBureauReportDocument().getDocumentPath().trim().equals(EnterpriseCreditAssessmentConsts.EMPTY_STR)
					)
					||
					// or is there a PrintImageDocument?
					(
						aReportDocument.getPrintImageDocument() != null &&
						aReportDocument.getPrintImageDocument().getDocumentPath()!= null &&
						!aReportDocument.getPrintImageDocument().getDocumentPath().trim().equals(EnterpriseCreditAssessmentConsts.EMPTY_STR)
					)
				)
			){
			return true;
		}
		return false;
	}	
	// check whether there is a BureauReportDocument and it is from equifax or transunion 
	public static boolean getCreditBureauReportInd(String aDocPath,String aCreditBureauReportSourceCd){

		// TODO remove check for source being equifax or transunion .

		aDocPath = (aDocPath == null) ? "" : aDocPath.trim();
		aCreditBureauReportSourceCd = (aCreditBureauReportSourceCd == null) ? ""
				: aCreditBureauReportSourceCd.trim();

		if (aDocPath.length() > 0
				&& aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_EQUIFAX)
				|| aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_TRANSUNION)
				|| aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_UNIFIED_CREDIT)
				|| aCreditBureauReportSourceCd.equals("UNIFIED_CREDIT")) {
			return true;
		}
		return false;
	}


	public static String determineCreditAssessmentDataSourceCd( CreditAssessmentTransaction aCreditAssessmentTransaction) {
		if (aCreditAssessmentTransaction != null
				&& aCreditAssessmentTransaction.getCreditDecisioningResult() != null) {
			if (aCreditAssessmentTransaction instanceof CreditAssessmentDetails) {
				CreditAssessmentDetails aCreditAssessmentDetails = (CreditAssessmentDetails) aCreditAssessmentTransaction;
				if (aCreditAssessmentDetails.getCreditBureauDataResult() != null) {
					return aCreditAssessmentDetails.getCreditBureauDataResult().getReportSourceCd();
				}
			} else if (aCreditAssessmentTransaction.getCreditBureauReportSourceCd() != null
					&& !aCreditAssessmentTransaction.getCreditBureauReportSourceCd().trim().equals("")) {
				return aCreditAssessmentTransaction.getCreditBureauReportSourceCd();
			}
			return EnterpriseCreditAssessmentConsts.DECISION_SYS_NAME;
		}
		return null;
	}

	
	
	

	// Provides the default/hardcoded system comment for asmt type/subtype to be used as default values
	private static final Map<String, String> defaultCommentTxtMap;
	static {
		defaultCommentTxtMap = new HashMap<String, String>();
		defaultCommentTxtMap.put("FULL_ASSESSMENT_AUTO_ASSESSMENT"		,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_002);
		defaultCommentTxtMap.put("OVRD_ASSESSMENT_UNMERGED"				,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_003);
		defaultCommentTxtMap.put("FULL_ASSESSMENT_MONTHLY_CVUD"			,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_004);
		defaultCommentTxtMap.put("OVRD_ASSESSMENT_CANCEL_DEPOSIT_INV"	,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_005);
		defaultCommentTxtMap.put("OVRD_ASSESSMENT_DEPOSIT_DOWNGRADE"	,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_006);
		defaultCommentTxtMap.put("FULL_ASSESSMENT_REOPEN_ASSESSMENT"	,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_007);
		defaultCommentTxtMap.put("FULL_ASSESSMENT_NEW_ACC_ASSESSMENT"	,EnterpriseCreditAssessmentConsts.CRDA_COMMENT_TEXT_008);
	}
	
	
	// Provides comment Ref pds code for asmt type/subtype
	private static final Map<String, String> comment_RefPds_Keys_Map; 
	static {
		comment_RefPds_Keys_Map = new HashMap<String, String>();
		comment_RefPds_Keys_Map.put("FULL_ASSESSMENT_AUTO_ASSESSMENT"			,"CRDA-TEXT-002");
		comment_RefPds_Keys_Map.put("OVRD_ASSESSMENT_UNMERGED"					,"CRDA-TEXT-003");
		comment_RefPds_Keys_Map.put("FULL_ASSESSMENT_MONTHLY_CVUD"				,"CRDA-TEXT-004");
		comment_RefPds_Keys_Map.put("OVRD_ASSESSMENT_CANCEL_DEPOSIT_INV"		,"CRDA-TEXT-005");
		comment_RefPds_Keys_Map.put("OVRD_ASSESSMENT_DEPOSIT_DOWNGRADE"			,"CRDA-TEXT-006");
		comment_RefPds_Keys_Map.put("FULL_ASSESSMENT_REOPEN_ASSESSMENT"			,"CRDA-TEXT-007");
		comment_RefPds_Keys_Map.put("FULL_ASSESSMENT_NEW_ACC_ASSESSMENT"		,"CRDA-TEXT-008");			

		// FULL_ASSESSMENT GET_BUREAU_DATA to be provided by caller app
		// FULL_ASSESSMENT MANUAL_ASSESSMENT to be provided by caller app
		// AUDIT BUREAU_CONSENT to be provided by caller app
		// OVRD_ASSESSMENT MANUAL_OVERRIDE to be provided by caller app

	} 
	

	//List of Unified Credit Supported Assessment Type_SubType
	//if found in this list the request can be evaluated as UC request
	private static final List<String> IS_UC_SUPPORTED_ASMT_TYPE_SUBTYPE; 
	static {
		IS_UC_SUPPORTED_ASMT_TYPE_SUBTYPE = new ArrayList<String>();
		IS_UC_SUPPORTED_ASMT_TYPE_SUBTYPE.add(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT + "_" + EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT);
		};



	
	public static String lookupCommentByAsmtTypeSubtype(
			CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentServiceException {
		return lookupCommentByAsmtTypeSubtype(creditAssessmentRequest, null);
	}

	public static String lookupCommentByAsmtTypeSubtype( 
			CreditAssessmentRequest creditAssessmentRequest,
			CreditAssessmentTransactionResult aCreditAssessmentTransactionResult) throws EnterpriseCreditAssessmentServiceException {
		
		String typesubtypeKey = creditAssessmentRequest
				.getCreditAssessmentTypeCd()
				+ "_" + creditAssessmentRequest.getCreditAssessmentSubTypeCd();

		// Init to default comment text defined in src code as constants
		String refpdsKeyTxt = defaultCommentTxtMap.get(typesubtypeKey) != null ? defaultCommentTxtMap
				.get(typesubtypeKey)
				: creditAssessmentRequest.getCommentTxt();

		// get comment text from ref pds combination of credit assessment type
		// subtype provided in the input request
		// ***********************************
		// refpdsKeyVal = get the system comment the ref pds code representing a
		// comment using combination of credit assessment type subtype provided
		// in the input request
		// if it doesn't exist it indicates it is expected to be provided by the
		// caller application and use the comment from request input.
		String refpdsKeyVal = comment_RefPds_Keys_Map.get(typesubtypeKey) != null ? comment_RefPds_Keys_Map
				.get(typesubtypeKey)
				: creditAssessmentRequest.getCommentTxt();
		ReferenceMessageDecode refMsg = ReferencePdsAccess.getMessage(
				refpdsKeyVal, ReferencePdsAccess.LANG_EN);

		if (refMsg != null)
			refpdsKeyTxt = refMsg.getText();

		// update the comment in case of ASMT_SUB_TYPE_MONTHLY_CVUD with
		// fromCrdValue and toCrdValue
		if (	
				creditAssessmentRequest.getCreditAssessmentSubTypeCd().equalsIgnoreCase(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MONTHLY_CVUD)
			) {
			if(	aCreditAssessmentTransactionResult != null ){				
				String fromCrdValue = "";
				String toCrdValue = "";
				ExistingCustomerCreditAssessmentRequest aExistingCustomerCreditAssessmentRequest=null;
				
				try {
					aExistingCustomerCreditAssessmentRequest = (ExistingCustomerCreditAssessmentRequest) creditAssessmentRequest;
				} catch (java.lang.ClassCastException e) {
					throw new EnterpriseCreditAssessmentServiceException(EnterpriseCreditAssessmentExceptionCodes.INVALIDA_REQUEST_SCHEMA_TYPE_STR+ " [ Credit assessment type_subtype: "+ typesubtypeKey+ "] "+ e.getMessage()+ " [ Customer ID ="+ creditAssessmentRequest.getCustomerID() + "]",EnterpriseCreditAssessmentExceptionCodes.INVALIDA_REQUEST_SCHEMA_TYPE);
				}
				//fromCrdValue
				if (aExistingCustomerCreditAssessmentRequest != null && 
						aExistingCustomerCreditAssessmentRequest.getCreditProfileData() != null && 
							aExistingCustomerCreditAssessmentRequest.getCreditProfileData().getCreditWorthiness() != null) {
					fromCrdValue = aExistingCustomerCreditAssessmentRequest.getCreditProfileData().getCreditWorthiness().getCreditValueCd();
				}
				//toCrdValue
				if (aCreditAssessmentTransactionResult != null
						&& aCreditAssessmentTransactionResult.getCreditDecisioningResult() != null) {
					toCrdValue = aCreditAssessmentTransactionResult.getCreditDecisioningResult().getCreditValueCd();
				}
				
				refpdsKeyTxt = refpdsKeyTxt + " " + fromCrdValue + " to " + toCrdValue;
			}else
			{
				refpdsKeyTxt = refpdsKeyTxt + " : NO CHANGE";
			}
		}

		return refpdsKeyTxt;
	}
		
	public static boolean isUnifiecCreditRuleApplies( String key ) {
		if( IS_UC_SUPPORTED_ASMT_TYPE_SUBTYPE.contains(key) ) {
			return true;
		}
		return false;
	}
		
	public static String getUnifiedCreditCommentText(String ucIndicator, String ucReason, Long ban) throws EnterpriseCreditAssessmentServiceException {
		String ucCommentFreg = EnterpriseCreditAssessmentConsts.CRDA_COMMENT_UNIFIED_CREDIT_INDICATOR + ucIndicator + ".\r\n";
		ucCommentFreg += EnterpriseCreditAssessmentConsts.CRDA_COMMENT_UNIFIED_CREDIT_REASON + ucReason;
		String ucReasonDesc = get_Operational_Data_By_Param(ucReason);
		ucCommentFreg += ((ucReasonDesc.length()>0) ? " - " + ucReasonDesc : "");
		if(ban != null) {
			ucCommentFreg += " [" + ban + "]";
		}
		return ucCommentFreg;
	}
	
	private static final String CREDIT_OPERATION_PARM_TABLE_NAME = "UC_ASSESSMENT_RESULT_REASON"; 
	private static String get_Operational_Data_By_Param(String param)
			throws EnterpriseCreditAssessmentServiceException {
		String value = "";
		try {
			value = ReferencePdsAccess.getDecodeText(CREDIT_OPERATION_PARM_TABLE_NAME, ReferencePdsAccess.LANG_EN, param);
			if(value==null) value = "";
		} catch (Throwable e) {
			throw new EnterpriseCreditAssessmentServiceException(
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION_STR
							+ "[operation = get_Operational_Data_By_Param]" + "[Table name ="
							+ CREDIT_OPERATION_PARM_TABLE_NAME + "]" + "[" + param + "]",
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION, e);
		}
		return value;
	}
}
