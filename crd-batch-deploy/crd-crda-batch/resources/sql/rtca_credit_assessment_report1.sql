/*
    CREDIT ASSESSMENT SUMMARY REPORT [2012/10/25 - for review]
    
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 32767
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool '&1';

select
    car.CREDIT_ASSESSMENT_REQUEST_ID            || '|' ||
    cust_car.CUSTOMER_ID                        || '|' ||
    stg.CUSTOMER_STATUS_CD                      || '|' ||
    car.CREATE_USER_ID                          || '|' ||
    car.DATA_SOURCE_ID                          || '|' ||
    TO_CHAR(car.CREATE_TS, 'YYYY-MM-DD HH24:mm:ss')        || '|' ||
    car.CAR_TYP_CD                              || '|' ||
    car.CAR_SUBTYPE_CD                          || '|' ||
    TO_CHAR(stg.CUSTOMER_CREATION_DT, 'YYYY-MM-DD')                    || '|' ||
    stg.BYPASS_MATCH_IND                        || '|' ||
    stg.FIRST_NM                                || '|' ||
    stg.MIDDLE_NM                               || '|' ||
    stg.LAST_NM                                 || '|' ||
    replace(stg.ADDRESS_LINE1_TXT, ',', ' ')    || '|' || 
    replace(stg.ADDRESS_LINE2_TXT, ',', ' ')    || '|' || 
    replace(stg.ADDRESS_LINE3_TXT, ',', ' ')    || '|' || 
    replace(stg.ADDRESS_LINE4_TXT, ',', ' ')    || '|' || 
    replace(stg.ADDRESS_LINE5_TXT, ',', ' ')    || '|' ||
    replace(stg.CITY_NM, ',', ' ')              || '|' ||  
    replace(stg.PROVINCE_CD, ',', ' ')          || '|' || 
    replace(stg.COUNTRY_CD, ',', ' ')           || '|' || 
    replace(stg.POSTAL_ZIP_CD, ',', ' ')        || '|' ||
    stg.APPLICATION_SUB_PROV_CD                 || '|' ||
    stg.CUST_TYP_CD                             || '|' ||
    cbt.REPORT_SOURCE_CD                        || '|' ||
    stg.SOCIAL_INSURANCE_NUM                    || '|' ||
    stg.DRIVER_LICENSE_STR                      || '|' ||
    stg.DRIVER_LICENSE_PROV_CD                  || '|' ||
    stg.PASSPORT_STR                            || '|' ||
    stg.PASSPORT_COUNTRY_CD                     || '|' ||
    stg.PROVINCIAL_ID_STR                       || '|' ||
    stg.PROVINCIAL_ID_PROV_CD                   || '|' ||
    stg.HEALTHCARE_NUM                          || '|' ||
    stg.HEALTHCARE_PROV_CD                      || '|' ||
    stg.EMPLOYMENT_STATUS_CD                    || '|' ||
    stg.RESIDENCY_CD                            || '|' ||
    stg.PRIM_CRED_CARD_TYP_CD                   || '|' ||
    stg.SEC_CRED_CARD_ISS_CO_TYP_CD             || '|' ||
    stg.LEGAL_CARE_CD                           || '|' ||
    TO_CHAR(stg.BIRTH_DT, 'YYYY-MM-DD')         || '|' ||
    CASE
        WHEN cbt.REPORT_SOURCE_CD = 'SIMULATOR'
    THEN
        'Y'
    ELSE
        'N'
    END                                         || '|' ||
    cbt.ADJDCTN_CREDIT_SCORE_TXT                || '|' ||
    cbt.ADJDCTN_CREDIT_CLASS_CD                 || '|' ||
    cbt.ADJDCTN_DSCN_CD                         || '|' ||
    cbt.ADJDCTN_DSCN_MSG_TXT                    || '|' ||
    TO_CHAR(car.CREATE_TS, 'YYYY-MM-DD')        || '|' ||
    stg.COLLECTION_SEGMENT                        || '|' ||
    stg.SCORECARD_ID 						|| '|' ||
    stg.CYCLES_DELINQUENT
from
    credit_assessment_request car 
    INNER JOIN cust_credit_assmnt_rqst cust_car ON
        (car.CREDIT_ASSESSMENT_REQUEST_ID = cust_car.CREDIT_ASSESSMENT_REQUEST_ID
        AND cust_car.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD')) 
    LEFT OUTER JOIN stg_credit_dcsn_trn stg ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = stg.CREDIT_ASSESSMENT_REQUEST_ID
        AND stg.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
    LEFT OUTER JOIN credit_bureau_trn cbt ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = cbt.CREDIT_ASSESSMENT_REQUEST_ID
        AND cbt.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
    LEFT OUTER JOIN INT_CRDT_DCSN_TRN icd_tran ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = icd_tran.CREDIT_ASSESSMENT_REQUEST_ID
        AND icd_tran.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') and icd_tran.ASSESSMENT_RSLT_CD='SUCCESS' AND icd_tran.ASSESSMENT_RSLT_REASON_CD IS NULL)
    where car.CREATE_TS BETWEEN TO_DATE('&2','YYYYMMDD') AND TO_DATE('&3','YYYYMMDD')
;


spool off;
exit 0
;
