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
/FIELDS file1_riskVal10			130: - 130:
/FIELDS file1_riskCd11         131: - 131:
/FIELDS file1_riskVal11         132: - 132:
/FIELDS file1_riskCd12          133: - 133:
/FIELDS file1_riskVal12 		134: - 134:
/FIELDS file1_fraudTypCd1 		135: - 135:
/FIELDS file1_fraudCd1 			136: - 136:
/FIELDS file1_fraudMsg1 		137: - 137:
/FIELDS file1_fraudTypCd2 		138: - 138:
/FIELDS file1_fraudCd2 			139: - 139:
/FIELDS file1_fraudMsg2 		140: - 140:
/FIELDS file1_fraudTypCd3 		141: - 141:	
/FIELDS file1_fraudCd3 			142: - 142:
/FIELDS file1_fraudMsg3 		143: - 143:
/FIELDS file1_fraudTypCd4 		144: - 144:	
/FIELDS file1_fraudCd4 			145: - 145:	
/FIELDS file1_fraudMsg4 		146: - 146:	
/FIELDS file1_fraudTypCd5 		147: - 147:	
/FIELDS file1_fraudCd5 			148: - 148:	
/FIELDS file1_fraudMsg5 		149: - 149:	

/JOINKEYS file1_carID

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_carID 1: - 1:
/FIELDS file2_scorenm1 2: - 2:
/FIELDS file2_scoreval1 3: - 3:
/FIELDS file2_scorenm2 4: - 4:
/FIELDS file2_scoreval2 5: - 5:
/FIELDS file2_scorenm3 6: - 6:
/FIELDS file2_scoreval3 7: - 7:
/FIELDS file2_scorenm4 8: - 8:
/FIELDS file2_scoreval4 9: - 9:
/FIELDS file2_scorenm5 10: - 10:
/FIELDS file2_scoreval5 11: - 11:
/FIELDS file2_scorenm6 12: - 12:
/FIELDS file2_scoreval6 13: - 13:
/FIELDS file2_scorenm7 14: - 14:
/FIELDS file2_scoreval7 15: - 15:
/FIELDS file2_scorenm8 16: - 16:
/FIELDS file2_scoreval8 17: - 17:
/FIELDS file2_scorenm9 18: - 18:
/FIELDS file2_scoreval9 19: - 19:
/FIELDS file2_scorenm10 20: - 20:
/FIELDS file2_scoreval10 21: - 21:
/FIELDS file2_scorenm11 22: - 22:
/FIELDS file2_scoreval11 23: - 23:
/FIELDS file2_scorenm12 24: - 24:
/FIELDS file2_scoreval12 25: - 25:
/FIELDS file2_scorenm13 26: - 26:
/FIELDS file2_scoreval13 27: - 27:
/FIELDS file2_scorenm14 28: - 28:
/FIELDS file2_scoreval14 29: - 29:
/FIELDS file2_scorenm15 30: - 30:
/FIELDS file2_scoreval15 31: - 31:
/FIELDS file2_scorenm16 32: - 32:
/FIELDS file2_scoreval16 33: - 33:
/FIELDS file2_scorenm17 34: - 34:
/FIELDS file2_scoreval17 35: - 35:
/FIELDS file2_scorenm18 36: - 36:
/FIELDS file2_scoreval18 37: - 37:
/FIELDS file2_scorenm19 38: - 38:
/FIELDS file2_scoreval19 39: - 39:
/FIELDS file2_scorenm20 40: - 41:
/FIELDS file2_scoreval20 41: - 41:
/FIELDS file2_scorenm21 42: - 42:
/FIELDS file2_scoreval21 43: - 43:
/FIELDS file2_scorenm22 44: - 44:
/FIELDS file2_scoreval22 45: - 45:
/FIELDS file2_scorenm23 46: - 46:
/FIELDS file2_scoreval23 47: - 47:
/FIELDS file2_scorenm24 48: - 48:
/FIELDS file2_scoreval24 49: - 49:
/FIELDS file2_scorenm25 50: - 50:
/FIELDS file2_scoreval25 51: - 51:
/FIELDS file2_scorenm26 52: - 52:
/FIELDS file2_scoreval26 53: - 53:
/FIELDS file2_scorenm27 54: - 54:
/FIELDS file2_scoreval27 55: - 55:
/FIELDS file2_scorenm28 56: - 56:
/FIELDS file2_scoreval28 57: - 57:
/FIELDS file2_scorenm29 58: - 58:
/FIELDS file2_scoreval29 59: - 59:
/FIELDS file2_scorenm30 60: - 60:
/FIELDS file2_scoreval30 61: - 61:
/FIELDS file2_scorenm31 62: - 62:
/FIELDS file2_scoreval31 63: - 63:
/FIELDS file2_scorenm32 64: - 64:
/FIELDS file2_scoreval32 65: - 65:
/FIELDS file2_scorenm33 66: - 66:
/FIELDS file2_scoreval33 67: - 67:
/FIELDS file2_scorenm34 68: - 68:
/FIELDS file2_scoreval34 69: - 69:
/FIELDS file2_scorenm35 70: - 70:
/FIELDS file2_scoreval35 71: - 71:
/FIELDS file2_scorenm36 72: - 72:
/FIELDS file2_scoreval36 73: - 73:
/FIELDS file2_scorenm37 74: - 74:
/FIELDS file2_scoreval37 75: - 75:
/FIELDS file2_scorenm38 76: - 76:
/FIELDS file2_scoreval38 77: - 77:
/FIELDS file2_scorenm39 78: - 78:
/FIELDS file2_scoreval39 79: - 79:
/FIELDS file2_scorenm40 80: - 80:
/FIELDS file2_scoreval40 81: - 81:
/FIELDS file2_scorenm41 82: - 82:
/FIELDS file2_scoreval41 83: - 83:
/FIELDS file2_scorenm42 84: - 84:
/FIELDS file2_scoreval42 85: - 85:
/FIELDS file2_scorenm43 86: - 86:
/FIELDS file2_scoreval43 87: - 87:
/FIELDS file2_scorenm44 88: - 88:
/FIELDS file2_scoreval44 89: - 89:
/FIELDS file2_scorenm45 90: - 90:
/FIELDS file2_scoreval45 91: - 91:
/FIELDS file2_scorenm46 92: - 92:
/FIELDS file2_scoreval46 93: - 93:
/FIELDS file2_scorenm47 94: - 94:
/FIELDS file2_scoreval47 95: - 95:
/FIELDS file2_scorenm48 96: - 96:
/FIELDS file2_scoreval48 97: - 97:
/FIELDS file2_scorenm49 98: - 98:
/FIELDS file2_scoreval49 99: - 99:
/FIELDS file2_scorenm50 100: - 100:
/FIELDS file2_scoreval50 101: - 101:
/FIELDS file2_scorenm51 102: - 102:
/FIELDS file2_scoreval51 103: - 103:
/FIELDS file2_scorenm52 104: - 104:
/FIELDS file2_scoreval52 105: - 105:
/FIELDS file2_scorenm53 106: - 106:
/FIELDS file2_scoreval53 107: - 107:
/FIELDS file2_scorenm54 108: - 108:
/FIELDS file2_scoreval54 109: - 109:
/FIELDS file2_scorenm55 110: - 110:
/FIELDS file2_scoreval55 111: - 111:
/FIELDS file2_scorenm56 112: - 112:
/FIELDS file2_scoreval56 113: - 113:
/FIELDS file2_scorenm57 114: - 114:
/FIELDS file2_scoreval57 115: - 115:
/FIELDS file2_scorenm58 116: - 116:
/FIELDS file2_scoreval58 117: - 117:
/FIELDS file2_scorenm59 118: - 118:
/FIELDS file2_scoreval59 119: - 119:
/FIELDS file2_scorenm60 120: - 120:
/FIELDS file2_scoreval60 121: - 121:
/FIELDS file2_scorenm61 122: - 122:
/FIELDS file2_scoreval61 123: - 123:
/FIELDS file2_scorenm62 124: - 124:
/FIELDS file2_scoreval62 125: - 125:
/FIELDS file2_scorenm63 126: - 126:
/FIELDS file2_scoreval63 127: - 127:
/FIELDS file2_scorenm64 128: - 128:
/FIELDS file2_scoreval64 129: - 129:
/FIELDS file2_scorenm65 130: - 130:
/FIELDS file2_scoreval65 131: - 131:
/FIELDS file2_scorenm66 132: - 132:
/FIELDS file2_scoreval66 133: - 133:
/FIELDS file2_scorenm67 134: - 134:
/FIELDS file2_scoreval67 135: - 135:
/FIELDS file2_scorenm68 136: - 136:
/FIELDS file2_scoreval68 137: - 137:
/FIELDS file2_scorenm69 138: - 138:
/FIELDS file2_scoreval69 139: - 139:
/FIELDS file2_scorenm70 140: - 140:
/FIELDS file2_scoreval70 141: - 141:
/FIELDS file2_scorenm71 142: - 142:
/FIELDS file2_scoreval71 143: - 143:

/JOINKEYS file2_carID

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_custID,file1_carID,file1_carTyp,file1_carSubTyp,file1_custTypCd,file1_lobCd,file1_channelOrgCd,file1_createTS,file1_createUserId,file1_lastUpdateTS,file1_lastUpdateUserId,file1_dataSourceID,file1_effStartTS,file1_carStatusTypCd,file1_commentTxt,file1_carActivityReasonCd,file1_firstName,file1_middleName,file1_lastName,file1_custCreationDT,file1_birthDT,file1_custMasterSrcID,file1_custStatusCd,file1_custSubTypCd,file1_custTypCd1,file1_langCd,file1_contactPhoneNum,file1_creditProfileID,file1_dlString,file1_dlProvCd,file1_sin,file1_healthCareNum,file1_healthCareProvCd,file1_passportString,file1_passportCountryCd,file1_provincialIDString,file1_provincialIDProvCd,file1_credValCd,file1_addressLine1,file1_addressLine2,file1_addressLine3,file1_addressLine4,file1_addressLine5,file1_cityName,file1_provCd,file1_countryCd,file1_postalCd,file1_primCredCardTypCd,file1_secCredCardTypCd,file1_legalCareCd,file1_resCd,file1_employmentStatCd,file1_creditProfileStatCd,file1_creditChkConsentCd,file1_creditProfileMethodCd,file1_applicationSubProvCd,file1_provResCd,file1_bypassMatchInd,file1_credBureauSimFOInd,file1_busLastUpdateTS,file1_firstCarDt,file1_lastCarDt,file1_collectionInd,file1_credValEffDt,file1_lastCollectionStartDt,file1_lastCollectionEndDt,file1_collectionScoreTxt,file1_accountsInAgencyCount,file1_latestAgencyAssignmentDt,file1_balanceOwingInAgencyAmount,file1_involuntaryCancelledAccountCount,file1_latestIcaDt,file1_balanceOwinOnIcaAmount,file1_NSFChequesCount,file1_fraudIndIn,file1_intCreditDcsnTrnID,file1_assessmentResultCd,file1_assessmentResultReasonCd,file1_intCreditDcsnResultID,file1_assessmentMsgCd,file1_creditValCd,file1_fraudIndCdOut,file1_prodCategoryBoltOn,file1_cbtCreditBureauTrnID,file1_cbtFirstName,file1_cbtMiddleName,file1_cbtLastName,file1_cbtTrnErrorCd,file1_cbtReportSourceCd,file1_cbtReportTypTxt,file1_cbtResultCreationDT,file1_creditBureauTrnResultStatCD,file1_creditBureauTrnResultStatUpdateDT,file1_adjCreditScoreTypCd,file1_adjCreditScoreTxt,file1_adjCreditClassCd,file1_adjCreditLimitAmount,file1_adjDecisionCd,file1_adjDecisionMsgTxt,file1_adjDepositAmount,file1_depLookupStratCd,file1_cvudStratCd,file1_creditValStratCd,file1_customerActStratCd,file1_tenureStratCd,file1_collsegment,file1_scorecardId,file1_cyclesDelinq,file1_inputRiskLvlNum,file1_outputRiskLvlNum,file1_riskCd1,file1_riskVal1,file1_riskCd2,file1_riskVal2,file1_riskCd3,file1_riskVal3,file1_riskCd4,file1_riskVal4,file1_riskCd5,file1_riskVal5,file1_riskCd6,file1_riskVal6,file1_riskCd7,file1_riskVal7,file1_riskCd8,file1_riskVal8,file1_riskCd9,file1_riskVal9,file1_riskCd10,file1_riskVal10,file1_riskCd11,file1_riskVal11,file1_riskCd12,file1_riskVal12,file1_fraudTypCd1,file1_fraudCd1,file1_fraudMsg1,file1_fraudTypCd2,file1_fraudCd2,file1_fraudMsg2,file1_fraudTypCd3,file1_fraudCd3,file1_fraudMsg3,file1_fraudTypCd4,file1_fraudCd4,file1_fraudMsg4,file1_fraudTypCd5,file1_fraudCd5,file1_fraudMsg5,rightside:file2_scorenm1,file2_scoreval1,file2_scorenm2,file2_scoreval2,file2_scorenm3,file2_scoreval3,file2_scorenm4,file2_scoreval4,file2_scorenm5,file2_scoreval5,file2_scorenm6,file2_scoreval6,file2_scorenm7,file2_scoreval7,file2_scorenm8,file2_scoreval8,file2_scorenm9,file2_scoreval9,file2_scorenm10,file2_scoreval10,file2_scorenm11,file2_scoreval11,file2_scorenm12,file2_scoreval12,file2_scorenm13,file2_scoreval13,file2_scorenm14,file2_scoreval14,file2_scorenm15,file2_scoreval15,file2_scorenm16,file2_scoreval16,file2_scorenm17,file2_scoreval17,file2_scorenm18,file2_scoreval18,file2_scorenm19,file2_scoreval19,file2_scorenm20,file2_scoreval20,file2_scorenm21,file2_scoreval21,file2_scorenm22,file2_scoreval22,file2_scorenm23,file2_scoreval23,file2_scorenm24,file2_scoreval24,file2_scorenm25,file2_scoreval25,file2_scorenm26,file2_scoreval26,file2_scorenm27,file2_scoreval27,file2_scorenm28,file2_scoreval28,file2_scorenm29,file2_scoreval29,file2_scorenm30,file2_scoreval30,file2_scorenm31,file2_scoreval31,file2_scorenm32,file2_scoreval32,file2_scorenm33,file2_scoreval33,file2_scorenm34,file2_scoreval34,file2_scorenm35,file2_scoreval35,file2_scorenm36,file2_scoreval36,file2_scorenm37,file2_scoreval37,file2_scorenm38,file2_scoreval38,file2_scorenm39,file2_scoreval39,file2_scorenm40,file2_scoreval40,file2_scorenm41,file2_scoreval41,file2_scorenm42,file2_scoreval42,file2_scorenm43,file2_scoreval43,file2_scorenm44,file2_scoreval44,file2_scorenm45,file2_scoreval45,file2_scorenm46,file2_scoreval46,file2_scorenm47,file2_scoreval47,file2_scorenm48,file2_scoreval48,file2_scorenm49,file2_scoreval49,file2_scorenm50,file2_scoreval50,file2_scorenm51,file2_scoreval51,file2_scorenm52,file2_scoreval52,file2_scorenm53,file2_scoreval53,file2_scorenm54,file2_scoreval54,file2_scorenm55,file2_scoreval55,file2_scorenm56,file2_scoreval56,file2_scorenm57,file2_scoreval57,file2_scorenm58,file2_scoreval58,file2_scorenm59,file2_scoreval59,file2_scorenm60,file2_scoreval60,file2_scorenm61,file2_scoreval61,file2_scorenm62,file2_scoreval62,file2_scorenm63,file2_scoreval63,file2_scorenm64,file2_scoreval64,file2_scorenm65,file2_scoreval65,file2_scorenm66,file2_scoreval66,file2_scorenm67,file2_scoreval67,file2_scorenm68,file2_scoreval68,file2_scorenm69,file2_scoreval69,file2_scorenm70,file2_scoreval70,file2_scorenm71,file2_scoreval71

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
