#!/usr/bin/ksh

echo "Step13a: Generating the candidate target feed file."

syncsort << EOF
   /JOIN unpaired rightside
   /INFILE "$1" 65535
   /JOINKEYS SORTED recordlayout1.BILLING_ACCOUNT_ID
   /INFILE "$2" 65535
   /JOINKEYS SORTED recordlayout2.BILLING_ACCOUNT_ID

   /OUTFILE "$3" 65535 OVERWRITE
   /REFORMAT LEFTSIDE:__Field_1, RIGHTSIDE:__Field_2, LEFTSIDE:__Field_3, LEFTSIDE:__Field_4, LEFTSIDE:__Field_5, RIGHTSIDE:__Field_6, RIGHTSIDE:__Field_7, RIGHTSIDE:__Field_8, RIGHTSIDE:__Field_9, RIGHTSIDE:__Field_10, RIGHTSIDE:__Field_11, RIGHTSIDE:__Field_12, RIGHTSIDE:__Field_13, LEFTSIDE:__Field_14, LEFTSIDE:__Field_15, LEFTSIDE:__Field_16, LEFTSIDE:__Field_17, LEFTSIDE:__Field_18, LEFTSIDE:__Field_19, LEFTSIDE:__Field_20, LEFTSIDE:__Field_21, LEFTSIDE:__Field_22, LEFTSIDE:__Field_14, RIGHTSIDE:SERVICE_INSTANCE_ACTVN_DT, RIGHTSIDE:SERVICE_INSTANCE_STAT_DT, LEFTSIDE:__Field_23, LEFTSIDE:__Field_24, RIGHTSIDE:__Field_25, RIGHTSIDE:__Field_26, RIGHTSIDE:__Field_27, LEFTSIDE:__Field_1, LEFTSIDE:__Field_28, LEFTSIDE:__Field_29, LEFTSIDE:__Field_30, LEFTSIDE:__Field_31, LEFTSIDE:__Field_32, LEFTSIDE:__Field_33, LEFTSIDE:__Field_34, LEFTSIDE:__Field_35, LEFTSIDE:__Field_36, RIGHTSIDE:__Field_37
   /COLLATINGSEQUENCE DEFAULT ASCII

   /RECORDLAYOUT recordlayout1 { BILLING_ACCOUNT_ID CHARACTER 18, CUSTOMER_ID_BILLING CHARACTER 9, CUSTOMER_ID_SERVICE CHARACTER 9, CUSTOMER_ID_CREDIT CHARACTER 9, FIRST_NM_SERVICE CHARACTER 60, MIDDLE_NM_SERVICE CHARACTER 60, LAST_NM_SERVICE CHARACTER 60, STREET_NM_BILLING CHARACTER 40, STREET_DIR_CD_BILLING CHARACTER 2, CIVIC_NUM_BILLING CHARACTER 7, CIVIC_NUM_SFX_TXT_BILLING CHARACTER 5, UNIT_NUM_BILLING CHARACTER 6, MUNIC_NM_BILLING CHARACTER 40, PROV_STATE_CD_BILLING CHARACTER 2, POSTAL_ZIP_CD_TXT_BILLING CHARACTER 9, CUST_TYPE_CD CHARACTER 1, RISK_IND_CD CHARACTER 2, FIRST_NM_BILLING CHARACTER 60, MIDDLE_NM_BILLING CHARACTER 60, LAST_NM_BILLING CHARACTER 60, IDENTIFICATION_NUM_DL CHARACTER 50, IDENTIFICATION_NUM_SIN CHARACTER 50}
   /RECORDLAYOUT recordlayout2 { BILLING_ACCOUNT_ID CHARACTER 18, WTN_SERVICE CHARACTER 10, WTN_OMS CHARACTER 10, SERVICE_INSTANCE_ID CHARACTER 18, CUSTOMER_ID CHARACTER 9, STREET_NM_SERVICE CHARACTER 40, STREET_DIR_CD_SERVICE CHARACTER 2, CIVIC_NUM_SERVICE CHARACTER 7, CIVIC_NUM_SFX_TXT_SERVICE CHARACTER 5, UNIT_NUM_SERVICE CHARACTER 6, MUNIC_NM_SERVICE CHARACTER 40, PROV_STATE_CD_SERVICE CHARACTER 2, POSTAL_ZIP_CD_TXT_SERVICE CHARACTER 9, SERVICE_INSTANCE_ACTVN_DT CHARACTER 10, SERVICE_INSTANCE_STAT_CD CHARACTER 1, SERVICE_INSTANCE_STAT_DT CHARACTER 10, SERVICE_RESRC_ALIAS_TYPE_CD CHARACTER 20, LSP_NAME CHARACTER 10, PIC_CODE CHARACTER 5, NON_PUB_INDICATOR CHARACTER 1, BTN CHARACTER 10}
   /DERIVEDFIELD SERVICE_INSTANCE_ACTVN_DT recordlayout2.SERVICE_INSTANCE_ACTVN_DT EXTRACT /([0-9]+)-([0-9]+)-([0-9]+)/ '\1\2\3' CHARACTER
   /DERIVEDFIELD SERVICE_INSTANCE_STAT_DT recordlayout2.SERVICE_INSTANCE_STAT_DT EXTRACT /([0-9]+)-([0-9]+)-([0-9]+)/ '\1\2\3' CHARACTER
   /DERIVEDFIELD FILLER1 ' '
   /DERIVEDFIELD CITY_NAME_SERVICE recordlayout2.MUNIC_NM_SERVICE EXTRACT /([A-Z]+)/ '\1 ' CHARACTER
   /DERIVEDFIELD CITY_NAME_BILLING recordlayout1.MUNIC_NM_BILLING EXTRACT /([A-Z]+)/ '\1 ' CHARACTER
   /DERIVEDFIELD FILLER12 12 ' '
   /DERIVEDFIELD BILLING_DATE 8 ' '
   /DERIVEDFIELD PREVIOUS_TN 10 ' '
   /DERIVEDFIELD EMPLOYER_NAME 42 ' '
   /DERIVEDFIELD EMPLOYER_TN 14 ' '
   /DERIVEDFIELD __Field_1 FILLER1 CHARACTER 1
   /DERIVEDFIELD __Field_2 recordlayout2.WTN_SERVICE CHARACTER 10
   /DERIVEDFIELD __Field_3 recordlayout1.FIRST_NM_SERVICE CHARACTER 21
   /DERIVEDFIELD __Field_4 recordlayout1.MIDDLE_NM_SERVICE CHARACTER 21
   /DERIVEDFIELD __Field_5 recordlayout1.LAST_NM_SERVICE CHARACTER 21
   /DERIVEDFIELD __Field_6 recordlayout2.STREET_NM_SERVICE CHARACTER 18
   /DERIVEDFIELD __Field_7 recordlayout2.STREET_DIR_CD_SERVICE CHARACTER 2
   /DERIVEDFIELD __Field_8 recordlayout2.CIVIC_NUM_SERVICE CHARACTER 6
   /DERIVEDFIELD __Field_9 recordlayout2.CIVIC_NUM_SFX_TXT_SERVICE CHARACTER 1
   /DERIVEDFIELD __Field_10 recordlayout2.UNIT_NUM_SERVICE CHARACTER 5
   /DERIVEDFIELD __Field_11 recordlayout2.MUNIC_NM_SERVICE CHARACTER 20
   /DERIVEDFIELD __Field_12 recordlayout2.PROV_STATE_CD_SERVICE CHARACTER 2
   /DERIVEDFIELD __Field_13 recordlayout2.POSTAL_ZIP_CD_TXT_SERVICE CHARACTER 6
   /DERIVEDFIELD __Field_14 FILLER12 CHARACTER 12
   /DERIVEDFIELD __Field_15 recordlayout1.STREET_NM_BILLING CHARACTER 18
   /DERIVEDFIELD __Field_16 recordlayout1.STREET_DIR_CD_BILLING CHARACTER 2
   /DERIVEDFIELD __Field_17 recordlayout1.CIVIC_NUM_BILLING CHARACTER 6
   /DERIVEDFIELD __Field_18 recordlayout1.CIVIC_NUM_SFX_TXT_BILLING CHARACTER 1
   /DERIVEDFIELD __Field_19 recordlayout1.UNIT_NUM_BILLING CHARACTER 5
   /DERIVEDFIELD __Field_20 recordlayout1.MUNIC_NM_BILLING CHARACTER 20
   /DERIVEDFIELD __Field_21 recordlayout1.PROV_STATE_CD_BILLING CHARACTER 2
   /DERIVEDFIELD __Field_22 recordlayout1.POSTAL_ZIP_CD_TXT_BILLING CHARACTER 6
   /DERIVEDFIELD __Field_23 recordlayout1.CUST_TYPE_CD CHARACTER 1
   /DERIVEDFIELD __Field_24 recordlayout1.RISK_IND_CD CHARACTER 2
   /DERIVEDFIELD __Field_25 recordlayout2.LSP_NAME CHARACTER 10
   /DERIVEDFIELD __Field_26 recordlayout2.PIC_CODE CHARACTER 5
   /DERIVEDFIELD __Field_27 recordlayout2.NON_PUB_INDICATOR CHARACTER 1
   /DERIVEDFIELD __Field_28 recordlayout1.FIRST_NM_BILLING CHARACTER 21
   /DERIVEDFIELD __Field_29 recordlayout1.MIDDLE_NM_BILLING CHARACTER 21
   /DERIVEDFIELD __Field_30 recordlayout1.LAST_NM_BILLING CHARACTER 21
   /DERIVEDFIELD __Field_31 BILLING_DATE CHARACTER 8
   /DERIVEDFIELD __Field_32 PREVIOUS_TN CHARACTER 10
   /DERIVEDFIELD __Field_33 recordlayout1.IDENTIFICATION_NUM_SIN CHARACTER 9
   /DERIVEDFIELD __Field_34 recordlayout1.IDENTIFICATION_NUM_DL CHARACTER 12
   /DERIVEDFIELD __Field_35 EMPLOYER_NAME CHARACTER 42
   /DERIVEDFIELD __Field_36 EMPLOYER_TN CHARACTER 14
   /DERIVEDFIELD __Field_37 recordlayout2.BTN CHARACTER 32

   /SILENT
   /END
EOF

return $?
