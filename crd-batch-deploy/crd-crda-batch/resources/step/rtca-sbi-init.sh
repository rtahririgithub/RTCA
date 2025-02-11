#!/usr/bin/ksh
# update local variables in

if [ $# -eq 3 ]; then
	echo " SBI main processbox directory path is: $1"
	echo " SBI credit processbox directory name processbox $2"
	echo " SBI deposit calc processbox directory name processbox $3"
else
    echo ""
    echo "usage:rtca-sbi-init.sh  [sbi directory path] [folder name (credit assessment)] [folder name (deposit calc)] [prev_sbi_start_date]"
    exit 1
fi



#Create SBI directory from processbox
if [ -d "$1" ];
then
	echo "directory(${1}) already exists"
else
	echo "write directory name ${1}"
	mkdir ${1}
fi

#Create SBI credit directory from processbox 
if [ -d "${1}/${2}" ];
then
	echo "directory(${1}/${2}) already exists"
else
	echo "write directory name ${1}/${2}"
	mkdir ${1}/${2}
fi

#Create SBI deposit calc directory from processbox
if [ -d "$1/$3" ];
then
	echo "directory(${1}/${3}) already exists"
else
	echo "write directory name ${1}/${3}"
	mkdir ${1}/${3}
fi

#exit $?
