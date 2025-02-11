#!/usr/bin/ksh
#GZIP_SUFFIX=.gz

echo "--Copying NDS Master file: $3 to $2/$4"
cp $1/$3 $2/$4
#gzip -S $GZIP_SUFFIX $2/$4
return $?