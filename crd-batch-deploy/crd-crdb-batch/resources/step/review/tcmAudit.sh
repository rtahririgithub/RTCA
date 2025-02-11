#!/usr/bin/ksh

#  ======================================================================
#  tcmAudit.sh					                     
#  **********************************************************************  
#  check TCM data file with audit file
#  mv data file into processbox of credit mgmt					
#*                                                                   
#* 2005-06-14   Lei Fan x089748 Initial version		              
#* ======================================================================= 

CRD_APP_HOME=$1
CRD_DATA_HOME=$2

ls -r $CRD_DATA_HOME/data/inbox/Extract_CREDIT_VALUE_REVIEW.dat* |read DATA_FILE
echo "SCRIPT DEBUG tcmAudit.sh: Data File detected, the file name = $DATA_FILE" 

ls $DATA_FILE|sed -e 's/\.dat/.aud/'|read AUDIT_FILE
echo "SCRIPT DEBUG tcmAudit.sh: Audit File detected, the file name = $AUDIT_FILE" 

. $CRD_APP_HOME/resources/step/review/checkAudit.sh $DATA_FILE $AUDIT_FILE

mv $DATA_FILE $CRD_DATA_HOME/data/processbox/review/TCMReviewResult.dat
rm -f $CRD_DATA_HOME/data/inbox/Extract_CREDIT_VALUE_REVIEW*