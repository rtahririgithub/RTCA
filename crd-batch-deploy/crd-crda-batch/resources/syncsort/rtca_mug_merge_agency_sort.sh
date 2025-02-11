#!/usr/bin/ksh

# Filename: rtca_mug_merge_sort.sh 
# Function: join  file with pre-processed   file
#           to retrieve all the  customers needed. 
#
#######################################
if [ $# -eq 5 ]; then
    CUSTOMER_PREV_TOTAL=$1
    CUSTOMER_PREV=$2
    FILE_NAME=$3
    CUSTOMER_LEFTOVER=$4
    FILE_PATTERN=$5
else
    echo ""
    echo "usage:rtca_mug_merge_sort.sh  [input file1] [input file2]  [output file] "

    exit 1
fi
find $2/. \( ! -name . -prune \) -type f \( -name "$5" \) -exec rm -f {} \;
touch $2/$3

for i in `ls -t $1`
do
  echo processing $i
  cat $i >> $CUSTOMER_PREV/$FILE_NAME
done

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/
  
    /INFILE $CUSTOMER_PREV/$FILE_NAME 65535 "|"
    /FIELDS customer_id 1: - 1: en
    /FIELDS ban 2: - 2: en 
    /FIELDS pdat 7: - 7:
    /KEY  customer_id, ban, pdat desc 

    /OUTFILE $CUSTOMER_LEFTOVER 65535  OVERWRITE

    /STATISTICS
    /WARNINGS 100
    /END

!EOF

return $?
