/*
    QUALIFIED CUSTOMERS EXTRACT [2011/07/26 - for review]
    
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
    	DISTINCT LPAD(wls.CUSTOMER_ID, 9, '0') || LPAD(wls.BILLING_ACCOUNT_NUM, 10, '0') || RPAD(wls.BILLING_MASTER_SRC_ID, 4, ' ')
FROM
	BILLING_ACCOUNT wls inner join BILLING_ACCOUNT wln on 
	wln.customer_id = wls.customer_id  and 
	wln.BILLING_MASTER_SRC_ID = '1001' and
	wls.BILLING_MASTER_SRC_ID  ='130'
;

spool off;
exit 0;

