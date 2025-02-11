#!/usr/bin/ksh

generateAuditFile()
{
FILE_MASK=$1
TEMP_FILE=$PROCESSBOX/extract/sbi/$2
dateStr=`date +%Y%m%d`
DATA_FILE=${FILE_MASK}.DAT.$dateStr
AUDIT_FILE=${FILE_MASK}.AUD.$dateStr
INFILE=$OUTBOX/${DATA_FILE}
OUTFILE=$OUTBOX/${AUDIT_FILE}
# copy data file to outbox
cp $TEMP_FILE $INFILE

# generate audit fields
typeset -L255 AUD_FILE_NAME
typeset -L19 AUD_CREATION_DATE
typeset -Z18 AUD_RECORD_COUNT
typeset -Z40 AUD_DATA_SIZE

ls $INFILE|sed -e 's/.*\///'|read AUD_FILE_NAME 
ls $INFILE|sed -e 's/.*\.//'|read AUD_CREATION_DATE
cat $INFILE|wc -l|read AUD_RECORD_COUNT
cat $INFILE|wc -c|read AUD_DATA_SIZE

echo "SCRIPT DEBUG: Audit field generated, AUD_FILE_NAME = $AUD_FILE_NAME" 
echo "SCRIPT DEBUG: Audit field generated, AUD_CREATION_DATE = $AUD_CREATION_DATE" 
echo "SCRIPT DEBUG: Audit field generated, AUD_RECORD_COUNT = $AUD_RECORD_COUNT" 
echo "SCRIPT DEBUG: Audit field generated, AUD_DATA_SIZE = $AUD_DATA_SIZE" 

printf "%255s" "$AUD_FILE_NAME" >$OUTFILE
printf "%19s" "$AUD_CREATION_DATE" >>$OUTFILE
print $AUD_RECORD_COUNT$AUD_DATA_SIZE >>$OUTFILE

# copy data file and audit file to archive
cp $INFILE $ARCHIVE/extract/sbi/${DATA_FILE}
cp $OUTFILE $ARCHIVE/extract/sbi/${AUDIT_FILE}
}


# clear outbox
rm -f $OUTBOX/*

generateAuditFile "CREDIT_MATCHED_CUSTOMER" "personListWithLn.dat"
generateAuditFile "CREDIT_CUSTOMER_CREDIT_INFO" "customerCreditInfo.dat"
generateAuditFile "CREDIT_PROFILE" "creditProfile.dat"
generateAuditFile "CPROFL_IDENTIFICATION" "cproflIdentification.dat"
generateAuditFile "CPROFL_ADDRESS" "cproflAddress.dat"
