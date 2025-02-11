#!/usr/bin/ksh

# Filename: rtca_pre_cods_billing.sh
# Function: pre-process the cods billing file.
#           1. the input file size is more than 10 gig, we retrieve the needed
#               part only this will speed up the synsort join.   
#           2. format the required fields for the next join step, for example 
#              the customer id. 
#######################################
if [ $# -eq 3 ]; then
    CUSTODS_REF_BILLINAG_ACCOUNT=$1
    CUSTODS_REF_BILLINAG_ACCOUNT_PROCESSED=$2
    CUSTODS_REF_SERVICE_DT=$3

else
    echo ""
    echo "usage:rtca_pre_cods_billing.sh  [input file1] [output file1] [output file2] } " 

    exit 1
fi

syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTODS_REF_BILLINAG_ACCOUNT 65535
    /FIELDS B_SID 19 EN 18
    /FIELDS XBAN 37 en 9
    /FIELDS BAN_STATUS 66 character 1
    /FIELDS XBAN_STATUS_DATE 67 character 19
    /FIELDS BAN_CREATION_YEAR 67 character 4
    /FIELDS BAN_CREATION_MONTH 72 character 2
    /FIELDS BAN_CREATION_DAY 75 character 2
    /FIELDS BAN_CREATION_TIME 77 character 9
    /FIELDS CUSTOMER_ID2 188 character 9 ASCII
    /FIELDS XCUST_ID 188 en 9
    /FIELDS CUSTOMER_TYPE 215 character 1
    /FIELDS START_SERVICE_DATE 1347 character 19
    
    /DERIVEDFIELD BSID B_SID compress
    /DERIVEDFIELD BAN XBAN EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD BAN_CREATION_DATE BAN_CREATION_YEAR + "/" + BAN_CREATION_MONTH + "/" + BAN_CREATION_DAY + BAN_CREATION_TIME
    /DERIVEDFIELD CUST_ID XCUST_ID EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD delim "|"
    /DERIVEDFIELD empty ""    
    /CONDITION statusDateNotNull (XBAN_STATUS_DATE MT /[0-9]+/ )
    /DERIVEDFIELD ST_DT IF statusDateNotNull THEN XBAN_STATUS_DATE ELSE empty 
 
    /KEYS CUST_ID, BAN 
  
    /CONDITION filter ((BSID=1001) and (CUSTOMER_TYPE="R"))
    /INCLUDE filter 
    /OUTFILE $CUSTODS_REF_BILLINAG_ACCOUNT_PROCESSED OVERWRITE
    /REFORMAT CUST_ID, delim, BAN, delim, BAN_STATUS, delim, ST_DT, delim, BSID, delim, CUSTOMER_TYPE 

    /OUTFILE $CUSTODS_REF_SERVICE_DT OVERWRITE
    /REFORMAT CUST_ID, delim, BAN, delim, START_SERVICE_DATE
    /COLLATINGSEQUENCE DEFAULT ASCII
    /SILENT
    /END
EOF

return $?
