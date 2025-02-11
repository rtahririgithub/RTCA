#!/bin/ksh

# remove duplicates from the specified file 

# check the parameters
if [ $# -ne 1 ] ; then
  echo "Usage : $0 <filename>"
  echo "    eg  $0 /work/users/prfin/fin/data/inbox/invoice/amdocs-inv-unpaired.dat"
  exit 1
fi

# setup vars
DIRECTORY=`dirname $1`
FILENAME=`basename $1`
TIMESTAMP=`date +%Y%m%d%H%M%S`
#FILENAME_BEFORE=${FILENAME}.before_correction.${TIMESTAMP}
FILENAME_AFTER=${FILENAME}.after_correction.${TIMESTAMP}

# remove duplicates from the specified file 
cd ${DIRECTORY}
sort ${FILENAME} | uniq > ${FILENAME_AFTER}
rm ${FILENAME}
cp ${FILENAME_AFTER} ${FILENAME}
rm ${FILENAME_AFTER}
