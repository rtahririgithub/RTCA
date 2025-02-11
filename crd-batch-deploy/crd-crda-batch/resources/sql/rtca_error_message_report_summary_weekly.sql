set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 250
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;
SELECT '&1' || TO_CHAR(SYSDATE-1, '_YYYYMMDD') || '.csv' spoolname from dual;
spool '&spoolname';

-- rtca_error_message_report_summary_weekly.sql
--SELECT 'ErrorCode,ErrorMessage,#Error' FROM dual;
SELECT 'ErrorCode,#Error' FROM dual;
SELECT TRANSACTION_ERROR_CD ||','||
             --TRANSACTION_ERROR_MSG_TXT ||','||
             COUNT(TRANSACTION_ERROR_CD)
    FROM CREDIT_REPORT_TRNS_LOG
    WHERE CREDIT_REPORT_SOURCE_SYS_CD = 'EQUIFAX'
         AND TRANSACTION_ERROR_CD <> '0'
         AND EXTERNAL_VENDOR_NM = 'EQFXIC'
         --AND TO_CHAR(CREATE_TS , 'WW') = TO_CHAR(SYSDATE, 'WW') 
         -- Run batch job at early of Monday morning, e.g. 12:00:01 AM
         -- CREATE_TS is of timestamp, e.g. 2013/01/07 12:00:01 AM > 2013/01/07 and 2013/01/13 11:59:59 PM< 2013/01/14
         AND CREATE_TS BETWEEN TRUNC(SYSDATE-7,'DD') and TRUNC(SYSDATE, 'DD')   
         GROUP BY TRANSACTION_ERROR_CD --, TRANSACTION_ERROR_MSG_TXT
         ORDER BY TRANSACTION_ERROR_CD
;
spool off;
exit;
