#!/usr/bin/ksh

echo "--Moving extract file: $3"
echo "== Executing mv -f $1/$3.* $2/$3"
gunzip $1/$3.*
mv -f $1/$3.* $2/$3
#gunzip $2/$3
return $?
