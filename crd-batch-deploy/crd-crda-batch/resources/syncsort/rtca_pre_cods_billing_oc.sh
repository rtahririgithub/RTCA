#!/usr/bin/ksh

# Filename: rtca_pre_cods_billing_oc.sh 
# Function: pre-process cods customer billing file
#           to retrieve all the needed ban details.
#
#######################################
if [ $# -eq 2 ]; then
    CUSODS_REF_BILLING_ACCOUNT_FILTER=$1
    CUSODS_REF_BILLING_ACCOUNT_OPEN=$2

else
    echo ""
    echo "usage:rtca_pre_cods_billing_active_ban.sh  [input file1] [output file] " 

    exit 1
fi


syncsort << EOF
   /INFILE $CUSODS_REF_BILLING_ACCOUNT_FILTER 65535 "|" 
   /FIELDS CUST_ID 1: - 1: en 
   /FIELDS BAN 2: - 2: en 
   /FIELDS BAN_STATUS 3: - 3: 
   /FIELDS BAN_STATUS_DATE 4: - 4: 
   /FIELDS BSID 5: - 5: 
   /FIELDS CUSTOMER_TYPE 6: - 6: 
 
   /KEYS CUST_ID, BAN 

   /CONDITION filter (BAN_STATUS="O" OR BAN_STATUS="C") 
   /INCLUDE filter 

   /OUTFILE $CUSODS_REF_BILLING_ACCOUNT_OPEN 65535  OVERWRITE
   /REFORMAT CUST_ID, BAN, BAN_STATUS, BAN_STATUS_DATE, BSID, CUSTOMER_TYPE 
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?
