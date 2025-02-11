#!/usr/bin/ksh

# Filename: rtca_pre_crd_customer.sh
# Function: pre-processed crd customer  file
#           1. retrieve all the  customers with credit values:
#              D, E, G, and R.
#           2. format the customer id for next join
#              step.
#
#######################################
if [ $# -eq 2 ]; then
    CUSTOMER_CREDIT_PROFILE=$1
    CUSTOMER_CREDIT_OUT_FILE=$2

else
    echo ""
    echo "usage: "
    echo "rtca_pre_crd_customer:.sh  [input file] [output file] "

    exit 1
fi


syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTOMER_CREDIT_PROFILE 65535
    /FIELDS CUST_ID1 19 en 9
    /FIELDS CREDIT_VALUE 28 character 1

    /DERIVEDFIELD CUSTOMER_ID CUST_ID1  EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD delim "|"
 
    /KEYS CUSTOMER_ID 

    /CONDITION filter ((CREDIT_VALUE="D") or (CREDIT_VALUE="E") or (CREDIT_VALUE="G") or ( CREDIT_VALUE="R"))
   /INCLUDE filter
 
    /OUTFILE $CUSTOMER_CREDIT_OUT_FILE OVERWRITE
    /REFORMAT CUSTOMER_ID, delim, CREDIT_VALUE     
    /COLLATINGSEQUENCE DEFAULT ASCII

    /SILENT 
EOF

return $?
