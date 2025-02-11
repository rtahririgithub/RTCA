set term off; 
set heading off; 
set linesize 1500;
set feedback off; 
set pagesize 0; 
set trims on; 
set echo off; 
set ver off; 

whenever sqlerror exit SQL.SQLCODE 
whenever oserror exit failure 

spool &1
SELECT /*+ FULL(ap) PARALLEL(ap, 5) */ TO_CHAR(ap.ITEM_ATRS_LIST)
FROM TBAP_ITEM ap
WHERE ap.STATUS IN ('AC', 'SU')
AND ap.STATE = 'AS'
AND ap.START_DATE < sysdate
AND ap.END_DATE > sysdate
AND ap.item_def_id='649';
spool off;

exit 0;
