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
/FIELDS file1_custID 1: - 1:
/FIELDS file1_carID 2: - 2:
/FIELDS file1_carTyp 3: - 3:
/FIELDS file1_carSubTyp 4: - 4:
/FIELDS file1_custTypCd 5: - 5:
/FIELDS file1_lobCd 6: - 6:
/FIELDS file1_channelOrgCd 7: - 7:
/FIELDS file1_createTS 8: - 8:
/FIELDS file1_createUserId 9: - 9:
/FIELDS file1_lastUpdateTS 10: - 10:
/FIELDS file1_lastUpdateUserId 11: - 11:
/FIELDS file1_dataSourceID 12: - 12:
/FIELDS file1_effStartTS 13: - 13:
/FIELDS file1_carStatusTypCd 14: - 14:
/FIELDS file1_commentTxt 15: - 15:
/FIELDS file1_carActivityReasonCd 16: - 16:
/FIELDS file1_firstName 17: - 17:
/FIELDS file1_middleName 18: - 18:
/FIELDS file1_lastName 19: - 19:
/FIELDS file1_custCreationDT 20: - 20:
/FIELDS file1_birthDT 21: - 21:
/FIELDS file1_custMasterSrcID 22: - 22:
/FIELDS file1_custStatusCd 23: - 23:
/FIELDS file1_custSubTypCd 24: - 24:
/FIELDS file1_custTypCd1 25: - 25:
/FIELDS file1_langCd 26: - 26:
/FIELDS file1_contactPhoneNum 27: - 27:
/FIELDS file1_creditProfileID 28: - 28:
/FIELDS file1_dlString 29: - 29:
/FIELDS file1_dlProvCd 30: - 30:
/FIELDS file1_sin 31: - 31:
/FIELDS file1_healthCareNum 32: - 32:
/FIELDS file1_healthCareProvCd 33: - 33:
/FIELDS file1_passportString 34: - 34:
/FIELDS file1_passportCountryCd 35: - 35:
/FIELDS file1_provincialIDString 36: - 36:
/FIELDS file1_provincialIDProvCd 37: - 37:
/FIELDS file1_credValCd 38: - 38:
/FIELDS file1_addressLine1 39: - 39:
/FIELDS file1_addressLine2 40: - 40:
/FIELDS file1_addressLine3 41: - 41:
/FIELDS file1_addressLine4 42: - 42:
/FIELDS file1_addressLine5 43: - 43:
/FIELDS file1_cityName 44: - 44:
/FIELDS file1_provCd 45: - 45:
/FIELDS file1_countryCd 46: - 46:
/FIELDS file1_postalCd 47: - 47:
/FIELDS file1_primCredCardTypCd 48: - 48:
/FIELDS file1_secCredCardTypCd 49: - 49:
/FIELDS file1_legalCareCd 50: - 50:
/FIELDS file1_resCd 51: - 51:
/FIELDS file1_employmentStatCd 52: - 52:
/FIELDS file1_creditProfileStatCd 53: - 53:
/FIELDS file1_creditChkConsentCd 54: - 54:
/FIELDS file1_creditProfileMethodCd 55: - 55:
/FIELDS file1_applicationSubProvCd 56: - 56:
/FIELDS file1_provResCd 57: - 57:
/FIELDS file1_bypassMatchInd 58: - 58:
/FIELDS file1_credBureauSimFOInd 59: - 59:
/FIELDS file1_busLastUpdateTS 60: - 60:
/FIELDS file1_firstCarDt 61: - 61:
/FIELDS file1_lastCarDt 62: - 62:
/FIELDS file1_collectionInd 63: - 63:
/FIELDS file1_credValEffDt 64: - 64:
/FIELDS file1_lastCollectionStartDt 65: - 65:
/FIELDS file1_lastCollectionEndDt 66: - 66:
/FIELDS file1_collectionScoreTxt 67: - 67:
/FIELDS file1_accountsInAgencyCount 68: - 68:
/FIELDS file1_latestAgencyAssignmentDt 69: - 69:
/FIELDS file1_balanceOwingInAgencyAmount 70: - 70:
/FIELDS file1_involuntaryCancelledAccountCount 71: - 71:
/FIELDS file1_latestIcaDt 72: - 72:
/FIELDS file1_balanceOwinOnIcaAmount 73: - 73:
/FIELDS file1_NSFChequesCount 74: - 74:
/FIELDS file1_fraudIndIn 75: - 75:
/FIELDS file1_intCreditDcsnTrnID 76: - 76:
/FIELDS file1_assessmentResultCd 77: - 77:
/FIELDS file1_assessmentResultReasonCd 78: - 78:
/FIELDS file1_intCreditDcsnResultID 79: - 79:
/FIELDS file1_assessmentMsgCd 80: - 80:
/FIELDS file1_creditValCd 81: - 81:
/FIELDS file1_fraudIndCdOut 82: - 82:
/FIELDS file1_prodCategoryBoltOn 83: - 83:
/FIELDS file1_cbtCreditBureauTrnID 84: - 84:
/FIELDS file1_cbtFirstName 85: - 85:
/FIELDS file1_cbtMiddleName 86: - 86:
/FIELDS file1_cbtLastName 87: - 87:
/FIELDS file1_cbtTrnErrorCd 88: - 88:
/FIELDS file1_cbtReportSourceCd 89: - 89:
/FIELDS file1_cbtReportTypTxt 90: - 90:
/FIELDS file1_cbtResultCreationDT 91: - 91:
/FIELDS file1_creditBureauTrnResultStatCD 92: - 92:
/FIELDS file1_creditBureauTrnResultStatUpdateDT 93: - 93:
/FIELDS file1_adjCreditScoreTypCd 94: - 94:
/FIELDS file1_adjCreditScoreTxt 95: - 95:
/FIELDS file1_adjCreditClassCd 96: - 96:
/FIELDS file1_adjCreditLimitAmount 97: - 97:
/FIELDS file1_adjDecisionCd 98: - 98:
/FIELDS file1_adjDecisionMsgTxt 99: - 99:
/FIELDS file1_adjDepositAmount 100: - 100:
/FIELDS file1_depLookupStratCd 101: - 101:
/FIELDS file1_cvudStratCd 102: - 102:
/FIELDS file1_creditValStratCd 103: - 103:
/FIELDS file1_customerActStratCd 104: - 104:
/FIELDS file1_tenureStratCd 105: - 105:
/FIELDS file1_collsegment 		106: - 106: 
/FIELDS file1_scorecardId 		107: - 107:
/FIELDS file1_cyclesDelinq 		108: - 108: 
/FIELDS file1_inputRiskLvlNum 	109: - 109:
/FIELDS file1_outputRiskLvlNum 	110: - 110: 
/FIELDS file1_riskCd1 			111: - 111:
/FIELDS file1_riskVal1 			112: - 112: 
/FIELDS file1_riskCd2 			113: - 113:
/FIELDS file1_riskVal2 			114: - 114: 
/FIELDS file1_riskCd3 			115: - 115:
/FIELDS file1_riskVal3 			116: - 116:
/FIELDS file1_riskCd4           117: - 117:
/FIELDS file1_riskVal4          118: - 118:
/FIELDS file1_riskCd5           119: - 119:
/FIELDS file1_riskVal5          120: - 120:
/FIELDS file1_riskCd6           121: - 121:
/FIELDS file1_riskVal6          122: - 122:
/FIELDS file1_riskCd7           123: - 123:
/FIELDS file1_riskVal7          124: - 124:
/FIELDS file1_riskCd8           125: - 125:
/FIELDS file1_riskVal8          126: - 126:
/FIELDS file1_riskCd9           127: - 127:
/FIELDS file1_riskVal9          128: - 128:
/FIELDS file1_riskCd10          129: - 129:
/FIELDS file1_riskVal10		130: - 130:
/FIELDS file1_riskCd11         131: - 131:
/FIELDS file1_riskVal11         132: - 132:
/FIELDS file1_riskCd12          133: - 133:
/FIELDS file1_riskVal12 	134: - 134:

/JOINKEYS file1_carID

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_carID 1: - 1:
/FIELDS file2_fraudTypCd1 2: - 2:
/FIELDS file2_fraudCd1 3: - 3:
/FIELDS file2_fraudMsg1 4: - 4:
/FIELDS file2_fraudTypCd2 5: - 5:
/FIELDS file2_fraudCd2 6: - 6:
/FIELDS file2_fraudMsg2 7: - 7:
/FIELDS file2_fraudTypCd3 8: - 8:
/FIELDS file2_fraudCd3 9: - 9:
/FIELDS file2_fraudMsg3 10: - 10:
/FIELDS file2_fraudTypCd4 11: - 11:
/FIELDS file2_fraudCd4 12: - 12:
/FIELDS file2_fraudMsg4 13: - 13:
/FIELDS file2_fraudTypCd5 14: - 14:
/FIELDS file2_fraudCd5 15: - 15:
/FIELDS file2_fraudMsg5 16: - 16:

/JOINKEYS file2_carID

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_custID,file1_carID,file1_carTyp,file1_carSubTyp,file1_custTypCd,file1_lobCd,file1_channelOrgCd,file1_createTS,file1_createUserId,file1_lastUpdateTS,file1_lastUpdateUserId,file1_dataSourceID,file1_effStartTS,file1_carStatusTypCd,file1_commentTxt,file1_carActivityReasonCd,file1_firstName,file1_middleName,file1_lastName,file1_custCreationDT,file1_birthDT,file1_custMasterSrcID,file1_custStatusCd,file1_custSubTypCd,file1_custTypCd1,file1_langCd,file1_contactPhoneNum,file1_creditProfileID,file1_dlString,file1_dlProvCd,file1_sin,file1_healthCareNum,file1_healthCareProvCd,file1_passportString,file1_passportCountryCd,file1_provincialIDString,file1_provincialIDProvCd,file1_credValCd,file1_addressLine1,file1_addressLine2,file1_addressLine3,file1_addressLine4,file1_addressLine5,file1_cityName,file1_provCd,file1_countryCd,file1_postalCd,file1_primCredCardTypCd,file1_secCredCardTypCd,file1_legalCareCd,file1_resCd,file1_employmentStatCd,file1_creditProfileStatCd,file1_creditChkConsentCd,file1_creditProfileMethodCd,file1_applicationSubProvCd,file1_provResCd,file1_bypassMatchInd,file1_credBureauSimFOInd,file1_busLastUpdateTS,file1_firstCarDt,file1_lastCarDt,file1_collectionInd,file1_credValEffDt,file1_lastCollectionStartDt,file1_lastCollectionEndDt,file1_collectionScoreTxt,file1_accountsInAgencyCount,file1_latestAgencyAssignmentDt,file1_balanceOwingInAgencyAmount,file1_involuntaryCancelledAccountCount,file1_latestIcaDt,file1_balanceOwinOnIcaAmount,file1_NSFChequesCount,file1_fraudIndIn,file1_intCreditDcsnTrnID,file1_assessmentResultCd,file1_assessmentResultReasonCd,file1_intCreditDcsnResultID,file1_assessmentMsgCd,file1_creditValCd,file1_fraudIndCdOut,file1_prodCategoryBoltOn,file1_cbtCreditBureauTrnID,file1_cbtFirstName,file1_cbtMiddleName,file1_cbtLastName,file1_cbtTrnErrorCd,file1_cbtReportSourceCd,file1_cbtReportTypTxt,file1_cbtResultCreationDT,file1_creditBureauTrnResultStatCD,file1_creditBureauTrnResultStatUpdateDT,file1_adjCreditScoreTypCd,file1_adjCreditScoreTxt,file1_adjCreditClassCd,file1_adjCreditLimitAmount,file1_adjDecisionCd,file1_adjDecisionMsgTxt,file1_adjDepositAmount,file1_depLookupStratCd,file1_cvudStratCd,file1_creditValStratCd,file1_customerActStratCd,file1_tenureStratCd,file1_collsegment,file1_scorecardId,file1_cyclesDelinq,file1_inputRiskLvlNum,file1_outputRiskLvlNum,file1_riskCd1,file1_riskVal1,file1_riskCd2,file1_riskVal2,file1_riskCd3,file1_riskVal3,file1_riskCd4,file1_riskVal4,file1_riskCd5,file1_riskVal5,file1_riskCd6,file1_riskVal6,file1_riskCd7,file1_riskVal7,file1_riskCd8,file1_riskVal8,file1_riskCd9,file1_riskVal9,file1_riskCd10,file1_riskVal10,file1_riskCd11,file1_riskVal11,file1_riskCd12,file1_riskVal12,rightside:file2_fraudTypCd1,file2_fraudCd1,file2_fraudMsg1,file2_fraudTypCd2,file2_fraudCd2,file2_fraudMsg2,file2_fraudTypCd3,file2_fraudCd3,file2_fraudMsg3,file2_fraudTypCd4,file2_fraudCd4,file2_fraudMsg4,file2_fraudTypCd5,file2_fraudCd5,file2_fraudMsg5

/STATISTICS
/WARNINGS 100
/END

EOF


return $?