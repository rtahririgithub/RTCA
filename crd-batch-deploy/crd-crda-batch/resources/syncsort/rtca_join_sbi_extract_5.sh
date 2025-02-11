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
/FIELDS file1_scorenm1 			150: - 150:
/FIELDS file1_scoreval1         151: - 151:
/FIELDS file1_scorenm2          152: - 152:
/FIELDS file1_scoreval2         153: - 153:
/FIELDS file1_scorenm3          154: - 154:
/FIELDS file1_scoreval3         155: - 155:
/FIELDS file1_scorenm4          156: - 156:
/FIELDS file1_scoreval4         157: - 157:
/FIELDS file1_scorenm5          158: - 158:
/FIELDS file1_scoreval5         159: - 159:
/FIELDS file1_scorenm6          160: - 160:
/FIELDS file1_scoreval6         161: - 161:
/FIELDS file1_scorenm7          162: - 162:
/FIELDS file1_scoreval7         163: - 163:
/FIELDS file1_scorenm8          164: - 164:
/FIELDS file1_scoreval8			165: - 165:
/FIELDS file1_scorenm9          166: - 166:
/FIELDS file1_scoreval9         167: - 167:
/FIELDS file1_scorenm10         168: - 168:
/FIELDS file1_scoreval10        169: - 169:
/FIELDS file1_scorenm11         170: - 170:
/FIELDS file1_scoreval11        171: - 171:
/FIELDS file1_scorenm12         172: - 172:
/FIELDS file1_scoreval12        173: - 173:
/FIELDS file1_scorenm13         174: - 174:
/FIELDS file1_scoreval13        175: - 175:
/FIELDS file1_scorenm14         176: - 176:
/FIELDS file1_scoreval14        177: - 177:
/FIELDS file1_scorenm15         178: - 178:
/FIELDS file1_scoreval15        179: - 179:
/FIELDS file1_scorenm16         180: - 180:
/FIELDS file1_scoreval16        181: - 181:
/FIELDS file1_scorenm17         182: - 182:
/FIELDS file1_scoreval17        183: - 183:
/FIELDS file1_scorenm18         184: - 184:
/FIELDS file1_scoreval18        185: - 185:
/FIELDS file1_scorenm19         186: - 186:
/FIELDS file1_scoreval19        187: - 187:
/FIELDS file1_scorenm20         188: - 188:
/FIELDS file1_scoreval20        189: - 189:
/FIELDS file1_scorenm21         190: - 190:
/FIELDS file1_scoreval21        191: - 191:
/FIELDS file1_scorenm22         192: - 192:
/FIELDS file1_scoreval22        193: - 193:
/FIELDS file1_scorenm23         194: - 194:
/FIELDS file1_scoreval23        195: - 195:
/FIELDS file1_scorenm24         196: - 196:
/FIELDS file1_scoreval24        197: - 197:
/FIELDS file1_scorenm25         198: - 198:
/FIELDS file1_scoreval25        199: - 199:
/FIELDS file1_scorenm26         200: - 200:
/FIELDS file1_scoreval26        201: - 201:
/FIELDS file1_scorenm27         202: - 202:
/FIELDS file1_scoreval27        203: - 203:
/FIELDS file1_scorenm28         204: - 204:
/FIELDS file1_scoreval28        205: - 205:
/FIELDS file1_scorenm29         206: - 206:
/FIELDS file1_scoreval29        207: - 207:
/FIELDS file1_scorenm30         208: - 208:
/FIELDS file1_scoreval30        209: - 209:
/FIELDS file1_scorenm31         210: - 210:
/FIELDS file1_scoreval31        211: - 211:
/FIELDS file1_scorenm32         212: - 212:
/FIELDS file1_scoreval32        213: - 213:
/FIELDS file1_scorenm33         214: - 214:
/FIELDS file1_scoreval33        215: - 215:
/FIELDS file1_scorenm34         216: - 216:
/FIELDS file1_scoreval34        217: - 217:
/FIELDS file1_scorenm35         218: - 218:
/FIELDS file1_scoreval35        219: - 219:
/FIELDS file1_scorenm36         220: - 220:
/FIELDS file1_scoreval36        221: - 221:
/FIELDS file1_scorenm37         222: - 222:
/FIELDS file1_scoreval37        223: - 223:
/FIELDS file1_scorenm38         224: - 224:
/FIELDS file1_scoreval38        225: - 225:
/FIELDS file1_scorenm39         226: - 226:
/FIELDS file1_scoreval39        227: - 227:
/FIELDS file1_scorenm40         228: - 228:
/FIELDS file1_scoreval40        229: - 229:
/FIELDS file1_scorenm41         230: - 230:
/FIELDS file1_scoreval41        231: - 231:
/FIELDS file1_scorenm42         232: - 232:
/FIELDS file1_scoreval42        233: - 233:
/FIELDS file1_scorenm43         234: - 234:
/FIELDS file1_scoreval43        235: - 235:
/FIELDS file1_scorenm44         236: - 236:
/FIELDS file1_scoreval44        237: - 237:
/FIELDS file1_scorenm45         238: - 238:
/FIELDS file1_scoreval45        239: - 239:
/FIELDS file1_scorenm46         240: - 240:
/FIELDS file1_scoreval46        241: - 241:
/FIELDS file1_scorenm47         242: - 242:
/FIELDS file1_scoreval47        243: - 243:
/FIELDS file1_scorenm48         244: - 244:
/FIELDS file1_scoreval48        245: - 245:
/FIELDS file1_scorenm49         246: - 246:
/FIELDS file1_scoreval49        247: - 247:
/FIELDS file1_scorenm50         248: - 248:
/FIELDS file1_scoreval50        249: - 249:
/FIELDS file1_scorenm51         250: - 250:
/FIELDS file1_scoreval51        251: - 251:
/FIELDS file1_scorenm52         252: - 252:
/FIELDS file1_scoreval52        253: - 253:
/FIELDS file1_scorenm53         254: - 254:
/FIELDS file1_scoreval53        255: - 255:
/FIELDS file1_scorenm54         256: - 256:
/FIELDS file1_scoreval54        257: - 257:
/FIELDS file1_scorenm55         258: - 258:
/FIELDS file1_scoreval55        259: - 259:
/FIELDS file1_scorenm56         260: - 260:
/FIELDS file1_scoreval56        261: - 261:
/FIELDS file1_scorenm57         262: - 262:
/FIELDS file1_scoreval57        263: - 263:
/FIELDS file1_scorenm58         264: - 264:
/FIELDS file1_scoreval58        265: - 265:
/FIELDS file1_scorenm59         266: - 266:
/FIELDS file1_scoreval59        267: - 267:
/FIELDS file1_scorenm60         268: - 268:
/FIELDS file1_scoreval60        269: - 269:
/FIELDS file1_scorenm61         270: - 270:
/FIELDS file1_scoreval61        271: - 271:
/FIELDS file1_scorenm62         272: - 272:
/FIELDS file1_scoreval62        273: - 273:
/FIELDS file1_scorenm63         274: - 274:
/FIELDS file1_scoreval63        275: - 275:
/FIELDS file1_scorenm64         276: - 276:
/FIELDS file1_scoreval64        277: - 277:
/FIELDS file1_scorenm65         278: - 278:
/FIELDS file1_scoreval65        279: - 279:
/FIELDS file1_scorenm66         280: - 280:
/FIELDS file1_scoreval66        281: - 281:
/FIELDS file1_scorenm67         282: - 282:
/FIELDS file1_scoreval67        283: - 283:
/FIELDS file1_scorenm68         284: - 284:
/FIELDS file1_scoreval68        285: - 285:
/FIELDS file1_scorenm69         286: - 286:
/FIELDS file1_scoreval69        287: - 287:
/FIELDS file1_scorenm70 	288: - 288:
/FIELDS file1_scoreval70        289: - 289:
/FIELDS file1_scorenm71         290: - 290:
/FIELDS file1_scoreval71        291: - 291:

/JOINKEYS file1_carID

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_carID 1: - 1:
/FIELDS file2_bureaurpt 2: - 2:

/JOINKEYS file2_carID

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_custID,file1_carID,file1_carTyp,file1_carSubTyp,file1_custTypCd,file1_lobCd,file1_channelOrgCd,file1_createTS,file1_createUserId,file1_lastUpdateTS,file1_lastUpdateUserId,file1_dataSourceID,file1_effStartTS,file1_carStatusTypCd,file1_commentTxt,file1_carActivityReasonCd,file1_firstName,file1_middleName,file1_lastName,file1_custCreationDT,file1_birthDT,file1_custMasterSrcID,file1_custStatusCd,file1_custSubTypCd,file1_custTypCd1,file1_langCd,file1_contactPhoneNum,file1_creditProfileID,file1_dlString,file1_dlProvCd,file1_sin,file1_healthCareNum,file1_healthCareProvCd,file1_passportString,file1_passportCountryCd,file1_provincialIDString,file1_provincialIDProvCd,file1_credValCd,file1_addressLine1,file1_addressLine2,file1_addressLine3,file1_addressLine4,file1_addressLine5,file1_cityName,file1_provCd,file1_countryCd,file1_postalCd,file1_primCredCardTypCd,file1_secCredCardTypCd,file1_legalCareCd,file1_resCd,file1_employmentStatCd,file1_creditProfileStatCd,file1_creditChkConsentCd,file1_creditProfileMethodCd,file1_applicationSubProvCd,file1_provResCd,file1_bypassMatchInd,file1_credBureauSimFOInd,file1_busLastUpdateTS,file1_firstCarDt,file1_lastCarDt,file1_collectionInd,file1_credValEffDt,file1_lastCollectionStartDt,file1_lastCollectionEndDt,file1_collectionScoreTxt,file1_accountsInAgencyCount,file1_latestAgencyAssignmentDt,file1_balanceOwingInAgencyAmount,file1_involuntaryCancelledAccountCount,file1_latestIcaDt,file1_balanceOwinOnIcaAmount,file1_NSFChequesCount,file1_fraudIndIn,file1_intCreditDcsnTrnID,file1_assessmentResultCd,file1_assessmentResultReasonCd,file1_intCreditDcsnResultID,file1_assessmentMsgCd,file1_creditValCd,file1_fraudIndCdOut,file1_prodCategoryBoltOn,file1_cbtCreditBureauTrnID,file1_cbtFirstName,file1_cbtMiddleName,file1_cbtLastName,file1_cbtTrnErrorCd,file1_cbtReportSourceCd,file1_cbtReportTypTxt,file1_cbtResultCreationDT,file1_creditBureauTrnResultStatCD,file1_creditBureauTrnResultStatUpdateDT,file1_adjCreditScoreTypCd,file1_adjCreditScoreTxt,file1_adjCreditClassCd,file1_adjCreditLimitAmount,file1_adjDecisionCd,file1_adjDecisionMsgTxt,file1_adjDepositAmount,file1_depLookupStratCd,file1_cvudStratCd,file1_creditValStratCd,file1_customerActStratCd,file1_tenureStratCd,file1_collsegment,file1_scorecardId,file1_cyclesDelinq,file1_inputRiskLvlNum,file1_outputRiskLvlNum,file1_riskCd1,file1_riskVal1,file1_riskCd2,file1_riskVal2,file1_riskCd3,file1_riskVal3,file1_riskCd4,file1_riskVal4,file1_riskCd5,file1_riskVal5,file1_riskCd6,file1_riskVal6,file1_riskCd7,file1_riskVal7,file1_riskCd8,file1_riskVal8,file1_riskCd9,file1_riskVal9,file1_riskCd10,file1_riskVal10,file1_riskCd11,file1_riskVal11,file1_riskCd12,file1_riskVal12,file1_fraudTypCd1,file1_fraudCd1,file1_fraudMsg1,file1_fraudTypCd2,file1_fraudCd2,file1_fraudMsg2,file1_fraudTypCd3,file1_fraudCd3,file1_fraudMsg3,file1_fraudTypCd4,file1_fraudCd4,file1_fraudMsg4,file1_fraudTypCd5,file1_fraudCd5,file1_fraudMsg5,file1_scorenm1,file1_scoreval1,file1_scorenm2,file1_scoreval2,file1_scorenm3,file1_scoreval3,file1_scorenm4,file1_scoreval4,file1_scorenm5,file1_scoreval5,file1_scorenm6,file1_scoreval6,file1_scorenm7,file1_scoreval7,file1_scorenm8,file1_scoreval8,file1_scorenm9,file1_scoreval9,file1_scorenm10,file1_scoreval10,file1_scorenm11,file1_scoreval11,file1_scorenm12,file1_scoreval12,file1_scorenm13,file1_scoreval13,file1_scorenm14,file1_scoreval14,file1_scorenm15,file1_scoreval15,file1_scorenm16,file1_scoreval16,file1_scorenm17,file1_scoreval17,file1_scorenm18,file1_scoreval18,file1_scorenm19,file1_scoreval19,file1_scorenm20,file1_scoreval20,file1_scorenm21,file1_scoreval21,file1_scorenm22,file1_scoreval22,file1_scorenm23,file1_scoreval23,file1_scorenm24,file1_scoreval24,file1_scorenm25,file1_scoreval25,file1_scorenm26,file1_scoreval26,file1_scorenm27,file1_scoreval27,file1_scorenm28,file1_scoreval28,file1_scorenm29,file1_scoreval29,file1_scorenm30,file1_scoreval30,file1_scorenm31,file1_scoreval31,file1_scorenm32,file1_scoreval32,file1_scorenm33,file1_scoreval33,file1_scorenm34,file1_scoreval34,file1_scorenm35,file1_scoreval35,file1_scorenm36,file1_scoreval36,file1_scorenm37,file1_scoreval37,file1_scorenm38,file1_scoreval38,file1_scorenm39,file1_scoreval39,file1_scorenm40,file1_scoreval40,file1_scorenm41,file1_scoreval41,file1_scorenm42,file1_scoreval42,file1_scorenm43,file1_scoreval43,file1_scorenm44,file1_scoreval44,file1_scorenm45,file1_scoreval45,file1_scorenm46,file1_scoreval46,file1_scorenm47,file1_scoreval47,file1_scorenm48,file1_scoreval48,file1_scorenm49,file1_scoreval49,file1_scorenm50,file1_scoreval50,file1_scorenm51,file1_scoreval51,file1_scorenm52,file1_scoreval52,file1_scorenm53,file1_scoreval53,file1_scorenm54,file1_scoreval54,file1_scorenm55,file1_scoreval55,file1_scorenm56,file1_scoreval56,file1_scorenm57,file1_scoreval57,file1_scorenm58,file1_scoreval58,file1_scorenm59,file1_scoreval59,file1_scorenm60,file1_scoreval60,file1_scorenm61,file1_scoreval61,file1_scorenm62,file1_scoreval62,file1_scorenm63,file1_scoreval63,file1_scorenm64,file1_scoreval64,file1_scorenm65,file1_scoreval65,file1_scorenm66,file1_scoreval66,file1_scorenm67,file1_scoreval67,file1_scorenm68,file1_scoreval68,file1_scorenm69,file1_scoreval69,file1_scorenm70,file1_scoreval70,file1_scorenm71,file1_scoreval71,rightside:file2_bureaurpt

/STATISTICS
/WARNINGS 100
/END

EOF


return $