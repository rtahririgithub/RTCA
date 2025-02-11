#!/usr/bin/ksh
# Title: join_input_to_customer_data.sh
# desc: This script takes two CSV files and joins them by RCID present in both files 
#       to produce third file with all the records matched.
#
# Validate the number of input parameters
if [ $# -eq 3 ]; then
    CPROF=$1
    CPROF_DL=$2
    OUTPUT_FILE=$3
else
    echo ""
    echo "Error INPUT FILES"
    exit 1
fi
syncsort <<-EOF
${sortstatistic}
${sortworkspace}

/INFILE $CPROF 68
/FIELDS QC_CUSTOMER_ID 1 character 9
/FIELDS QC_BAN_ID 10 character 10
/FIELDS QC_CPROF_ID 20 character 18
/FIELDS BTDT 38 character 10
/JOINKEYS QC_CPROF_ID

/INFILE $CPROF_DL 80
/FIELDS CPROF_ID 1 character 18
/FIELDS CPROF_DL 19 character 52
/FIELDS CPROF_SIN 71 character 9
/JOINKEYS CPROF_ID

/JOIN UNPAIRED LEFTSIDE

/PADBYTE X"20"

/OUTFILE ${OUTPUT_FILE} OVERWRITE
/REFORMAT LEFTSIDE:QC_CUSTOMER_ID, QC_BAN_ID, BTDT, RIGHTSIDE:CPROF_DL,CPROF_SIN 
/SILENT
/WARNINGS 100
/END
EOF

syncsort <<-EOF
${sortstatistic}
${sortworkspace}

/INFILE ${OUTPUT_FILE} 91
/FIELDS CUSTOMER_ID 1 character 9
/FIELDS BAN_ID 10 character 10
/FIELDS BTDT 20 character 10
/FIELDS CPROF_DL 30 character 52
/FIELDS CPROF_SIN 82 character 9

/PADBYTE X"20"

/OUTFILE ${OUTPUT_FILE} OVERWRITE
/REFORMAT CUSTOMER_ID, BAN_ID, CPROF_DL, BTDT, CPROF_SIN 
/SILENT
/WARNINGS 100
/END
EOF

return $?