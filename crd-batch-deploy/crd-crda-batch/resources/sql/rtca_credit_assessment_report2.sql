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
    car.CREDIT_ASSESSMENT_REQUEST_ID        || '|' ||
    icd_result.CREDIT_VALUE_CD              || '|' ||
    icd_result.PRODUCT_CATEGORY_BOLT_ON     || '|' ||
    icd_result.ASSESSMENT_MSG_CD            || '|' ||
    icd_result.FRAUD_IND_CD				|| '|' ||
    icd_result.RISK_LEVEL_NUM
from 
    credit_assessment_request car 
    INNER JOIN INT_CRDT_DCSN_TRN icd_tran ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = icd_tran.CREDIT_ASSESSMENT_REQUEST_ID
        AND icd_tran.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') and icd_tran.ASSESSMENT_RSLT_CD='SUCCESS' AND icd_tran.ASSESSMENT_RSLT_REASON_CD IS NULL)
    LEFT OUTER JOIN INT_CRDT_DCSN_TRN_RSLT icd_result ON 
        (icd_tran.INT_CRDT_DSCN_TRN_ID = icd_result.INT_CRDT_DSCN_TRN_ID
        AND icd_result.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
where car.CREATE_TS BETWEEN TO_DATE('&2','YYYYMMDD') AND TO_DATE('&3','YYYYMMDD')
;

spool off;
exit 0
;



