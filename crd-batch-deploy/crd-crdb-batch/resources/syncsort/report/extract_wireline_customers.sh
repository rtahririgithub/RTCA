#!/usr/bin/ksh
# Title: extract_wireline_customers.sh
# desc: This script takes two files and joins them by CUSTOMER_ID present in both files 
#       to produce third file with all the records matched.
#
# Validate the number of input parameters
MYDATE="`date +%Y%m%d`"
if [ $# -eq 3 ]; then
    QUALIFIED_CUSTOMERS_FILE=$1
    CREDIT_PROFILE=$2.$MYDATE
    OUTPUT_FILE=$3
else
    echo ""
    echo "Usage: extract_wireline_customers.sh qualified_customers_files credit_profile_extract "
    echo ""
    exit 1
fi
syncsort <<-EOF
${sortstatistic}
${sortworkspace}
/INFILE $QUALIFIED_CUSTOMERS_FILE 23
/FIELDS QC_CUSTOMER_ID 1 character 9
/FIELDS QC_BAN_ID 10 character 10
/JOINKEYS QC_CUSTOMER_ID

/INFILE $CREDIT_PROFILE 52
/FIELDS CPROF_ID 1 en 18 
/FIELDS CPROF_CUSTOMER_ID 19 character 9
/FIELDS CPROF_BTDT 29 character 10
/JOINKEYS CPROF_CUSTOMER_ID

/JOIN UNPAIRED LEFTSIDE

/PADBYTE X"20"

/OUTFILE ${OUTPUT_FILE} OVERWRITE
/REFORMAT LEFTSIDE:QC_CUSTOMER_ID, QC_BAN_ID, RIGHTSIDE:CPROF_ID, CPROF_BTDT
/SILENT
/WARNINGS 100
/END
EOF
return $?