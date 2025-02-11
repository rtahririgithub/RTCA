#!/usr/bin/ksh
# update local variables in

if [ $# -eq 3 ]; then
	filename=$1
	max_col=$2
	fpath=$3
	echo " file name is: $filename"
	echo " max column: $max_col"
else
    echo ""
    echo "usage:rtca-uc-add-blank-col.sh  [filename] [max column]"
    exit 1
fi

if [ -e "$fpath/sbi-uc-warning-hist.tmp" ];
then
rm $fpath/sbi-uc-warning-hist.tmp
fi

if [ -e "$1" ];
then
echo "File $1 exists"


cat $filename | awk -v awk_maxcol=$max_col -F'|' '{NF=awk_maxcol}1' OFS="|" | awk '{gsub ("\001","|")}1' >$fpath/sbi-uc-warning-hist.tmp | mv $fpath/sbi-uc-warning-hist.tmp $filename

else

echo "Warning: SBI param file($1) missing"

fi

#exit $?
