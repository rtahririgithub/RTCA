#!/usr/bin/ksh

# Filename: rtca_pre_cods_billing_7_year.sh
# Function: pre-process the cods billing file.
#           1. the input file size is more than 10 gig, we retrieve the needed
#               part only this will speed up the synsort join.   
#           2. format the required fields for the next join step, for example 
#              the customer id. 
#######################################
if [ $# -eq 3 ]; then
    CUSTODS_REF_BILLINAG_ACCOUNT=$1
    CUSTODS_REF_BILLINAG_ACCOUNT_PROCESSED=$2
    #YEAR=`expr $3 - 7`
    #old_date=$YEAR$4$5   
    # echo " 7 year is:$old_date"
else
    echo ""
    echo "usage:rtca_pre_cods_billing_7_year.sh  [input file1] [output file} " 

    exit 1
fi
if [ -d  $3 ]
then
    echo "dir :$3 is alreadt exist"
else
    mkdir $3
fi

syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTODS_REF_BILLINAG_ACCOUNT 65535
    /FIELDS XBAN 37 en 9
    /FIELDS BAN_STATUS 66 character 1
    /FIELDS XBAN_STATUS_DATE 67 character 19
    /FIELDS XBAN_STATUS_YEAR 67 en 4
    /FIELDS XBAN_STATUS_MONTH 72 en 2
    /FIELDS XBAN_STATUS_DAY   75 en2
    /FIELDS XCUST_ID 188 en 9
    
    /DERIVEDFIELD BAN_STATUS_DATE XBAN_STATUS_DATE EXTRACT /([0-9]+)-([0-9]+)-([0-9]+) / '\1\2\3' EN 
    /DERIVEDFIELD STATUS_DT  BAN_STATUS_DATE +70000  
    /DERIVEDFIELD BAN XBAN EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD CUST_ID XCUST_ID EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)  
    /DERIVEDFIELD TODAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en
    /DERIVEDFIELD delim "|"
    /DERIVEDFIELD count 1 en 10 
    /KEY CUST_ID 
    /SUMMARIZE TOTAL count   
  
    /CONDITION filter ((BAN_STATUS="C") and (STATUS_DT<=TODAY)) 
    /INCLUDE filter 
    /OUTFILE $CUSTODS_REF_BILLINAG_ACCOUNT_PROCESSED OVERWRITE
    /REFORMAT CUST_ID, delim, BAN, delim, BAN_STATUS,delim,STATUS_DT, delim, BAN_STATUS_DATE,delim, TODAY 
    /COLLATINGSEQUENCE DEFAULT ASCII
    /SILENT
    /END
EOF

return $?
