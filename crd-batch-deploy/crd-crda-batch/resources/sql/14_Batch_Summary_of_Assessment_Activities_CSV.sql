set echo off
set verify off
set heading off
set termout on
set pages 50000
set feedback off
set newpage none
set linesize 160 

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        Summary of Assessment Activities (Monthly)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual;

SELECT '00' SORT_ORDER, 'Date Range~~~From-To Date (inclusive) '  || TO_CHAR(TRUNC(ADD_MONTHS(SYSDATE, -1), 'MM'), 'YYYY-MM-DD') || ' to ' || TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)), 'YYYY-MM-DD') FROM dual
UNION
SELECT
    '01' SORT_ORDER,
    'Total # of CARs Completed or Cancelled'    ||'~'||
    ''                                          ||'~'||
    ''                                          ||'~'||
    COUNT(1)
FROM 
    car_status car_status
WHERE 
    car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM')
    AND TRUNC (SYSDATE, 'MM')
UNION
SELECT '020' SORT_ORDER, '' FROM dual
UNION
SELECT
    '021' SORT_ORDER,
    'By CAR Type'               ||'~'||
    'Full Assessment'           ||'~'||
    ''                          ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
        car.credit_assessment_request_id = car_status.credit_assessment_request_id
        AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
        AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
WHERE car.car_typ_cd = 'FULL_ASSESSMENT'
UNION
SELECT
    '03' SORT_ORDER,
    ''                  ||'~'||
    ''                  ||'~'||
    'Quote value <= $5K' ||'~'|| 
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    LEFT OUTER JOIN car_assessment_amt car_amount
    ON (    
            car.credit_assessment_request_id = car_amount.credit_assessment_request_id
            AND car_amount.eff_stop_ts = DATE '9999-12-31'
            AND car_amount.car_assessment_amt_type_cd = 'AMT_TOT_QTE'
            AND car_amount.car_total_value_amt <= 5000
       )
WHERE 
    car.car_typ_cd = 'FULL_ASSESSMENT'
UNION
SELECT
    '04' SORT_ORDER,
    ''                  ||'~'||
    ''                  ||'~'||
    'Quote value > $5K' ||'~'||
    COUNT(1)
FROM credit_assessment_request car JOIN car_status
        ON (    
                car.credit_assessment_request_id = car_status.credit_assessment_request_id
                AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
                AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
          )
        INNER JOIN car_assessment_amt car_amount
        ON (    
                car.credit_assessment_request_id = car_amount.credit_assessment_request_id
                AND car_amount.eff_stop_ts = DATE '9999-12-31'
                AND car_amount.car_assessment_amt_type_cd = 'AMT_TOT_QTE'
                AND car_amount.car_total_value_amt > 5000
          )
WHERE 
    car.car_typ_cd = 'FULL_ASSESSMENT'
UNION
SELECT
    '05' SORT_ORDER,
    ''                              ||'~'||
    'Legal name validation only'    ||'~'||
    ''                              ||'~'||
    COUNT(1)
FROM credit_assessment_request car INNER JOIN car_status
    ON (    car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
WHERE 
    car.car_typ_cd = 'LEG_NM_VALIDATION'
UNION
SELECT
    '06' SORT_ORDER,
    ''                          ||'~'||
    'Internal credit review'    ||'~'||
    ''                          ||'~'||
    COUNT(1)
FROM credit_assessment_request car INNER JOIN car_status
    ON (    
        car.credit_assessment_request_id = car_status.credit_assessment_request_id
        AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
        AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
WHERE 
    car.car_typ_cd = 'INTERNAL_CRD_RVW'
UNION
SELECT '070' SORT_ORDER, '' FROM dual
UNION
SELECT
    '071' SORT_ORDER,
    'By customer type'                      ||'~'||
    '# of CAR''s for active customers'      ||'~'||          
    ''                                      ||'~'||
    COUNT(1)                   
FROM credit_assessment_request car INNER JOIN car_status
    ON (    
         car.credit_assessment_request_id = car_status.credit_assessment_request_id
         AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
         AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_credit_profile car_crd_profile
    ON (    
         car.credit_assessment_request_id = car_crd_profile.credit_assessment_request_id
         AND car_crd_profile.eff_stop_ts = DATE '9999-12-31'          
       )
    INNER JOIN car_attr_value
    ON (    
         car.credit_assessment_request_id = car_attr_value.credit_assessment_request_id
         AND car_attr_value.car_attr_typ_cd = 'MASTER_CUST_TYPE'
         AND car_attr_value.car_attr_value_txt = 'AC'
       )
UNION
SELECT
    '08' SORT_ORDER,
    ''                      ||'~'||
    '# of CAR''s for non-active customers'  ||'~'||
    ''                                      ||'~'||
    COUNT (1)
FROM credit_assessment_request car INNER JOIN car_status
    ON (    
         car.credit_assessment_request_id = car_status.credit_assessment_request_id
         AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
         AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_credit_profile car_crd_profile
    ON (    
         car.credit_assessment_request_id = car_crd_profile.credit_assessment_request_id
         AND car_crd_profile.eff_stop_ts = DATE '9999-12-31'          
       )
    INNER JOIN car_attr_value
    ON (    
         car.credit_assessment_request_id = car_attr_value.credit_assessment_request_id
         AND car_attr_value.car_attr_typ_cd = 'MASTER_CUST_TYPE'
         AND car_attr_value.car_attr_value_txt != 'AC'
       )
UNION
SELECT '090' SORT_ORDER, '' FROM dual
UNION
SELECT
    '091' SORT_ORDER,
    'By Auto/Manual'                            ||'~'||
    'Auto approved (pass simple assessment)'    ||'~'||
    ''                                          ||'~'||    
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC(ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC(SYSDATE, 'MM')
       )
    INNER JOIN car_activity ca
    ON (    car.credit_assessment_request_id = ca.credit_assessment_request_id
            AND car_status.car_status_id = ca.car_status_id
            AND ca.eff_stop_ts = DATE '9999-12-31'
       )
    INNER JOIN car_activity_type cat
    ON (    
            ca.car_activity_type_id = cat.car_activity_type_id
            AND cat.activity_type_cd = 'SIMPLE_ASSESSMT'
            AND cat.eff_stop_ts = DATE '9999-12-31'
       )
UNION
SELECT
    '10' SORT_ORDER,
    ''                  ||'~'||
    'Manual assessment' ||'~'||
    ''                  ||'~'||    
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_activity ca
    ON (    
            car.credit_assessment_request_id = ca.credit_assessment_request_id
            AND car_status.car_status_id = ca.car_status_id
            AND ca.eff_stop_ts = DATE '9999-12-31'
       )
    INNER JOIN car_activity_type cat
    ON (    
            ca.car_activity_type_id = cat.car_activity_type_id 
            AND cat.activity_type_cd IN ('FINALIZE', 'CANCEL')
            AND cat.eff_stop_ts = DATE '9999-12-31'
       )
UNION
SELECT '110' SORT_ORDER, '' FROM dual
UNION
SELECT
    '111' SORT_ORDER,
    'By Result Status'              ||'~'||
    'Completed approved'            ||'~'||
    ''                              ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'COMPLETED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'APPROVED'
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT
    '12' SORT_ORDER,
    ''                  ||'~'||
    ''                  ||'~'||
    'No condition'      ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'COMPLETED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'APPROVED'
            AND car_result.car_result_reason_typ_cd = 'NO_CONDITION'
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT
    '13' SORT_ORDER,
    ''                  ||'~'||
    ''                  ||'~'||
    'With condition'    ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'COMPLETED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'APPROVED'
            AND car_result.car_result_reason_typ_cd IN ('WITH_COND_CMTS', 'WITH_COND_LG_CORR', 'WITH_COND_LIEN')
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT
    '14' SORT_ORDER,
    ''                  ||'~'||         
    ''                  ||'~'||
    'Simple Assessment' ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'COMPLETED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'APPROVED'
            AND car_result.car_result_reason_typ_cd = 'SIMPL_ASSMT'
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT
    '15' SORT_ORDER,
    ''                  ||'~'||
    'Completed Denied'  ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'COMPLETED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'DENIED' 
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT
    '16' SORT_ORDER,
    ''                  ||'~'||
    'Cancelled'         ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd = 'CANCELLED'
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_result
    ON (    
            car.credit_assessment_request_id = car_result.credit_assessment_request_id
            AND car_result.car_result_typ_cd = 'CANCELLED'
            AND car_result.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT '170' SORT_ORDER, '' FROM dual
UNION
SELECT
    '171' SORT_ORDER,
    '# of CAR''s ever sent to manager'  ||'~'||
    ''                                  ||'~'||
    ''                                  ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_date
    ON (    
            car.credit_assessment_request_id = car_date.credit_assessment_request_id
            AND car_date.car_date_typ_cd = 'ESCALATION'
            AND car_date.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION   
SELECT '180' SORT_ORDER, '' FROM dual
UNION
SELECT
    '181' SORT_ORDER,
    '# of CAR''s resubmitted'           ||'~'||
    ''                                  ||'~'||
    ''                                  ||'~'||
    COUNT(1)
FROM 
    credit_assessment_request car INNER JOIN car_status
    ON (    
            car.credit_assessment_request_id = car_status.credit_assessment_request_id
            AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
            AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
       )
    INNER JOIN car_date
    ON (    
            car.credit_assessment_request_id = car_date.credit_assessment_request_id
            AND car_date.car_date_typ_cd = 'RESUBMISSION'
            AND car_date.eff_stop_ts = TIMESTAMP '9999-12-31 00:00:00'
       )
UNION
SELECT '190' SORT_ORDER, '' FROM dual
UNION
SELECT
    '191' SORT_ORDER,
    '# of corp registry check'      ||'~'||
    ''                              ||'~'||
    ''                              ||'~'||
    COUNT(1)
FROM 
    car_status car_status INNER JOIN credit_report_trns_log crlog
    ON (    
            car_status.credit_assessment_request_id = crlog.credit_request_context_id
            AND crlog.credit_request_context_typ_cd = 'CARID'
            AND crlog.credit_report_source_sys_cd = 'MISCELLANEOUS'
            AND crlog.credit_hit_cd IN ('Y', 'N', '1', '2')
       )
WHERE 
    car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
UNION
SELECT
    '20' SORT_ORDER,
    ''                  ||'~'||
    'Hit'               ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    car_status car_status INNER JOIN credit_report_trns_log crlog
    ON (    
            car_status.credit_assessment_request_id = crlog.credit_request_context_id
            AND crlog.credit_request_context_typ_cd = 'CARID'
            AND crlog.credit_report_source_sys_cd = 'MISCELLANEOUS'
            AND crlog.credit_hit_cd IN ('Y', '1')
       )
WHERE 
    car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
UNION
SELECT
    '21' SORT_ORDER,
    ''                  ||'~'||
    'No hit'            ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    car_status car_status INNER JOIN credit_report_trns_log crlog
    ON (    
            car_status.credit_assessment_request_id = crlog.credit_request_context_id
            AND crlog.credit_request_context_typ_cd = 'CARID'
            AND crlog.credit_report_source_sys_cd = 'MISCELLANEOUS'
            AND crlog.credit_hit_cd IN ('N', '2')
       )
WHERE 
    car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
UNION
SELECT '220' SORT_ORDER, '' FROM dual
UNION
SELECT
    '221' SORT_ORDER,
    '# of bureau checks'        ||'~'||
    ''                          ||'~'||
    ''                          ||'~'||
    COUNT(1)
FROM 
    credit_report_trns_log crlog
WHERE 
    crlog.create_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
    AND crlog.credit_report_source_sys_cd != 'MISCELLANEOUS'
    AND crlog.credit_hit_cd IN ('Y', 'N', '1', '2')
UNION
SELECT
    '23' SORT_ORDER,
    ''                  ||'~'||
    'Hit'               ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    credit_report_trns_log crlog
WHERE 
    crlog.create_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
    AND crlog.credit_report_source_sys_cd != 'MISCELLANEOUS'
    AND crlog.credit_hit_cd IN ('Y', '1')
UNION
SELECT
    '24' SORT_ORDER,
    ''                  ||'~'||
    'No hit'            ||'~'||
    ''                  ||'~'||
    COUNT(1)
FROM 
    credit_report_trns_log crlog
WHERE 
    crlog.create_ts BETWEEN TRUNC (ADD_MONTHS (SYSDATE, -1), 'MM') AND TRUNC (SYSDATE, 'MM')
    AND crlog.credit_report_source_sys_cd != 'MISCELLANEOUS'
    AND crlog.credit_hit_cd IN ('N', '2')
;

spool off;

exit 0
;