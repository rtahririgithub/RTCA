set term off; 
set heading off; 
set linesize 1500; 
set feedback off; 
set pagesize 0; 
set trims on; 
set trimspool on;
set echo off; 
set ver off; 

whenever sqlerror exit SQL.SQLCODE 
whenever oserror exit failure 

spool &1
SELECT TO_CHAR(ap.ITEM_ATRS_LIST)
FROM TBAP_ITEM ap
WHERE ap.STATUS in ('AC', 'SU') 
AND ap.STATE = 'AS'
AND ap.START_DATE < sysdate 
AND ap.end_date > sysdate
AND ap.MAIN_IND='1'
AND ap.item_def_id='24';
spool off;

exit 0;
