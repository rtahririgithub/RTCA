set term off;
set echo off;
set verify off;
set heading off;
set pages 0;
set feedback off;
set newpage none;
set linesize 160;
set trimspool on;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1

/* credit_profile_id,customer_id,legal_name_result,proposed_legal_name,corp_registry_status,last_validation_date,jur_country_code,jur_code,quote_threshold,mandatory_assessmt,reg_inc_num,reg_inc_date */

SELECT cp.credit_profile_id ||'~'|| 
cpmap.customer_id ||'~'||
ca1.cprofl_attribute_value ||'~'|| 
ca2.cprofl_attribute_value ||'~'|| 
ca3.cprofl_attribute_value ||'~'||
ca4.cprofl_attribute_value ||'~'||
ca5.cprofl_attribute_value ||'~'||
ca6.cprofl_attribute_value ||'~'||
ca7.cprofl_attribute_value ||'~'||
ca8.cprofl_attribute_value ||'~'||
ci.identification_num ||'~'||
TO_CHAR(ci.identification_start_dt, 'YYYY/MM/DD')  
FROM credit_profile cp
LEFT OUTER JOIN  cprofl_customer_map cpmap ON
    (
        cp.credit_profile_id=cpmap.CREDIT_PROFILE_ID
        AND cpmap.cprofl_cust_map_typ_cd = 'PRI'
        AND cpmap.EFF_STOP_DTM > SYSDATE        
    )
LEFT OUTER JOIN  cprofl_attribute ca1 ON
    (
        cp.credit_profile_id=ca1.CREDIT_PROFILE_ID
        AND ca1.cprofl_attribute_cd = 'LegalNameResult'
        AND ca1.EFF_STOP_DTM > SYSDATE
    ) 
LEFT OUTER JOIN  cprofl_attribute ca2 ON
    (
        cp.credit_profile_id=ca2.CREDIT_PROFILE_ID
        AND ca2.cprofl_attribute_cd = 'ProposedLegalName'
        AND ca2.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN  cprofl_attribute ca3 ON
    (
        cp.credit_profile_id=ca3.CREDIT_PROFILE_ID
        AND ca3.cprofl_attribute_cd='CorpRegistryStatus'
        AND ca3.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN  cprofl_attribute ca4 ON
    (
        cp.credit_profile_id=ca4.CREDIT_PROFILE_ID
        AND ca4.cprofl_attribute_cd='LastValidationDate'
        AND ca4.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN  cprofl_attribute ca5 ON
    (
        cp.credit_profile_id=ca5.CREDIT_PROFILE_ID
        AND ca5.cprofl_attribute_cd='JurisdctionCountryCd'
        AND ca5.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN  cprofl_attribute ca6 ON
    (
        cp.credit_profile_id=ca6.CREDIT_PROFILE_ID
        AND ca6.cprofl_attribute_cd='JurisdictionCode'
        AND ca6.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN  cprofl_attribute ca7 ON
    (
        cp.credit_profile_id=ca7.CREDIT_PROFILE_ID
        AND ca7.cprofl_attribute_cd='QuoteThreshold'
        AND ca7.EFF_STOP_DTM > SYSDATE 
    )
LEFT OUTER JOIN  cprofl_attribute ca8 ON
    (
        cp.credit_profile_id=ca8.CREDIT_PROFILE_ID
        AND ca8.cprofl_attribute_cd='MandatoryAssessInd'
        AND ca8.EFF_STOP_DTM > SYSDATE
    )
LEFT OUTER JOIN cprofl_identification ci ON
    (
        cp.credit_profile_id = ci.CREDIT_PROFILE_ID
        AND (ci.IDENTIFICATION_TYP_CD='INC' OR ci.IDENTIFICATION_TYP_CD='REG') 
        AND ci.EFF_STOP_DTM > SYSDATE           
    );


spool off;
exit 0;
