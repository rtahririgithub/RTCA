set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set LINESIZE 2594;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID ||'|'||
       CREDIT_BUREAU_TRN_DTL_ID ||'|'||
       CREDIT_BUREAU_TRN_DTL_CD ||'|'||
       SCORE_NM ||'|'||
       SCORE_VALUE_TXT ||'|'||
       RISK_IND_TYP_CD ||'|'||
       RISK_IND_VALUE_TXT ||'|'||
       FRAUD_TYP_CD ||'|'||
       FRAUD_CD ||'|'||
       FRAUD_MSG_TXT ||'|'||
       CREDIT_BUREAU_DATA_DOC_NM ||'|'||
       EXT_SRC_SYS_CD ||'|'||
--       EXT_DOC_PATH_STR ||'|'||
       EXT_DOC_FORMAT_TYP ||'|'||
       TO_CHAR(SYSDATE, 'YYYYMMDD')
FROM CREDIT_BUREAU_TRN_DTL credit_bureau_trn_dtl
INNER JOIN (SELECT  car_cust.CUSTOMER_ID, max(credit_bureau_trn_inner.CREDIT_BUREAU_TRN_ID) max_CREDIT_BUREAU_TRN_ID
FROM STG_CREDIT_UPDOWN_CUST mud_cust
INNER JOIN CUST_CREDIT_ASSMNT_RQST car_cust
      ON ( mud_cust.CUSTOMER_ID = car_cust.CUSTOMER_ID 
            and car_cust.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
INNER JOIN CREDIT_BUREAU_TRN credit_bureau_trn_inner
      ON ( credit_bureau_trn_inner.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID
           and credit_bureau_trn_inner.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') 
	   and credit_bureau_trn_inner.CREDIT_BUREAU_TRN_RSLT_STAT_CD != '2' )
INNER JOIN INT_CRDT_DCSN_TRN int_crd_decision_trn 
      ON ( int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID
	   AND int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD')
	   AND int_crd_decision_trn.ASSESSMENT_RSLT_CD = 'SUCCESS'  
	   AND ( int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD is null   
		OR int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD = 'NO_CHANGE' ) )
GROUP BY car_cust.CUSTOMER_ID) max_credit_bureau_trn
ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID )
ORDER BY max_credit_bureau_trn.CUSTOMER_ID,CREDIT_BUREAU_TRN_ID;

spool off;
exit 0;
