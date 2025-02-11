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

SELECT '01' SORT_ORDER, 'Report Name        Credit Assessment Report By Customer (Manual)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     (' || TO_CHAR(TO_DATE('&2', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ' to ' || TO_CHAR(TO_DATE('&3', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ')' from dual;

SELECT 'Credit Assessment Ref #~Offer ID~Offer Name~Product Name~Service ID~Order Action~Contract Term' FROM dual;
select
car.CREDIT_ASSESSMENT_REQUEST_ID ||'~'||
car_dtl.OFF_CATALOG_ID ||'~'||         
car_dtl.OFFER_NM ||'~'||           
car_dtl.PRODUCT_NM ||'~'||         
car_dtl.SERVICE_ID ||'~'||         
car_dtl.ORDER_ACTION_TYP_CD ||'~'||
    CASE
        WHEN 
            car_dtl.CONTRACT_TERM_DURATION <> 'MTM' OR NOT(car_dtl.CONTRACT_TERM_DURATION is null)
        THEN
            car_dtl.CONTRACT_TERM_DURATION || ' years'
        ELSE
            car_dtl.CONTRACT_TERM_DURATION
    END as contract_term
FROM credit_assessment_request car
INNER JOIN car_status
ON 
(    
    car.credit_assessment_request_id = car_status.credit_assessment_request_id
    AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TO_DATE('&2','YYYY-MM-DD')
                                           AND TO_DATE('&3','YYYY-MM-DD')
)
LEFT OUTER JOIN car_order_context car_context
ON
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = car_context.CREDIT_ASSESSMENT_REQUEST_ID
    AND car_context.EFF_STOP_TS = DATE '9999-12-31'
)
LEFT OUTER JOIN CAR_ORD_CNTXT_DTL car_dtl
ON
(
    car_context.CAR_ORDER_CONTEXT_ID = car_dtl.CAR_ORDER_CONTEXT_ID
);
spool off;
exit;