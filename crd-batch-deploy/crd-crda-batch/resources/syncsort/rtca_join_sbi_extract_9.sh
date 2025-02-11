#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 3 ]; then
        CRDA_INPUT_file1=$1
        CRDA_INPUT_file2=$2
        CRDA_OUTPUT_FILE=$3
else
    echo ""
    echo "usage:crda_join_sbi_extract.sh  [input file1] [input file2]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRDA_INPUT_file1 65535 "|"
/JOIN UNPAIRED LEFTSIDE
/FIELDS file1_carID 1: - 1:
/FIELDS file1_ucWlsMatchId 2: - 2:
/FIELDS file1_lob 3: - 3:
/FIELDS file1_brandId 4: - 4:
/FIELDS file1_acctId 5: - 5:
/FIELDS file1_acctType 6: - 6:
/FIELDS file1_acctSubType 7: - 7:
/FIELDS file1_acctStatus 8: - 8:
/FIELDS file1_statActCd 9: - 9:
/FIELDS file1_credClassCd 10: - 10:
/FIELDS file1_riskLvlNum 11: - 11:
/FIELDS file1_riskLvlDecCd 12: - 12:
/FIELDS file1_burDecCd 13: - 13:
/FIELDS file1_totalActSub 14: - 14:
/FIELDS file1_totalResSub 15: - 15:
/FIELDS file1_totalSusSub 16: - 16:
/FIELDS file1_delinqInd 17: - 17:

/JOINKEYS file1_carID

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_carId 1: - 1:
/FIELDS file2_assmntInd 2: - 2:
/FIELDS file2_assmntRsnCd 3: - 3:

/JOINKEYS file2_carId


/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT file1_carID,file1_ucWlsMatchId,file1_lob,file1_brandId,file1_acctId,file1_acctType,file1_acctSubType,file1_acctStatus,file1_statActCd,file1_credClassCd,file1_riskLvlNum,file1_riskLvlDecCd,file1_burDecCd,file1_totalActSub,file1_totalResSub,file1_totalSusSub,file1_delinqInd ,rightside:file2_assmntInd, file2_assmntRsnCd


/STATISTICS
/WARNINGS 100
/END

EOF


return $?