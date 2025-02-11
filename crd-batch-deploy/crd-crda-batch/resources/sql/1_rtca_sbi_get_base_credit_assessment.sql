set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set trimspool on
set linesize 32767;
set ARRAYSIZE 5000;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1
SELECT   
	car_cust.customer_id ||'|'||
	car.CREDIT_ASSESSMENT_REQUEST_ID ||'|'||
    car.CAR_TYP_CD ||'|'||
	car.car_subtype_cd ||'|'||
	car.customer_typ_cd ||'|'||
	car.lob_cd ||'|'||
	car.CHANNEL_ORG_CD ||'|'||
	TO_CHAR(car.CREATE_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
	car.create_user_id ||'|'||
	TO_CHAR(car.LAST_UPDT_TS, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
	car.LAST_UPDT_USER_ID ||'|'||
	car.DATA_SOURCE_ID ||'|'||
	TO_CHAR(car_status.eff_start_ts, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
	car_status.CAR_STATUS_TYP_CD ||'|'||
	cmnt.COMMENT_TXT ||'|'||
    	car_activity.CAR_ACTIVITY_REASON_CD
FROM 
    credit_assessment_request car
    INNER JOIN car_status car_status
    ON ( car_status.CREDIT_ASSESSMENT_REQUEST_ID = car.CREDIT_ASSESSMENT_REQUEST_ID 
        AND car_status.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    INNER JOIN CUST_CREDIT_ASSMNT_RQST car_cust
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID 
        and car_cust.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT OUTER JOIN CAR_ACTIVITY car_activity
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_activity.CREDIT_ASSESSMENT_REQUEST_ID
        AND car_activity.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
    LEFT OUTER JOIN CAR_ACTIVITY_TYPE car_activity_type
    ON ( car_activity.CAR_ACTIVITY_TYPE_ID = car_activity_type.CAR_ACTIVITY_TYPE_ID
        AND car_activity_type.EFF_STOP_TS = TO_DATE('99991231','YYYYMMDD')
	    AND car_activity_type.ACTIVITY_TYPE_CD = 'VOID' )
    LEFT OUTER JOIN credit_mgmnt_comment cmnt 
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = cmnt.OBJECT_ID
        AND cmnt.OBJECT_TYPE_CD = 'CRD_ASMNT_REQUEST' 
	    AND cmnt.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
WHERE 
    car.LAST_UPDT_TS between 
    CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
    AND CASE WHEN '&4'='1' THEN TO_DATE('&7','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
    AND car.CAR_TYP_CD LIKE CASE WHEN '&4'='1' THEN '&5' ELSE '%%' END
ORDER BY 
    car.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
