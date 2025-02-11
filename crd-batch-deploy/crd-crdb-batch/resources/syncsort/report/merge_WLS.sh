#!/usr/bin/ksh
# Title: merge_WLS.sh
# desc: This script takes two CSV files and joins them by BAN_ID present in both files 
#       to produce third file with all the records matched.
#
# Validate the number of input parameters
if [ $# -eq 3 ]; then
    QC_CUST=$1
    WLS_CUST=$2
    OUTPUT_FILE=$3
else
    echo ""
    echo "Error INPUT FILES"
    exit 1
fi
syncsort <<-EOF
${sortstatistic}
${sortworkspace}

/INFILE $QC_CUST 23
/FIELDS QC_CUSTOMER_ID 1 character 9
/FIELDS QC_BAN_ID 10 character 10
/FIELDS QC_SRC_ID 20 character 4
/JOINKEYS QC_BAN_ID

/INFILE $WLS_CUST "|"
/FIELDS WLS_BAN_ID 1: - 1:
/FIELDS WLS_DL 2: - 2:
/FIELDS WLS_DOB 3: - 3:
/FIELDS WLS_SSN 4: - 4:
/FIELDS WLS_PROV 5: - 5:
/JOINKEYS WLS_BAN_ID

/DERIVEDFIELD delim "|"

/PADBYTE X"20"

/OUTFILE ${OUTPUT_FILE} OVERWRITE
/REFORMAT LEFTSIDE:QC_CUSTOMER_ID, delim, QC_BAN_ID, delim, RIGHTSIDE:WLS_PROV, WLS_DL, WLS_DOB, WLS_SSN
/SILENT
/WARNINGS 100
/END
EOF

return $?