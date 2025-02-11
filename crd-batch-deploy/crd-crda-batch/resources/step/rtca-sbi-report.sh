#!/usr/bin/ksh
# update local variables in
#NUM=$1

if [ $# -eq 5 ]; then
	echo " file name is: $5"
	echo " # header file is $1"
	echo " # L_SBI_START_DATE $2"
	echo " # L_SBI_END_DATE $3"
	echo " # db file is  $4"
else
    echo ""
    echo "usage:rtca-sbi-report.sh  [headerfile] [start_date] [end_date] [main file] [output file]"
    exit 1
fi


if [ -n "$1" ] && [ -n "$2" ]; then 
echo "write to file"
cat $1 $4 > $5
fi
#exit $?

