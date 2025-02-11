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
    stg.CREDIT_ASSESSMENT_REQUEST_ID ||'|'||
    wls_match.UC_WLS_MATCH_ACCOUNT_ID ||'|'||
    search.LINE_OF_BUSINESS ||'|'||
    search.BRAND_ID ||'|'||
    wls_match.ACCOUNT_ID ||'|'||
    wls_match.ACCOUNT_TYPE ||'|'||
    wls_match.ACCOUNT_SUB_TYP ||'|'||
    wls_match.ACCOUNT_STATUS ||'|'||
    wls_match.STATUS_ACTV_CD ||'|'||
    wls_match.CREDIT_CLASS_CD ||'|'||
    wls_match.RISK_LEVEL_NUM ||'|'||
    wls_match.RISK_LEVEL_DECISION_CD ||'|'||
    search.BUREAU_DECISION_CD ||'|'||
    wls_match.TOTAL_ACTIVE_SUB ||'|'||
    wls_match.TOTAL_RESERVED_SUB ||'|'||
    wls_match.TOTAL_SUSPEND_SUB ||'|'||
    wls_match.DELINQUENT_IND
FROM 
    STG_CREDIT_DCSN_TRN stg
    INNER JOIN UC_SEARCH_RESULT  search
    ON ( search.STG_CREDIT_DSCN_TRN_ID = stg.STG_CREDIT_DSCN_TRN_ID 
        AND stg.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT OUTER JOIN UC_WLS_MATCH_ACCOUNT  wls_match
    ON ( wls_match.UC_SEARCH_RESULT_ID = search.UC_SEARCH_RESULT_ID) 
WHERE
    stg.LAST_UPDT_TS between 
    CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
    AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
ORDER BY 
    stg.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
