#!/usr/bin/ksh

# Filename: rtca_sbi_extract_order_total.sh  
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_OUTPUT_FILE=$2
        
else
    echo ""
    echo "usage:rtca_sbi_extract_order_total.sh  [input file1] [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortmemory}
${sortworkspace}

/INFILE $CRDA_INPUT_FILE1 65535 "|"
/FIELDS depositCalcId 1: - 1:
/FIELDS monthlyChargeAmt 7: - 7:EN
/FIELDS prevAssessedDepAmt 8: - 8:EN
/FIELDS assessedDepAmt 9: - 9:EN
/KEYS depositCalcId

/SUMMARIZE TOTAL monthlyChargeAmt , TOTAL prevAssessedDepAmt , TOTAL assessedDepAmt 

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT depositCalcId, monthlyChargeAmt , prevAssessedDepAmt , assessedDepAmt 

/STATISTICS
/WARNINGS 100
/END

EOF


return $?