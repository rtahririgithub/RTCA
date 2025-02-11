set echo off
set verify off
set termout off
set heading off
set pages 50000
set feedback off
set newpage none
set linesize 2000 
set trimout on
set trimspool on
set time off
set timing off
set arraysize 1000
spool '&1';

select
'Credit_Assessment_REASON_CD'||','||'Old Credit Value'||','||'New Credit Value'||','||'Count'
from dual;
select icdt.assessment_rslt_reason_cd ||','||scdt.credit_value_cd||','||icdtr.credit_value_cd||','|| count(*)
from CREDIT_ASSESSMENT_REQUEST car
inner join INT_CRDT_DCSN_TRN  icdt
    on ( car.credit_assessment_request_id  = icdt.credit_assessment_request_id 
    and car.car_typ_cd = 'FULL_ASSESSMENT'and car.car_subtype_cd ='MONTHLY_CVUD' and car.create_ts > to_date('&2','YYYYMMDDHH24MI')
    and icdt.assessment_rslt_cd = 'SUCCESS' and icdt.eff_stop_ts = DATE '4444-12-31')
inner join STG_CREDIT_DCSN_TRN scdt
    on ( car.credit_assessment_request_id = scdt.credit_assessment_request_id and scdt.eff_stop_ts = DATE '4444-12-31')
inner join INT_CRDT_DCSN_TRN_RSLT icdtr
    on ( icdt.int_crdt_dscn_trn_id = icdtr.int_crdt_dscn_trn_id and icdtr.eff_stop_ts = DATE '4444-12-31')
group by icdt.assessment_rslt_reason_cd ,scdt.credit_value_cd,icdtr.credit_value_cd;

spool off;

exit;
