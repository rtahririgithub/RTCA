#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_OUTPUT_FILE=$2
        
else
    echo ""
    echo "usage:crda_join_sbi_extract.sh  [input file1] [input file2]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortmemory}
${sortworkspace}

/INFILE $CRDA_INPUT_FILE1 65535 "|"
/FIELDS carId 1: - 1: en
/FIELDS fraudMsgCd 2: - 2:
/DERIVEDFIELD count 1 en 10 
/KEY carId,fraudMsgCd
/SUMMARIZE TOTAL count  
/CONDITION IS_WITH_FRAUD (fraudMsgCd != "")

/INCLUDE IS_WITH_FRAUD
/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE

/STATISTICS
/WARNINGS 100
/END

EOF


return $?

