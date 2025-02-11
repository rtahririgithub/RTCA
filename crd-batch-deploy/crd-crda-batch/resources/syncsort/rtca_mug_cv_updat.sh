#!/usr/bin/ksh

# Filename: rtca_mug_merge-new.sh
# Function: join  file with pre-processed   file
#           to retrieve all the  customers needed. 
#
#######################################
if [ $# -eq 5 ]; then
    CUSTOMER_PREV=$1
    CUSTOMER_LEFTOVER=$2
    CUSTOMER_CURRENT=$3
    CUSTOMER_READY=$4         
    CUSTOMER_TEMP=$5
else
    echo ""
    echo "usage:rtca_mug_merge_new.sh  [input file1] [input file2]  [output file] "

    exit 1
fi
#for i in `ls -t $1`
#do
#  echo processing $i
#  cat $i >> $3
#done
touch $3

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join files*/
    /join unpaired leftside only  
    /INFILE $CUSTOMER_PREV 65535 "|"
    /FIELDS customer_id 1: - 1: en
    /JOINKEYS  customer_id

    /INFILE $CUSTOMER_LEFTOVER 65535 "|"
    /FIELDS cust_id 1: - 1: en 
    /JOINKEYS  cust_id

    /OUTFILE $CUSTOMER_TEMP 65535  OVERWRITE

    /STATISTICS
    /WARNINGS 100
    /END

!EOF

cat $CUSTOMER_CURRENT $CUSTOMER_TEMP >> $CUSTOMER_READY

return $?
