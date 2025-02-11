#!/usr/bin/ksh
# update local variables in
#NUM=$1
echo " file name is: $1"
echo " # L_RPT_START_DATE $2"
echo " # L_RPT_END_DATE $3"

if [ -n "$1" ] && [ -n "$2" ]; then 
echo "write to file"
echo "%%L_RPT_START_DATE=$2" > $1
echo "%%L_RPT_END_DATE=$3" >> $1
#echo "%%L_CRD_FAILURE_THRESHOLD=$7" >> $1
echo "The Credit Assessment Report With Start Date=$2, End Date=$3" > $4
fi
#exit $?
