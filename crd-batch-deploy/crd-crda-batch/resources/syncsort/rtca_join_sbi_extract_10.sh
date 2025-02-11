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
/FIELDS file1_assmntInd 18: - 18:
/FIELDS file1_assmntRsnCd 19: - 19:

/JOINKEYS file1_ucWlsMatchId

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_ucWlsMatchId 1: - 1:
/FIELDS file2_warnCatCd_1 2: - 2:
/FIELDS file2_warnTypCd_1 3: - 3:
/FIELDS file2_warnCd_1 4: - 4:
/FIELDS file2_warnStatCd_1 5: - 5:
/FIELDS file2_warnCatCd_2 6: - 6:
/FIELDS file2_warnTypCd_2 7: - 7:
/FIELDS file2_warnCd_2 8: - 8:
/FIELDS file2_warnStatCd_2 9: - 9:
/FIELDS file2_warnCatCd_3 10: - 10:
/FIELDS file2_warnTypCd_3 11: - 11:
/FIELDS file2_warnCd_3 12: - 12:
/FIELDS file2_warnStatCd_3 13: - 13:
/FIELDS file2_warnCatCd_4 14: - 14:
/FIELDS file2_warnTypCd_4 15: - 15:
/FIELDS file2_warnCd_4 16: - 16:
/FIELDS file2_warnStatCd_4 17: - 17:
/FIELDS file2_warnCatCd_5 18: - 18:
/FIELDS file2_warnTypCd_5 19: - 19:
/FIELDS file2_warnCd_5 20: - 20:
/FIELDS file2_warnStatCd_5 21: - 21:
/FIELDS file2_warnCatCd_6 22: - 22:
/FIELDS file2_warnTypCd_6 23: - 23:
/FIELDS file2_warnCd_6 24: - 24:
/FIELDS file2_warnStatCd_6 25: - 25:
/FIELDS file2_warnCatCd_7 26: - 26:
/FIELDS file2_warnTypCd_7 27: - 27:
/FIELDS file2_warnCd_7 28: - 28:
/FIELDS file2_warnStatCd_7 29: - 29:
/FIELDS file2_warnCatCd_8 30: - 30:
/FIELDS file2_warnTypCd_8 31: - 31:
/FIELDS file2_warnCd_8 32: - 32:
/FIELDS file2_warnStatCd_8 33: - 33:
/FIELDS file2_warnCatCd_9 34: - 34:
/FIELDS file2_warnTypCd_9 35: - 35:
/FIELDS file2_warnCd_9 36: - 36:
/FIELDS file2_warnStatCd_9 37: - 37:
/FIELDS file2_warnCatCd_10 38: - 38:
/FIELDS file2_warnTypCd_10 39: - 39:
/FIELDS file2_warnCd_10 40: - 40:
/FIELDS file2_warnStatCd_10 41: - 41:

/JOINKEYS file2_ucWlsMatchId


/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT file1_carID,file1_lob,file1_brandId,file1_acctId,file1_acctType,file1_acctSubType,file1_acctStatus,file1_statActCd,file1_credClassCd,file1_riskLvlNum,file1_riskLvlDecCd,file1_burDecCd,file1_totalActSub,file1_totalResSub,file1_totalSusSub,file1_delinqInd,file1_assmntInd, file1_assmntRsnCd, rightside:file2_warnCatCd_1,file2_warnTypCd_1,file2_warnCd_1,file2_warnStatCd_1,file2_warnCatCd_2,file2_warnTypCd_2,file2_warnCd_2,file2_warnStatCd_2,file2_warnCatCd_3,file2_warnTypCd_3,file2_warnCd_3,file2_warnStatCd_3,file2_warnCatCd_4,file2_warnTypCd_4,file2_warnCd_4,file2_warnStatCd_4,file2_warnCatCd_5,file2_warnTypCd_5,file2_warnCd_5,file2_warnStatCd_5,file2_warnCatCd_6,file2_warnTypCd_6,file2_warnCd_6,file2_warnStatCd_6,file2_warnCatCd_7,file2_warnTypCd_7,file2_warnCd_7,file2_warnStatCd_7,file2_warnCatCd_8,file2_warnTypCd_8,file2_warnCd_8,file2_warnStatCd_8,file2_warnCatCd_9,file2_warnTypCd_9,file2_warnCd_9,file2_warnStatCd_9,file2_warnCatCd_10,file2_warnTypCd_10,file2_warnCd_10,file2_warnStatCd_10


/STATISTICS
/WARNINGS 100
/END

EOF


return $?