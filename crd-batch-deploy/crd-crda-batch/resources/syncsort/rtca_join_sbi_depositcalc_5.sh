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
    echo "usage:crda_join_sbi_extract.sh  [input file1] [input file1]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRDA_INPUT_file1 65535 "|"
/JOIN UNPAIRED LEFTSIDE
/FIELDS file1_orderDepId 1: - 1:
/FIELDS file1_customerId 2: - 2:
/FIELDS file1_orderId 3: - 3:
/FIELDS file1_decisionCd 4: - 4:
/FIELDS file1_totalARDepPaidAmt 5: - 5:
/FIELDS file1_lastARDepPaidTS 6: - 6:
/FIELDS file1_totalDepReleasedAmtTot 7: - 7:
/FIELDS file1_lastARDepARDepReleasedTS 8: - 8:
/FIELDS file1_totalARDepPendingAmt 9: - 9:
/FIELDS file1_lastARDepPendingTS 10: - 10:
/FIELDS file1_DepAdjAmt 11: - 11:
/FIELDS file1_DepOnHandAmt 12: - 12:
/FIELDS file1_finaldepamtass 13: - 13:
/FIELDS file1_CalcRsltMsgCd 14: - 14:
/FIELDS file1_CalcRsltReasonCd 15: - 15:

/JOINKEY file1_orderDepId

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS depositCalcId 1: - 1:
/FIELDS stv_reqrentedCount 3: - 3:
/FIELDS stv_reqpurchasedCount 4: - 4:
/FIELDS stv_ownedCount 5: - 5:
/FIELDS stv_rentedCount 6: - 6:

/JOINKEY depositCalcId

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_orderDepId,file1_customerId,file1_orderId,file1_decisionCd,file1_totalARDepPaidAmt,file1_lastARDepPaidTS,file1_totalDepReleasedAmtTot,file1_lastARDepARDepReleasedTS,file1_totalARDepPendingAmt,file1_lastARDepPendingTS,file1_DepAdjAmt,file1_DepOnHandAmt,file1_finaldepamtass,file1_CalcRsltMsgCd,file1_CalcRsltReasonCd,RIGHTSIDE:stv_reqrentedCount,stv_reqpurchasedCount,stv_ownedCount,stv_rentedCount

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
