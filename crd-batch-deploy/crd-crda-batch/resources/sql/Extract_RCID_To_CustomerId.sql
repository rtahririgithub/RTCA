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

/* customer_id,rcid,legal_name, trade_name, legal_entity_type, DUNS */
SELECT cus.CUSTOMER_ID ||'~'||
eid.ENTITY_SRC_KEY_ID ||'~'|| 
na.ORGANIZATION_NM ||'~'||
na.SUPPLMT_NM ||'~'||
cus.CUST_SUBTYPE_CD ||'~'||
eid2.ENTITY_SRC_KEY_ID 
FROM customer cus 
LEFT OUTER JOIN ENTITY_ID_XREF eid on
(
    cus.customer_id = eid.entity_id
    AND eid.ENTITY_SOURCE_KEY_TYPE_CD = 'RCID'
    AND eid.ENTITY_TYPE_CD = 'CUSTOMER'
    AND eid.EFF_END_DT = TO_DATE( '99991231', 'YYYYMMDD')
)
LEFT OUTER JOIN NAME_ASGNMT na ON 
(
    cus.customer_id = na.entity_id 
    AND na.NM_ASGNMT_TYP_CD = 'L'
    AND na.NM_ASGNMT_SUBTYP_CD = 'O'
    AND na.EFF_END_DT = TO_DATE('9999-12-31','YYYY-MM-DD')
    AND na.ENTITY_TYPE_CD = 'CUSTOMER'
)
LEFT OUTER JOIN ENTITY_ID_XREF eid2 ON
(
    cus.customer_id=eid2.entity_id 
    AND eid2.ENTITY_SOURCE_KEY_TYPE_CD = 'DB DUNSID'
    AND eid2.ENTITY_TYPE_CD = 'CUSTOMER'
    AND eid2.EFF_END_DT = TO_DATE( '99991231', 'YYYYMMDD')
);


spool off;
exit 0;
