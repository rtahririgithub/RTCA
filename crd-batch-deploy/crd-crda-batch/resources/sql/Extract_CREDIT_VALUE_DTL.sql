set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set linesize 64 ;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1
select      map.customer_id ||'|'||            
            cp.credit_profile_id ||'|'||
	    cv_dtl.FRAUD_MSG_CD  ||'|'||
	    TO_CHAR(SYSDATE, 'YYYYMMDD')
        from STG_CREDIT_UPDOWN_CUST mud_cust
        INNER JOIN CPROFL_CUSTOMER_MAP map
        ON ( mud_cust.customer_id = map.customer_id
                and map.CPROFL_CUST_MAP_TYP_CD='PRI'
                   and map.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
            inner join CREDIT_PROFILE cp
            ON ( map.CREDIT_PROFILE_ID = cp.CREDIT_PROFILE_ID
                   and cp.CPROFL_STATUS_CD='A'
                   and cp.CPROFL_FORMAT_CD='P' )
           inner join credit_value cv
           on ( cp.credit_profile_id = cv.credit_profile_id  
                  and  cv.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') ) 
	   inner join credit_value_dtl cv_dtl
	   on ( cv.credit_value_id = cv_dtl.credit_value_id
	        and cv_dtl.CREDIT_VALUE_DTL_TYP_CD = '2'
	        and cv_dtl.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
           order by map.customer_id;

spool off;
exit 0;
