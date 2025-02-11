#!/usr/bin/ksh

echo "--Copying NDS Master file: $3 to $2/$4"
cp $1/$3* $2 

return $?
