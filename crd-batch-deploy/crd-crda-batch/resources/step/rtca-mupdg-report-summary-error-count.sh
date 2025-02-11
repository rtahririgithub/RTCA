#!/usr/bin/ksh
echo "Current batch process date: $1" >> $3
echo "Previous batch process date: $2" >> $3

if [ -f $4 ];
then
	echo "counting total records from file $4"
	echo "Total Customer Records Processed" >> $3
	wc -l < $4 >> $3
else
	echo "File $4 does not exist!"
fi

if [ -f $5 ];
then
	echo "counting error records from file $5"
	echo "Processing Error Records Count" >> $3
	wc -l < $5 >> $3
else
	echo "File $5 does not exist!"
fi

if [ -f $6 ];
then
	echo "counting closed BAN from file $6"
	echo "Count of closed BAN that was not included in MUPDG process" >> $3
	wc -l < $6 >> $3
else
	echo "File $6 does not exist!"
fi

if [ -f $7 ];
then
	echo "counting non-processed BAN dut to credit value out of scope from file $7"
	echo "Count of BAN that was not included in MUPDG process due to credit value out of scope for MUPDG" >> $3
	wc -l < $7 >> $3
else
	echo "File $7 does not exist!"
fi
