#!/usr/bin/ksh

# Title: customerODS_billing_account.ksh

# Desc: This SS shell select a set of fields from the CUSTOMER ODS 
#       BILLING ACCOUNT file and filters any records that its CREDIT_VAL_CD
#       is not equal to 'D' or 'R' and sorts the output by CUSTOMER_ID

. ./env.conf

syncsort << EOF
/*Loading the data file*/
/INFILE $1 3215
/FIELDS BILLING_ACCOUNT_NUMBER  37 en 9
/FIELDS BACCT_STATUS_CD 66 character 1
/FIELDS CUSTOMER_ID 188 en 9
/FIELDS CREDIT_VAL_CD 268 character 1

/DERIVEDFIELD delim "|"

/KEYS CUSTOMER_ID

/CONDITION creditValIsDorR ( CREDIT_VAL_CD = 'D' OR CREDIT_VAL_CD = 'R' )
/INCLUDE creditValIsDorR

/OUTFILE "$tmpDir/CustomerODSBillingAccount.dat" OVERWRITE
/REFORMAT BILLING_ACCOUNT_NUMBER, delim, BACCT_STATUS_CD, delim, CUSTOMER_ID, delim, CREDIT_VAL_CD

/WARNINGS 100
/END
EOF

return $?

