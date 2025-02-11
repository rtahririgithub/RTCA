#!/usr/bin/ksh
########################
### file name mvp-run.sh
###  INPUT_FODER = $1
###  OUT_PUT_FOLDER = $2
### process 1 file at a time
########################

echo "6 is $6, 7 is $7, 8 is ${8}, 9 is $9"
for i in $1/$5
do
   echo processing $i
   fname=$(basename $i)
   echo filename $fname
   echo "run migration, input file is $i "
  
   if  echo $i | grep -q ${6} ; 
   then
   	echo "${10} -Ddfc.properties.file=$2/dfc-config/dfc.properties -Dmvp3.config=$2/dfc-config/config.properties -classpath ${11}/dctm.jar:${12}:$2/util.jar com.telus.mvp3.MigratorApp  ${9} $fname"
   	${10} -Ddfc.properties.file=$2/dfc-config/dfc.properties -Dmvp3.config=$2/dfc-config/config.properties -classpath ${11}/dctm.jar:${12}:$2/util.jar com.telus.mvp3.MigratorApp ${9} $fname
   else
   	echo "${10} -Ddfc.properties.file=$2/dfc-config/dfc.properties -Dmvp3.config=$2/dfc-config/config.properties -classpath ${11}/dctm.jar:${12}:$2/util.jar com.telus.mvp3.MigratorApp  ${9} $fname"
   	${10} -Ddfc.properties.file=${2}/dfc-config/dfc.properties -Dmvp3.config=$2/dfc-config/config.properties -classpath ${11}/dctm.jar:${12}:$2/util.jar com.telus.mvp3.MigratorApp ${9} $fname
   fi  
   mv $i ${i}_processed 
   exit 0
done

