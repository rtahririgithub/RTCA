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
/FIELDS carId 1: - 1:
/FIELDS intCreditDcsnTranId 2: - 2:
/FIELDS intCreditDcsnTranRsltId 3: - 3:
/FIELDS credValCd 4: - 4:
/FIELDS decisionCd 5: - 5:
/FIELDS prodCategoryBoltOn 6: - 6:
/FIELDS assessmentMsgCd 7: - 7:
/FIELDS intCrdtDcsnTrnRsltDetailId 8: - 8:
/FIELDS intCrdtDcsnTrnRsltDetailCd 9: - 9:
/FIELDS fraudMsgCd 10: - 10:
/FIELDS prodQualInd 11: - 11:
/FIELDS credApprvdProdCatCd 12: - 12:

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT carId,intCrdtDcsnTrnRsltDetailCd

/STATISTICS
/WARNINGS 100
/END

EOF


return $?

