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
COLUMN C0_F FORMAT A9
COLUMN C1_F FORMAT A50
COLUMN C2_F FORMAT A3
select lpad(to_char(cm.customer_id),9,'0') C0_F,
		 ci.identification_num C1_F,
		 ci.identification_typ_cd C2_F
from cprofl_customer_map cm, 
	  credit_profile cp, 
	  cprofl_identification ci
where cm.credit_profile_id = cp.credit_profile_id and
      cp.credit_profile_id = ci.credit_profile_id and
      cm.cprofl_cust_map_typ_cd = 'PRI' and
      cp.cprofl_status_cd = 'A' and
      ci.identification_typ_cd = 'DL' and
      to_char(cm.eff_stop_dtm,'yy-mm-dd') = '44-12-31' and
      to_char(ci.eff_stop_dtm,'yy-mm-dd') = '44-12-31'
order by cm.customer_id;
spool off;

exit 0;
