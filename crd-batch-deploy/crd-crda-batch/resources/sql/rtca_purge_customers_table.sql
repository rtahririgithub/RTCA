--------------------------------------------------------------------------------
-- rtca_purge_customers_table.sql
-- 
-- Description:
--   Creates the filter table that is used by purge. 
--
 
--   Side-effect: Creates the table: temp_purge_customers
--------------------------------------------------------------------------------
set term off;
set echo off;
set verify off;
set feedback off;
set pagesize 0;
set wrap off;
set heading off;
set linesize 67;
set colsep |;
spool off;

---#whenever sqlerror exit SQL.SQLCODE
--------
whenever oserror exit failure

spool &1

--------------------------------------------------------------------------------
begin
   execute immediate 'drop table temp_rtca_purge_customers';
   exception when others then null;
end;
/
create table temp_rtca_purge_customers (
     CUSTOMER_ID            NUMBER(9)    NOT NULL
);
spool off;
exit 0;

