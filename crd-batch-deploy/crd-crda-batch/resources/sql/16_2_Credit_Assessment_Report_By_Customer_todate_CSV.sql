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
SELECT '&1'|| '.csv' spoolname from dual;
--SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT 
TO_CHAR(car_date.car_dt, 'DD-MON-YYYY') ||'~'||
car.CAR_TYP_CD ||'~'|| 
car.CREDIT_ASSESSMENT_REQUEST_ID ||'~'||
car_attr_quote.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_cust_type.car_attr_value_txt ||'~'|| 
car_attr_cust_seg.CAR_ATTR_VALUE_TXT ||'~'|| 
car_originator.TEAM_MEMBER_ID ||'~'||
car_attr_leg_name.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_leg_type.car_attr_value_txt ||'~'||
car_attr_rcid.CAR_ATTR_VALUE_TXT ||'~'|| 
car_attr_duns.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_reg_jur.CAR_ATTR_VALUE_TXT ||'~'|| 
car_attr_regno.CAR_ATTR_VALUE_TXT ||'~'|| 
car_attr_regdate.car_attr_value_txt ||'~'||
car_attr_fname.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_lname.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_bdate.CAR_ATTR_VALUE_TXT ||'~'||
car_attr_check.CAR_ATTR_VALUE_TXT ||'~'|| 
car_attr_lessor.CAR_ATTR_VALUE_TXT ||'~'||
car_order_context.EXTERNAL_ORDER_ID ||'~'|| 
car_ord_dtl.m_charge ||'~'|| 
car_ord_dtl.onetime_charge ||'~'||
car_ord_dtl.total_quote_value ||'~'||
car_order_context.PREV_IMPACTED_OC_AMT ||'~'||
car_order_context.PREV_IMPACTED_RC_AMT ||'~'|| 
car_order_context.TOTAL_ORD_IMPACTED_OC_AMT ||'~'||
car_order_context.TOTAL_ORD_IMPACTED_RC_AMT 
FROM credit_assessment_request car
INNER JOIN car_status
ON 
(    
    car.credit_assessment_request_id = car_status.credit_assessment_request_id
    AND car_status.car_status_typ_cd IN ('COMPLETED', 'CANCELLED')
    AND car_status.eff_start_ts BETWEEN TO_DATE('&2','YYYY-MM-DD')
                                           AND TO_DATE('&3','YYYY-MM-DD')
)
INNER JOIN car_date
on
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = car_date.CREDIT_ASSESSMENT_REQUEST_ID
    AND car_date.CAR_DATE_TYP_CD = 'SUBMISSION'
    AND car_date.eff_stop_ts = DATE '9999-12-31'
)
LEFT OUTER JOIN CAR_ATTR_VALUE car_attr_quote
on 
(
    car.CREDIT_ASSESSMENT_REQUEST_ID = car_attr_quote.credit_assessment_request_id
    AND car_attr_quote.car_attr_typ_cd = 'MASTER_QTE_THLD'
    AND car_attr_quote.eff_stop_ts = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_cust_type
ON 
(    
    car.credit_assessment_request_id = car_attr_cust_type.credit_assessment_request_id
    AND car_attr_cust_type.car_attr_typ_cd = 'MASTER_CUST_TYPE'
    AND car_attr_cust_type.eff_stop_ts = DATE '9999-12-31'           
)
LEFT OUTER JOIN car_attr_value car_attr_cust_seg
ON
(
    car.credit_assessment_request_id = car_attr_cust_seg.credit_assessment_request_id
    AND car_attr_cust_seg.CAR_ATTR_TYP_CD = 'MASTER_CUST_SEG'
    AND car_attr_cust_seg.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN CAR_TEAM_MEMBER_INVLVMNT car_originator
ON
(
    car.credit_assessment_request_id = car_originator.credit_assessment_request_id
    AND car_originator.CAR_TEAM_MEMBER_ROLE_TYP_CD = 'ORIGINATOR'
    AND car_originator.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_leg_name
ON
(
    car.credit_assessment_request_id = car_attr_leg_name.credit_assessment_request_id
    AND car_attr_leg_name.CAR_ATTR_TYP_CD = 'MASTER_LEG_NAME'
    AND car_attr_leg_name.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_leg_type
ON
(
    car.credit_assessment_request_id = car_attr_leg_type.credit_assessment_request_id
    AND car_attr_leg_type.CAR_ATTR_TYP_CD = 'MASTER_LEG_TYPE'
    AND car_attr_leg_type.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_rcid
ON
(
    car.credit_assessment_request_id = car_attr_rcid.credit_assessment_request_id
    AND car_attr_rcid.CAR_ATTR_TYP_CD = 'MASTER_RCID'
    AND car_attr_rcid.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_duns
ON
(
    car.credit_assessment_request_id = car_attr_duns.credit_assessment_request_id
    AND car_attr_duns.CAR_ATTR_TYP_CD = 'MASTER_DUNS_NO'
    AND car_attr_duns.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_reg_jur
ON
(
    car.credit_assessment_request_id = car_attr_reg_jur.credit_assessment_request_id
    AND car_attr_reg_jur.CAR_ATTR_TYP_CD = 'MASTER_JUR_CD'
    AND car_attr_reg_jur.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_regno
ON
(
    car.credit_assessment_request_id = car_attr_regno.credit_assessment_request_id
    AND car_attr_regno.CAR_ATTR_TYP_CD = 'MASTER_REG_NO'
    AND car_attr_regno.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_regdate
ON
(
    car.credit_assessment_request_id = car_attr_regdate.credit_assessment_request_id
    AND car_attr_regdate.CAR_ATTR_TYP_CD = 'MASTER_REG_DTE'
    AND car_attr_regdate.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_fname
ON
(
    car.credit_assessment_request_id = car_attr_fname.credit_assessment_request_id
    AND car_attr_fname.CAR_ATTR_TYP_CD = 'PROP_F_NM'
    AND car_attr_fname.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_lname
ON
(
    car.credit_assessment_request_id = car_attr_lname.credit_assessment_request_id
    AND car_attr_lname.CAR_ATTR_TYP_CD = 'PROP_L_NM'
    AND car_attr_lname.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_bdate
ON
(
    car.credit_assessment_request_id = car_attr_bdate.credit_assessment_request_id
    AND car_attr_bdate.CAR_ATTR_TYP_CD = 'PROP_BDATE'
    AND car_attr_bdate.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_check
ON
(
    car.credit_assessment_request_id = car_attr_check.credit_assessment_request_id
    AND car_attr_check.CAR_ATTR_TYP_CD = 'PROP_CCHECK'
    AND car_attr_check.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_attr_value car_attr_lessor
ON
(
    car.credit_assessment_request_id = car_attr_lessor.credit_assessment_request_id
    AND car_attr_lessor.CAR_ATTR_TYP_CD = 'LEASE_NAME'
    AND car_attr_lessor.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN car_order_context
ON
(
    car.credit_assessment_request_id = car_order_context.CREDIT_ASSESSMENT_REQUEST_ID
    AND car_order_context.EFF_STOP_TS = DATE '9999-12-31'    
)
LEFT OUTER JOIN
(
    select car_order_context_id,sum(RC_MONTHLY_CHARGE_FEE_AMT) as m_charge,
    SUM(ONETIME_CHARGE_FEE_AMT) as onetime_charge,
    SUM(RC_MONTHLY_CHARGE_FEE_AMT * CNTRCT_TERM_REMAIN_IN_MTH) as total_quote_value 
    from CAR_ORD_CNTXT_DTL  
    group by car_order_context_id
) car_ord_dtl 
ON
(
    car_order_context.CAR_ORDER_CONTEXT_ID = car_ord_dtl.car_order_context_id
);

spool off;
exit;
       
