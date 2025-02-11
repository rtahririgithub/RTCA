#!/usr/bin/ksh

# Filename: rtca_pre_crd_customer.sh
# Function: pre-processed cods customer file
#           retrieve needed fields, and format.
#
#######################################
if [ $# -eq 2 ]; then
    CUSTODS_REF_CUSTOMER=$1
    CUSTODS_REF_CUSTOMER_OUTPUT=$2

else
    echo ""
    echo "usage:rtca_pre_cods_customer.sh [input file1] [output file] " 

    exit 1
fi


syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE  $CUSTODS_REF_CUSTOMER 65535
    /FIELDS CUST_ID1 1 en 9
    /FIELDS CUST_MAST_SRC_ID 10 en 18
    /FIELDS CUST_TYPE_CD 28 character 1
    /FIELDS CUST_STYPE_CD 29 character 4
    /FIELDS CUST_STATUS_CD 33 character 2
    /FIELDS CREATE_DT1 35 character 19
    /FIELDS CREATE_YEAR 35 character 4
    /FIELDS CREATE_MONTH 40 character 2
    /FIELDS CREATE_DAY 43 character 2
    /FIELDS CREATE_TIME 45 character 9
    /FIELDS CUST_TITLE 345 character 10
    /FIELDS FIRST_NM 355 character 60
    /FIELDS MIDDLE_NM 415 character 60
    /FIELDS LAST_NM 475 character 60
    /FIELDS NM_SUFFIX 535 character 10
    /FIELDS EMPLOYEE_ID 1057 character 20
    /FIELDS REVENU_CD 1190 character 50

    /DERIVEDFIELD CUSTOMER_ID CUST_ID1  EXTRACT /([0-9]+)/ '\1' compress
    /DERIVEDFIELD CUST_MASTER_SRC_ID CUST_MAST_SRC_ID compress
    /DERIVEDFIELD CUSTOMER_TITLE CUST_TITLE compress
    /DERIVEDFIELD FIRST_NAME FIRST_NM compress
    /DERIVEDFIELD MIDDLE_NAME MIDDLE_NM compress 
    /DERIVEDFIELD LAST_NAME LAST_NM compress
    /DERIVEDFIELD NAME_SUFFIX NM_SUFFIX compress
    /DERIVEDFIELD CUST_SUB_TYPE_CD CUST_STYPE_CD compress 
    /DERIVEDFIELD REVENU_CD1 REVENU_CD compress
    /DERIVEDFIELD EMPLOYEE_ID1 EMPLOYEE_ID compress
    /DERIVEDFIELD CREATE_DATE CREATE_YEAR + "/" + CREATE_MONTH + "/" + CREATE_DAY + CREATE_TIME

    /DERIVEDFIELD delim "|"
    /DERIVEDFIELD empty ""
    /CONDITION ctDateNotNull (CREATE_DT1 MT /[0-9]+/ )
    /DERIVEDFIELD CT_DT IF ctDateNotNull THEN CREATE_DT1 ELSE empty
 
    /KEYS CUSTOMER_ID 
   
    /OUTFILE $CUSTODS_REF_CUSTOMER_OUTPUT OVERWRITE
    /REFORMAT CUSTOMER_ID, delim, CUST_MASTER_SRC_ID, delim, CUSTOMER_TITLE, delim, FIRST_NAME, delim, MIDDLE_NAME, delim, LAST_NAME, delim, NAME_SUFFIX, delim, CT_DT, delim, CUST_TYPE_CD, delim, CUST_SUB_TYPE_CD, delim, CUST_STATUS_CD, delim, REVENU_CD1, delim, EMPLOYEE_ID1    
    /COLLATINGSEQUENCE DEFAULT ASCII

    /SILENT 
EOF

return $?
