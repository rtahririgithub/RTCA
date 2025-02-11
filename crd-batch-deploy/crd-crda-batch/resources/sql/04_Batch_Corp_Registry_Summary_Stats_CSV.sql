/*
    CORPORATE REGISTRY SUMMARY STATS [2010/08/16 - for review]

    Please Note: >> To remove extra lines and spaces
                Step #1.  unix> sed 's/[ \t]*$//' your_file_name > temp_file_name
                Step #2.  unix> sed '/^$/d' temp_file_name > your_file_name
*/

set echo off
set verify off
set termout on
set heading off
set pages 50000
set feedback off
set newpage none
set linesize 250 

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Corporate Registry Summary Stats' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual;

SELECT '0' SORT_ORDER, '~~~~Unsuccessful~~' FROM dual
UNION
SELECT '1' SORT_ORDER, 'Report Type~Business Entity Type~# Total Requests~# Successful~No-Hit~Error From inside TELUS~Error From outside TELUS~#Failed (Corp Reg. down)' FROM dual
UNION
SELECT
    '2' SORT_ORDER,
    crlog.CREDIT_DOC_CATEGORY_TYP_CD                ||'~'||
    carAttrval.CAR_ATTR_VALUE_TXT                      ||'~'||
    COUNT(  crlog.CREDIT_REPORT_TRNS_LOG_ID )       ||'~'||
    SUM(
        CASE
            WHEN 
                crlog.TRANSACTION_ERROR_CD = '0'
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                crlog.CREDIT_HIT_CD IN ('N', '2')
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                SUBSTR( 
                        crlog.TRANSACTION_ERROR_CD, 1, 1
                      ) IN ('1', '4')
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                SUBSTR( 
                        crlog.TRANSACTION_ERROR_CD, 1, 1
                      ) = '2'
                OR crlog.TRANSACTION_ERROR_CD = 'UNKNOWN'
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                crlog.TRANSACTION_ERROR_CD in ('300001','300002')
            THEN
                1
            ELSE
                0
        END
    )                                               
FROM
    CREDIT_REPORT_TRNS_LOG crlog INNER JOIN CAR_ATTR_VALUE carAttrVal ON
    crlog.CREDIT_REQUEST_CONTEXT_ID = carAttrVal.CREDIT_ASSESSMENT_REQUEST_ID AND
    crlog.CREDIT_REQUEST_CONTEXT_TYP_CD = 'CARID'
WHERE
    crlog.CREATE_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
    AND crlog.CREDIT_DOC_CATEGORY_TYP_CD IN ('CORP_SMPL_REGISTRY', 'CORP_FULL_REGISTRY')
    AND carAttrVal.CAR_ATTR_TYP_CD = 'MASTER_LEG_TYPE'
    AND carAttrVal.CAR_ATTR_VALUE_TXT IN ('INC', 'LTD', 'LLC', 'ULC', 'PR', 'SOC', 'ASSN', 'GOV', 'OTH', 'PT', 'LP', 'LLP')
    AND crlog.CREDIT_REPORT_SOURCE_SYS_CD = 'MISCELLANEOUS'
GROUP BY
    crlog.CREDIT_DOC_CATEGORY_TYP_CD,
    carAttrVal.CAR_ATTR_VALUE_TXT
;

spool off;
exit 0;
