#!/usr/bin/ksh

# Title: sortCustomerId.ksh
# Description:sort creditProfileCustomerMappingData file by customer id
#

. /work/users/credcol/creditbatch/resources/job/env.conf

syncsort << EOF
/*Loading the data file*/
/INFILE $1 ","
/FIELDS CREDIT_PROFILE_ID 1: - 1: en
/FIELDS CUSTOMER_ID 2: - 2: en
/KEYS CUSTOMER_ID

/OUTFILE "$commonDataPath/out/linkedCustomers2.dat" OVERWRITE
/REFORMAT CUSTOMER_ID,CREDIT_PROFILE_ID

/WARNINGS 100
/END
EOF

return $?
