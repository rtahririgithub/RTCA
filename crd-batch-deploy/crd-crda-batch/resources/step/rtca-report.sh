#!/usr/bin/ksh
# update local variables in
#NUM=$1
echo " file name is: $5"
echo " # header file is $1"
echo " # L_RPT_START_DATE $2"
echo " # L_RPT_END_DATE $3"
echo " # db file is  $4"

if [ -n "$1" ] && [ -n "$2" ]; then 
echo "write to file"
#echo "The Credit Assessment Report With Start Date=$2, End Date=$3" > $6
cat $1 $4 > $5
fi
#exit $?
