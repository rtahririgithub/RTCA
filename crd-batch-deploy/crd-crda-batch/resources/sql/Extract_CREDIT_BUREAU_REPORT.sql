--set echo on time on timing on;
--alter session set optimizer_mode = all_rows;
whenever sqlerror CONTINUE

DROP TABLE max_credit_bureau_trn;
whenever sqlerror exit SQL.SQLCODE

CREATE TABLE max_credit_bureau_trn
AS
    SELECT car_cust.CUSTOMER_ID CUSTOMER_ID, 
            max(credit_bureau_trn_inner.CREDIT_BUREAU_TRN_ID) max_CREDIT_BUREAU_TRN_ID
        FROM STG_CREDIT_UPDOWN_CUST mud_cust
            INNER JOIN CUST_CREDIT_ASSMNT_RQST car_cust
                  ON ( mud_cust.CUSTOMER_ID = car_cust.CUSTOMER_ID 
                    AND car_cust.EFF_STOP_TS = TO_DATE('44441231', 'YYYYMMDD') )
            INNER JOIN CREDIT_BUREAU_TRN credit_bureau_trn_inner
                  ON ( credit_bureau_trn_inner.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID
                       AND credit_bureau_trn_inner.EFF_STOP_TS = TO_DATE('44441231', 'YYYYMMDD') 
                       AND credit_bureau_trn_inner.CREDIT_BUREAU_TRN_RSLT_STAT_CD != '2' )
            INNER JOIN INT_CRDT_DCSN_TRN int_crd_decision_trn 
                  ON ( int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = car_cust.CREDIT_ASSESSMENT_REQUEST_ID
                       AND int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231', 'YYYYMMDD')
                       AND int_crd_decision_trn.ASSESSMENT_RSLT_CD = 'SUCCESS'  
                       AND ( int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD is null   
                        OR int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD = 'NO_CHANGE' ) )
            GROUP BY car_cust.CUSTOMER_ID
 ;
ALTER TABLE max_credit_bureau_trn ADD CONSTRAINT max_credit_bureau_trn_pk PRIMARY KEY (CUSTOMER_ID);

set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set ARRAYSIZE 5000;

--spool off;
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

/*
    1. Customer information. Credit score/decision [2013/10/10]    
*/
spool &1
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
     credit_bureau_trn.CREDIT_BUREAU_TRN_ID ||'|'||
     credit_bureau_trn.FIRST_NM ||'|'||
     credit_bureau_trn.MIDDLE_NM ||'|'||
     credit_bureau_trn.LAST_NM ||'|'||
     credit_bureau_trn.TRN_ERROR_CD ||'|'||
     credit_bureau_trn.REPORT_SOURCE_CD ||'|'||
     credit_bureau_trn.REPORT_TYP_TXT ||'|'||
     credit_bureau_trn.RSLT_CREATION_DT ||'|'||
     credit_bureau_trn.CREDIT_BUREAU_TRN_RSLT_STAT_CD ||'|'||
     credit_bureau_trn.CR_BRU_TRN_RSLT_STAT_UPDT_DT ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_SCORE_TYPE_CD   || '|' || -- new  field
     credit_bureau_trn.ADJDCTN_CREDIT_CLASS_CD ||'|'||
     credit_bureau_trn.ADJDCTN_CREDIT_LIMIT_AMT ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_CD ||'|'||
     credit_bureau_trn.ADJDCTN_DSCN_MSG_TXT ||'|'||
     credit_bureau_trn.ADJDCTN_DEPOSIT_AMT
FROM CREDIT_BUREAU_TRN credit_bureau_trn
	INNER JOIN max_credit_bureau_trn
		ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn.CREDIT_BUREAU_TRN_ID )
ORDER BY max_credit_bureau_trn.CUSTOMER_ID, credit_bureau_trn.CREDIT_BUREAU_TRN_ID
;
spool off;

/*
    2. Score Card Attribute List [2013/10/10]    
*/
set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set ARRAYSIZE 5000;

--spool off;
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &2
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_CD ||'|'||
       credit_bureau_trn_dtl.SCORE_NM ||'|'||
       credit_bureau_trn_dtl.SCORE_VALUE_TXT       
FROM CREDIT_BUREAU_TRN_DTL credit_bureau_trn_dtl
    INNER JOIN max_credit_bureau_trn 
        ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID )
            AND credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_CD = '1' -- CREDIT_BUREAU_DTL_SCORE_TYPE
ORDER BY max_credit_bureau_trn.CUSTOMER_ID, CREDIT_BUREAU_TRN_ID
;
spool off;

/*
    3. Fraud Warning List [2013/10/10]    
*/
set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set ARRAYSIZE 5000;

--spool off;
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &3
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_CD ||'|'||      
       credit_bureau_trn_dtl.FRAUD_TYP_CD ||'|'||
       credit_bureau_trn_dtl.FRAUD_CD ||'|'||
       credit_bureau_trn_dtl.FRAUD_MSG_TXT ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_DATA_DOC_NM ||'|'||
       credit_bureau_trn_dtl.EXT_SRC_SYS_CD ||'|'||
       credit_bureau_trn_dtl.EXT_DOC_PATH_STR ||'|'||
       credit_bureau_trn_dtl.EXT_DOC_FORMAT_TYP
FROM CREDIT_BUREAU_TRN_DTL credit_bureau_trn_dtl
    INNER JOIN max_credit_bureau_trn
        ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID )
            AND credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_CD = '3' -- CREDIT_BUREAU_DTL_FRAUD_TYPE 
ORDER BY max_credit_bureau_trn.CUSTOMER_ID, CREDIT_BUREAU_TRN_ID
;
spool off;

/*
    4. RiskIndicators [2013/10/10]    
*/
set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set ARRAYSIZE 5000;

--spool off;
whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &4
SELECT max_credit_bureau_trn.CUSTOMER_ID ||'|'||
       credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID ||'|'||
       CREDIT_BUREAU_TRN_DTL_ID ||'|'||
       CREDIT_BUREAU_TRN_DTL_CD ||'|'||       
       RISK_IND_TYP_CD ||'|'||
       RISK_IND_VALUE_TXT      
FROM CREDIT_BUREAU_TRN_DTL credit_bureau_trn_dtl
    INNER JOIN max_credit_bureau_trn
        ON (max_credit_bureau_trn.max_CREDIT_BUREAU_TRN_ID = credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_ID )
            AND credit_bureau_trn_dtl.CREDIT_BUREAU_TRN_DTL_CD = '2' -- CREDIT_BUREAU_DTL_RISK_TYPE  
ORDER BY max_credit_bureau_trn.CUSTOMER_ID, CREDIT_BUREAU_TRN_ID
;
spool off
;       
exit 0;
