CRD_CRDA_APP_BASE=$1
CRD_CRDA_DATA_BASE=$2
CRD_CRDA_ENV=$3
#    i.e. trt-bill
JOB=$4
#    i.e. NORMAL  or  RESTART
RESTART_MODE=$5

DParam0="D=java.security.auth.login.config=/apps/common/env/${CRD_CRDA_ENV}/jaas.config"
DParamEnablerLog="D=amdocs.uams.config.print=false"
DParamEnablerHome="D=amdocs.system.home=${CRD_CRDA_DATA_BASE}"

# Set Argument
DParam1=""
if [ $# -ge 6 ]
then
DParam1="D=$6"
fi

echo "DParam1: $DParam1"

# Set Argument
DParam2=""
if [ $# -ge 7 ]
then
DParam2="D=$7"
fi

# Set Argument
DParam3=""
if [ $# -ge 8 ]
then
DParam3="D=$8"
fi

# Set Argument
DParam4=""
if [ $# -ge 9 ]
then
DParam4="D=$9"
fi

# Set Argument
DParam5=""
if [ $# -ge 10 ]
then
DParam5="D=${10}"
fi

# Set Argument
DParam6=""
if [ $# -ge 11 ]
then
DParam6="D=${11}"
fi

# Set Argument
DParam7=""
if [ $# -ge 12 ]
then
DParam7="D=${12}"
fi

# Set Argument
DParam8=""
if [ $# -ge 13 ]
then
DParam8="D=${13}"
fi

# Set Argument
DParam9=""
if [ $# -ge 14 ]
then
DParam9="D=${14}"
fi

# Set Argument
DParam10=""
if [ $# -ge 15 ]
then
DParam10="D=${15}"
fi

# Set Argument
DParam11=""
if [ $# -ge 16 ]
then
DParam11="D=${16}"
fi

# Set Argument
DParam12=""
if [ $# -ge 17 ]
then
DParam12="D=${17}"
fi

# Set Argument
DParam13=""
if [ $# -ge 18 ]
then
DParam13="D=${18}"
fi

# Set Argument
DParam14=""
if [ $# -ge 19 ]
then
DParam14="D=${19}"
fi


APP_HOME="APP_HOME=${CRD_CRDA_APP_BASE}"
DATA_HOME="DATA_HOME=${CRD_CRDA_DATA_BASE}"
ENV="ENV=${CRD_CRDA_ENV}"
APPID="APPID=CreditAssessment"
JOB="JOB=${JOB}"
MODE=${RESTART_MODE}
D=$DATAHOME
LEVEL1_SHORTNAME="LEVEL1_SHORTNAME=crd" 
LEVEL2_SHORTNAME="LEVEL2_SHORTNAME=crda"
INSTANCE_ID="SCHEDULER_INSTNC_ID=1234"
COMMON_DIR=/apps/common
COMMON_HOME="COMMON_HOME=$COMMON_DIR"
END_OF_PARMS=END_OF_PARMS


echo ' '
echo 'runBECJobDef'

echo "$COMMON_DIR/scripts/${CRD_CRDA_ENV}/fw-BEC.sh $APP_HOME $DATA_HOME $ENV $APPID $JOB $MODE $LEVEL1_SHORTNAME $LEVEL2_SHORTNAME $INSTANCE_ID  $DParamEnablerLog $DParamEnablerHome $DParam0 D=BATCH_USERID=SYS-$4 $DParam1 $DParam2 $DParam3 $DParam4 $DParam5 $DParam6 $DParam7 $DParam8 $DParam9 $DParam10 $DParam11 $DParam12 $DParam13 $DParam14 $DParam15 $DParam16 $END_OF_PARMS"

$COMMON_DIR/scripts/${CRD_CRDA_ENV}/fw-BEC.sh $APP_HOME $DATA_HOME $ENV $APPID $JOB $MODE $LEVEL1_SHORTNAME $LEVEL2_SHORTNAME $INSTANCE_ID $DParamEnablerLog $DParamEnablerHome $DParam0 D=BATCH_USERID=SYS-trt-$4 $DParam1 $DParam2 $DParam3 $DParam4 $DParam5 $DParam6 $DParam7 $DParam8 $DParam9 $DParam10 $DParam11 $DParam12 $DParam13 $DParam14 $DParam15 $DParam16 $END_OF_PARMS

returnCode=$?
echo JOBCONTROLLER RETURN CODE IS $returnCode
exit $returnCode
