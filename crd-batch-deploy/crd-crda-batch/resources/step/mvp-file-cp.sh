#!/usr/bin/ksh
########################
### file name: mvp-file-cp.sh
###  INPUT_FODER = $1
###  OUT_PUT_FOLDER = $2
### it copy one file at a time to the data inbox
########################
if [ -d "$2/$6" ];
then
	echo "directory($2/$6) already exists"
else
	echo "write directory name $2/$6"
	mkdir $2/$6 
fi
if [ -d "$3/$6" ];
then
	echo "directory($3/$6) already exists"
else
	echo "write directory name $3/$6"
	mkdir $3/$6
fi
if [ -d "$4/$6" ];
then
	echo "directory($4/$6) already exists"
else
	echo "write directory name $4/$6"
	mkdir $4/$6 
fi
if [ -d "$5/$6" ];
then
	echo "directory($5/$6) already exists"
else
	echo "write directory name $5/$6"
	mkdir $5/$6
fi
for i in $1/$8/$7
do
   echo processing $i
   fname=$(basename $i)
   echo filename $fname
   
   cp $i $2/$6/$fname  
   mv $i ${i}_processed 
   exit 0
done

exit 225
