#!/usr/bin/ksh
# update local variables in

if [ $# -eq 2 ]; then
	echo " SBI main processbox directory path is: $1"
	echo " File Pattern $2"
else
    echo ""
    echo "usage:rtca-sbi-clean.sh  [sbi directory path] [file pattern to remove]"
    exit 1
fi


# Create SBI directory from processbox
if [ -d "$1" ];
then
	echo find $1/. \( ! -name . -prune \) -type f \( -name "$2" \) -exec rm -f {} \; 
	#find $1/. -type f -name $2 | xargs rm
	find $1/. \( ! -name . -prune \) -type f \( -name "$2" \) -exec rm -f {} \;
fi
