set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set trimspool on
set linesize 32767;
set ARRAYSIZE 5000;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1

SELECT 
     int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID ||'|'||
     stg_decision_trn.STG_CREDIT_DSCN_TRN_ID ||'|'|| 
     stg_decision_trn.CUSTOMER_ID ||'|'|| 
     stg_decision_trn.FIRST_NM ||'|'||
     stg_decision_trn.MIDDLE_NM ||'|'||
     stg_decision_trn.LAST_NM ||'|'|| 
     TO_CHAR(stg_decision_trn.CUSTOMER_CREATION_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     TO_CHAR(stg_decision_trn.BIRTH_DT, 'YYYY-MM-DD') ||'|'||
     stg_decision_trn.CUSTOMER_MASTER_SRC_ID ||'|'|| 
     stg_decision_trn.CUSTOMER_STATUS_CD ||'|'|| 
     stg_decision_trn.CUST_SUBTYP_CD ||'|'||
     stg_decision_trn.CUST_TYP_CD ||'|'||
     stg_decision_trn.LANGUAGE_CD ||'|'|| 
     stg_decision_trn.CONTACT_PHONE_NUM ||'|'||
     stg_decision_trn.CREDIT_PROFILE_ID ||'|'||
     stg_decision_trn.DRIVER_LICENSE_STR ||'|'|| 
     stg_decision_trn.DRIVER_LICENSE_PROV_CD ||'|'||
     stg_decision_trn.SOCIAL_INSURANCE_NUM ||'|'||
     stg_decision_trn.HEALTHCARE_NUM ||'|'|| 
     stg_decision_trn.HEALTHCARE_PROV_CD ||'|'||
     stg_decision_trn.PASSPORT_STR ||'|'||
     stg_decision_trn.PASSPORT_COUNTRY_CD ||'|'|| 
     stg_decision_trn.PROVINCIAL_ID_STR ||'|'|| 
     stg_decision_trn.PROVINCIAL_ID_PROV_CD ||'|'||
     stg_decision_trn.CREDIT_VALUE_CD ||'|'|| 
     stg_decision_trn.ADDRESS_LINE1_TXT ||'|'||
     stg_decision_trn.ADDRESS_LINE2_TXT ||'|'||
     stg_decision_trn.ADDRESS_LINE3_TXT ||'|'||
     stg_decision_trn.ADDRESS_LINE4_TXT ||'|'||
     stg_decision_trn.ADDRESS_LINE5_TXT ||'|'||
     stg_decision_trn.CITY_NM ||'|'||
     stg_decision_trn.PROVINCE_CD ||'|'||
     stg_decision_trn.COUNTRY_CD ||'|'||
     stg_decision_trn.POSTAL_ZIP_CD ||'|'||
     stg_decision_trn.PRIM_CRED_CARD_TYP_CD ||'|'||
     stg_decision_trn.SEC_CRED_CARD_ISS_CO_TYP_CD ||'|'||
     stg_decision_trn.LEGAL_CARE_CD ||'|'||
     stg_decision_trn.RESIDENCY_CD ||'|'||
     stg_decision_trn.EMPLOYMENT_STATUS_CD ||'|'||
     stg_decision_trn.CREDIT_PROFILE_STATUS_CD ||'|'||
     stg_decision_trn.CREDIT_CHK_CONSENT_CD ||'|'||
     stg_decision_trn.CREDIT_PROFILE_METHOD_CD ||'|'||
     stg_decision_trn.APPLICATION_SUB_PROV_CD ||'|'||
     stg_decision_trn.PROVINCE_OF_RESIDENCY_CD ||'|'||
     stg_decision_trn.BYPASS_MATCH_IND ||'|'||
     stg_decision_trn.CREDIT_BUREAU_SIM_FO_IND ||'|'||
     TO_CHAR(stg_decision_trn.BUSINESS_LAST_UPDT_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     TO_CHAR(stg_decision_trn.FIRST_CREDIT_ASSESMENT_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     TO_CHAR(stg_decision_trn.LAST_CREDIT_ASSESMENT_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     TO_CHAR(stg_decision_trn.CREDIT_VALUE_EFFECTIVE_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     stg_decision_trn.COLLECTION_IND ||'|'||
     TO_CHAR(stg_decision_trn.LAST_COLLECTION_START_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     TO_CHAR(stg_decision_trn.LAST_COLLECTION_END_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     stg_decision_trn.COLLECTION_SCORE_TXT ||'|'||
     stg_decision_trn.ACCOUNTS_IN_AGENCY_CNT ||'|'||
     TO_CHAR(stg_decision_trn.LATEST_AGENCY_ASSIGNMENT_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     stg_decision_trn.BALANCE_OWING_IN_AGENCY_AMT ||'|'||
     stg_decision_trn.INVOLUNTARY_CANCELLED_ACCT_CNT ||'|'||
     TO_CHAR(stg_decision_trn.LATEST_ICA_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     stg_decision_trn.BALANCE_OWING_ON_ICA_AMT ||'|'||
     stg_decision_trn.NSF_CHEQUES_CNT ||'|'||
     stg_decision_trn.FRAUD_IND_CD ||'|'||
     int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID ||'|'||
     int_crd_decision_trn.ASSESSMENT_RSLT_CD ||'|'||
     int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD ||'|'||
     int_crd_decision_trn_result.INT_CRDT_DSCN_TRN_RSLT_ID ||'|'||
     int_crd_decision_trn_result.ASSESSMENT_MSG_CD ||'|'||
     int_crd_decision_trn_result.CREDIT_VALUE_CD ||'|'||
     int_crd_decision_trn_result.FRAUD_IND_CD ||'|'||
     int_crd_decision_trn_result.PRODUCT_CATEGORY_BOLT_ON ||'|'||
     credit_bureau_trn.CREDIT_BUREAU_TRN_ID ||'|'||
     credit_bureau_trn.FIRST_NM ||'|'||
     credit_bureau_trn.MIDDLE_NM ||'|'||
     credit_bureau_trn.LAST_NM ||'|'||
     credit_bureau_trn.TRN_ERROR_CD ||'|'||
     credit_bureau_trn.REPORT_SOURCE_CD ||'|'||
     credit_bureau_trn.REPORT_TYP_TXT ||'|'||
     TO_CHAR(credit_bureau_trn.RSLT_CREATION_DT, 'YYYY-MM-DD') ||'|'||
     credit_bureau_trn.CREDIT_BUREAU_TRN_RSLT_STAT_CD ||'|'||
     TO_CHAR(credit_bureau_trn.CR_BRU_TRN_RSLT_STAT_UPDT_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TYPE_CD ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_CLASS_CD ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_LIMIT_AMT ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_CD ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_MSG_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_DEPOSIT_AMT ||'|'||
     int_crd_decision_trn_result.DEPOSIT_LOOKUP_STGY_CD ||'|'||
     int_crd_decision_trn_result.CVUD_STGY_CD ||'|'||
     int_crd_decision_trn_result.CREDIT_VAL_ASGMT_STGY_CD ||'|'||
     int_crd_decision_trn_result.CUSTOMER_ACTY_STGY_CD ||'|'||
     int_crd_decision_trn_result.TENURE_STGY_CD ||'|'||
     stg_decision_trn.COLLECTION_SEGMENT ||'|'||
     stg_decision_trn.SCORECARD_ID ||'|'||
     stg_decision_trn.CYCLES_DELINQUENT ||'|'||
     stg_decision_trn.RISK_LEVEL_NUM ||'|'||
     int_crd_decision_trn_result.RISK_LEVEL_NUM
    FROM
    INT_CRDT_DCSN_TRN int_crd_decision_trn
    INNER JOIN (SELECT inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID, 
                        MAX(inner_int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID) INT_CRDT_DSCN_TRN_ID
                FROM INT_CRDT_DCSN_TRN inner_int_crd_decision_trn
                INNER JOIN CREDIT_ASSESSMENT_REQUEST inner_car
                ON ( inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = inner_car.CREDIT_ASSESSMENT_REQUEST_ID 
                       AND inner_int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
                WHERE inner_car.LAST_UPDT_TS BETWEEN 
                CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
                AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
                GROUP BY   inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID ) max_int_crd_decision_trn
    ON (max_int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID )
    INNER JOIN INT_CRDT_DCSN_TRN_STAT int_crd_decision_trn_stat
    ON ( int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn_stat.INT_CRDT_DSCN_TRN_ID 
         AND int_crd_decision_trn_stat.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') 
    AND int_crd_decision_trn_stat.CREDIT_DSCN_TRN_STAT_CD = '2' )
    LEFT OUTER JOIN STG_CREDIT_DCSN_TRN stg_decision_trn
    ON ( int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = stg_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
         AND stg_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT OUTER JOIN INT_CRDT_DCSN_TRN_RSLT int_crd_decision_trn_result
    ON ( int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn_result.INT_CRDT_DSCN_TRN_ID 
         AND int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT OUTER JOIN CREDIT_BUREAU_TRN credit_bureau_trn
    ON ( int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = credit_bureau_trn.CREDIT_ASSESSMENT_REQUEST_ID
         AND credit_bureau_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
WHERE 
    int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD')
    AND int_crd_decision_trn.ASSESSMENT_RSLT_CD='SUCCESS'
    AND int_crd_decision_trn.CREATE_TS between 
    CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
    AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
ORDER BY 
    int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
