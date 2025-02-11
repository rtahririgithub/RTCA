#!/usr/bin/ksh

#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done
 echo processing cp $1 $4
cp $1 $4
echo processing cp $2 $5
cp $2 $5
echo processing cp $3 $6
cp $3 $6



return $?



