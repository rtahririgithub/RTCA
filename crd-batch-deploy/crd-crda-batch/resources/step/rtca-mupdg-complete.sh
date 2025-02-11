#!/usr/bin/ksh

#for i in `ls -t $3`
#do
#  echo processing $i 
#  mv $i "$i`date +%Y%m%d_%H%M%S`" 
#done
echo " step 1: archive files  "

if [ -d  $1/$5  ]
then
    echo "dir :$1/$5 is alreadt exist"
else
    mkdir $1/$5
fi
if [ -d  $1/$5/temp  ]
then
    echo "dir :$1/$4/temp is alreadt exist"
else
    mkdir $1/$5/temp 
fi
if [ -d  $1/$5/temp/job  ]
then
    echo "dir :$1/$4/temp/job is alreadt exist"
else
    mkdir $1/$5/temp/job
fi

if [ -d  $1/$4  ]
then
    echo "dir :$1/$4 is alreadt exist"
else
    mkdir $1/$4
fi

if [ -d  $1/$4/passbox  ]
then
    echo "dir :$1/$4/passbox is alreadt exist"
else
    mkdir $1/$4/passbox
fi
if [ -d  $1/$5/outbox  ]
then
    echo "dir :$1/$5/outbox is alreadt exist"
else
    mkdir $1/$5/outbox
fi

mv $2/$5/*.dat $1/$5/temp/.
mv $3/$4/*.dat $1/$4/passbox/.
mv $6/$7*.csv $1/$5/outbox/.
mv $2/$5/job/* $1/$5/temp/job/.

gzip -S .gz $1/$5/temp/*.dat
gzip -S .gz $1/$4/passbox/*.dat
gzip -S .gz $1/$5/outbox/*
gzip -S .gz $1/$5/temp/job/*
echo " step 2: clean up dir and temp files"

echo find $2/$5/. -mtime +3 \( ! -name . -prune \) -type f \( -name "Temp*" \) -exec rm -f {}

find $2/$5/.  \( ! -name . -prune \) -type f \( -name "*Temp*.dat" \) -exec rm -f {} \;
find $2/$5/. \( ! -name . -prune \) -type f \( -name "*temp*.dat" \) -exec rm -f {} \;

echo find $2/$5/.  -mtime +7 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {}
find $2/$5/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \;
echo find $3/$5/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {}
find $3/$5/.  -mtime +5 \( ! -name . -prune \)  \( -name "20*" \) -exec rm -r -f {} \;

find $2/$4/.  \( ! -name . -prune \) -type f \( -name "*.dat" \) -exec rm -f {} \;

return $?



