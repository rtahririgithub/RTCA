#!/usr/bin/ksh

#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done
echo " temp $4"
echo " archive $5"
echo " step 1: delete dir and files old than 3 "

echo find $1/. -mtime +7 \( ! -name . -prune \) -type f \( -name "*.DAT*" \) -exec rm -f {} 
find $1/. -mtime +7 \( ! -name . -prune \) -type f \( -name "*.DAT*" \) -exec rm -f {} \;

echo find $1/. -mtime +7 \( ! -name . -prune \) -type f \( -name "*.txt" \) -exec rm -f {}
find $1/. -mtime +7 \( ! -name . -prune \) -type f \( -name "*.txt" \) -exec rm -f {} \;
echo find $2/. -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {}
find $2/. -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {}  \; 
echo find $3/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} 
find $3/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \;
echo find $4/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} 
find $4/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \; 
echo find $5/.  -mtime +30 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {}
find $5/.  -mtime +30 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \;
echo " clean up error box"
find $7/.  -mtime +30 \( ! -name . -prune \)  \( -name "crda*" \) -exec rm -r -f {} \;
echo " step 2: make sub dir in processbox, and passbox"

if [ -d  $2/$6 ]
then
    echo "dir :$2/$6 is alreadt exist"
else
    mkdir $2/$6
fi
if [ -d  $3/$6 ]
then
    echo "dir :$3/$6 is alreadt exist"
else
    mkdir $3/$6
fi
if [ -d  $4/$6 ]
then
    echo "dir :$4/$6  is alreadt exist"
else
    mkdir $4/$6 
fi
if [ -d  $5/$6 ]
then
    echo "dir :$5/$6 is alreadt exist"
else
    mkdir $5/$6
fi
if [ -d  $5/$6/processbox ]
then
    echo "dir :$5/$6/processbox is alreadt exist"
else
    mkdir $5/$6/processbox
fi
if [ -d  $5/$6/temp ]
then
    echo "dir :$5/$6/temp is alreadt exist"
else
    mkdir $5/$6/temp
fi
if [ -d  $5/$6/passbox ]
then
    echo "dir :$5/$6/passbox is alreadt exist"
else
    mkdir $5/$6/passbox
fi
if [ -d  $5/$6/outbox ]
then
    echo "dir :$5/$6/outbox is alreadt exist"
else
    mkdir $5/$6/outbox
fi

if [ -d  $5/$6/inbox ]
then
    echo "dir :$5/$6/inbox is alreadt exist"
else
    mkdir $5/$6/inbox
fi
if [ -d  $1/leftover ]
then
    echo "dir :$1/leftover is alreadt exist"
else
    mkdir $1/leftover
fi
find $1/leftover/.  -mtime +60 \( ! -name . -prune \)  \( -name "crda*" \) -exec rm -r -f  {} \;
