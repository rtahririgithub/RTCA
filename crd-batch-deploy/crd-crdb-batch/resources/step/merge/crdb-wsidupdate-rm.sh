#!/usr/bin/ksh

#:crdb-wsidupdate-rm.sh 
#1. archive wsidupdate files 
#2.remove the  idupdatef it is more than 30  old 

echo "archive files $3/$1*$4.dat to $2"
mv $3/$1*$4.dat $2 
 
#echo "clean archive $1"

find $2/. -mtime +30 \( ! -name . -prune \) -type f \( -name "$1*.dat" \) -exec rm -f {} \;
#find $3/.  \( ! -name . -prune \) -type f \( -name "$1$4.dat" \) -exec rm -f {} \;
