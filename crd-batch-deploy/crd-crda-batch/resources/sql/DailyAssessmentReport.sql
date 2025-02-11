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

SELECT  customer_map.customer_id                                      || '|' || 
       int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID       || '|' ||
       TO_CHAR(car.create_ts, 'YYYY-MM-DD HH24:mm:ss')               || '|' ||
       car.car_typ_cd                                                || '|' ||
       car.CAR_SUBTYPE_CD                                            || '|' ||
       int_crd_decision_trn_result.ASSESSMENT_MSG_CD  || '|' ||
       int_crd_decision_trn_result.CREDIT_VALUE_CD   || '|' ||
        int_crd_decision_trn_result.FRAUD_IND_CD       || '|' ||
       credit_value.credit_value_cd                   || '|' ||
        credit_value.fraud_ind_cd                       || '|' ||
        c_credit_value.CAR_ATTR_VALUE_TXT   || '|' ||
        n_credit_value.CAR_ATTR_VALUE_TXT  || '|' ||
         c_fraud_cd.CAR_ATTR_VALUE_TXT          || '|' ||
        n_fraud_cd.CAR_ATTR_VALUE_TXT               || '|' ||
         c_consent_cd.CAR_ATTR_VALUE_TXT      || '|' ||
        n_consent_cd.CAR_ATTR_VALUE_TXT 
    FROM CREDIT_ASSESSMENT_REQUEST car
    INNER JOIN CAR_CREDIT_PROFILE car_credit_profile
    on ( car.credit_assessment_request_id = car_credit_profile.credit_assessment_request_id)
    INNER JOIN INT_CRDT_DCSN_TRN int_crd_decision_trn
    on ( car_credit_profile.credit_assessment_request_id = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID 
           and car_credit_profile.eff_stop_ts = DATE '4444-12-31' )
    inner join cprofl_customer_map customer_map
     on ( customer_map.credit_profile_id = car_credit_profile.credit_profile_id 
            and customer_map.eff_stop_dtm = DATE '4444-12-31'
           and customer_map.CPROFL_CUST_MAP_TYP_CD  = 'PRI' )
    INNER JOIN INT_CRDT_DCSN_TRN_STAT int_crd_decision_trn_stat
    ON ( int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn_stat.INT_CRDT_DSCN_TRN_ID 
         AND int_crd_decision_trn_stat.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') 
     AND int_crd_decision_trn_stat.CREDIT_DSCN_TRN_STAT_CD = '2' )
    LEFT OUTER JOIN INT_CRDT_DCSN_TRN_RSLT int_crd_decision_trn_result
    ON ( int_crd_decision_trn.INT_CRDT_DSCN_TRN_ID = int_crd_decision_trn_result.INT_CRDT_DSCN_TRN_ID 
         AND int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
     LEFT OUTER JOIN CREDIT_VALUE credit_value
     on (  int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = credit_value.CREDIT_ASSESSMENT_REQUEST_ID )
     LEFT OUTER JOIN CAR_ATTR_VALUE c_credit_value
     on (  c_credit_value.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
              AND c_credit_value.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND c_credit_value.CAR_ATTR_TYP_CD = 'cCreditValCd' )
     LEFT OUTER JOIN CAR_ATTR_VALUE c_fraud_cd
     on (  c_fraud_cd.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
             AND  c_fraud_cd.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND c_fraud_cd.CAR_ATTR_TYP_CD = 'cFraudCd' )
     LEFT OUTER JOIN CAR_ATTR_VALUE c_consent_cd
     on (  c_consent_cd.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
              AND c_consent_cd.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND c_consent_cd.CAR_ATTR_TYP_CD = 'cConsentCd' )
      LEFT OUTER JOIN CAR_ATTR_VALUE n_credit_value
     on (  n_credit_value.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
              AND n_credit_value.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND n_credit_value.CAR_ATTR_TYP_CD = 'nCreditValCd' )
     LEFT OUTER JOIN CAR_ATTR_VALUE n_fraud_cd
     on (  n_fraud_cd.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
             AND  n_fraud_cd.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND n_fraud_cd.CAR_ATTR_TYP_CD = 'nFraudCd' )
     LEFT OUTER JOIN CAR_ATTR_VALUE n_consent_cd
     on (  n_consent_cd.CREDIT_ASSESSMENT_REQUEST_ID = int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID
              AND n_consent_cd.EFF_STOP_TS  = TO_DATE('44441231','YYYYMMDD') 
             AND n_consent_cd.CAR_ATTR_TYP_CD = 'nConsentCd' )
    WHERE int_crd_decision_trn.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD')
      AND int_crd_decision_trn.ASSESSMENT_RSLT_CD='SUCCESS' AND int_crd_decision_trn.ASSESSMENT_RSLT_REASON_CD IS NULL
          --AND int_crd_decision_trn.CREDIT_ASSESSMENT_REQUEST_ID = 457
          AND car.create_ts BETWEEN TO_DATE('&2','YYYYMMDD') AND TO_DATE('&3','YYYYMMDD')
          order by customer_map.customer_id,car.create_ts asc;
         	  
	  spool off;
	  exit 0
;
