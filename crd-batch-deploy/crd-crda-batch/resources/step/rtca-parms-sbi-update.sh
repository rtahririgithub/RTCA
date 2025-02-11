#!/usr/bin/ksh
# update local variables in

if [ $# -eq 3 ]; then
	echo " file name is: $1"
	echo " # L_SBI_START_DATE $2"
	echo " # L_SBI_END_DATE $3"
else
    echo ""
    echo "usage:rtca_parms-sbi-update.sh  [libmemfilepath] [sbi_start_date] [sbi_end_date]"
    exit 1
fi


if [ -e "$1" ];
then

echo "File $1 exists"
echo "write to file"
echo "%%L_SBI_START_DATE=$2" > $1
echo "%%L_SBI_END_DATE=$3" >> $1
echo "%%L_SBI_PREV_START=$2" >> $1
echo "%%L_SBI_PREV_END=$3" >> $1

else

echo "Warning: SBI param file($1) missing"

fi

#exit $?