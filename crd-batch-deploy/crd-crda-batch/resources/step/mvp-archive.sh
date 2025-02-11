#!/usr/bin/ksh
#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done

#GZIP_SUFFIX=.gz

#gzip -S $GZIP_SUFFIX $3*
mv $1/* $2/.
#cp $2/$1 $3 
#echo find $2/.  \( ! -name . -prune \) -type f \( -name "*.gz" \) -exec rm -f {} 
#find $2/. \( ! -name . -prune \) -type f \( -name "$1" \) -exec rm -f {} \;
#echo find $3/. -mtime +365 \( ! -name . -prune \) -type f \( -name "*.csv*" \) -exec rm -f {}
#find $3/. -mtime +365 \( ! -name . -prune \) -type f \( -name "*.gz" \) -exec rm -f {} \;
#find $4/. \( ! -name . -prune \) -type f \( -name "*.dat" \) -exec rm -f {} \;



return $?



