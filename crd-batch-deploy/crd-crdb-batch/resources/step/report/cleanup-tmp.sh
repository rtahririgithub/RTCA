if [ -f  $1/*.tmp ]
then
	rm $1/*.tmp
fi
exit $?