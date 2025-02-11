set echo off
set verify off
set termout on
set heading off
set pages 50000
set feedback off
set newpage none
set linesize 2000 
set trimout on
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Credit Reference Type (Manual)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     (' || TO_CHAR(TO_DATE('&2', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ' to ' || TO_CHAR(TO_DATE('&3', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ')' from dual;

SELECT '09' SORT_ORDER, 'Credit Assessment Ref #~Credit Reference Type' FROM dual;

select 
car.CREDIT_ASSESSMENT_REQUEST_ID ||'~'||
bcr.CREDIT_REF_TYP_CD
FROM credit_assessment_request car
INNER JOIN car_status
ON 
(    
    car.credit_assessment_request_id = car_status.credit_assessment_request_id
    AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TO_DATE('&2','YYYY-MM-DD')
                                           AND TO_DATE('&3','YYYY-MM-DD')
)
LEFT OUTER JOIN CAR_BUSINESS_CREDIT_REF cbcr
ON
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = cbcr.CREDIT_ASSESSMENT_REQUEST_ID
    and cbcr.EFF_STOP_TS = DATE '9999-12-31'
)
LEFT OUTER JOIN BUSINESS_CREDIT_REF bcr
ON
(
    cbcr.business_credit_ref_id = bcr.business_credit_ref_id
    and bcr.EFF_STOP_TS = DATE '9999-12-31'
);

spool off;
exit;