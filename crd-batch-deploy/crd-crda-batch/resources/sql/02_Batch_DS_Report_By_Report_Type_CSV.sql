/*
    DS REPORT BY REPORT TYPE [2010/08/12 - for review]
    
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
set linesize 500
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        DS Report By Report Type' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual;

SELECT 'Report Type~# Total Requests~# Successful Calls~# Failed (DS down)~Response Time(sec)' FROM dual;
SELECT TABLE_1.GROUP_ORDER ||'~'|| TABLE_1.COL_1 ||'~'|| TABLE_2.COL_1 FROM 
(SELECT
    crlog.CREDIT_DOC_CATEGORY_TYP_CD                GROUP_ORDER,
    COUNT(  crlog.CREDIT_REPORT_TRNS_LOG_ID )       ||'~'||
    SUM(  CASE 
            WHEN 
                crlog.TRANSACTION_ERROR_CD = '0'
            THEN
                1
            ELSE
                0
            END
       )                                          ||'~'||
    SUM(  CASE 
                WHEN 
                    crlog.TRANSACTION_ERROR_CD = '300009'
            THEN
                1
            ELSE
                0
            END
       )             COL_1                             
FROM
    CREDIT_REPORT_TRNS_LOG crlog
WHERE
    crlog.CREATE_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
GROUP BY
    crlog.CREDIT_DOC_CATEGORY_TYP_CD
) TABLE_1 FULL JOIN 
(
SELECT
    crlog.CREDIT_DOC_CATEGORY_TYP_CD                GROUP_ORDER,
     (AVG(    
              (EXTRACT(DAY FROM crlog.TRANSACTION_RSPNS_TS)     -   EXTRACT(DAY FROM crlog.TRANSACTION_RQST_TS))*86400000
              +
              (EXTRACT(HOUR FROM crlog.TRANSACTION_RSPNS_TS)     -   EXTRACT(HOUR FROM crlog.TRANSACTION_RQST_TS))*3600000
              +
              (EXTRACT(MINUTE FROM crlog.TRANSACTION_RSPNS_TS)     -   EXTRACT(MINUTE FROM crlog.TRANSACTION_RQST_TS))*60000
              +
              (EXTRACT(SECOND FROM crlog.TRANSACTION_RSPNS_TS)     -   EXTRACT(SECOND FROM crlog.TRANSACTION_RQST_TS))*1000
        )) / 1000         COL_1                                         
FROM
    CREDIT_REPORT_TRNS_LOG crlog
WHERE
    crlog.TRANSACTION_ERROR_CD = '0'
    AND crlog.CREATE_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
GROUP BY
    crlog.CREDIT_DOC_CATEGORY_TYP_CD
) TABLE_2
ON TABLE_1.GROUP_ORDER = TABLE_2.GROUP_ORDER
;

spool off;
exit 0;
