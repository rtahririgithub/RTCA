/*    Work Queue Report [2010/09/02 - for review] 
    
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
set linesize 280
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Work Queue Report 3 (Daily)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(SYSDATE -1, 'DD/MM/YYYY') from dual;

SELECT 'Agent ID~# of CARs pulled from queue within the past 24 hrs~# of CARs were completed or cancelled~# of CARs were pending~# of CARs were in progress~# of CARs were escalated~# of CARs were re-queued' FROM DUAL;

SELECT latest_activity_by_user_id.user_id       ||'~'||
       SUM (CASE WHEN (car_status_typ_cd IS NOT NULL) THEN 1 ELSE 0 END) ||'~'||
       SUM (CASE WHEN (car_status_typ_cd IN ('COMPLETED', 'CANCELLED')) THEN 1 ELSE 0 END)  ||'~'||
       SUM (CASE WHEN (car_status_typ_cd = 'PENDING' ) THEN 1 ELSE 0 END)  ||'~'||
       SUM (CASE WHEN (car_status_typ_cd = 'IN_PROGRESS' ) THEN 1 ELSE 0 END)  ||'~'||
       SUM (CASE WHEN (car_status_typ_cd = 'ESCALATED' ) THEN 1 ELSE 0 END)  ||'~'||
       SUM (CASE WHEN (car_status_typ_cd = 'QUEUED' AND activity_type.activity_type_cd = 'FOLLOW_UP' ) THEN 1 ELSE 0 END) 
FROM car_activity activity
INNER JOIN car_activity_type activity_type
ON ( activity.CAR_ACTIVITY_TYPE_ID =  activity_type.CAR_ACTIVITY_TYPE_ID )
INNER JOIN
(SELECT
activity.CREDIT_ASSESSMENT_REQUEST_ID, 
(CASE WHEN detail_type.activity_detail_cd =  'ASSIGNEE_USER_ID' AND detail.activity_detail_value_txt IS NOT NULL THEN detail.activity_detail_value_txt ELSE activity.create_user_id  END) user_id,
MAX(activity.car_activity_id) max_car_activity_id
FROM car_activity activity
INNER JOIN car_activity_type activity_type
ON ( activity.CAR_ACTIVITY_TYPE_ID =  activity_type.CAR_ACTIVITY_TYPE_ID )
left outer join (car_activity_dtl_val detail)
ON ( activity.car_activity_id = detail.car_activity_id )
left outer join car_activity_type_dtl detail_type
ON ( detail.CAR_ACTIVITY_TYPE_DTL_ID = detail_type.CAR_ACTIVITY_TYPE_DTL_ID )
WHERE activity_type_cd  NOT IN ( 'RESUBMIT', 'CHECKOUT')
AND CAR_ACTIVITY_CATEGORY_TYPE_CD = 'MANUAL'
AND activity.EFF_START_TS  BETWEEN TRUNC (SYSDATE - 1, 'DD') AND TRUNC (SYSDATE, 'DD')
GROUP BY activity.CREDIT_ASSESSMENT_REQUEST_ID, 
(CASE WHEN detail_type.activity_detail_cd =  'ASSIGNEE_USER_ID' AND detail.activity_detail_value_txt IS NOT NULL THEN detail.activity_detail_value_txt ELSE activity.create_user_id  END)) latest_activity_by_user_id
ON ( activity.car_activity_id = latest_activity_by_user_id.max_car_activity_id 
     AND activity.credit_assessment_request_id = latest_activity_by_user_id.credit_assessment_request_id )
inner join car_status 
ON ( activity.credit_assessment_request_id = car_status.credit_assessment_request_id
     AND activity.car_status_id = car_status.car_status_id )
WHERE latest_activity_by_user_id.user_id != 'Sys-OBPM'
GROUP BY latest_activity_by_user_id.user_id
;
spool off;
exit 0;

