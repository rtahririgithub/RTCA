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

BEGIN
  ARCHIVE_LOAD( TRUE );
  COMMIT;
END;
/

EXIT
