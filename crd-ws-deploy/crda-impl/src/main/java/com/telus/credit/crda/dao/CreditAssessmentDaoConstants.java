package com.telus.credit.crda.dao;

public class CreditAssessmentDaoConstants {


    public static final String NO_HIT_THIN_FILE_CD = "1";
    public static final String TRUE_THIN_FILE_CD = "2";
    public static final String HIGH_RISK_THIN_FILE_CD = "3";
    public static final String TEMP_SIN_CD = "4";
    public static final String UNDER_AGE_CD = "5";
    public static final String BANKCRUPCY_CD = "6";
    public static final String PRIMARY_RISK_CD = "7";
    public static final String SECONDARY_RISK_CD = "8";
    public static final String WRITE_OFF_INDICATOR_CD = "9";

    public static final String INT_CRD_DCSN_STAT_PENDING = "1";
    public static final String INT_CRD_DCSN_STAT_COMPLETED = "2";

    public static final String RESULT_DTL_PROD_QUALIFICATION_CD = "1";
    public static final String RESULT_DTL_FRAUD_WARNING_CD = "2";
    public static final String RESULT_DTL_BUREAU_INFO_CD = "3";

    public static final String CREDIT_BUREAU_DTL_DOC_TYPE = "4";
    public static final String CREDIT_BUREAU_DTL_FRAUD_TYPE = "3";
    public static final String CREDIT_BUREAU_DTL_RISK_TYPE = "2";
    public static final String CREDIT_BUREAU_DTL_SCORE_TYPE = "1";

    public static final String CAR_ATTR_CURRENT_CREDIT_CHECK_CONSENT_CD = "cConsentCd";
    public static final String CAR_ATTR_NEW_CREDIT_CHECK_CONSENT_CD = "nConsentCd";
    public static final String CAR_ATTR_CURRENT_CREDIT_VALUE_CD = "cCreditValCd";
    public static final String CAR_ATTR_NEW_CREDIT_VALUE_CD = "nCreditValCd";
    public static final String CAR_ATTR_CURRENT_FRAUD_CD = "cFraudCd";
    public static final String CAR_ATTR_NEW_FRAUD_CD = "nFraudCd";

    public static final String GET_BASE_CREDIT_ASSESSMENT_BY_ID = "get_credit_assessment.get_base_credit_assessment";
    public static final String GET_FULL_CRD_ASSESSMENT_REQUEST = "get_credit_assessment.get_full_credit_assessment";
    
    public static final String GET_CRD_DCSN_RESULT_BY_PARENT_ID = "get_credit_assessment.get_int_crdt_dcsn_trn_rslt_by_parent_id";
    public static final String GET_CRD_BUREAU_TRN = "get_credit_assessment.get_credit_bureau_trn";
    public static final String GET_CRD_BUREAU_TRN_DTL_BY_PARENT_ID = "get_credit_assessment.get_credit_bureau_trn_dtl_by_parent_id";
    public static final String GET_OVERRIDE_CRD_ASSESSMENT_REQUEST = "get_credit_assessment.get_override_credit_assessment";
    public static final String GET_CAR_ATTR_VALUE_BY_CAR_ID = "get_credit_assessment.get_car_attr_values_car_id";
 
    public static final String SEARCH_CREDIT_ASSESSMENT = "search_credit_assessment.search_credit_assessments";
    public static final String SEARCH_CREDIT_ASSESSMENT_DECISION_RESULT_DTL = "search_credit_assessment.search_credit_assessment_decision_result_dtl";

    public static final String INSERT_CAR_ACTIVITY = "credit_assessment.insert_car_activity";
    public static final String EXPIRE_CAR_STATUS = "credit_assessment.expire_car_status";
    public static final String EXPIRE_CREDIT_BUREAU_TRN = "credit_assessment.expire_credit_bureau_transaction";

    public static final String INSERT_CAR = "credit_assessment.insert_car";
    public static final String INSERT_CAR_STATUS = "credit_assessment.insert_car_status";
    public final static String STAT_ID_EXPIRE_CRDMGT_COMMENT = "credit_assessment.expire_crdmgt_comment";
    public final static String STAT_ID_INSERT_CRDMGT_COMMENT = "credit_assessment.insert_crdmgt_comment";
    public static final String INSERT_CAR_CREDIT_PROFILE = "credit_assessment.insert_car_credit_profile";
    public static final String INSERT_CUST_CREDIT_ASSMNT_RQST = "credit_assessment.cust_credit_assmnt_rqst";
    public static final String GET_PRIMARY_CREDIT_PROFILE_ID = "credit_assessment.get_primary_credit_profile_id";

    public static final String INSERT_CREDIT_BUREAU_TRN = "credit_bureau.save_credit_bureau_trn";
    public static final String INSERT_CREDIT_BUREAU_DTL_TRN_FRAUD = "credit_bureau.save_credit_bureau_trn_dtl_fraud_type";
    public static final String INSERT_CREDIT_BUREAU_DTL_TRN_RISK = "credit_bureau.save_credit_bureau_trn_dtl_risk";
    public static final String INSERT_CREDIT_BUREAU_DTL_TRN_DOC_TYPE = "credit_bureau.save_credit_bureau_trn_dtl_doc_type";
    public static final String INSERT_CREDIT_BUREAU_DTL_TRN_SCORE_TYPE = "credit_bureau.save_credit_bureau_trn_dtl_score";
    public static final String GET_CREDIT_BUREAU_TRN_DTL_DOC_PATH = "credit_bureau.get_credit_bureau_trn_dtl_doc_path";
    public static final String GET_CREDIT_BUREAU_TRN_MAP = "credit_bureau.get_credit_bureau_trn_map";

    public static final String SAVE_STG_CREDIT_DCSN_TRN = "credit_decision.save_stg_credit_dcsn_trn";
    public static final String SAVE_INT_CRDT_DCSN_TRN = "credit_decision.save_int_crdt_dcsn_trn";
    public static final String SAVE_INT_CRDT_DCSN_TRN_STAT = "credit_decision.save_int_crdt_dcsn_trn_stat";
    public static final String SAVE_INT_CRDT_DCSN_TRN_RSLT = "credit_decision.save_int_crdt_dcsn_trn_rslt";
    public static final String SAVE_INT_CRDT_DCSN_TRN_RSLT_DTL = "credit_decision.save_int_crdt_dcsn_trn_rslt_dtl";
    public static final String UPDATE_INT_CRDT_DCSN_TRN = "credit_decision.update_int_crdt_dcsn_trn";
    public static final String UPDATE_INT_CRDT_DCSN_TRN_STAT = "credit_decision.update_int_crdt_dcsn_trn_stat";
    public static final String INSERT_CAR_ATTR_VALUE = "credit_decision.save_car_attr_value";
    //UC New Tables
    public static final String SAVE_UC_SEARCH_RESULT = "credit_decision.save_uc_search_result";
    public static final String SAVE_UC_DATA_INQUIRY_ERROR = "credit_decision.save_uc_data_inquiry_error";
    public static final String SAVE_UC_WLS_MATCH_ACCOUNT = "credit_decision.save_uc_wls_match_account";
    public static final String SAVE_UC_WARNING_HIST = "credit_decision.save_uc_warning_hist";
    //public static final String SAVE_ = "credit_decision.";
    
    public static final String REPORT_SOURCE_EQUIFAX = "EQUIFAX";
    public static final Object REPORT_SOURCE_TRANSUNION = "TRANSUNION";
    public static final String REPORT_SOURCE_UNIFIED_CREDIT = "UNIFIEDCREDIT";
    public static final String REPORT_DOC_TYPE_DAT = "DAT";
    public static final Object REPORT_DOC_TYPE_TXT = "TXT";
    
}
