#!/usr/bin/ksh

# Title: sortCustomerId.sh
# Description:sort creditProfileCustomerMappingData file by customer id
#

syncsort << EOF
/*Loading the data file*/
/INFILE $PROCESSBOX/extract/linkedCustomers.dat ","
/FIELDS CREDIT_PROFILE_ID 1: - 1: en
/FIELDS CUSTOMER_ID 2: - 2: en
/KEYS CUSTOMER_ID

/OUTFILE $PROCESSBOX/extract/linkedCustomers2.dat OVERWRITE
/REFORMAT CUSTOMER_ID,CREDIT_PROFILE_ID

/WARNINGS 100
/END
EOF

return $?
