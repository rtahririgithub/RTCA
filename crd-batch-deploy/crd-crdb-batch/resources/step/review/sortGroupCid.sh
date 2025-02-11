#!/usr/bin/ksh

# Title: sortGroupCid.sh
# Author:Lei Fan
# Description:sort matched customers by customer id
#

syncsort << EOF
/*Loading the data file*/
/INFILE $PROCESSBOX/review/personListWithLn.dat "|"
/FIELDS INDIVIDUAL_NUM 1: - 1: en
/FIELDS CUSTOMER_ID 2: - 2: en
/KEYS CUSTOMER_ID

/OUTFILE $PROCESSBOX/review/groupCustomer.dat OVERWRITE
/REFORMAT INDIVIDUAL_NUM,CUSTOMER_ID

/WARNINGS 100
/END
EOF

return $?
