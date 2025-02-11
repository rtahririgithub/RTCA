--------whenever sqlerror exit SQL.SQLCODE
---------
whenever oserror exit failure

exec PDSCR_LOAD_MAINT (P_ENABLE=>false,P_VLD_LOADED_TBLS=>false,P_TRUNCATE=>true,P_VLD_CHILD_TBLS=>false,P_LOADED_TBLS=>'STG_CREDIT_UPDOWN_CUST,STG_CREDIT_UPDOWN_CUST_BAN');


exit 0;
