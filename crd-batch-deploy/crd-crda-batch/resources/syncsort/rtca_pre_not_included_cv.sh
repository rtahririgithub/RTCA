#!/usr/bin/ksh

# Filename: rtca_pre_not_included_cv.sh
# Function: retrievce closed  customers from driver  file
#           1. retrieve all the  customers with credit values not:
#              D, E, G, and R.
#
#######################################
if [ $# -eq 2 ]; then
    CUSTOMER_DRIVE=$1
    CUSTOMER_OUT_FILE=$2

else
    echo ""
    echo "usage: "
    echo "rtca_pre_not_included_closed:.sh  [input file] [output file] "

    exit 1
fi


syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTOMER_DRIVE  65535 "|"
    /FIELDS cust_id  1: - 1: en 
    /FIELDS ban 2: - 2: en 
    /FIELDS ban_status 3: - 3: 

    /KEYS cust_id, ban 

    /CONDITION filter (ban_status="O" ) 
    /INCLUDE filter
 
    /OUTFILE $CUSTOMER_OUT_FILE OVERWRITE
    /REFORMAT cust_id, ban, ban_status      
    /COLLATINGSEQUENCE DEFAULT ASCII

    /SILENT 
EOF

return $?
