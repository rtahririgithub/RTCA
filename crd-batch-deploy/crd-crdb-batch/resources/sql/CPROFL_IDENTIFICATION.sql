-- set system variables
set term off;
set heading off;
set linesize 94;
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

select /*+ ALL_ROWS */ LPAD(CID.CPROFL_IDENTIFICATION_ID,18,0)
      ||LPAD(CID.CREDIT_PROFILE_ID,18,0)
      ||RPAD(CID.IDENTIFICATION_TYP_CD,3)
      ||RPAD(CID.IDENTIFICATION_NUM,50)
      ||RPAD(NVL(CID.PROVINCE_CD,' '),2)
      ||RPAD(NVL(CID.COUNTRY_CD,' '),3)
from  CPROFL_IDENTIFICATION CID,CREDIT_PROFILE CP
where  CP.CREDIT_PROFILE_ID=CID.CREDIT_PROFILE_ID
and   CID.EFF_STOP_DTM=TO_TIMESTAMP('31/12/4444','DD/MM/YYYY') 
and   CP.CPROFL_STATUS_CD='A'
and   CP.CPROFL_FORMAT_CD='P' 
order by CID.CREDIT_PROFILE_ID asc;

-- end spooling
spool off;