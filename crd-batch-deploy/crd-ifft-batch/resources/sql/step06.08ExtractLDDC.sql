set term off; 
set heading off; 
set linesize 42; 
set feedback off; 
set pagesize 0; 
set trims off; 
set trimout off;
set echo off; 
set ver off; 
set colsep "";

whenever sqlerror exit SQL.SQLCODE 
whenever oserror exit failure 

spool &1
COLUMN C0_F FORMAT A32
SELECT d.decode_id C0_F, 
       lpad(substr(d.caption, 1, 10), 10)
FROM tbdecode d 
WHERE d.decode_id LIKE 'Dyn_LocalProvider_1_%';

spool off;

exit 0;
