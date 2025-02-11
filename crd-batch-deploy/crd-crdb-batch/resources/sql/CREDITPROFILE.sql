-- set system variables
set term off;
set heading off;
set linesize 53;
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

select /*+ ALL_ROWS */  LPAD(CP.CREDIT_PROFILE_ID,18,0)
       ||LPAD(CPCMAP.CUSTOMER_ID,9,0)
       ||RPAD(CV.CREDIT_VALUE_CD,1)
       ||RPAD(NVL(TO_CHAR(CCI.BIRTH_DT,'YYYY-MM-DD'),' '),10)
       ||RPAD(CCI.EMPLOYMENT_STATUS_CD,2)
       ||RPAD(CCI.RESIDENCY_CD,2)
       ||RPAD(CCI.LEGAL_CARE_CD,2)
       ||RPAD(CCI.CRED_CHECK_CONSENT_CD,2)
       ||RPAD(CCI.PRIM_CRED_CARD_TYP_CD,3)
       ||RPAD(CCI.SEC_CRED_CARD_ISS_CO_TYP_CD,4)
from  CPROFL_CHARSTC_INDVDL CCI,CREDIT_VALUE CV,CPROFL_CUSTOMER_MAP CPCMAP,CREDIT_PROFILE CP
where  CP.CREDIT_PROFILE_ID=CCI.CREDIT_PROFILE_ID
and   CP.CREDIT_PROFILE_ID=CV.CREDIT_PROFILE_ID
and   CP.CREDIT_PROFILE_ID=CPCMAP.CREDIT_PROFILE_ID
and   CCI.EFF_STOP_DTM=TO_TIMESTAMP('31/12/4444','DD/MM/YYYY')
and   CV.EFF_STOP_DTM=TO_TIMESTAMP('31/12/4444','DD/MM/YYYY') 
and   CPCMAP.EFF_STOP_DTM=TO_TIMESTAMP('31/12/4444','DD/MM/YYYY')
and   CPCMAP.CPROFL_CUST_MAP_TYP_CD='PRI'
and   CP.CPROFL_STATUS_CD='A'
and   CP.CPROFL_FORMAT_CD='P'  
order by CP.CREDIT_PROFILE_ID asc;

-- end spooling
spool off;