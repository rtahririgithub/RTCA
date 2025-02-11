#!/usr/bin/ksh

# Filename:rtca_mug_leftover.sh
# Function: join drive file with result processed file
#           to retrieve all the customers that are not processed. 
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_DRIVER_FILTE=$1   
    CUSTOMER_PROCESSED_FILE=$2
    CUSTOMER_LEFTOVER=$3
        
else
    echo ""
    echo "usage:rtca_mug_leftover.sh [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/
    /join unpaired leftside only
    /INFILE $CUSTOMER_DRIVER_FILTE 65535 "|"
    /FIELDS cust_id 1: - 1: en
    /JOINKEYS  cust_id

    /INFILE $CUSTOMER_PROCESSED_FILE 65535 "|"
    /FIELDS cust 1: - 1: en
    /JOINKEYS cust 


    /OUTFILE $CUSTOMER_LEFTOVER 65535  OVERWRITE

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
