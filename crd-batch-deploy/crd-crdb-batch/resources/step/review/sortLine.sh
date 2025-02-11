#!/usr/bin/ksh

# Title: sortCustomerId.sh
# Author:Lei Fan
# Description:sort creditProfileCustomerMappingData file by customer id
#

syncsort << EOF
/*Loading the data file*/
/INFILE $PROCESSBOX/review/reviewedGroupedDRCustomerODSBillingAccount.dat "|"
/FIELDS BILLING_ACCOUNT_NUMBER 1: - 1: en
/FIELDS BACCT_STATUS_CD 2: - 2: en
/FIELDS CUSTOMER_ID 3: - 3: en
/FIELDS CREDIT_VAL_CD 4: - 4:
/FIELDS INDIVIDUAL_NUM 5: - 5: en
/FIELDS REVIEW_RESULT 6: - 6:

/KEYS INDIVIDUAL_NUM

/OUTFILE $PROCESSBOX/review/sortedReviewedGroupedDRCustomerODSBillingAccount.dat OVERWRITE
/REFORMAT BILLING_ACCOUNT_NUMBER,BACCT_STATUS_CD,CUSTOMER_ID,CREDIT_VAL_CD,INDIVIDUAL_NUM,REVIEW_RESULT

/WARNINGS 100
/END
EOF

return $?
