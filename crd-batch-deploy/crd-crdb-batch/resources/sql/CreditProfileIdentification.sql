-- set system variables
set term off;
set heading off;
set linesize 109;
set feedback off;
set pagesize 0;
set trims off;
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
select /*+ ALL_ROWS */ LPAD(PROFLSUBSET.CID,9,0)||RPAD(SIN,50)||RPAD(DL,50)
from  
   (select CPCMAP.CUSTOMER_ID CID,CP.CREDIT_PROFILE_ID PID
	from CREDIT_PROFILE CP,CPROFL_CUSTOMER_MAP CPCMAP
	where CP.CREDIT_PROFILE_ID=CPCMAP.CREDIT_PROFILE_ID 
	  and CP.CPROFL_STATUS_CD='A'
	  and CPCMAP.CPROFL_CUST_MAP_TYP_CD='PRI' and TO_CHAR(CPCMAP.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44'
    ) PROFLSUBSET
LEFT JOIN 	
   (select CPID.CREDIT_PROFILE_ID PID,
	     MAX(CASE WHEN CPID.IDENTIFICATION_TYP_CD='SIN' THEN CPID.IDENTIFICATION_NUM ELSE NULL END) AS SIN,
		 MAX(CASE WHEN CPID.IDENTIFICATION_TYP_CD='DL' THEN CPID.IDENTIFICATION_NUM ELSE NULL END) AS DL
	from CPROFL_IDENTIFICATION CPID
	where TO_CHAR(CPID.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44' 
	group by CPID.CREDIT_PROFILE_ID
    ) IDSUBSET
on PROFLSUBSET.PID=IDSUBSET.PID
order by PROFLSUBSET.CID ASC;

-- end spooling
spool off;
