#!/usr/bin/ksh
###
### rtca-cv-leftover.sh
#
#
###
#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done
# echo processing cp $1/$3 $2/$3 
#cp $1/$3 $2/$3 


echo processing merge processed customers
find $1/. \( ! -name . -prune \) -type f \( -name "$4" \) -exec rm -f {} \;

for i in `ls -t $1/$2`
do
  echo processing $i
  cat $i >> $1/$3
done

