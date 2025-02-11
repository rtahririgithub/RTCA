#!/usr/bin/ksh

export USER="APPCR_BATCH/APPCR_BATCH@PDSCRDV"
export ORACLE_HOME=/ora/fs1130/s00/oracle/product/9.2.0.6
export ORACLE_SID=pdscrdv
export PATH=$ORACLE_HOME/bin:$PATH
 
${ORACLE_HOME}/bin/sqlplus -S 2>&1 >/dev/null<<EOF
${USER}
set term off;
set heading off;
set linesize 100;
set feedback off;
set pagesize 0;
set trims on;
set echo off;
set ver off;
spool /work/users/credcol/creditbatch/resources/data/merge/out/creditProfileCustomerMappingData.dat;
select CREDIT_PROFILE_ID || ',' || CUSTOMER_ID from CPROFL_CUSTOMER_MAP 
where TO_CHAR(EFF_STOP_DTM,'DD-MM-YY')='31-12-44' order by CREDIT_PROFILE_ID;
spool off
EOF