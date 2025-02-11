#!/usr/bin/ksh

# Title: join_crd_custmr_profile_attr_data.sh
# desc: This script takes two CSV files one with extracted credit profile attributes and second one with 
#       customer profile attributes and merges(joins) them together using SyncSort.
#

# Validate the number of input parameters
if [ $# -eq 3 ]; then
    CRD_PRFL_ATTRS_FILE=$1
    CUSTMR_PRFL_ATTRS_FILE=$2
    OUTPUT_FILE=$3
else
    echo ""
    echo "Usage: join_crd_custmr_profile_attr_data.sh cred_profile_attrs custmr_profile_attrs output_file"
    echo ""
    exit 1
fi

# Validate that two input file exist.
if [ -f $CRD_PRFL_ATTRS_FILE ] && [ -f $CUSTMR_PRFL_ATTRS_FILE ]; then
   echo "Credit profile attrs file: $CRD_PRFL_ATTRS_FILE , Customer profile attrs file: $CUSTMR_PRFL_ATTRS_FILE"
else
    echo ""
    echo "Can't find one or both of input files: $CRD_PRFL_ATTRS_FILE or $CUSTMR_PRFL_ATTRS_FILE"
    echo ""
    exit 1
fi

# Create output file with the header.
# echo "#Report name:    Credit Assessment Synchronize Report" > $OUTPUT_FILE
# echo "CREDIT_PRFL_ID~CUSTOMERID~LEGAL_NAME~LEGAL_NAME_VALID~PROPOSED_LEGAL_NAME~TRADE_NAME~LEGAL_ENTITY_TYPE~DUNS~RCID~CORP_REG_STATUS~LAST_VALID_DATE~JUR_COUNTRY_CD~JUR_CODE~QUOTE_THRHOLD~MANDRY_ASSMT~REG_INC_NUM~REG_INC_DATE" >> $OUTPUT_FILE



# Actual SyncSort implementation of the script.
syncsort <<-EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRD_PRFL_ATTRS_FILE "~"
/FIELDS CREDIT_PRFL_ID 1: - 1:
/FIELDS CUSTOMERID 2: - 2:
/FIELDS LEGAL_NAME_RES 3: - 3:
/FIELDS PROP_LEGAL_NAME 4: - 4:
/FIELDS CORP_REG_STATUS 5: - 5:
/FIELDS LAST_VALID_DATE 6: - 6:
/FIELDS JUR_COUNTRY_CD 7: - 7:
/FIELDS JUR_CODE 8: - 8:
/FIELDS QUOTE_THRHOLD 9: - 9:
/FIELDS MANDRY_ASSMT 10: - 10:
/FIELDS REG_INC_NUM 11: - 11:
/FIELDS REG_INC_DATE 12: - 12:
/JOINKEYS CUSTOMERID

/INFILE $CUSTMR_PRFL_ATTRS_FILE "~"
/FIELDS CUSTOMERID2 1: - 1:
/FIELDS RCID2 2: - 2: en
/FIELDS LEGAL_NAME 3: - 3:
/FIELDS TRADE_NAME 4: - 4:
/FIELDS LEGAL_ENTITY_TYPE 5: - 5:
/FIELDS DUNS 6: - 6:
/JOINKEYS CUSTOMERID2


/OUTFILE $OUTPUT_FILE OVERWRITE

/REFORMAT LEFTSIDE: CREDIT_PRFL_ID, CUSTOMERID, RIGHTSIDE: LEGAL_NAME, LEFTSIDE: LEGAL_NAME_RES, PROP_LEGAL_NAME, RIGHTSIDE: TRADE_NAME, LEGAL_ENTITY_TYPE, DUNS, RCID2, LEFTSIDE: CORP_REG_STATUS, LAST_VALID_DATE, JUR_COUNTRY_CD, JUR_CODE, QUOTE_THRHOLD, MANDRY_ASSMT, REG_INC_NUM, REG_INC_DATE

/SILENT
/WARNINGS 100
/END
EOF
return $?
