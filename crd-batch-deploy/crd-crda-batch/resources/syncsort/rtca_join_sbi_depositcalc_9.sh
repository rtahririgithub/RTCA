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
/FIELDS stv_reqrentedCount 16: - 16:
/FIELDS stv_reqpurchasedCount 17: - 17:
/FIELDS stv_ownedCount 18: - 18:
/FIELDS stv_rentedCount 19: - 19:
/FIELDS dsl_reqrentedCount 20: - 20:
/FIELDS dsl_reqpurchasedCount 21: - 21:
/FIELDS dsl_ownedCount 22: - 22:
/FIELDS dsl_rentedCount 23: - 23:
/FIELDS ttv_reqrentedCount 24: - 24:
/FIELDS ttv_reqpurchasedCount 25: - 25:
/FIELDS ttv_ownedCount 26: - 26:
/FIELDS ttv_rentedCount 27: - 27:
/FIELDS sl_reqrentedCount 28: - 28:
/FIELDS sl_reqpurchasedCount 29: - 29:
/FIELDS sl_ownedCount 30: - 30:
/FIELDS sl_rentedCount 31: - 31:

/JOINKEY file1_orderDepId

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS depositCalcId 1: - 1:
/FIELDS forborneInd 2: - 2:

/JOINKEY depositCalcId

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_orderDepId,file1_customerId,file1_orderId,file1_decisionCd,file1_totalARDepPaidAmt,file1_lastARDepPaidTS,file1_totalDepReleasedAmtTot,file1_lastARDepARDepReleasedTS,file1_totalARDepPendingAmt,file1_lastARDepPendingTS,file1_DepAdjAmt,file1_DepOnHandAmt,file1_finaldepamtass,file1_CalcRsltMsgCd,file1_CalcRsltReasonCd,stv_reqrentedCount,stv_reqpurchasedCount,stv_ownedCount,stv_rentedCount,dsl_reqrentedCount,dsl_reqpurchasedCount,dsl_ownedCount,dsl_rentedCount,ttv_reqrentedCount,ttv_reqpurchasedCount,ttv_ownedCount,ttv_rentedCount,sl_reqrentedCount,sl_reqpurchasedCount,sl_ownedCount,sl_rentedCount,RIGHTSIDE:forborneInd

/STATISTICS
/WARNINGS 100
/END

EOF


return $?