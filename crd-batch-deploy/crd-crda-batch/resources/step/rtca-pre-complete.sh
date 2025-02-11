#!/usr/bin/ksh

#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done

echo " step 1: make sub dir in archive"
if [ -d  $1/$4 ]
then
    echo "dir :$1/$4 is alreadt exist"
else
    mkdir $1/$4
fi


if [ -d  $1/$4/processbox  ]
then
    echo "dir :$1/$4/processbox is alreadt exist"  
else
    mkdir $1/$5/processbox 
   # mv $2/$5/*.dat $1/$5/processbox/.
   # gzip -S .gz $1/$5/processbox/*.dat
fi
if [ -d  $2/$3  ]
then
    echo "dir :$2/$3 is alreadt exist"
else
    mkdir $2/$3
fi

mv $5/DriverFile.* $1/$4/inbox/.
gzip -S .gz $1/$4/inbox/*
mv $2/$4/* $1/$4/processbox/.
gzip -S .gz $1/$4/processbox/*

echo find $2/$4/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} 
find $2/$4/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \; 

find $2/$3/.  \( ! -name . -prune \) -type f \( -name "*dat*" \) -exec rm -f {} \;