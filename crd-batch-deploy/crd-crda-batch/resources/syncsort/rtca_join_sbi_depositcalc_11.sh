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
/FIELDS forborneInd 32: - 32:
/FIELDS totalMonthlyChargeAmt 33: - 33:
/FIELDS totalprevAssessedDepAmt 34: - 34:
/FIELDS totalAssessedDepAmt 35: - 35:

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT file1_orderDepId,file1_customerId,file1_orderId,forborneInd,ttv_reqrentedCount,stv_reqrentedCount,dsl_reqrentedCount,ttv_reqpurchasedCount,stv_reqpurchasedCount,dsl_reqpurchasedCount,sl_reqpurchasedCount,stv_ownedCount,dsl_ownedCount,ttv_ownedCount,ttv_rentedCount,stv_rentedCount,dsl_rentedCount,totalMonthlyChargeAmt,totalprevAssessedDepAmt,file1_finaldepamtass,file1_decisionCd,file1_totalARDepPaidAmt,file1_lastARDepPaidTS,file1_totalDepReleasedAmtTot,file1_lastARDepARDepReleasedTS,file1_totalARDepPendingAmt,file1_lastARDepPendingTS,file1_DepAdjAmt,file1_DepOnHandAmt,file1_CalcRsltMsgCd,file1_CalcRsltReasonCd

/STATISTICS
/WARNINGS 100
/END

EOF


return $?