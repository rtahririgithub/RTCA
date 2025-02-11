/*    Work Queue Report [2010/08/12 - for review]
    
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
set linesize 250
set trimspool on
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Work Queue Report 1 (Daily)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(SYSDATE -1, 'DD/MM/YYYY') from dual;

SELECT '~Exclude CAR''s with FUP date~~~~~' FROM DUAL
UNION
SELECT '~Submitted within 24 hrs~Submitted older than 24 hrs but less than 48 hrs~Submitted older than 48 hrs~FUP today~Total # (of the left 4 columns)~FUP on a future day' FROM DUAL;
SELECT QUEUE_NAME ||'~'|| SUM(WITHIN_24HRS) ||'~'|| SUM(WITHIN_24_To_48HRS) ||'~'|| SUM(OLDER_THAN_48HRS) ||'~'|| SUM(FOLLOWUP_TODAY) ||'~'|| SUM(WITHIN_24HRS + WITHIN_24_To_48HRS + OLDER_THAN_48HRS + FOLLOWUP_TODAY ) ||'~'|| SUM(FOLLOWUP_FUTURE)
FROM (
SELECT 
       CASE WHEN ( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS' ) AND carAttrVal.CAR_ATTR_VALUE_TXT = 'SML' ) THEN 'Queued (small)'
            WHEN ( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS' ) AND carAttrVal.CAR_ATTR_VALUE_TXT = 'MED' ) THEN 'Queued (medium)'
        WHEN ( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS' ) AND carAttrVal.CAR_ATTR_VALUE_TXT = 'LRG' ) THEN 'Queued (large)'
        WHEN ( carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS' ) AND carAttrVal.CAR_ATTR_VALUE_TXT = 'LNV' ) THEN 'Legal name validation queue'
        WHEN ( carStatus.CAR_STATUS_TYP_CD = 'ESCALATED'  ) THEN 'Escalation queue' 
        ELSE 'Others' END QUEUE_NAME,
   SUM( CASE WHEN ( carDate.CAR_DT BETWEEN TRUNC(SYSDATE-1,'DD') AND TRUNC(SYSDATE,'DD')
            AND followup_car_date.CAR_DT IS NULL )
        THEN 1
    ELSE 0 END ) WITHIN_24HRS,
   SUM( CASE WHEN ( carDate.CAR_DT BETWEEN TRUNC(SYSDATE-2,'DD') AND TRUNC(SYSDATE-1,'DD')
                    AND followup_car_date.CAR_DT IS NULL )
        THEN 1
    ELSE 0 END ) WITHIN_24_To_48HRS,
   SUM( CASE WHEN ( carDate.CAR_DT < TRUNC(SYSDATE-2,'DD')
                    AND followup_car_date.CAR_DT IS NULL )
        THEN 1
    ELSE 0 END ) OLDER_THAN_48HRS,
   SUM ( CASE WHEN ( followup_car_date.CAR_DT IS NOT NULL
                     AND TRUNC(followup_car_date.CAR_DT, 'DD' ) = TRUNC(SYSDATE,'DD') )
        THEN 1
    ELSE 0 END ) FOLLOWUP_TODAY,
   SUM ( CASE WHEN ( followup_car_date.CAR_DT IS NOT NULL
                     AND followup_car_date.CAR_DT >= TRUNC(SYSDATE+1,'DD') )
        THEN 1
    ELSE 0 END ) FOLLOWUP_FUTURE
FROM CAR_STATUS carStatus
INNER JOIN CAR_DATE carDate
ON ( carStatus.CREDIT_ASSESSMENT_REQUEST_ID = carDate.CREDIT_ASSESSMENT_REQUEST_ID )
INNER JOIN CAR_ATTR_VALUE carAttrVal ON
    (carDate.CREDIT_ASSESSMENT_REQUEST_ID = carAttrVal.CREDIT_ASSESSMENT_REQUEST_ID)
LEFT OUTER JOIN  CAR_DATE followup_car_date
ON ( carStatus.CREDIT_ASSESSMENT_REQUEST_ID = followup_car_date.CREDIT_ASSESSMENT_REQUEST_ID
     AND followup_car_date.CAR_DATE_TYP_CD = 'FOLLOWUP'
     AND followup_car_date.EFF_STOP_TS =  DATE '9999-12-31'
     AND followup_car_date.CAR_DT >= TRUNC(SYSDATE,'DD') )
WHERE
    carAttrVal.CAR_ATTR_TYP_CD = 'CAR_QUEUE'
    AND carAttrVal.EFF_STOP_TS = DATE '9999-12-31'
    AND carDate.EFF_STOP_TS = DATE '9999-12-31'
    AND carDate.CAR_DATE_TYP_CD = 'SUBMISSION'
    AND carStatus.EFF_STOP_TS = DATE '9999-12-31'
    AND carStatus.CAR_STATUS_TYP_CD IN ( 'QUEUED', 'IN_PROGRESS','ESCALATED' )
GROUP BY carStatus.CAR_STATUS_TYP_CD,carAttrVal.CAR_ATTR_VALUE_TXT )
GROUP BY QUEUE_NAME
ORDER BY
    CASE 
        WHEN QUEUE_NAME = 'Queued (small)' THEN 1 
        WHEN QUEUE_NAME = 'Queued (medium)' THEN 2
        WHEN QUEUE_NAME = 'Queued (large)' THEN 3
        WHEN QUEUE_NAME = 'Legal name validation queue' THEN 4
        WHEN QUEUE_NAME = 'Escalation queue' THEN 5
        else
            6
    END;

spool off;

exit 0
;
