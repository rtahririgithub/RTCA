#!/usr/bin/ksh

echo "--Moving extract file: $3"
echo "== Executing mv -f $1/$3 $2/$3"
echo "== Executing cp $1/$3 $5"

cp $1/$3 $5
mv -f $1/$3 $2/$3
rm $1/$4
return $?
