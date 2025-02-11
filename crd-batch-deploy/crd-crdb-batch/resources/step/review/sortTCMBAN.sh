#!/usr/bin/ksh

# Title: sortCustomerId.sh
# Author:Lei Fan
# Description:sort review result file by customer id
#

syncsort << EOF
/*Loading the data file*/
/INFILE $PROCESSBOX/review/TCMReviewResult.dat "|"
/FIELDS CUSTOMER_ID 1: - 1: en
/FIELDS BILLING_ACCOUNT_NUMBER 2: - 2: en
/FIELDS REVIEW_RESULT 3:1 - 3:1

/KEYS BILLING_ACCOUNT_NUMBER

/OUTFILE $PROCESSBOX/review/sortedTCMReviewResult.dat OVERWRITE
/REFORMAT BILLING_ACCOUNT_NUMBER,CUSTOMER_ID,REVIEW_RESULT

/WARNINGS 100
/END
EOF

return $?
