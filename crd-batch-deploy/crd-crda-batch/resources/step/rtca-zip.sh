#!/usr/bin/ksh

#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done

GZIP_SUFFIX=.gz

gzip -S $GZIP_SUFFIX $1