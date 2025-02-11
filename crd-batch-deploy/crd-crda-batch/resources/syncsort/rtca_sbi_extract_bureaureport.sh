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
/FIELDS dtlCd 2: - 2:
/FIELDS riskCd 3: - 3:
/FIELDS riskValue 4: - 4:
/FIELDS fraudCd 5: - 5:
/FIELDS fraudTxt 6: - 6:
/FIELDS scoreName 7: - 7:
/FIELDS scoreTxt 8: - 8:
/FIELDS extDocFormatTyp 9: - 9:

/CONDITION isScore(dtlCd = "4" )
/CONDITION hasBureauRpt(extDocFormatTyp = "txt")
/DERIVEDFIELD DERIVED_BUREAURPT IF hasBureauRpt THEN "Y" ELSE "N"

/INCLUDE isScore
/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT carId, DERIVED_BUREAURPT

/STATISTICS
/WARNINGS 100
/END

EOF


return $?

