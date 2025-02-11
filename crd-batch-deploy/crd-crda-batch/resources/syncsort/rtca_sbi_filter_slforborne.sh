#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_file1=$1
        CRDA_OUTPUT_FILE=$2
        
else
    echo ""
    echo "usage:crda_join_sbi_extract.sh  [input file1] [input file1]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRDA_INPUT_file1 65535 "|"
/FIELDS file1_orderDepId 1: - 1:
/FIELDS file1_forborneInd 2: - 2:
/DERIVEDFIELD count 1 en 10 
/KEY file1_orderDepId

/SUMMARIZE TOTAL count  

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT file1_orderDepId,file1_forborneInd

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
