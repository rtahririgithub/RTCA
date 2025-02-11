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

CREATE OR REPLACE PROCEDURE check_empty_z_table
(
   p_table_name IN VARCHAR2
)
AS
   v_z_table_name VARCHAR2(100);
   v_z_table_count INTEGER;
BEGIN
   EXECUTE IMMEDIATE 'select SRC_TABLE_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_table_name || '''' INTO v_z_table_name;
   EXECUTE IMMEDIATE 'select count(*) from ' || v_z_table_name INTO v_z_table_count;

   IF v_z_table_count <> 0 THEN
      raise_application_error (-20009, 'Table ' || v_z_table_name || ' must be empty before purging can proceed.');
   END IF;
END check_empty_z_table;
/

-------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE insert_z_table
(
   p_child_table_name IN VARCHAR2,
   p_parent_table_name IN VARCHAR2
)
AS
   v_child_z_table_name VARCHAR2(100);
   v_parent_z_table_name VARCHAR2(100);
   v_parent_pk_name VARCHAR2(100);
BEGIN
   EXECUTE IMMEDIATE 'select SRC_TABLE_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_child_table_name || '''' INTO v_child_z_table_name;
   EXECUTE IMMEDIATE 'select SRC_TABLE_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_parent_table_name || '''' INTO v_parent_z_table_name;
   EXECUTE IMMEDIATE 'select PK_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_parent_table_name || '''' INTO v_parent_pk_name;

   EXECUTE IMMEDIATE ' insert /*+ append */' ||
                     ' into ' || v_child_z_table_name ||
                     ' select /*+ parallel(' || p_child_table_name || ',5) */ *' ||
                     ' from ' || p_child_table_name ||
                     ' where ' || v_parent_pk_name || ' in (select ' || v_parent_pk_name || ' from ' || v_parent_z_table_name || ')';

   COMMIT;
   dbms_output.put_line(p_child_table_name || ' records were inserted.');
END insert_z_table;
/

-------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE purge_table
(
   p_table_name IN VARCHAR2
)
AS
   v_z_table_name VARCHAR2(100);
   v_pk_name VARCHAR2(100);
BEGIN
   EXECUTE IMMEDIATE 'select SRC_TABLE_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_table_name || '''' INTO v_z_table_name;
   EXECUTE IMMEDIATE 'select PK_NM from ARCHIVE_THESE_TABLES where TABLE_NM = ''' || p_table_name || '''' INTO v_pk_name;

   EXECUTE IMMEDIATE ' delete from ' || p_table_name ||
                     ' where ' || v_pk_name || ' in (select ' || v_pk_name || ' from ' || v_z_table_name || ')';

   dbms_output.put_line(p_table_name || ' purged.');
END purge_table;
/

COMMIT;
EXIT
