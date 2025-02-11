#!/usr/bin/ksh

# Title: join_input_to_customer_data.sh
# desc: This script takes two CSV files and joins them by RCID present in both files 
#       to produce third file with all the records matched.
#

# Validate the number of input parameters
if [ $# -eq 3 ]; then
    INPUT_FILE=$1
    RCID_TO_CUSTOMERID_FILE=$2
    OUTPUT_FILE=$3
else
    echo ""
    echo "Usage: join_input_to_customer_data.sh input_file rcid_customerid_file output_file"
    echo ""
    exit 1
fi

# Validate that two input file exist.
if [ -f $INPUT_FILE ] && [ -f $RCID_TO_CUSTOMERID_FILE ]; then
   echo "input file: $INPUT_FILE , rcid to customer id file: $RCID_TO_CUSTOMERID_FILE"
else
    echo ""
    echo "Can't find one or both of input files: $INPUT_FILE or $RCID_TO_CUSTOMERID_FILE"
    echo ""
    exit 1
fi


# Actual SyncSort implementation of the script.
syncsort <<-EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $INPUT_FILE ","
/FIELDS RCID 1: - 1: en
/FIELDS MANDRY_ASSMT 2: - 2:
/FIELDS QUOTE_THRHOLD 3: - 3:
/FIELDS DELINQCY 4: - 4:
/JOINKEYS RCID

/INFILE $RCID_TO_CUSTOMERID_FILE "~"
/FIELDS CUSTOMERID 1: - 1:
/FIELDS RCID2 2: - 2: en
/CONDITION NOT_NULL_RCID RCID2 GT 0
/DERIVEDFIELD DERIVED_RCID IF NOT_NULL_RCID THEN RCID2 ELSE -1
/JOINKEYS DERIVED_RCID

/OUTFILE $OUTPUT_FILE OVERWRITE
/REFORMAT  LEFTSIDE: RCID, RIGHTSIDE: CUSTOMERID, LEFTSIDE: MANDRY_ASSMT, QUOTE_THRHOLD, DELINQCY

/SILENT
/WARNINGS 100
/END
EOF

return $?
