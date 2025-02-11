set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set trimspool on
set linesize 32767;
set ARRAYSIZE 5000;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1

 SELECT 
     int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID ||'|'||
     int_crd_decision_trn_result.CREDIT_VALUE_CD ||'|'||
     int_crd_decision_trn_result.DECISION_CD ||'|'||
     int_crd_decision_trn_result.PRODUCT_CATEGORY_BOLT_ON ||'|'||
     int_crd_decision_trn_result.ASSESSMENT_MSG_CD
 FROM 
     INT_CRDT_DCSN_TRN int_crd_decision_trn
     inner join (select inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID, 
                         max(inner_int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID) INT_CRDT_DSCN_TRN_ID
                 from INT_CRDT_DCSN_TRN inner_int_crd_decision_trn
                 INNER JOIN CREDIT_ASSESSMENT_REQUEST inner_car
                 on ( inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = inner_car.CREDIT_ASSESSMENT_REQUEST_ID 
                        AND inner_int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
                 WHERE inner_car.CREATE_TS between TO_DATE('&2','YYYYMMDD') and TO_DATE('&3','YYYYMMDD')
                 GROUP BY   inner_int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID ) max_int_crd_decision_trn
     ON (max_int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID )
     INNER JOIN INT_CRDT_DCSN_TRN_RSLT int_crd_decision_trn_result
     ON ( int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn_result.INT_CRDT_DSCN_TRN_ID 
          AND int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
;

spool off;
exit 0;
