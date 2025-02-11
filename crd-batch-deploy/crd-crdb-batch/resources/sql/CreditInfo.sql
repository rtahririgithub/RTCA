-- set system variables
set term off;
set heading off;
set linesize 100;
set feedback off;
set pagesize 0;
set trims on;
set echo off;
set ver off;
set arraysize 5000;
spool off;

-- exceptions
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

-- start spooling
spool &1

-- detail statement
select /*+ ALL_ROWS */ CPCMAP.CUSTOMER_ID  ||'|'|| TO_CHAR(IND.BIRTH_DT, 'YYYY-MM-DD') ||'|'|| 
       CV.CREDIT_VALUE_CD  ||'|'|| TO_CHAR(CV.EFF_START_DTM,'YYYY-MM-DD') ||'|'|| 
           (CASE WHEN GUA.GUARANTEED_CUSTOMER_ID IS NULL THEN 'N' ELSE 'Y' END) 
from CPROFL_CHARSTC_INDVDL IND, CREDIT_VALUE CV,CREDIT_PROFILE CP,CPROFL_CUSTOMER_MAP CPCMAP
         LEFT JOIN CUSTOMER_GUARANTOR GUA 
         ON (GUA.GUARANTEED_CUSTOMER_ID=CPCMAP.CUSTOMER_ID and GUA.EFF_STOP_DTM =TO_DATE('31-12-4444','DD-MM-YYYY')) 
where   CP.CREDIT_PROFILE_ID=IND.CREDIT_PROFILE_ID 
    and   CP.CREDIT_PROFILE_ID=CV.CREDIT_PROFILE_ID 
    and   CP.CREDIT_PROFILE_ID=CPCMAP.CREDIT_PROFILE_ID 
    and   IND.EFF_STOP_DTM = TO_DATE('31-12-4444','DD-MM-YYYY')
    and   CV.EFF_STOP_DTM = TO_DATE('31-12-4444','DD-MM-YYYY')
    and   CP.CPROFL_STATUS_CD='A'
    and   CP.CPROFL_FORMAT_CD='P'
    and   CPCMAP.EFF_STOP_DTM =TO_DATE('31-12-4444','DD-MM-YYYY')
    and   CPCMAP.CPROFL_CUST_MAP_TYP_CD='PRI';
   
-- end spooling
spool off;

-- exit SQL*Plus
exit 0;