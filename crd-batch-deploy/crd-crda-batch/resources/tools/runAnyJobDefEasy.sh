#/bin/sh
if [ $# -ge 2 ]; then
    JOB_NAME=$1
    MODE=$2
else
    echo ""
    echo "Please specify the job name, run mode and any parameter that this job takes"
    echo "For example: ./runAnyJobDefEasy.sh obpm-usr-mgmt NORMAL L_COL_TRT_LOGICAL_PROCESS_DATE 20060519\n"
    exit 1
fi
which ctmvar awk
if [[ $? -ne 0 ]]; then
 echo "command failed, exit"
 exit 1
fi
I_APP_HOME=`ctmvar -action LIST | grep G_APP_HOME_PREFIX | awk '{print $2}'`crd/crda
I_BATCH_ENV=`ctmvar -action LIST | grep G_ENV | head -1 | awk '{print $2}'`
I_DATA_HOME=`ctmvar -action LIST | grep G_DATA_HOME_PREFIX | awk '{print $2}'`crd/crda

I_DATE=`date +%Y%m%d`
I_TIME=`date +%H%M%S`

echo "I_APP_HOME=${I_APP_HOME}, I_BATCH_ENV=${I_BATCH_ENV}, I_DATA_HOME=${I_DATA_HOME}, I_DATE=${I_DATE}, I_TIME=${I_TIME}"

PARAM1=""
if [ $# -ge 4 ]; then
   PARAM1="$3=$4"
fi
echo "param count: $# PARAM1= ${PARAM1}"
PARAM2=""
if [ $# -ge 6 ]; then
   PARAM2="$5=$6"
fi

PARAM3=""
if [ $# -ge 8 ]; then
   PARAM3="${7}=${8}"
fi

PARAM4=""
if [ $# -ge 10 ]; then
   PARAM4="${9}=${10}"
fi

PARAM5=""
if [ $# -ge 12 ]; then
   PARAM5="${11}=${12}"
fi

echo "log file: $I_DATA_HOME/logs/${JOB_NAME}.log"
rm -f $I_DATA_HOME/logs/${JOB_NAME}.log
echo "$I_DATA_HOME/resources/job/runBECJobDef.sh $I_APP_HOME $I_DATA_HOME $I_BATCH_ENV ${JOB_NAME} ${MODE} ${PARAM1} ${PARAM2} ${PARAM3} ${PARAM4} ${PARAM5}"
./runBECJobDef.sh $I_APP_HOME $I_DATA_HOME $I_BATCH_ENV ${JOB_NAME} ${MODE} ${PARAM1} ${PARAM2} ${PARAM3} ${PARAM4} ${PARAM5}| tee $I_DATA_HOME/logs/${JOB_NAME}.log 2>&1

echo "Job Completed, Please check: $I_DATA_HOME/logs/${JOB_NAME}.log for results"

exit 0
