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
    deposit.ORDER_DEPOSIT_CALC_TRN_ID ||'|'||   
    deposit.CUSTOMER_ID ||'|'||
    deposit.ORDER_ID ||'|'||
    deposit.DECISION_CD ||'|'||
    deposit.TOTAL_AR_DEPOSIT_PAID_AMT ||'|'||
    TO_CHAR(deposit.LAST_AR_DEPOSIT_PAID_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
    deposit.TOTAL_DEPOSIT_RELEASED_AMT_TOT ||'|'||
    TO_CHAR(deposit.LAST_AR_DEPOSIT_RELEASED_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
    deposit.TOTAL_AR_DEPOSIT_PENDING_AMT ||'|'||
    TO_CHAR(deposit.LAST_AR_DEPOSIT_PENDING_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
    deposit.DEPOSIT_ADJUSTMENT_AMT ||'|'||
    deposit.DEPOSIT_ON_HAND_AMT ||'|'||
    deposit.TOTAL_ASSESSED_DEPOSIT_AMT ||'|'||
    deposit.CALCULATION_RESULT_MSG_CD ||'|'||
    deposit.CALCULATION_RESULT_REASON_CD
FROM 
    ORDER_DEPOSIT_CALC_TRN deposit 
WHERE 
    deposit.CREATE_TS between TO_DATE('&2','YYYYMMDDHH24MISS') and TO_DATE('&3','YYYYMMDDHH24MISS')
ORDER BY 
    deposit.ORDER_DEPOSIT_CALC_TRN_ID
;

spool off;
exit 0;
