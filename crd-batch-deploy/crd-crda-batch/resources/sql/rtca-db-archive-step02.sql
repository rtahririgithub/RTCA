set echo on
set serveroutput on size 1000000
set feedback on
set wrap on
set linesize 300
set termout on
set pagesize 0
set trimout on
set trimspool on
set timing on

whenever sqlerror exit failure
whenever oserror exit failure

-------------------------------------------------------------------------------
-- check empty z_tables
-------------------------------------------------------------------------------
BEGIN
   -- CREDIT_ASSESSMENT_REQUEST tables
   check_empty_z_table('CAR_ACTIVITY');
   check_empty_z_table('CAR_ACTIVITY_DTL_VAL');
   check_empty_z_table('CAR_ACTIVITY_STATUS');
   check_empty_z_table('CAR_ASSESSMENT_AMT');
   check_empty_z_table('CAR_ATTR_VALUE');
   check_empty_z_table('CAR_BUSINESS_CREDIT_REF');
   check_empty_z_table('CAR_CREDIT_PROFILE');
   check_empty_z_table('CAR_DATE');
   check_empty_z_table('CAR_DTL');
   check_empty_z_table('CAR_ENCRYPTED_ATTR_VAL');
   check_empty_z_table('CAR_ORDER_CONTEXT');
   check_empty_z_table('CAR_ORD_CNTXT_DTL');
   check_empty_z_table('CAR_QUOTATION');
   check_empty_z_table('CAR_RESULT');
   check_empty_z_table('CAR_STATUS');
   check_empty_z_table('CAR_TEAM_MEMBER_INVLVMNT');
   check_empty_z_table('CREDIT_ASSESSMENT_REQUEST');
   check_empty_z_table('CREDIT_BUREAU_TRN');
   check_empty_z_table('CREDIT_BUREAU_TRN_DTL');
   check_empty_z_table('CREDIT_MGMNT_COMMENT');
   check_empty_z_table('CUST_CREDIT_ASSMNT_RQST');
   check_empty_z_table('INT_CRDT_DCSN_TRN');
   check_empty_z_table('INT_CRDT_DCSN_TRN_RSLT');
   check_empty_z_table('INT_CRDT_DCSN_TRN_RSLT_DTL');
   check_empty_z_table('INT_CRDT_DCSN_TRN_STAT');
   check_empty_z_table('STG_CREDIT_DCSN_TRN');

   -- ORDER_DEPOSIT_CALC_TRN tables
   check_empty_z_table('ORDER_DEPOSIT_CALC_TRN');
   check_empty_z_table('ODC_PRODUCT_INSTANCE');
   check_empty_z_table('ODC_PRODUCT_PAY_CHANNEL');
END;
/

-------------------------------------------------------------------------------
-- insert data into z_tables
-------------------------------------------------------------------------------
-- CREDIT_ASSESSMENT_REQUEST tables
insert /*+ append */
into Z_CREDIT_ASSESSMENT_REQUEST
select /*+ parallel(car,5) */ car.*
  from CREDIT_ASSESSMENT_REQUEST car
 where car.CREATE_TS < trunc(sysdate) - interval '4' year
and 
car.credit_assessment_request_id not in
(
	select credit_assessment_request_id from
	(
	select latest_car.credit_assessment_request_id , latest_car.car_count, cbt1.credit_bureau_trn_rslt_stat_cd from 
		(
		select max( ccar.credit_assessment_request_id) as credit_assessment_request_id, count(*) as car_count  from
		cust_credit_assmnt_rqst ccar,  CREDIT_BUREAU_TRN cbt
		where ccar.credit_assessment_request_id = cbt.credit_assessment_request_id 
		and cbt.REPORT_SOURCE_CD in ('EQUIFAX', 'TRANSUNION')
		and cbt.EFF_STOP_TS = to_date('4444-12-31', 'YYYY-MM-DD')
		group by ccar.customer_id
		) latest_car   --select latest CAR that has an effective bureau report  for each and every  customer and also the total number of CARs each customer has
	    inner join credit_bureau_trn cbt1 on
	    cbt1.credit_assessment_request_id = latest_car.credit_assessment_request_id   -- select the bureau transaction result status in addition
	) latest_car_with_report_status
	where credit_bureau_trn_rslt_stat_cd!='2'
	or credit_bureau_trn_rslt_stat_cd = '2' and car_count = 1  
	--select the latest CARs which has an active bureau report or  those which has a void bureau report 
	--but is the only bureau report customer has
)
and not exists -- exclude the car which is attached to an active Credit Value
(
	select cv.CREDIT_ASSESSMENT_REQUEST_ID
	from CREDIT_VALUE cv
	where cv.CREDIT_ASSESSMENT_REQUEST_ID = car.CREDIT_ASSESSMENT_REQUEST_ID
	and cv.EFF_STOP_DTM = to_date('4444-12-31', 'YYYY-MM-DD')
 );


COMMIT;
SELECT 'CREDIT_ASSESSMENT_REQUEST Records are inserted.' FROM dual;


insert /*+ append */
into Z_CREDIT_MGMNT_COMMENT
select /*+ parallel(CREDIT_MGMNT_COMMENT,5) */ *
  from CREDIT_MGMNT_COMMENT
where OBJECT_TYPE_CD = 'CRD_ASMNT_REQUEST'
  and OBJECT_ID in (select CREDIT_ASSESSMENT_REQUEST_ID from Z_CREDIT_ASSESSMENT_REQUEST);

COMMIT;
SELECT 'CREDIT_MGMNT_COMMENT Records are inserted.' FROM dual;


insert /*+ append */
into Z_ORDER_DEPOSIT_CALC_TRN
select /*+ parallel(ORDER_DEPOSIT_CALC_TRN,5) */ odct.*
  from ORDER_DEPOSIT_CALC_TRN odct,
       CPROFL_CUSTOMER_MAP ccm,
       CREDIT_VALUE cv
 where odct.CREATE_TS < trunc(sysdate) - interval '6' month
   and ccm.CUSTOMER_ID = odct.CUSTOMER_ID
   and ccm.CPROFL_CUST_MAP_TYP_CD = 'PRI'
   and ccm.EFF_STOP_DTM = to_date('4444-12-31', 'YYYY-MM-DD')
   and cv.CREDIT_PROFILE_ID = ccm.CREDIT_PROFILE_ID
   and cv.CREDIT_VALUE_CD != 'D'
   and cv.EFF_STOP_DTM = to_date('4444-12-31', 'YYYY-MM-DD');

insert /*+ append */
into Z_ORDER_DEPOSIT_CALC_TRN
select /*+ parallel(ORDER_DEPOSIT_CALC_TRN,5) */ odct.*
  from ORDER_DEPOSIT_CALC_TRN odct,
       CPROFL_CUSTOMER_MAP ccm,
       TEMP_RTCA_PURGE_CUSTOMERS cust
where odct.CREATE_TS < trunc(sysdate) - interval '6' month
   and odct.CUSTOMER_ID = ccm.CUSTOMER_ID
   and ccm.CPROFL_CUST_MAP_TYP_CD = 'PRI'
   and ccm.EFF_STOP_DTM = to_date('4444-12-31', 'YYYY-MM-DD')
   and ccm.CUSTOMER_ID = cust.CUSTOMER_ID
   
COMMIT;
SELECT 'ORDER_DEPOSIT_CALC_TRN Records are inserted.' FROM dual;


DECLARE
BEGIN
   -- CREDIT_ASSESSMENT_REQUEST tables
   insert_z_table('CAR_ACTIVITY',               'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_ACTIVITY_DTL_VAL',       'CAR_ACTIVITY');
   insert_z_table('CAR_ACTIVITY_STATUS',        'CAR_ACTIVITY');
   insert_z_table('CAR_ASSESSMENT_AMT',         'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_ATTR_VALUE',             'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_BUSINESS_CREDIT_REF',    'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_CREDIT_PROFILE',         'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_DATE',                   'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_DTL',                    'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_ENCRYPTED_ATTR_VAL',     'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_ORDER_CONTEXT',          'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_ORD_CNTXT_DTL',          'CAR_ORDER_CONTEXT');
   insert_z_table('CAR_QUOTATION',              'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_RESULT',                 'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_STATUS',                 'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CAR_TEAM_MEMBER_INVLVMNT',   'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CREDIT_BUREAU_TRN',          'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('CREDIT_BUREAU_TRN_DTL',      'CREDIT_BUREAU_TRN');
   insert_z_table('CUST_CREDIT_ASSMNT_RQST',    'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('INT_CRDT_DCSN_TRN',          'CREDIT_ASSESSMENT_REQUEST');
   insert_z_table('INT_CRDT_DCSN_TRN_RSLT',     'INT_CRDT_DCSN_TRN');
   insert_z_table('INT_CRDT_DCSN_TRN_RSLT_DTL', 'INT_CRDT_DCSN_TRN_RSLT');
   insert_z_table('INT_CRDT_DCSN_TRN_STAT',     'INT_CRDT_DCSN_TRN');
   insert_z_table('STG_CREDIT_DCSN_TRN',        'CREDIT_ASSESSMENT_REQUEST');

   -- ORDER_DEPOSIT_CALC_TRN tables
   insert_z_table('ODC_PRODUCT_INSTANCE',       'ORDER_DEPOSIT_CALC_TRN');
   insert_z_table('ODC_PRODUCT_PAY_CHANNEL',    'ODC_PRODUCT_INSTANCE');
END;
/

-------------------------------------------------------------------------------
-- delete data from main tables
-------------------------------------------------------------------------------
DECLARE
BEGIN
   -- CREDIT_ASSESSMENT_REQUEST tables
   purge_table('CAR_ACTIVITY_DTL_VAL');
   purge_table('CAR_ACTIVITY_STATUS');
   purge_table('CAR_ACTIVITY');
   purge_table('CAR_ASSESSMENT_AMT');
   purge_table('CAR_ATTR_VALUE');
   purge_table('CAR_BUSINESS_CREDIT_REF');
   purge_table('CAR_CREDIT_PROFILE');
   purge_table('CAR_DATE');
   purge_table('CAR_DTL');
   purge_table('CAR_ENCRYPTED_ATTR_VAL');
   purge_table('CAR_ORD_CNTXT_DTL');
   purge_table('CAR_ORDER_CONTEXT');
   purge_table('CAR_QUOTATION');
   purge_table('CAR_RESULT');
   purge_table('CAR_STATUS');
   purge_table('CAR_TEAM_MEMBER_INVLVMNT');
   purge_table('INT_CRDT_DCSN_TRN_RSLT_DTL');
   purge_table('INT_CRDT_DCSN_TRN_RSLT');
   purge_table('INT_CRDT_DCSN_TRN_STAT');
   purge_table('INT_CRDT_DCSN_TRN');
   purge_table('CREDIT_BUREAU_TRN_DTL');
   purge_table('CREDIT_BUREAU_TRN');
   purge_table('CREDIT_MGMNT_COMMENT');
   purge_table('CUST_CREDIT_ASSMNT_RQST');
   purge_table('STG_CREDIT_DCSN_TRN');
   purge_table('CREDIT_ASSESSMENT_REQUEST');

   -- ORDER_DEPOSIT_CALC_TRN tables
   purge_table('ODC_PRODUCT_PAY_CHANNEL');
   purge_table('ODC_PRODUCT_INSTANCE');
   purge_table('ORDER_DEPOSIT_CALC_TRN');
   COMMIT;
END;
/

EXIT
