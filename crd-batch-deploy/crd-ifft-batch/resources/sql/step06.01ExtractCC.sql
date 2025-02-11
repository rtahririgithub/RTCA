set term off; 
set heading off; 
set linesize 100; 
set feedback off; 
set pagesize 0; 
set trims on; 
set echo off; 
set ver off; 

whenever sqlerror exit SQL.SQLCODE 
whenever oserror exit failure 

spool &1
-- COLUMN C0_F FORMAT A10
-- SELECT ap.SERVICE_ID A10
SELECT /*+ FULL(ap) PARALLEL(ap, 5) */ TO_CHAR(ap.ITEM_ATRS_LIST)
FROM TBAP_ITEM ap
WHERE	 ap.STATUS in ('AC', 'SU') 
	AND ap.STATE = 'AS'
	AND ap.START_DATE < sysdate 
	AND ap.end_date > sysdate
        AND ap.MAIN_IND='1'
        AND ap.item_def_id='28';
spool off;

exit 0;
