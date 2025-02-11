#!/usr/bin/ksh

echo "--Copying extract file: $3"
echo "== Executing cp $1/$3.* $2"

cp $1/$3.* $2

return $?
