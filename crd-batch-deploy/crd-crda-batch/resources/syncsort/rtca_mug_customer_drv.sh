#!/usr/bin/ksh

# Filename: rtca_mug_customer_drv.sh
# Function: join drive file with pre-processed billing  file
#
#           to retrieve all the active customers with open or closed account. 
#
#######################################
if [ $# -eq 6 ]; then
    CUSTOMER_CURRENT=$1/$6
    CUSTOMER_PREV=$2
    CUSTOMER_PREV_PROCESSED=$3
    CUSTOMER_READY=$4
    CUSTOMER_OUT=$5         
else
    echo ""
    echo "usage:rtca_mug_customer_drv.sh  [input file1] [input file2]  [output file] "

    exit 1
fi
echo "check it run on PREV day"
if [ -d  $1 ]
then
    echo "dir :$1 is alreadt exist, it run on PREV day"
else
    mkdir $1
fi
touch $CUSTOMER_CURRENT
syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/
   /join unpaired leftside only
    /INFILE $CUSTOMER_PREV 65535 "|"
    /FIELDS customer_id 1: - 1: en
    /JOINKEYS  customer_id

    /INFILE $CUSTOMER_PREV_PROCESSED 65535 "|"
    /FIELDS cust_id 1: - 1: en 
    /JOINKEYS  cust_id


    /OUTFILE $CUSTOMER_READY 65535  OVERWRITE
    /REFORMAT leftside:customer_id

    /STATISTICS
    /WARNINGS 100
    /END

!EOF

cat $CUSTOMER_CURRENT $CUSTOMER_READY >> $CUSTOMER_OUT

return $?
