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

SELECT '01' SORT_ORDER, 'Report Name        Assessment Summary (Manual)' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     (' || TO_CHAR(TO_DATE('&2', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ' to ' || TO_CHAR(TO_DATE('&3', 'YYYY-MM-DD'), 'DD/MM/YYYY') || ')' from dual;

SELECT 'Credit Assessment Ref #~Quote Threshold~Total Quote~Mandatory Assessment Request~Legal Name Valid~Legal Name should be~Corp. Registry Status~Last Validation Date~Control Year~TELUS Rating of Combined Credit reports~Result Status~Result Reason Code~System or Agent ID~CAR pickup Date/Time~CAR complete Date/Time' FROM dual;
select 
car.CREDIT_ASSESSMENT_REQUEST_ID ||'~'||
ca1.CPROFL_ATTRIBUTE_VALUE ||'~'|| 
car_assessment_amt.CAR_TOTAL_VALUE_AMT ||'~'||
ca3.CPROFL_ATTRIBUTE_VALUE ||'~'|| 
ca4.CPROFL_ATTRIBUTE_VALUE ||'~'||
ca5.CPROFL_ATTRIBUTE_VALUE ||'~'||
ca6.CPROFL_ATTRIBUTE_VALUE ||'~'||
ca7.CPROFL_ATTRIBUTE_VALUE ||'~'||
control_year.control_year_value ||'~'||
telus_rating.telus_rating_value ||'~'||
car_result.car_result_typ_cd ||'~'||
car_result.car_result_reason_typ_cd ||'~'||
car.CREATE_USER_ID ||'~'||
TO_CHAR(car_date.EFF_START_TS,'YYYY/MM/DD HH24:MI:SS') ||'~'||
TO_CHAR(car_status.EFF_START_TS, 'YYYY/MM/DD HH24:MI:SS')
FROM credit_assessment_request car
INNER JOIN car_status
ON 
(    
    car.credit_assessment_request_id = car_status.credit_assessment_request_id
    AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TO_DATE('&2','YYYY-MM-DD')
                                           AND TO_DATE('&3','YYYY-MM-DD')
)
INNER JOIN car_credit_profile ccp
on 
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = ccp.CREDIT_ASSESSMENT_REQUEST_ID
)
LEFT OUTER JOIN  cprofl_attribute ca1 ON
(
    ccp.credit_profile_id=ca1.CREDIT_PROFILE_ID
    AND ca1.cprofl_attribute_cd='QuoteThreshold'
    AND ca1.EFF_STOP_DTM > SYSDATE 
)
LEFT OUTER JOIN car_assessment_amt ON
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = car_assessment_amt.CREDIT_ASSESSMENT_REQUEST_ID
    and car_assessment_amt.CAR_ASSESSMENT_AMT_TYPE_CD = 'AMT_TOT_QTE'
)
LEFT OUTER JOIN  cprofl_attribute ca2 ON
(
    ccp.credit_profile_id=ca2.CREDIT_PROFILE_ID
    AND ca2.cprofl_attribute_cd='QuoteThreshold'
    AND ca2.EFF_STOP_DTM > SYSDATE 
)
LEFT OUTER JOIN  cprofl_attribute ca3 ON
(
    ccp.credit_profile_id=ca3.CREDIT_PROFILE_ID
    AND ca3.cprofl_attribute_cd='MandatoryAssessInd'
    AND ca3.EFF_STOP_DTM > SYSDATE
)
LEFT OUTER JOIN  cprofl_attribute ca4 ON
(
    ccp.credit_profile_id=ca4.CREDIT_PROFILE_ID
    AND ca4.cprofl_attribute_cd = 'LegalNameResult'
    AND ca4.EFF_STOP_DTM > SYSDATE
) 
LEFT OUTER JOIN  cprofl_attribute ca5 ON
(
    ccp.credit_profile_id=ca5.CREDIT_PROFILE_ID
    AND ca5.cprofl_attribute_cd = 'ProposedLegalName'
    AND ca5.EFF_STOP_DTM > SYSDATE
)
LEFT OUTER JOIN  cprofl_attribute ca6 ON
(
    ccp.credit_profile_id=ca6.CREDIT_PROFILE_ID
    AND ca6.cprofl_attribute_cd='CorpRegistryStatus'
    AND ca6.EFF_STOP_DTM > SYSDATE
)
LEFT OUTER JOIN  cprofl_attribute ca7 ON
(
    ccp.credit_profile_id=ca7.CREDIT_PROFILE_ID
    AND ca7.cprofl_attribute_cd='LastValidationDate'
    AND ca7.EFF_STOP_DTM > SYSDATE
)
LEFT OUTER JOIN
(
    select car_activity.car_activity_id, car_activity.CREDIT_ASSESSMENT_REQUEST_ID,
    car_activity_dtl_val.ACTIVITY_DETAIL_VALUE_TXT as control_year_value
    FROM car_activity
    inner join car_activity_type
    on 
    (
        car_activity.CAR_ACTIVITY_TYPE_ID = car_activity_type.CAR_ACTIVITY_TYPE_ID
        and car_activity_type.ACTIVITY_TYPE_CD ='DEC_CONTROL_YEAR'
        and car_activity_type.EFF_STOP_TS = DATE '9999-12-31'   
    )
    inner join car_activity_type_dtl
    on
    (
        car_activity_type.CAR_ACTIVITY_TYPE_ID = car_activity_type_dtl.CAR_ACTIVITY_TYPE_ID
        and car_activity_type_dtl.EFF_STOP_TS = DATE '9999-12-31'
    )
    left outer join CAR_ACTIVITY_DTL_VAL
    on 
    (
        car_activity_type_dtl.CAR_ACTIVITY_TYPE_DTL_ID = car_activity_dtl_val.CAR_ACTIVITY_TYPE_DTL_ID
        and car_activity_dtl_val.EFF_STOP_TS = DATE '9999-12-31'
    )
) control_year 
ON car.credit_assessment_request_id = control_year.credit_assessment_request_id
LEFT OUTER JOIN
(
    select car_activity.car_activity_id, car_activity.CREDIT_ASSESSMENT_REQUEST_ID,
    car_activity_dtl_val.ACTIVITY_DETAIL_VALUE_TXT as telus_rating_value
    FROM car_activity
    inner join car_activity_type
    on 
    (
        car_activity.CAR_ACTIVITY_TYPE_ID = car_activity_type.CAR_ACTIVITY_TYPE_ID
        and car_activity_type.ACTIVITY_TYPE_CD ='DEC_TELUS_RAITING'
        and car_activity_type.EFF_STOP_TS = DATE '9999-12-31'   
    )
    inner join car_activity_type_dtl
    on
    (
        car_activity_type.CAR_ACTIVITY_TYPE_ID = car_activity_type_dtl.CAR_ACTIVITY_TYPE_ID
        and car_activity_type_dtl.EFF_STOP_TS = DATE '9999-12-31'
    )
    left outer join CAR_ACTIVITY_DTL_VAL
    on 
    (
        car_activity_type_dtl.CAR_ACTIVITY_TYPE_DTL_ID = car_activity_dtl_val.CAR_ACTIVITY_TYPE_DTL_ID
        and car_activity_dtl_val.EFF_STOP_TS = DATE '9999-12-31'
    )
) telus_rating on car.credit_assessment_request_id = telus_rating.credit_assessment_request_id  
left outer JOIN car_result
ON 
(
    car.credit_assessment_request_id = car_result.credit_assessment_request_id           
    AND car_result.eff_stop_ts = DATE '9999-12-31'
)
inner join car_date
ON 
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = car_date.CREDIT_ASSESSMENT_REQUEST_ID
    and car_date.EFF_STOP_TS = DATE '9999-12-31'
    and car_date.CAR_DATE_TYP_CD='SUBMISSION'
);

spool off;
exit;