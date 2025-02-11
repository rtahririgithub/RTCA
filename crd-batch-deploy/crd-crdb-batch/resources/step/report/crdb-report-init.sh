
MYDATE="`date +%Y%m%d`"
MYCPROFILE="$1.$MYDATE"
MYCPROFILEID="$2.$MYDATE"
DEST=$3

if [ -d $DEST ]
then 
	echo "$DEST found"
else
	echo "$DEST not found. Creating new folder"
	mkdir $DEST
fi

if [ -f  $MYCPROFILE ]
then
	cp $MYCPROFILE $DEST
else
	echo "$MYCPROFILE not found."
	exit 1
fi

if [ -f  $MYCPROFILEID ]
then
	cp $MYCPROFILEID $DEST
else
	echo "$MYCPROFILEID not found."
	exit 1
fi

exit $?

