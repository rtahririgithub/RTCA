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
        agg_concat(int_crdt_dscn_trn_rslt_dtl.CREDIT_APPRVD_PROD_CATGY_CD ||'|'|| int_crdt_dscn_trn_rslt_dtl.PRODUCT_QUAL_IND )
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
        INNER JOIN INT_CRDT_DCSN_TRN_RSLT_DTL int_crdt_dscn_trn_rslt_dtl
        ON ( int_crdt_dscn_trn_rslt_dtl.INT_CRDT_DSCN_TRN_RSLT_ID = int_crd_decision_trn_result.INT_CRDT_DSCN_TRN_RSLT_ID )
   GROUP BY
        int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
;

spool off;
exit 0;
