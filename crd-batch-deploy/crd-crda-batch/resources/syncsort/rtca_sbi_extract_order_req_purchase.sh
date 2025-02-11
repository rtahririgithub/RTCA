#!/usr/bin/ksh

# Filename: rtca_sbi_extract_order_req_rent.sh  
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_OUTPUT_FILE=$2
        
else
    echo ""
    echo "usage:rtca_sbi_extract_order_req_rent.sh  [input file1] [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortmemory}
${sortworkspace}

/INFILE $CRDA_INPUT_FILE1 65535 "|"
/FIELDS depositCalcId 1: - 1:
/FIELDS forborneInd 2: - 2:
/FIELDS orderPrdStatCd 3: - 3:
/FIELDS serviceTypCd 4: - 4:
/FIELDS rentedEquipCount 5: - 5:
/FIELDS purchasedEquipCount 6: - 6:EN
/FIELDS monthlyChargeAmt 7: - 7:
/FIELDS prevAssessedDepAmt 8: - 8:
/FIELDS assessedDepAmt 9: - 9:
/KEYS depositCalcId, serviceTypCd, orderPrdStatCd

/CONDITION isRequested(orderPrdStatCd = "C" )

/SUMMARIZE TOTAL purchasedEquipCount

/INCLUDE isRequested
/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT depositCalcId , serviceTypCd , purchasedEquipCount

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
