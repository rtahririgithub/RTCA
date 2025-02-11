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
select /*+ ALL_ROWS */MAP.CREDIT_PROFILE_ID || ',' || MAP.CUSTOMER_ID 
from  CREDIT_PROFILE CP,CPROFL_CUSTOMER_MAP MAP
where  MAP.CREDIT_PROFILE_ID=CP.CREDIT_PROFILE_ID
and  CP.CPROFL_FORMAT_CD='P'
and  MAP.EFF_STOP_DTM =TO_DATE('31-12-4444','DD-MM-YYYY') 
order by MAP.CREDIT_PROFILE_ID;

-- end spooling
spool off;

-- exit SQL*Plus
exit 0;