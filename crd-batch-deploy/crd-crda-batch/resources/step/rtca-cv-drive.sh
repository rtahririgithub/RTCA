#!/usr/bin/ksh
###
### rtca-cv-drive.sh
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
find $3/. \( ! -name . -prune \) -type f \( -name "$6" \) -exec rm -f {} \;
find $3/. \( ! -name . -prune \) -type f \( -name "$7" \) -exec rm -f {} \;
touch $3/$4
touch $8
touch $9

echo processing merge customers 
for i in `ls -t $1`
do
  echo processing $i
  cat $i >> $3/$4 
done
#find $3/. \( ! -name . -prune \) -type f \( -name "$5" \) -exec rm -f {} \;
touch $3/$5

echo processing merge processed customers
for i in `ls -t $2`
do
  echo processing $i
  cat $i >> $3/$5
done

