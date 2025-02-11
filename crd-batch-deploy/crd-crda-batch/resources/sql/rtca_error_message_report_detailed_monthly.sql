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

-- rtca_error_message_report_detailed_monthly.sql
SELECT 'CustomerId,ErrorCode,ErrorMessage,Datetime' FROM dual;
SELECT EXTERNAL_CUSTOMER_ID ||','||
            TRANSACTION_ERROR_CD ||','||
            --TRANSACTION_ERROR_MSG_TXT  ||','||
            CASE
                 WHEN INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) > INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 2) >0
                    THEN SUBSTR(TRANSACTION_ERROR_MSG_TXT, INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)+1, INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 2)-INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)-1)
                 WHEN INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) > INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 2) =0
                    THEN SUBSTR(TRANSACTION_ERROR_MSG_TXT, INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)+1)
                 WHEN INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) < INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1)
                    THEN SUBSTR(TRANSACTION_ERROR_MSG_TXT, INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)+1, INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1)-INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)-1)
                 WHEN INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) >0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) =0
                    THEN SUBSTR(TRANSACTION_ERROR_MSG_TXT, 1, INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1)-1)
                 WHEN INSTR(TRANSACTION_ERROR_MSG_TXT, ',', 1, 1) =0 AND
                           INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1) >0
                    THEN SUBSTR(TRANSACTION_ERROR_MSG_TXT, INSTR(TRANSACTION_ERROR_MSG_TXT, '}', 1, 1)+1)
                 ELSE TRANSACTION_ERROR_MSG_TXT
            END ||','||
            --CREATE_TS
            TO_CHAR (CREATE_TS, 'YYYY/MM/DD HH24:MI:SS')
    FROM CREDIT_REPORT_TRNS_LOG
    WHERE CREDIT_REPORT_SOURCE_SYS_CD = 'EQUIFAX'
         AND TRANSACTION_ERROR_CD <> '0'
         AND EXTERNAL_VENDOR_NM = 'EQFXIC'
         AND EXTRACT(MONTH FROM CREATE_TS) = EXTRACT(MONTH FROM SYSDATE)
        -- GROUP BY EXTERNAL_CUSTOMER_ID, TRANSACTION_ERROR_CD, TRANSACTION_ERROR_MSG_TXT
         ORDER BY EXTERNAL_CUSTOMER_ID, CREATE_TS
;
spool off;
exit;
