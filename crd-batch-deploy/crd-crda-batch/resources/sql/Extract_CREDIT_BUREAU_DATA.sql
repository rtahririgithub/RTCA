set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set LINESIZE 2500;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
       credit_bureau_trn.CREDIT_BUREAU_TRN_ID ||'|'||
     credit_bureau_trn.FIRST_NM ||'|'||
     credit_bureau_trn.MIDDLE_NM ||'|'||
     credit_bureau_trn.LAST_NM ||'|'||
     credit_bureau_trn.TRN_ERROR_CD ||'|'||
     credit_bureau_trn.REPORT_SOURCE_CD ||'|'||
     credit_bureau_trn.REPORT_TYP_TXT ||'|'||
     TO_CHAR(credit_bureau_trn.RSLT_CREATION_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
     credit_bureau_trn.CREDIT_BUREAU_TRN_RSLT_STAT_CD ||'|'||
     TO_CHAR(credit_bureau_trn.CR_BRU_TRN_RSLT_STAT_UPDT_DT, 'YYYY-MM-DD HH24:mm:ss')  ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TYPE_CD || '|' || -- new  field
     credit_bureau_trn.ADJDCTN_CREDIT_CLASS_CD ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_LIMIT_AMT ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_CD ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_MSG_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_DEPOSIT_AMT ||'|'||
     TO_CHAR(SYSDATE, 'YYYYMMDD')
FROM CREDIT_BUREAU_TRN credit_bureau_trn
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
ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn.CREDIT_BUREAU_TRN_ID )
ORDER BY max_credit_bureau_trn.CUSTOMER_ID,credit_bureau_trn.CREDIT_BUREAU_TRN_ID;

spool off;
exit 0;
