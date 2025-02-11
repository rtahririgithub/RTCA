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
    car.CREDIT_ASSESSMENT_REQUEST_ID	||'|'||
    fraud_warnings.FRAUD_CD			||'|'||
    fraud_warnings.FRAUD_TYP_CD		||'|'||
    fraud_warnings.FRAUD_MSG_TXT
from
    credit_assessment_request car 
    INNER JOIN credit_bureau_trn cbt ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = cbt.CREDIT_ASSESSMENT_REQUEST_ID
        AND cbt.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
    LEFT OUTER JOIN credit_bureau_trn_dtl fraud_warnings ON 
        (cbt.CREDIT_BUREAU_TRN_ID = fraud_warnings.CREDIT_BUREAU_TRN_ID
        AND fraud_warnings.CREDIT_BUREAU_TRN_DTL_CD = '3'
        AND fraud_warnings.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
    where car.CREATE_TS BETWEEN TO_DATE('&2','YYYYMMDD') AND TO_DATE('&3','YYYYMMDD') and (fraud_warnings.FRAUD_CD is not null or fraud_warnings.FRAUD_TYP_CD is not null or fraud_warnings.FRAUD_MSG_TXT is not null)
;

spool off;
exit 0
;



