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
    UC_WLS_MATCH_ACCOUNT_ID || '|' ||
    LISTAGG( CASE WHEN seqnum <= 10 then
                            (WARNING_CATEGORY_CD || '|' || 
                WARNING_TYPE_CD || '|' || 
                WARNING_CD || '|' || 
                WARNING_STATUS_CD)
                END, '|')
    WITHIN 
        GROUP 
            (ORDER BY LAST_UPDT_TS) AS TOP_MATCH_ACCOUNT
FROM 
    (SELECT uc_warn.*, ROW_NUMBER() OVER (PARTITION BY uc_warn.UC_WLS_MATCH_ACCOUNT_ID ORDER BY uc_warn.LAST_UPDT_TS DESC) as seqnum
      FROM UC_WARNING_HIST uc_warn
WHERE
    uc_warn.LAST_UPDT_TS between 
    CASE WHEN '&4'='1' THEN TO_DATE('&5','YYYYMMDDHH24MISS') ELSE TO_DATE('&2','YYYYMMDDHH24MISS') END
    AND CASE WHEN '&4'='1' THEN TO_DATE('&6','YYYYMMDDHH24MISS') ELSE TO_DATE('&3','YYYYMMDDHH24MISS') END
     ) uc_warn
GROUP BY 
    uc_warn.UC_WLS_MATCH_ACCOUNT_ID
;

spool off;
exit 0;