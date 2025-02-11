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
   cbtran.CREDIT_ASSESSMENT_REQUEST_ID ||'|'|| 
   CREDIT_BUREAU_TRN_DTL_CD ||'|'||
   RISK_IND_TYP_CD ||'|'|| 
   RISK_IND_VALUE_TXT ||'|'|| 
   FRAUD_CD ||'|'||
   FRAUD_MSG_TXT ||'|'||
   SCORE_NM ||'|'||
   SCORE_VALUE_TXT ||'|'||
   EXT_DOC_FORMAT_TYP ||'|'||
   FRAUD_TYP_CD
   FROM 
    CREDIT_BUREAU_TRN cbtran
       INNER JOIN CREDIT_BUREAU_TRN_DTL cbdtl 
       ON ( cbtran.CREDIT_BUREAU_TRN_ID = cbdtl.CREDIT_BUREAU_TRN_ID
            AND cbdtl.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
   WHERE 
        cbtran.CREATE_TS between 
        CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
        AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
        AND cbtran.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') 
   ORDER BY 
        cbtran.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
