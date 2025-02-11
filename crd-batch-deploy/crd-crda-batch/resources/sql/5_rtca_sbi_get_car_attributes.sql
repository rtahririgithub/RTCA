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
    car.CREDIT_ASSESSMENT_REQUEST_ID ||'|'|| 
    car_val_credit_val_1.CAR_ATTR_VALUE_TXT ||'|'|| car_val_credit_val_1.CAR_ATTR_TYP_CD||'|'||
    car_val_credit_val_2.CAR_ATTR_VALUE_TXT ||'|'|| car_val_credit_val_2.CAR_ATTR_TYP_CD||'|'||
    car_val_fraud_1.CAR_ATTR_VALUE_TXT      ||'|'|| car_val_fraud_1.CAR_ATTR_TYP_CD||'|'||
    car_val_fraud_2.CAR_ATTR_VALUE_TXT      ||'|'|| car_val_fraud_2.CAR_ATTR_TYP_CD||'|'||
    car_val_consent_1.CAR_ATTR_VALUE_TXT    ||'|'|| car_val_consent_1.CAR_ATTR_TYP_CD||'|'||
    car_val_consent_2.CAR_ATTR_VALUE_TXT    ||'|'|| car_val_consent_2.CAR_ATTR_TYP_CD
FROM credit_assessment_request car
    INNER JOIN car_status car_status
    ON ( car_status.CREDIT_ASSESSMENT_REQUEST_ID = car.CREDIT_ASSESSMENT_REQUEST_ID 
            AND car_status.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    INNER JOIN CUST_CREDIT_ASSMNT_RQST car_cust
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID 
            and car_cust.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT JOIN CAR_ATTR_VALUE car_val_credit_val_1 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_credit_val_1.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_credit_val_1.CAR_ATTR_TYP_CD = 'cCreditValCd'
    and car_val_credit_val_1.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
    LEFT OUTER JOIN CAR_ATTR_VALUE car_val_credit_val_2 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_credit_val_2.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_credit_val_2.CAR_ATTR_TYP_CD = 'nCreditValCd'
    and car_val_credit_val_2.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
    LEFT OUTER JOIN CAR_ATTR_VALUE car_val_fraud_1 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_fraud_1.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_fraud_1.CAR_ATTR_TYP_CD = 'cFraudCd' 
    and car_val_fraud_1.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
    LEFT OUTER JOIN CAR_ATTR_VALUE car_val_fraud_2 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_fraud_2.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_fraud_2.CAR_ATTR_TYP_CD = 'nFraudCd' 
    and car_val_fraud_2.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
    LEFT OUTER JOIN CAR_ATTR_VALUE car_val_consent_1 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_consent_1.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_consent_1.CAR_ATTR_TYP_CD = 'cConsentCd' 
    and car_val_consent_1.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
    LEFT OUTER JOIN CAR_ATTR_VALUE car_val_consent_2 
    ON (car.CREDIT_ASSESSMENT_REQUEST_ID = car_val_consent_2.CREDIT_ASSESSMENT_REQUEST_ID
    and car_val_consent_2.CAR_ATTR_TYP_CD = 'nConsentCd'
    and car_val_consent_2.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') ) 
WHERE 
        car.CREATE_TS between 
        CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
        AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
   ORDER BY 
        car.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
