/*
    CREDIT ASSESSMENT SUMMARY REPORT [2012/10/25 - for review]
    
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 32767
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool '&1';

select
    car.CREDIT_ASSESSMENT_REQUEST_ID	||'|'||
    -- Risk Indicator - no hit thin file    
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '1')	||'|'||  
    -- Risk Indicator - true thin file   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '2')	||'|'|| 
    -- Risk Indicator - high risk thin file    
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '3')	||'|'|| 
    -- Risk Indicator - temp SIN   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '4') 	||'|'||
    -- Risk Indicator - underage   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '5') 	||'|'||
    -- Risk Indicator - bankruptcy   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '6' ) 	||'|'||
    -- Risk indicator - primary risk   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '7' ) 	||'|'||
    -- Risk indicator - secondary risk   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '8' ) 	||'|'||
    -- RRisk indicator - writeoff risk   
    (SELECT risk_ind.RISK_IND_VALUE_TXT FROM credit_bureau_trn_dtl risk_ind WHERE rownum <= 1 and cbt.CREDIT_BUREAU_TRN_ID = risk_ind.CREDIT_BUREAU_TRN_ID AND risk_ind.CREDIT_BUREAU_TRN_DTL_CD = '2' AND risk_ind.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') AND risk_ind.RISK_IND_TYP_CD = '9' )
from
    credit_assessment_request car 
    INNER JOIN credit_bureau_trn cbt ON 
        (car.CREDIT_ASSESSMENT_REQUEST_ID = cbt.CREDIT_ASSESSMENT_REQUEST_ID
        AND cbt.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD'))
    where car.CREATE_TS BETWEEN TO_DATE('&2','YYYYMMDD') AND TO_DATE('&3','YYYYMMDD')
;

spool off;
exit 0
;



