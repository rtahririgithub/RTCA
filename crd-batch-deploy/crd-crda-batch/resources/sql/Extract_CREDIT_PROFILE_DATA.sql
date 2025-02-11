set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set LINESIZE 2064;

spool off;

whenever sqlerror exit SQL.SQLCODE
whenever oserror exit failure

spool &1
  select   /*+ ALL_ROWS */  LPAD(CP.CREDIT_PROFILE_ID,18,0)
           ||LPAD(MAP.CUSTOMER_ID,9,0)
           ||RPAD(NVL(cp.CPROFL_STATUS_CD,' '),1) 
           ||RPAD(NVL(cp.CPROFL_FORMAT_CD,' '),2) 
           ||RPAD(NVL(TO_CHAR(cp.BUS_LAST_UPDT_TS ,'YYYY-MM-DD HH24:mm:ss'),' '),21)   
           ||RPAD(NVL(cp.POPULATE_METHOD_CD,' '),3)  
           ||RPAD(NVL(sin_id.IDENTIFICATION_NUM,' '),50)
           ||RPAD(NVL(dl_id.IDENTIFICATION_NUM,' '),50)
           ||RPAD(NVL(dl_id.PROVINCE_CD,' '),2) 
           ||RPAD(NVL(passport_id.IDENTIFICATION_NUM,' '),50)
           ||RPAD(NVL(passport_id.COUNTRY_CD,' '),3) 
           ||RPAD(NVL(hc_id.IDENTIFICATION_NUM,' '),50)
           ||RPAD(NVL(prv_id.IDENTIFICATION_NUM,' '),50)
           ||RPAD(NVL(prv_id.PROVINCE_CD,' '),2)  
           ||RPAD(NVL(address.CPROFL_ADDRESS_TYP_CD,' '),2) 
           ||RPAD(NVL(address.RENDERED_ADDR_LN_1_TXT,' '),50)
           ||RPAD(NVL(address.RENDERED_ADDR_LN_2_TXT,' '),50)
           ||RPAD(NVL(address.RENDERED_ADDR_LN_3_TXT,' '),50)
           ||RPAD(NVL(address.RENDERED_ADDR_LN_4_TXT,' '),50)
           ||RPAD(NVL(address.RENDERED_ADDR_LN_5_TXT,' '),50)
           ||RPAD(NVL(address.MUNIC_NM,' '),40)
           ||RPAD(NVL(address.POSTAL_ZIP_CD_TXT,' '),9)
           ||RPAD(NVL(address.PROVINCE_CD,' '),2)
           ||RPAD(NVL(address.COUNTRY_CD,' '),3)
           ||RPAD(NVL(TO_CHAR(CCI.BIRTH_DT,'YYYY-MM-DD'),' '),10)
           ||RPAD(NVL(CCI.CRED_CHECK_CONSENT_CD,' '),2)
           ||RPAD(NVL(CCI.EMPLOYMENT_STATUS_CD,' '),2)
           ||RPAD(NVL(CCI.RESIDENCY_CD,' '),2)
           ||RPAD(NVL(CCI.PRIM_CRED_CARD_TYP_CD,' '),3)
           ||RPAD(NVL(CCI.SEC_CRED_CARD_ISS_CO_TYP_CD,' '),4)
           ||RPAD(NVL(CCI.LEGAL_CARE_CD,' '),2)
           ||RPAD(NVL(CCI.APPLICATION_SUB_PROV_CD,' '),2)
           ||RPAD(NVL(cci.BYPASS_MATCH_IND, ' '),1)
           ||RPAD(NVL(attr_prov_res.CPROFL_ATTRIBUTE_VALUE,' '),60)
           ||RPAD(NVL(attr_report_ind.CPROFL_ATTRIBUTE_VALUE,' '),60)
           ||RPAD(NVL(attr_cv_efff_date.CPROFL_ATTRIBUTE_VALUE,' '),60)
           ||RPAD(NVL(attr_first_asmt_date.CPROFL_ATTRIBUTE_VALUE,' '),60)
           ||RPAD(NVL(attr_last_asmt_date.CPROFL_ATTRIBUTE_VALUE,' '),60)
           ||RPAD(NVL(CV.CREDIT_VALUE_CD,' '),1)
           ||RPAD(NVL(CV.ASSESSMENT_MSG_CD,' '),30)
           ||RPAD(NVL(cv.PRODUCT_CATEGORY_BOLT_ON, ' '),1)
           ||RPAD(NVL(cv.DECISION_CD,' '),30)
           ||RPAD(NVL(cv.FRAUD_IND_CD,' '),18)
           ||RPAD(NVL(cv.CREDIT_SCORE_NUM,' '),9)
           ||RPAD(NVL(cv.CREDIT_CLASS_CD,' '),2)
           ||LPAD(NVL(cv.CREDIT_ASSESSMENT_REQUEST_ID, '-10000000000000000'),18,0)
           ||RPAD(NVL(replace(replace(cmt.COMMENT_TXT,chr(13),''),chr(10),''),' '),1000) 
           ||LPAD(TO_CHAR(SYSDATE,'YYYYMMDD'),8,0)
        from STG_CREDIT_UPDOWN_CUST mud_cust
        INNER JOIN CPROFL_CUSTOMER_MAP map
        ON ( mud_cust.customer_id = map.customer_id
                and map.CPROFL_CUST_MAP_TYP_CD='PRI'
                   and map.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
            inner join CREDIT_PROFILE cp
            ON ( map.CREDIT_PROFILE_ID = cp.CREDIT_PROFILE_ID
                   and cp.CPROFL_STATUS_CD='A'
                   and cp.CPROFL_FORMAT_CD='P' )
            left outer join cprofl_identification sin_id
            on ( cp.credit_profile_id = sin_id.credit_profile_id
                  and sin_id.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD')
                  and sin_id.IDENTIFICATION_TYP_CD = 'SIN' )
           left outer join cprofl_identification dl_id
            on ( cp.credit_profile_id = dl_id.credit_profile_id
                  and dl_id.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD')
                  and dl_id.IDENTIFICATION_TYP_CD = 'DL' )
           left outer join cprofl_identification passport_id
            on ( cp.credit_profile_id = passport_id.credit_profile_id
                  and passport_id.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD')
                  and passport_id.IDENTIFICATION_TYP_CD = 'PSP' )
           left outer join cprofl_identification hc_id
            on ( cp.credit_profile_id = hc_id.credit_profile_id
                  and hc_id.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD')
                  and hc_id.IDENTIFICATION_TYP_CD = 'HC' )
           left outer join cprofl_identification prv_id
            on ( cp.credit_profile_id = prv_id.credit_profile_id
                  and prv_id.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD')
                  and prv_id.IDENTIFICATION_TYP_CD = 'PRV' )
           left outer join cprofl_address  address
           on ( cp.credit_profile_id = address.credit_profile_id
                  and address.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
           left outer join CPROFL_CHARSTC_INDVDL cci
                  on ( cp.credit_profile_id = cci.credit_profile_id
                  and cci.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
           left outer join CREDIT_MGMNT_COMMENT cmt
            on ( cp.credit_profile_id = cmt.OBJECT_ID
                 and cmt.OBJECT_TYPE_CD = 'CRD_PROFILE' 
                 and cmt.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
           left outer join cprofl_attribute attr_prov_res
           on ( cp.credit_profile_id = attr_prov_res.credit_profile_id  
                  and  attr_prov_res.CPROFL_ATTRIBUTE_CD='CPR'
                  and  attr_prov_res.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
           left outer join  cprofl_attribute attr_report_ind
           on ( cp.credit_profile_id = attr_report_ind.credit_profile_id  
                  and  attr_report_ind.CPROFL_ATTRIBUTE_CD='CRI'
                  and  attr_report_ind.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )
            left outer join  cprofl_attribute attr_cv_efff_date
           on ( cp.credit_profile_id = attr_cv_efff_date.credit_profile_id  
                  and  attr_cv_efff_date.CPROFL_ATTRIBUTE_CD='CVED'
                  and  attr_cv_efff_date.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') )      
            left outer join  cprofl_attribute attr_first_asmt_date
           on ( cp.credit_profile_id = attr_first_asmt_date.credit_profile_id  
                  and  attr_first_asmt_date.CPROFL_ATTRIBUTE_CD='FAD'
                  and  attr_first_asmt_date.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') ) 
           left outer join  cprofl_attribute attr_last_asmt_date
           on ( cp.credit_profile_id = attr_last_asmt_date.credit_profile_id  
                  and  attr_last_asmt_date.CPROFL_ATTRIBUTE_CD='LAD'
                  and  attr_last_asmt_date.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') ) 
           left outer join credit_value cv
           on ( cp.credit_profile_id = cv.credit_profile_id  
                  and  cv.EFF_STOP_DTM = TO_DATE('44441231','YYYYMMDD') ) 
           order by map.customer_id;

spool off;
exit 0;
