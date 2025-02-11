#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 3 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_INPUT_FILE2=$2
        CRDA_OUTPUT_FILE=$3
else
    echo ""
    echo "usage:rtca_join_sbi_extract_1.sh  [input file1] [input file2] [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRDA_INPUT_FILE1 65535 "|"
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

/JOINKEY file1_carID

/INFILE $CRDA_INPUT_FILE2 65535 "|"
/FIELDS file2_carID 1: - 1:
/FIELDS file2_stgCreditDcsnTranId 2: - 2:
/FIELDS file2_custID 3: - 3:
/FIELDS file2_firstName 4: - 4:
/FIELDS file2_middleName 5: - 5:
/FIELDS file2_lastName 6: - 6:
/FIELDS file2_custCreationDT 7: - 7:
/FIELDS file2_birthDT 8: - 8:
/FIELDS file2_custMasterSrcID 9: - 9:
/FIELDS file2_custStatusCd 10: - 10:
/FIELDS file2_custSubTypCd 11: - 11:
/FIELDS file2_custTypCd1 12: - 12:
/FIELDS file2_langCd 13: - 13:
/FIELDS file2_contactPhoneNum 14: - 14:
/FIELDS file2_creditProfileID 15: - 15:
/FIELDS file2_dlString 16: - 16:
/FIELDS file2_dlProvCd 17: - 17:
/FIELDS file2_sin 18: - 18:
/FIELDS file2_healthCareNum 19: - 19:
/FIELDS file2_healthCareProvCd 20: - 20:
/FIELDS file2_passportString 21: - 21:
/FIELDS file2_passportCountryCd 22: - 22:
/FIELDS file2_provincialIDString 23: - 23:
/FIELDS file2_provincialIDProvCd 24: - 24:
/FIELDS file2_credValCd 25: - 25:
/FIELDS file2_addressLine1 26: - 26:
/FIELDS file2_addressLine2 27: - 27:
/FIELDS file2_addressLine3 28: - 28:
/FIELDS file2_addressLine4 29: - 29:
/FIELDS file2_addressLine5 30: - 30:
/FIELDS file2_cityName 31: - 31:
/FIELDS file2_provCd 32: - 32:
/FIELDS file2_countryCd 33: - 33:
/FIELDS file2_postalCd 34: - 34:
/FIELDS file2_primCredCardTypCd 35: - 35:
/FIELDS file2_secCredCardTypCd 36: - 36:
/FIELDS file2_legalCareCd 37: - 37:
/FIELDS file2_resCd 38: - 38:
/FIELDS file2_employmentStatCd 39: - 39:
/FIELDS file2_creditProfileStatCd 40: - 40:
/FIELDS file2_creditChkConsentCd 41: - 41:
/FIELDS file2_creditProfileMethodCd 42: - 42:
/FIELDS file2_applicationSubProvCd 43: - 43:
/FIELDS file2_provResCd 44: - 44:
/FIELDS file2_bypassMatchInd 45: - 45:
/FIELDS file2_credBureauSimFOInd 46: - 46:
/FIELDS file2_busLastUpdateTS 47: - 47:
/FIELDS file2_firstCarDt 48: - 48:
/FIELDS file2_lastCarDt 49: - 49:
/FIELDS file2_collectionInd 50: - 50:
/FIELDS file2_credValEffDt 51: - 51:
/FIELDS file2_lastCollectionStartDt 52: - 52:
/FIELDS file2_lastCollectionEndDt 53: - 53:
/FIELDS file2_collectionScoreTxt 54: - 54:
/FIELDS file2_accountsInAgencyCount 55: - 55:
/FIELDS file2_latestAgencyAssignmentDt 56: - 56:
/FIELDS file2_balanceOwingInAgencyAmount 57: - 57:
/FIELDS file2_involuntaryCancelledAccountCount 58: - 58:
/FIELDS file2_latestIcaDt 59: - 59:
/FIELDS file2_balanceOwinOnIcaAmount 60: - 60:
/FIELDS file2_NSFChequesCount 61: - 61:
/FIELDS file2_fraudIndIn 62: - 62:
/FIELDS file2_intCreditDcsnTrnID 63: - 63:
/FIELDS file2_assessmentResultCd 64: - 64:
/FIELDS file2_assessmentResultReasonCd 65: - 65:
/FIELDS file2_intCreditDcsnResultID 66: - 66:
/FIELDS file2_assessmentMsgCd 67: - 67:
/FIELDS file2_creditValCd 68: - 68:
/FIELDS file2_fraudIndCdOut 69: - 69:
/FIELDS file2_prodCategoryBoltOn 70: - 70:
/FIELDS file2_cbtCreditBureauTrnID 71: - 71:
/FIELDS file2_cbtFirstName 72: - 72:
/FIELDS file2_cbtMiddleName 73: - 73:
/FIELDS file2_cbtLastName 74: - 74:
/FIELDS file2_cbtTrnErrorCd 75: - 75:
/FIELDS file2_cbtReportSourceCd 76: - 76:
/FIELDS file2_cbtReportTypTxt 77: - 77:
/FIELDS file2_cbtResultCreationDT 78: - 78:
/FIELDS file2_creditBureauTrnResultStatCD 79: - 79:
/FIELDS file2_creditBureauTrnResultStatUpdateDT 80: - 80:
/FIELDS file2_adjCreditScoreTypCd 81: - 81:
/FIELDS file2_adjCreditScoreTxt 82: - 82:
/FIELDS file2_adjCreditClassCd 83: - 83:
/FIELDS file2_adjCreditLimitAmount 84: - 84:
/FIELDS file2_adjDecisionCd 85: - 85:
/FIELDS file2_adjDecisionMsgTxt 86: - 86:
/FIELDS file2_adjDepositAmount 87: - 87:
/FIELDS file2_depLookupStratCd 88: - 88:
/FIELDS file2_cvudStratCd 89: - 89:
/FIELDS file2_creditValStratCd 90: - 90:
/FIELDS file2_customerActStratCd 91: - 91:
/FIELDS file2_tenureStratCd 92: - 92:
/FIELDS file2_collsegment 93: - 93:
/FIELDS file2_scorecardId 94: - 94:
/FIELDS file2_cyclesDelinq 95: - 95:
/FIELDS file2_inputRiskLvlNum 96: - 96: 
/FIELDS file2_outputRiskLvlNum 97: - 97: 

/JOINKEY file2_carID

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside: file1_custID,file1_carID,file1_carTyp,file1_carSubTyp,file1_custTypCd,file1_lobCd,file1_channelOrgCd,file1_createTS,file1_createUserId,file1_lastUpdateTS,file1_lastUpdateUserId,file1_dataSourceID,file1_effStartTS,file1_carStatusTypCd,file1_commentTxt,file1_carActivityReasonCd,rightside:file2_firstName,file2_middleName,file2_lastName,file2_custCreationDT,file2_birthDT,file2_custMasterSrcID,file2_custStatusCd,file2_custSubTypCd,file2_custTypCd1,file2_langCd,file2_contactPhoneNum,file2_creditProfileID,file2_dlString,file2_dlProvCd,file2_sin,file2_healthCareNum,file2_healthCareProvCd,file2_passportString,file2_passportCountryCd,file2_provincialIDString,file2_provincialIDProvCd,file2_credValCd,file2_addressLine1,file2_addressLine2,file2_addressLine3,file2_addressLine4,file2_addressLine5,file2_cityName,file2_provCd,file2_countryCd,file2_postalCd,file2_primCredCardTypCd,file2_secCredCardTypCd,file2_legalCareCd,file2_resCd,file2_employmentStatCd,file2_creditProfileStatCd,file2_creditChkConsentCd,file2_creditProfileMethodCd,file2_applicationSubProvCd,file2_provResCd,file2_bypassMatchInd,file2_credBureauSimFOInd,file2_busLastUpdateTS,file2_firstCarDt,file2_lastCarDt,file2_collectionInd,file2_credValEffDt,file2_lastCollectionStartDt,file2_lastCollectionEndDt,file2_collectionScoreTxt,file2_accountsInAgencyCount,file2_latestAgencyAssignmentDt,file2_balanceOwingInAgencyAmount,file2_involuntaryCancelledAccountCount,file2_latestIcaDt,file2_balanceOwinOnIcaAmount,file2_NSFChequesCount,file2_fraudIndIn,file2_intCreditDcsnTrnID,file2_assessmentResultCd,file2_assessmentResultReasonCd,file2_intCreditDcsnResultID,file2_assessmentMsgCd,file2_creditValCd,file2_fraudIndCdOut,file2_prodCategoryBoltOn,file2_cbtCreditBureauTrnID,file2_cbtFirstName,file2_cbtMiddleName,file2_cbtLastName,file2_cbtTrnErrorCd,file2_cbtReportSourceCd,file2_cbtReportTypTxt,file2_cbtResultCreationDT,file2_creditBureauTrnResultStatCD,file2_creditBureauTrnResultStatUpdateDT,file2_adjCreditScoreTypCd,file2_adjCreditScoreTxt,file2_adjCreditClassCd,file2_adjCreditLimitAmount,file2_adjDecisionCd,file2_adjDecisionMsgTxt,file2_adjDepositAmount,file2_depLookupStratCd,file2_cvudStratCd,file2_creditValStratCd,file2_customerActStratCd,file2_tenureStratCd,file2_collsegment,file2_scorecardId,file2_cyclesDelinq,file2_inputRiskLvlNum,file2_outputRiskLvlNum


/STATISTICS
/WARNINGS 100
/END

EOF

return $?

