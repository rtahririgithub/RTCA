#!/usr/bin/ksh
#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done

#GZIP_SUFFIX=.gz

#gzip -S $GZIP_SUFFIX $3*

echo find $1/.  \( ! -name . -prune \) -type f \( -name "*Report.csv" \) -exec rm -f {} 
find $1/. \( ! -name . -prune \) -type f \( -name "*Report.csv" \) -exec rm -f {} \;
echo find $1/.  \( ! -name . -prune \) -type f \( -name "*Score.csv*" \) -exec rm -f {}
find $1/.  \( ! -name . -prune \) -type f \( -name "*e.csv" \) -exec rm -f {} \;
find $1/.  \( ! -name . -prune \) -type f \( -name "*d.csv" \) -exec rm -f {} \;



return $?



