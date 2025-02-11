#!/usr/bin/sh
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
# echo processing cp $1/$3 $2/$3 
#cp $1/$3 $2/$3 
echo "test"
echo "add odate dir $1/$3 in temp "
if [ -d  $1/$3 ]
then
    echo "dir :$1/$3 is alreadt exist"
else
    mkdir $1/$3 
fi
echo "check if it run yesterday "
if [ -d  $1/$2 ]
then
    echo "dir :$1/$2 is alreadt exist, it run on PREV day"
else
    mkdir $1/$2
fi
if [ -d  ${12}/leftover ]
then
    echo "dir ${12}/leftover is alreadt exist, it run on PREV day"
else
    mkdir ${12}/leftover
fi

find $1/$3/. \( ! -name . -prune \) -type f \( -name "$8" \) -exec rm -f {} \;
find $1/$3/. \( ! -name . -prune \) -type f \( -name "$9" \) -exec rm -f {} \;
touch $1/$3/$6
touch $1/$2/${10}
touch $1/$2/${11}
echo processing merge customers 
for i in `ls -t $1/$2/$4`
do
  echo processing $i
  cat $i >> $1/$3/$6 
done

touch $1/$3/$7

echo processing merge processed customers
for i in `ls -t $1/$2/$5`
do
  echo processing $i
  cat $i >> $1/$3/$7
done

echo find $1/. -mtime +3 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} 
find $1/. -mtime +3 \( ! -name . -prune \) \( -name "20*" \) -exec rm -r -f {} \;


