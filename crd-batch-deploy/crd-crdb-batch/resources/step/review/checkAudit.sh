#!/bin/ksh
#  ======================================================================
#  checkAudit.sh					                     
#  **********************************************************************  
#  Checks data size and record number of a extract against its audit file. 
#  
#
#  Returns	the reason the check failed 
#		If successful returns 0						
#*                                                                   
#* 2005-06-14   Lei Fan x089748 Initial version		              
#* =======================================================================    

ERR_DATA_FILE_NOT_FOUND=1000
ERR_AUDIT_FILE_NOT_FOUND=1001
ERR_AUDIT_DIFF_RECNUM=1002
ERR_AUDIT_DIFF_DATASIZE=1003

###########################################################################################
# List files and select the most recent one
# ODSFileName is pure name - without the directory
###########################################################################################
DATA_FILE=$1
AUDIT_FILE=$2

if ls $DATA_FILE
then
	echo "SCRIPT DEBUG: Data File detected, the file name = $DATA_FILE" 
else
	echo "AUDIT ERROR:  Data File NOT FOUND! Exiting script, exit code = $ERR_DATA_FILE_NOT_FOUND "
	exit $ERR_DATA_FILE_NOT_FOUND
fi

if ls $AUDIT_FILE
then
	echo "SCRIPT DEBUG: Audit File detected, the file name = $AUDIT_FILE" 
else
	echo "AUDIT ERROR:  Audit File NOT FOUND! Exiting script, exit code = $ERR_AUDIT_FILE_NOT_FOUND "
	exit $ERR_AUDIT_FILE_NOT_FOUND
fi


###########################################################################################
# get data file size and record number based on the data received in the *.dat file
###########################################################################################
# generate audit fields
typeset -L255 AUD_FILE_NAME
typeset -L19 AUD_CREATION_DATE
typeset -Z18 AUD_RECORD_COUNT
typeset -Z40 AUD_DATA_SIZE

ls $DATA_FILE|sed -e 's/.*\///'|read AUD_FILE_NAME 
ls $DATA_FILE|sed -e 's/.*\.//'|read AUD_CREATION_DATE
cat $DATA_FILE|wc -l|read AUD_RECORD_COUNT
cat $DATA_FILE|wc -c|read AUD_DATA_SIZE

echo "SCRIPT DEBUG: Audit field generated, AUD_FILE_NAME = $AUD_FILE_NAME" 
echo "SCRIPT DEBUG: Audit field generated, AUD_CREATION_DATE = $AUD_CREATION_DATE" 
echo "SCRIPT DEBUG: Audit field generated, AUD_RECORD_COUNT = $AUD_RECORD_COUNT" 
echo "SCRIPT DEBUG: Audit field generated, AUD_DATA_SIZE = $AUD_DATA_SIZE" 

###########################################################################################
#  extract the data file size and record number from the audit file
###########################################################################################
awk  '{print substr ($0,275,18)}' $AUDIT_FILE|read RECNUM
awk  '{print substr ($0,293,40)}' $AUDIT_FILE|read DATASIZE

if [ "$AUD_RECORD_COUNT" != "$RECNUM" ]; then 
	echo "AUDIT ERROR:  Record number is not match! Exiting script, exit code = $ERR_AUDIT_DIFF_RECNUM "
	exit $ERR_AUDIT_DIFF_RECNUM
fi
if [ "$AUD_DATA_SIZE" != "$DATASIZE" ]; then 
	echo "AUDIT ERROR:  Data file size is not match! Exiting script, exit code = $ERR_AUDIT_DIFF_DATASIZE "
	exit $ERR_AUDIT_DIFF_DATASIZE
fi

return 0