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

SELECT '01' SORT_ORDER, 'Report Name        Work Queue Report 2 (Daily)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(SYSDATE -1, 'DD/MM/YYYY') from dual;

SELECT '~# started 24 hrs ago in this queue (no count CAR''s with future FUP)~# added within past 24 hrs~# has not been worked~# status changed during the past 24 hrs~# were completed/cancelled or pending~# are still in progress~# are escalated~# are re-queued' FROM DUAL;

SELECT
       QUEUE_NAME                       ||'~'||
       SUM(STARTED_24_HRS_AGO)          ||'~'|| 
       SUM(WITH_IN_24_HRS)              ||'~'||
       SUM(NOT_WORKED_ON)               ||'~'||
       SUM(STATUS_CHANGED)              ||'~'||
       SUM(COMPLETED_CANCELLED)         ||'~'||
       SUM(CARS_IN_PROGRESS)            ||'~'||
       SUM(CARS_ESCALATED)              ||'~'||
       SUM(CARS_REQUEUED)
from (
select cars_in_pool.CAR_STATUS_TYP_CD, carAttrVal.CAR_ATTR_VALUE_TXT,
       CASE 
        WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED' AND carAttrVal.CAR_ATTR_VALUE_TXT = 'SML' ) THEN 'Queued (small)'
        WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED' AND carAttrVal.CAR_ATTR_VALUE_TXT = 'MED' ) THEN 'Queued (medium)'
        WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED' AND carAttrVal.CAR_ATTR_VALUE_TXT = 'LRG' ) THEN 'Queued (large)'
        WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED' AND carAttrVal.CAR_ATTR_VALUE_TXT = 'LNV' ) THEN 'Legal name validation queue'
        WHEN ( cars_in_pool.CAR_STATUS_TYP_CD = 'ESCALATED'  ) THEN 'Escalation queue' 
        ELSE 'Others' END QUEUE_NAME,
       SUM ( CASE WHEN ( cars_in_pool.car_start_type = 'STARTED_24_HRS_AGO' ) THEN 1 ELSE 0 END ) STARTED_24_HRS_AGO,
       SUM ( CASE WHEN ( cars_in_pool.car_start_type = 'STARTED_WITH_IN_24_HRS' ) THEN 1 ELSE 0 END ) WITH_IN_24_HRS,
       SUM ( CASE WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED'
                 AND (( cars_in_pool.CAR_START_TYPE = 'STARTED_WITH_IN_24_HRS' AND 
                 carStatus.CAR_STATUS_TYP_CD = 'QUEUED' ) OR
             ( cars_in_pool.CAR_START_TYPE = 'STARTED_24_HRS_AGO' AND
               carStatus.CAR_STATUS_TYP_CD = 'QUEUED' AND carStatus.EFF_START_TS < TRUNC(SYSDATE-1,'DD') ) ) )
          THEN 1 ELSE 0 END ) NOT_WORKED_ON,
    SUM ( 
        CASE WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED'
              AND ((carStatus.CAR_STATUS_TYP_CD != 'QUEUED')
              OR ( carStatus.CAR_STATUS_TYP_CD = 'QUEUED' AND carStatus.EFF_START_TS > TRUNC(SYSDATE-1,'DD') ) ) )
              THEN 1 ELSE 0 END ) STATUS_CHANGED,
        SUM ( CASE WHEN ( carStatus.CAR_STATUS_TYP_CD in ( 'COMPLETED', 'CANCELLED' ) )
              THEN 1 ELSE 0 END ) COMPLETED_CANCELLED,
        SUM ( CASE WHEN ( carStatus.CAR_STATUS_TYP_CD = 'IN_PROGRESS' )
              THEN 1 ELSE 0 END ) CARS_IN_PROGRESS,
        SUM ( CASE WHEN ( carStatus.CAR_STATUS_TYP_CD = 'ESCALATED' )
              THEN 1 ELSE 0 END ) CARS_ESCALATED,
        SUM ( CASE WHEN ( cars_in_pool.CAR_STATUS_TYP_CD != 'ESCALATED'
              AND carStatus.CAR_STATUS_TYP_CD = 'QUEUED' AND carStatus.EFF_START_TS > TRUNC(SYSDATE-1,'DD') )
            THEN 1 ELSE 0 END ) CARS_REQUEUED
from
(select distinct(carStatus.CREDIT_ASSESSMENT_REQUEST_ID), carStatus.CAR_STATUS_TYP_CD,
       CASE WHEN ( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS','ESCALATED' )
                 AND  carStatus.EFF_START_TS < TRUNC(SYSDATE-1,'DD')
                 AND carStatus.EFF_STOP_TS > TRUNC(SYSDATE-1,'DD')
             AND followup_car_date.CAR_DT IS NULL ) THEN 'STARTED_24_HRS_AGO'
            WHEN ( ( carStatus.CAR_STATUS_TYP_CD = 'PENDING'
             AND carStatus.EFF_START_TS < TRUNC(SYSDATE-1,'DD')
             AND carStatus.EFF_STOP_TS BETWEEN TRUNC(SYSDATE-1,'DD') AND TRUNC(SYSDATE,'DD')
             AND carStatus.EFF_STOP_TS != DATE '9999-12-31' )
             OR ( carStatus.CAR_STATUS_TYP_CD IN ( 'SUBMITTED','ESCALATED' )
              AND carStatus.EFF_START_TS  BETWEEN TRUNC(SYSDATE-1,'DD') AND TRUNC(SYSDATE,'DD') )
             OR ( followup_car_date.CAR_DT IS NOT NULL
             AND TRUNC(followup_car_date.CAR_DT, 'DD' ) = TRUNC(SYSDATE-1,'DD') ) ) THEN 'STARTED_WITH_IN_24_HRS'
       END CAR_START_TYPE 
from CAR_STATUS carStatus
LEFT OUTER JOIN  CAR_DATE followup_car_date
ON ( carStatus.CREDIT_ASSESSMENT_REQUEST_ID = followup_car_date.CREDIT_ASSESSMENT_REQUEST_ID
     AND followup_car_date.CAR_DATE_TYP_CD = 'FOLLOWUP'
     AND followup_car_date.EFF_STOP_TS =  DATE '9999-12-31'
     AND followup_car_date.CAR_DT >= TRUNC(SYSDATE,'DD') )
WHERE 
( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS','ESCALATED' )
                 AND  carStatus.EFF_START_TS < TRUNC(SYSDATE-1,'DD')
                 AND carStatus.EFF_STOP_TS > TRUNC(SYSDATE-1,'DD')
             AND followup_car_date.CAR_DT IS NULL )
OR 
( ( carStatus.CAR_STATUS_TYP_CD = 'PENDING'
    AND carStatus.EFF_START_TS < TRUNC(SYSDATE-1,'DD')
    AND carStatus.EFF_STOP_TS BETWEEN TRUNC(SYSDATE-1,'DD') AND TRUNC(SYSDATE,'DD')
    AND carStatus.EFF_STOP_TS != DATE '9999-12-31' )
    OR ( carStatus.CAR_STATUS_TYP_CD IN ( 'SUBMITTED','ESCALATED')
       AND carStatus.EFF_START_TS  BETWEEN TRUNC(SYSDATE-1,'DD') AND TRUNC(SYSDATE,'DD') )
    OR ( followup_car_date.CAR_DT IS NOT NULL
             AND TRUNC(followup_car_date.CAR_DT, 'DD' ) = TRUNC(SYSDATE-1,'DD') ) ) ) cars_in_pool
INNER JOIN CAR_ATTR_VALUE carAttrVal ON
( cars_in_pool.CREDIT_ASSESSMENT_REQUEST_ID = carAttrVal.CREDIT_ASSESSMENT_REQUEST_ID
  AND carAttrVal.CAR_ATTR_TYP_CD = 'CAR_QUEUE'
  AND carAttrVal.EFF_STOP_TS = DATE '9999-12-31' )
INNER JOIN car_status carStatus
on ( carStatus.CREDIT_ASSESSMENT_REQUEST_ID = cars_in_pool.CREDIT_ASSESSMENT_REQUEST_ID
     AND carStatus.EFF_STOP_TS = DATE '9999-12-31' )
GROUP BY cars_in_pool.CAR_STATUS_TYP_CD, carAttrVal.CAR_ATTR_VALUE_TXT)
GROUP BY QUEUE_NAME
ORDER BY
    CASE 
        WHEN QUEUE_NAME = 'Queued (small)'              THEN 1 
        WHEN QUEUE_NAME = 'Queued (medium)'             THEN 2
        WHEN QUEUE_NAME = 'Queued (large)'              THEN 3
        WHEN QUEUE_NAME = 'Legal name validation queue' THEN 4
        WHEN QUEUE_NAME = 'Escalation queue'            THEN 5
        else
            null
    END
;
spool off;
exit 0;

