#!/usr/bin/ksh

echo "--Copying NDS Master file: $3 to $2/$4"
touch $1/$3`date +%Y%m%d%H%M%S`.done
mv $1/$3 $1/$3`date +%Y%m%d%H%M%S`.dat  

#mv $1/$3*.done $2 

#gzip -S .gz $1/$3*
#mv $1/$3* $4
#touch $2/sftpdone.txt
return $?
