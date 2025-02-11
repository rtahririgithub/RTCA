-- set system variables
set term off;
set heading off;
set linesize 342;
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

select /*+ ALL_ROWS */  LPAD(CA.CPROFL_ADDRESS_ID,18,0)
      ||LPAD(CA.CREDIT_PROFILE_ID,18,0)
      ||RPAD(CA.CPROFL_ADDRESS_TYP_CD,2)
      ||RPAD(NVL(CA.RENDERED_ADDR_LN_1_TXT,' '),50)
      ||RPAD(NVL(CA.RENDERED_ADDR_LN_2_TXT,' '),50)
      ||RPAD(NVL(CA.RENDERED_ADDR_LN_3_TXT,' '),50)
      ||RPAD(NVL(CA.RENDERED_ADDR_LN_4_TXT,' '),50)
      ||RPAD(NVL(CA.RENDERED_ADDR_LN_5_TXT,' '),50)
      ||RPAD(NVL(CA.MUNIC_NM,' '),40)
      ||RPAD(NVL(CA.POSTAL_ZIP_CD_TXT,' '),9)
      ||RPAD(NVL(CA.PROVINCE_CD,' '),2)
      ||RPAD(NVL(CA.COUNTRY_CD,' '),3)
from  CPROFL_ADDRESS CA,CREDIT_PROFILE CP
where CP.CREDIT_PROFILE_ID=CA.CREDIT_PROFILE_ID
and   CA.EFF_STOP_DTM=TO_TIMESTAMP('31/12/4444','DD/MM/YYYY') 
and   CP.CPROFL_STATUS_CD='A'
and   CP.CPROFL_FORMAT_CD='P' 
order by CA.CREDIT_PROFILE_ID asc;

-- end spooling
spool off;