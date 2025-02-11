#!/usr/bin/ksh

#  ======================================================================
#  custODSMvin.sh					                     
#  **********************************************************************  
#  move customerODS extract from sftp inbox to application process box				
#*                                                                   
#* 2007-01-23   Lei Fan x089748 Initial version		              
#* ======================================================================= 

SFTP_HOME=$1
CRD_DATA_HOME=$2

ls -r $SFTP_HOME/CUSTODS_REF_BILLING_ACCOUNT.DAT* | read ZIPPED_SFTP_FILE
echo "SCRIPT DEBUG custODSMvin.sh: CustomerODS Zipped Data File detected, the file name = $ZIPPED_SFTP_FILE"

cp $ZIPPED_SFTP_FILE $CRD_DATA_HOME/data/inbox/CUSTODS_BILLING_ACCOUNT.dat.gz
ls $CRD_DATA_HOME/data/inbox/CUSTODS_BILLING_ACCOUNT.dat.gz | read ZIPPED_APP_FILE
echo "SCRIPT DEBUG custODSMvin.sh: COPY SUCCESSFULLY, the file name = $ZIPPED_APP_FILE"

gunzip -f $ZIPPED_APP_FILE
echo "SCRIPT DEBUG custODSMvin.sh: Unzipping successfully"
rm $ZIPPED_SFTP_FILE
echo "SCRIPT DEBUG custODSMvin.sh: Mving successfully"
