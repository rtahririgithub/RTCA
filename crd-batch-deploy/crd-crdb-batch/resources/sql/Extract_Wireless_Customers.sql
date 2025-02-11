/*
    EXTRACT WIRELESS CUSTOMERS [2011/08/09 - for review]
    
    Please Note: >> To remove extra lines and spaces
                Step #1.  unix> sed 's/[ \t]*$//' your_file_name > temp_file_name
                Step #2.  unix> sed '/^$/d' temp_file_name > your_file_name
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 160
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1

SELECT
    LPAD(C.CUSTOMER_ID, 10, '0')    ||'|'||  
	C.DRIVR_LICNS_NO			    ||'|'||
    TO_CHAR(C.BIRTH_DATE, 'YYYY-MM-DD') ||'|'||
	C.CUSTOMER_SSN			        ||'|'||
	C.DRIVR_LICNS_STATE			    
FROM 
    CUSTOMER C
;
spool off;
exit 0;

