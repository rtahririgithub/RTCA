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
    deposit.ORDER_DEPOSIT_CALC_TRN_ID ||'|'||   
    odc.FORBORNE_IND ||'|'||
    odc.ORDER_PRD_STATUS_CD ||'|'||    
    odc.SERVICE_TYP_CD ||'|'||    
    odc.RENTED_EQUIPMENT_CNT ||'|'||
    odc.PURCHASED_EQUIPMENT_CNT ||'|'||
    odc.MONTHLY_CHARGE_AMT ||'|'||
    odc.PREVIOUSLY_ASESED_DEPOSIT_AMT ||'|'||
    odc.ASSESSED_DEPOSIT_AMT
FROM 
    ORDER_DEPOSIT_CALC_TRN deposit 
    INNER JOIN ODC_PRODUCT_INSTANCE odc
    ON ( deposit.ORDER_DEPOSIT_CALC_TRN_ID = odc.ORDER_DEPOSIT_CALC_TRN_ID 
        and odc.EFF_STOP_TS = TO_DATE('44441231','YYYYMMDD') )
WHERE 
    deposit.CREATE_TS between TO_DATE('&2','YYYYMMDDHH24MISS') and TO_DATE('&3','YYYYMMDDHH24MISS')
ORDER BY 
    deposit.ORDER_DEPOSIT_CALC_TRN_ID, odc.SERVICE_TYP_CD
;

spool off;
exit 0;
