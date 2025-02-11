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
    car.CREDIT_ASSESSMENT_REQUEST_ID ||'|'||
    dcsn.UC_ASSESSMENT_IND ||'|'||
    dcsn.UC_ASSESSMENT_IND_RSN_CD 
FROM 
    credit_assessment_request car
    INNER JOIN INT_CRDT_DCSN_TRN  dcsn
    ON ( dcsn.CREDIT_ASSESSMENT_REQUEST_ID = car.CREDIT_ASSESSMENT_REQUEST_ID 
        AND dcsn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
WHERE
    car.LAST_UPDT_TS between 
    CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
    AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
ORDER BY 
    car.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
