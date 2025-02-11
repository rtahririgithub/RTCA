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

--col spoolname new_value spoolname;
--SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual;
spool '&1';

-- rtca_error_message_report_summary_monthly.sql
--SELECT 'ErrorCode,ErrorMessage,#Error' FROM dual;
SELECT 'ErrorCode,#Error' FROM dual;
SELECT TRANSACTION_ERROR_CD ||','||
             --TRANSACTION_ERROR_MSG_TXT ||','||
             COUNT(TRANSACTION_ERROR_CD)
    FROM CREDIT_REPORT_TRNS_LOG
    WHERE CREDIT_REPORT_SOURCE_SYS_CD = 'EQUIFAX'
         AND TRANSACTION_ERROR_CD <> '0'
         AND EXTERNAL_VENDOR_NM = 'EQFXIC'
         AND EXTRACT(MONTH FROM CREATE_TS) = EXTRACT(MONTH FROM SYSDATE)
         GROUP BY TRANSACTION_ERROR_CD --, TRANSACTION_ERROR_MSG_TXT
         ORDER BY TRANSACTION_ERROR_CD
;
spool off;
exit;
