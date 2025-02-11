set term off; 
set heading off; 
set linesize 42; 
set feedback off; 
set pagesize 0; 
set trimspool off; 
set trimout off;
set echo off; 
set ver off; 
set colsep "";

whenever sqlerror exit SQL.SQLCODE 
whenever oserror exit failure 

spool &1
COLUMN C0_F FORMAT A10
COLUMN C1_F FORMAT A32
SELECT vv.discrete_code C0_F,
       vv.decode_id C1_F
FROM tbvalid_value vv WHERE vv.domain_name='Dyn_LocalProvider_1';

spool off;

exit 0;
