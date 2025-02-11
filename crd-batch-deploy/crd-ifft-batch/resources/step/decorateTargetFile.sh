#!/usr/bin/ksh

echo "--Decorating the manifested target file and moving it to the outbox."

cat $1 $2 $3 > $4

return $?