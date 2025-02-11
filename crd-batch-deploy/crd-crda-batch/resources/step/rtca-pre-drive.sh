#!/usr/bin/ksh
###
### rtca-pre-drive.sh
#
#
###
#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done
# echo processing cp $1 $2/. 
find $2/. \( ! -name . -prune \) -type f \( -name "$5" \) -exec rm -f {} \;
#touch $1
touch $2/$5
#cat $1 >> $2/$5
 
echo processing merge 
for i in `ls -t $1/$4`
do
  echo processing $i
  cat $i >> $2/$5 
done

#return $?



