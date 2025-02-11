set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set colsep "";
set LINESIZE 100;

spool off;

--------whenever sqlerror exit SQL.SQLCODE
--------
whenever oserror exit failure

spool &1
SELECT staging_ban.CUSTOMER_ID ||'|'||
       op.EXTERNAL_ENTITY_KEY ||'|'||
       op.OBSERVATION_PROFILE_ID ||'|'||
            (CASE
           WHEN (ci.EFFECTIVE_START_DT is not null 
           and ci.EFFECTIVE_END_DT is not null 
           and ci.EFFECTIVE_END_DT = TO_DATE('44441231','YYYYMMDD'))
           THEN 1
           ELSE 0
          END)  ||'|'||
       TO_CHAR(ci.EFFECTIVE_START_DT, 'YYYY-MM-DD HH24:mm:ss')  ||'|'||
       TO_CHAR( ci.EFFECTIVE_END_DT, 'YYYY-MM-DD HH24:mm:ss') ||'|'||
       cs.COLLECTION_STATUS_CODE  ||'|'||
       decode(op.INVOLUNTARY_CEASED_IND, 'Y', 1, '1', 1, 0) ||'|'|| --as INVOLUNTARY_CEASED_IND 
       TO_CHAR(SYSDATE, 'YYYYMMDD')
    FROM observation_profile op
    INNER JOIN STG_CREDIT_UPDOWN_CUST_BAN staging_ban
    on ( op.external_entity_key = staging_ban.BAN )
    LEFT OUTER JOIN COLLECTION_INVOLVEMENT ci
    on ( op.observation_profile_id = ci.observation_profile_id )
    LEFT OUTER JOIN COLLECTION_STATUS cs
    on (op.OBSERVATION_PROFILE_ID = cs.OBSERVATION_PROFILE_ID
         and cs.EFFECTIVE_STOP_DATETIME = TO_DATE('44441231','YYYYMMDD') )    
     where (ci.collection_involvement_id is null
                 or ci.collection_involvement_id = ( select max(collection_involvement_id) 
                                                                   from collection_involvement ci2 
                                                                   where ci2.observation_profile_id = op.observation_profile_id ) )     
     and op.OBSERVATION_PROFILE_TYPE_CODE = 'BACCT'
;

spool off;
exit 0;
