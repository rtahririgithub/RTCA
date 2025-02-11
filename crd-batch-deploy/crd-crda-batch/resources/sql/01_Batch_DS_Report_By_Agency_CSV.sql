/*
    DS REPORT BY AGENCY [2010/08/12 - for review]
    
    Please Note: >> To remove extra lines and spaces
                Step #1.  unix> sed 's/[ \t]*$//' your_file_name > temp_file_name
                Step #2.  unix> sed '/^$/d' temp_file_name > your_file_name
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 160
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        DS Report By Agency' from dual
UNION
SELECT '02' SORT_ORDER, 'Date                   ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual;

SELECT 'Agency~# Total Requests~# Successful Calls~# Failed (DS down)' FROM dual;
SELECT
    crlog.CREDIT_REPORT_SOURCE_SYS_CD               ||'~'||
    COUNT(  crlog.CREDIT_REPORT_TRNS_LOG_ID )       ||'~'||
    SUM(  CASE 
                WHEN 
                    crlog.TRANSACTION_ERROR_CD = '0'
            THEN
                1
            ELSE
                0
            END
       )                                            ||'~'||
    SUM(  CASE 
                WHEN 
                    crlog.TRANSACTION_ERROR_CD = '300009'
            THEN
                1
            ELSE
                0
            END
       )                                          
FROM
    CREDIT_REPORT_TRNS_LOG crlog
WHERE
    crlog.CREATE_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
    AND crlog.CREDIT_REPORT_SOURCE_SYS_CD != 'MISCELLANEOUS'
GROUP BY
    crlog.CREDIT_REPORT_SOURCE_SYS_CD;

spool off;
exit 0
;

