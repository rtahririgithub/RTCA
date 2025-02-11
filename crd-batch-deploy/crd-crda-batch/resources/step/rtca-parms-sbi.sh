#!/usr/bin/ksh
# update local variables in

if [ $# -eq 7 ]; then
	echo " file name is: $1"
	echo " # L_SBI_START_DATE $2"
	echo " # L_SBI_END_DATE $3"
	echo " # L_SBI_PREV_START $4"
	echo " # L_SBI_PREV_END $5"
	echo " # L_SBI_CURRENT_DATE $6"
	echo " # L_SBI_PREV_ODATE $7"
else
    echo ""
    echo "usage:rtca_parms-sbi.sh  [libmemfilepath] [sbi_start_date] [sbi_end_date] [prev_sbi_start_date] [prev_sbi_end_date] [current_date] [prev_date] | noskip"
    exit 1
fi



if [ -e "$1" ];
then

echo "File $1 exists"
echo "write to file"
echo "%%L_SBI_START_DATE=$3" > $1
echo "%%L_SBI_END_DATE=$6`date +%H%M%S`" >> $1
echo "%%L_SBI_PREV_START=$4" >> $1
echo "%%L_SBI_PREV_END=$5" >> $1

else

echo "Create param file"
touch $1 
echo "write to file"
echo "%%L_SBI_START_DATE=20130126000000" > $1
echo "%%L_SBI_END_DATE=${6}`date +%H%M%S`" >> $1
echo "%%L_PREV_START=20130126000000" >> $1
echo "%%L_PREV_END=${6}`date +%H%M%S`" >> $1

chmod 776 $1

fi

#exit $?