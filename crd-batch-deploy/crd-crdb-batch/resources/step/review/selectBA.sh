#!/usr/bin/ksh

# Title: selectBA.sh
# Author:Lei Fan

# Desc: This SS shell selects a set of fields of active billing accounts 
#       from the CUSTOMER ODS BILLING ACCOUNT file
#       and filters out any records that its CREDIT_VAL_CD
#       is not equal to 'D' or 'R' and sorts the output by CUSTOMER_ID

syncsort << EOF
/*Loading the data file*/
/INFILE $PROCESSBOX/review/CUSTODS_BILLING_ACCOUNT.dat 3215
/FIELDS BILLING_ACCOUNT_NUMBER  37 en 9
/FIELDS BACCT_STATUS_CD 66 character 1
/FIELDS CUSTOMER_ID 188 en 9
/FIELDS CREDIT_VAL_CD 268 character 1

/DERIVEDFIELD delim "|"

/KEYS CUSTOMER_ID

/CONDITION creditValIsDorR ( CREDIT_VAL_CD = 'D' OR CREDIT_VAL_CD = 'R' ) AND (BACCT_STATUS_CD = 'O')
/INCLUDE creditValIsDorR

/OUTFILE $PROCESSBOX/review/DRCustomerODSBillingAccount.dat OVERWRITE
/REFORMAT BILLING_ACCOUNT_NUMBER, delim, BACCT_STATUS_CD, delim, CUSTOMER_ID, delim, CREDIT_VAL_CD

/WARNINGS 100
/END
EOF

return $?
