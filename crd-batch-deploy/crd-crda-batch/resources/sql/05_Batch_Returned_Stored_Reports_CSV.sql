/*
    RETURNED SIMPLE REPORTS AND STORED REPORTS [2010/08/19 - for review]

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
set linesize 160 
SET UNDERLINE '='

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Returned Stored Reports' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual;

SELECT '0' SORT_ORDER, '# Simple Search Request~# Simple Report Stored~# Full Report Stored' FROM dual
UNION
SELECT
    '1' SORT_ORDER,
    SUM(
        CASE
            WHEN 
                cglog.TRANSACTION_ERROR_CD = '0'
                AND
                cglog.CREDIT_DOC_CATEGORY_TYP_CD = 'CORP_SMPL_REGISTRY'            
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                cglog.TRANSACTION_ERROR_CD = '0'
                AND
                cglog.CREDIT_DOC_CATEGORY_TYP_CD = 'CORP_SMPL_REGISTRY' 
                AND
                crdDoc.REPORT_DATA_STR IS NOT NULL
            THEN
                1
            ELSE
                0
        END
    )                                               ||'~'||
    SUM(
        CASE
            WHEN 
                cglog.TRANSACTION_ERROR_CD = '0'
                AND
                cglog.CREDIT_DOC_CATEGORY_TYP_CD = 'CORP_FULL_REGISTRY' 
                AND
                cglog.IMAGE_EXTERNAL_RPT_LOC_STR IS NOT NULL
            THEN
                1
            ELSE
                0
        END
    )                                               
FROM
    (
        (
            CREDIT_REPORT_TRNS_LOG cglog LEFT OUTER JOIN CAR_DOCUMENT carDoc ON
            cglog.CREDIT_REQUEST_CONTEXT_ID = carDoc.OBJECT_ID 
            AND cglog.CREDIT_REQUEST_CONTEXT_TYP_CD = 'CARID' 
            AND carDoc.OBJECT_TYPE_CD = 'CAR_ID'
        )
        LEFT OUTER JOIN CREDIT_DOCUMENT crdDoc ON
        carDoc.CREDIT_DOCUMENT_ID = crdDoc.CREDIT_DOCUMENT_ID    
    )   
WHERE
    cglog.CREATE_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
    AND cglog.CREDIT_DOC_CATEGORY_TYP_CD IN ('CORP_SMPL_REGISTRY', 'CORP_FULL_REGISTRY')
;

spool off;
exit 0;
