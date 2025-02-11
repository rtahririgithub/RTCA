/*
    GENERAL STAT MONTHLY [2010/08/12 - for review]

    Please Note: >> To remove extra lines and spaces
                Step #1.  unix> sed 's/[ \t]*$//' your_file_name > temp_file_name
                Step #2.  unix> sed '/^$/d' temp_file_name > your_file_name
*/
set echo off
set verify off
set termout on
set heading off
set pages 0
set feedback off
set newpage none
set linesize 500
set trimspool on

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

col spoolname new_value spoolname;  
SELECT '&1' || TO_CHAR(SYSDATE, '_YYYYMMDDHH24MISS') || '.csv' spoolname from dual; 

spool '&spoolname'; 

COL SORT_ORDER NOPRINT

SELECT '01' SORT_ORDER, 'Report Name        General Stat Monthly' from dual
UNION
SELECT '02' SORT_ORDER, 'Date               ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY') from dual
UNION
SELECT '03' SORT_ORDER, 'Period Covered     ' || TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'MONTH YYYY') from dual
UNION 
SELECT '04' SORT_ORDER, '' from dual;

SELECT 
    '00' SORT_ORDER, 
    'CANCELLED' ||'~'||
    COUNT(*) 
FROM
	CAR_STATUS car_status
WHERE 
    car_status.CAR_STATUS_TYP_CD = 'CANCELLED'
	AND car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
UNION
SELECT
    '01' SORT_ORDER, 
    'COMPLETED' ||'~'||
    COUNT(*) 
FROM
	CAR_STATUS car_status
WHERE 
    car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
	AND car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
UNION
SELECT '020' SORT_ORDER, '' FROM dual
UNION
SELECT '021' SORT_ORDER, 'Agent ID~Consumer - Equifax~Consumer - TransUnion~Commercial - DB~Commercial - Equifax~Simple CorpReg~Full CorpReg~Cancelled~Unable to Validate Business Name~Credit Denied~Terms Not Met: Deposit~Terms Not Met: Arrears~Terms Not Met: Legal Name Correction~Terms Not Met: Prepayment~Terms Not Met: see comments~No Conditions~With Condition - Legal Name Correction Required~With Condition - Lien~With Conditions - see comments~Legal Name Validated~Legal Name Cannot be Validated' FROM dual
UNION
SELECT 
       '03' SORT_ORDER, 
       car_status.CREATE_USER_ID                        ||'~'||
       SUM(CONSUMER_EQUIFAX)                            ||'~'||
       SUM(CONSUMER_TRANS_UNION)                        ||'~'||
       SUM(COMMERCIAL_DB)                               ||'~'||
       SUM(COMMERCIAL_EQUIFAX)                          ||'~'||
       SUM(CORP_SIMPLE)                                 ||'~'||
       SUM(CORP_FULL)                                   ||'~'||
       SUM(  CASE
            WHEN car_status.CAR_STATUS_TYP_CD = 'CANCELLED'
             THEN 1
         ELSE 0
             END )                                      ||'~'||
       SUM( CASE
        WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'BUS_NAME_NOT_VALID' )
        THEN 1
        ELSE 0
         END )                                      ||'~'||
       SUM( CASE
        WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'CRD_DENIED' )
        THEN 1
        ELSE 0
         END )                                      ||'~'||
       SUM( CASE
        WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'TERMS_N_MET_DEP' )
        THEN 1
        ELSE 0
         END )                                      ||'~'||
       SUM( CASE
        WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'TERMS_N_MET_ARR' )
        THEN 1
        ELSE 0
         END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'TERMS_N_MET_LGL_CORR' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'TERMS_N_MET_PRE_PAY' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'OTHER' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'NO_CONDITION' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'WITH_COND_LG_CORR' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'WITH_COND_LIEN' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'WITH_COND_CMTS' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'LEG_NM_VERIFIED' )
		THEN 1
		ELSE 0
	     END )                                      ||'~'||
       SUM( CASE
		WHEN ( car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
                     AND car_result.CAR_RESULT_REASON_TYP_CD = 'LEG_NM_NOT_VERIFIED' )
		THEN 1
		ELSE 0
	     END ) 
FROM car_status car_status
    INNER JOIN car_result car_result
    ON ( car_result.CREDIT_ASSESSMENT_REQUEST_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID
         AND car_result.EFF_STOP_TS = DATE '9999-12-31' )
LEFT OUTER JOIN (SELECT
    crlog.CREDIT_REQUEST_CONTEXT_ID,
    SUM(  CASE 
                WHEN 
                    CREDIT_REPORT_SOURCE_SYS_CD = 'EQUIFAX' AND crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'CONS_CRD_RPT'
            THEN
                1
            ELSE
                0
            END
       ) AS CONSUMER_EQUIFAX,
    SUM(  CASE 
                WHEN 
                    CREDIT_REPORT_SOURCE_SYS_CD = 'TRANSUNION' AND crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'CONS_CRD_RPT'
            THEN
                1
            ELSE
                0
            END
       ) AS CONSUMER_TRANS_UNION,
    SUM(  CASE 
                WHEN 
                    CREDIT_REPORT_SOURCE_SYS_CD = 'DUNN_BRAD' AND crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'BUS_CRD_RPT'
            THEN
                1
            ELSE
                0
            END
       ) AS COMMERCIAL_DB,
    SUM(  CASE 
                WHEN 
                    CREDIT_REPORT_SOURCE_SYS_CD = 'EQUIFAX' AND crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'BUS_CRD_RPT'
            THEN
                1
            ELSE
                0
            END
       )  AS COMMERCIAL_EQUIFAX,
    SUM(  CASE 
                WHEN crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'COPR_SMPL_REGISTRY'
            THEN
                1
            ELSE
                0
            END
       ) AS CORP_SIMPLE,
    SUM(  CASE 
                WHEN crlog.CREDIT_DOC_CATEGORY_TYP_CD = 'CORP_FULL_REGISTRY'
            THEN
                1
            ELSE
                0
            END
       )  AS CORP_FULL
FROM
    CREDIT_REPORT_TRNS_LOG crlog
WHERE CREDIT_DOC_CATEGORY_TYP_CD NOT IN ('BUS_CRD_SMLR_RPT')
AND crlog.CREDIT_REQUEST_CONTEXT_TYP_CD = 'CARID'
GROUP BY crlog.CREDIT_REQUEST_CONTEXT_ID ) crtranslog_summary_by_car_id
ON ( crtranslog_summary_by_car_id.CREDIT_REQUEST_CONTEXT_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID )
WHERE car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') AND TRUNC(SYSDATE,'MM')
      AND car_status.CAR_STATUS_TYP_CD IN ( 'CANCELLED', 'COMPLETED' ) 
      AND car_status.EFF_STOP_TS = DATE '9999-12-31'
      AND car_result.CAR_RESULT_REASON_TYP_CD <> 'SIMPL_ASSMT'
GROUP BY
     car_status.CREATE_USER_ID
UNION
SELECT '040' SORT_ORDER, '' FROM dual
UNION
SELECT 
    '041' SORT_ORDER, 
    'Applications Requesting Multiple Bureau Reports' ||'~'||
    SUM(CASE
            WHEN
                COUNT(credit_request_context_id) > 1
            THEN
                1
            ELSE
                0
        END)
FROM CREDIT_REPORT_TRNS_LOG trans_log
inner join car_status car_status
ON ( trans_log.CREDIT_REQUEST_CONTEXT_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID 
       AND car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') and TRUNC(SYSDATE,'MM')
	   AND car_status.CAR_STATUS_TYP_CD = 'COMPLETED'
	   AND trans_log.CREDIT_REQUEST_CONTEXT_TYP_CD = 'CARID' 
	   AND CREDIT_DOC_CATEGORY_TYP_CD NOT IN ('BUS_CRD_SMLR_RPT') )
GROUP BY credit_request_context_id
HAVING COUNT(credit_request_context_id) > 1
UNION
SELECT '050' SORT_ORDER, '' FROM dual
UNION
SELECT 
    '051' SORT_ORDER, 
    'Auto Evaluated' ||'~'||
    COUNT(*)
FROM car_status car_status
    INNER JOIN car_result car_result
    ON ( car_result.CREDIT_ASSESSMENT_REQUEST_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID
         AND car_result.EFF_STOP_TS = DATE '9999-12-31' )
WHERE car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') AND TRUNC(SYSDATE,'MM')
      AND car_status.CAR_STATUS_TYP_CD IN ( 'CANCELLED', 'COMPLETED' ) 
      AND car_status.EFF_STOP_TS = DATE '9999-12-31'
      AND car_result.CAR_RESULT_REASON_TYP_CD = 'SIMPL_ASSMT'
UNION
SELECT 
    '06' SORT_ORDER, 
    'Manually Evaluated' ||'~'||
    COUNT(*)
FROM car_status car_status
    INNER JOIN car_result car_result
    ON ( car_result.CREDIT_ASSESSMENT_REQUEST_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID
         AND car_result.EFF_STOP_TS = DATE '9999-12-31' )
WHERE car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') AND TRUNC(SYSDATE,'MM')
      AND car_status.CAR_STATUS_TYP_CD IN ( 'CANCELLED', 'COMPLETED' ) 
      AND car_status.EFF_STOP_TS = DATE '9999-12-31'
      AND car_result.CAR_RESULT_REASON_TYP_CD <> 'SIMPL_ASSMT'
UNION
SELECT '070' SORD_ORDER, '' FROM dual
UNION
SELECT '071' SORT_ORDER, '~~~~~~~~~~Results of Assessment' FROM dual
UNION
SELECT '072' SORT_ORDER,  '~Total Number of CARs Manually Evaluated~Reason 1 - Mandatory Assessment Required~Reason 2 - Over Quote Threshold~Reason 3 - Name last verified date~Reason 4 - Delinquency Flag~Reason 5 - Corporate Registry Status~Reason 6 - Legal Name not Valid~Reason 7 - Info updated by Sales~Multiple validation failure~Reason 8 - Legal Name Validation~Reason 9 - Interview Credit Review	Results of Assessment~Cancelled~Completed - Approved~Completed - Denied~Legal Name Validated~Legal Name Cannot be Validated' FROM dual
UNION
SELECT
       '08' SORT_ORDER, 
       'Sample A - Manual Evaluation count'     ||'~'||
       COUNT(*)               ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%MR%' THEN 1 ELSE 0 END )      ||'~'||  
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%QT%' THEN 1 ELSE 0 END )      ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%VT%' THEN 1 ELSE 0 END )        ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%DQ%' THEN 1 ELSE 0 END )         ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%CR%' THEN 1 ELSE 0 END )        ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%LG%' THEN 1 ELSE 0 END )         ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%CP%' THEN 1 ELSE 0 END )        ||'~~'||
	   SUM(CASE WHEN car.CAR_TYP_CD = 'LEG_NM_VALIDATION' THEN 1 ELSE 0 END)          ||'~'||
	   SUM(CASE WHEN car.CAR_TYP_CD = 'INTERNAL_CRD_RVW' THEN 1 ELSE 0 END)                 ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'CANCELLED' THEN 1 ELSE 0 END)               ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'APPROVED' THEN 1 ELSE 0 END) ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'DENIED' THEN 1 ELSE 0 END)    ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'LEG_NM_VERIFIED' THEN 1 ELSE 0 END) ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'LEG_NM_NOT_VERIFIED' THEN 1 ELSE 0 END)
FROM credit_assessment_request car
    INNER JOIN  car_status car_status
	ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID ) 
    INNER JOIN car_result car_result
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_result.CREDIT_ASSESSMENT_REQUEST_ID
         AND car_result.EFF_STOP_TS = DATE '9999-12-31' )
	LEFT OUTER JOIN car_activity activity
   ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = activity.CREDIT_ASSESSMENT_REQUEST_ID
          AND activity.CAR_ACTIVITY_TYPE_ID = 11 )
WHERE car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') AND TRUNC(SYSDATE,'MM')
      AND car_status.CAR_STATUS_TYP_CD IN ( 'CANCELLED', 'COMPLETED' ) 
      AND car_status.EFF_STOP_TS = DATE '9999-12-31'
      AND car_result.CAR_RESULT_REASON_TYP_CD <> 'SIMPL_ASSMT'
UNION
SELECT 
       '09' SORT_ORDER, 
       'Sample B - Reason for Manual Evaluation count'                  ||'~'||
       COUNT(*)                                       ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'MR' THEN 1 ELSE 0 END )    ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'QT' THEN 1 ELSE 0 END )    ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'VT' THEN 1 ELSE 0 END )     ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'DQ' THEN 1 ELSE 0 END )       ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'CR' THEN 1 ELSE 0 END )       ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'LG' THEN 1 ELSE 0 END )      ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD = 'CP' THEN 1 ELSE 0 END )     ||'~'||
       SUM( CASE WHEN activity.CAR_ACTIVITY_REASON_CD LIKE '%,%' THEN 1 ELSE 0 END )   ||'~'||
	   SUM(CASE WHEN car.CAR_TYP_CD = 'LEG_NM_VALIDATION' THEN 1 ELSE 0 END)   ||'~'||
	   SUM(CASE WHEN car.CAR_TYP_CD = 'INTERNAL_CRD_RVW' THEN 1 ELSE 0 END)         ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'CANCELLED' THEN 1 ELSE 0 END)        ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'APPROVED' THEN 1 ELSE 0 END) ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'DENIED' THEN 1 ELSE 0 END) ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'LEG_NM_VERIFIED' THEN 1 ELSE 0 END)  ||'~'||
	   SUM(CASE WHEN car_status.CAR_STATUS_TYP_CD = 'COMPLETED' AND car_result.CAR_RESULT_TYP_CD = 'LEG_NM_NOT_VERIFIED' THEN 1 ELSE 0 END)  
FROM credit_assessment_request car
    INNER JOIN  car_status car_status
	ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_status.CREDIT_ASSESSMENT_REQUEST_ID ) 
    INNER JOIN car_result car_result
    ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = car_result.CREDIT_ASSESSMENT_REQUEST_ID
         AND car_result.EFF_STOP_TS = DATE '9999-12-31' )
	LEFT OUTER JOIN car_activity activity
   ON ( car.CREDIT_ASSESSMENT_REQUEST_ID = activity.CREDIT_ASSESSMENT_REQUEST_ID
          AND activity.CAR_ACTIVITY_TYPE_ID = 11 )
WHERE car_status.EFF_START_TS BETWEEN TRUNC(ADD_MONTHS(SYSDATE,-1),'MM') AND TRUNC(SYSDATE,'MM')
      AND car_status.CAR_STATUS_TYP_CD IN ( 'CANCELLED', 'COMPLETED' ) 
      AND car_status.EFF_STOP_TS = DATE '9999-12-31'
      AND car_result.CAR_RESULT_REASON_TYP_CD <> 'SIMPL_ASSMT';
      
spool off;

exit 0
;
