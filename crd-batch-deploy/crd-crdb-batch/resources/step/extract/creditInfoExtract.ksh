#!/usr/bin/ksh 

export USER="APPCR_BATCH/APPCR_BATCH@PDSCRDV"
export ORACLE_HOME=/ora/fs1130/s00/oracle/product/9.2.0.6 
export ORACLE_SID=pdscrdv
export PATH=$ORACLE_HOME/bin:$PATH 

sqlplus -S 2>&1 >/dev/null<<EOF 
$USER 
set term off; 
set heading off; 
set linesize 100; 
set feedback off; 
set pagesize 0; 
set trims on; 
set echo off; 
set ver off; 
spool /work/users/credcol/credit/outbox/customerCreditInfo.dat;
select CPCMAP.CUSTOMER_ID  ||'|'|| TO_CHAR(IND.BIRTH_DT, 'YYYY-MM-DD') ||'|'|| 
       CV.CREDIT_VALUE_CD  ||'|'|| TO_CHAR(CV.EFF_START_DTM,'YYYY-MM-DD') ||'|'|| 
           (CASE WHEN GUA.GUARANTEED_CUSTOMER_ID IS NULL THEN 'N' ELSE 'Y' END) 
from CPROFL_CHARSTC_INDVDL IND, CREDIT_PROFILE CP,CREDIT_VALUE CV,CPROFL_CUSTOMER_MAP CPCMAP 
         LEFT JOIN CUSTOMER_GUARANTOR GUA 
         ON (GUA.GUARANTEED_CUSTOMER_ID=CPCMAP.CUSTOMER_ID and TO_CHAR(GUA.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44') 
where CP.CREDIT_PROFILE_ID=IND.CREDIT_PROFILE_ID 
    and   CP.CPROFL_STATUS_CD='A' 
    and   TO_CHAR(IND.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44' 
    and   CP.CREDIT_PROFILE_ID=CPCMAP.CREDIT_PROFILE_ID 
    and   CPCMAP.CPROFL_CUST_MAP_TYP_CD='PRI' and TO_CHAR(CPCMAP.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44' 
    and   CP.CREDIT_PROFILE_ID=CV.CREDIT_PROFILE_ID 
    and   TO_CHAR(CV.EFF_STOP_DTM,'DD-MM-YY')= '31-12-44';
spool off
EOF