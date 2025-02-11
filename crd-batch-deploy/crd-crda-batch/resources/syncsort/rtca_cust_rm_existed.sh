#!/usr/bin/ksh

# Filename: rtca_cust_rm_existed.sh
# Function: remove already existed customer from prev files
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_DRIVER_FILTE=$1   
    CUSTOMER_NEW_DRIVER=$2
    CUSTOMER_NOT_IN_NEW=$3
        
else
    echo ""
    echo "usage:rtca_cust_rm_existed.sh [input file1] [input file2]  [output file] "

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

    /INFILE $CUSTOMER_NEW_DRIVER 65535 "|"
    /FIELDS cust 1: - 1: en
    /JOINKEYS cust 


    /OUTFILE $CUSTOMER_NOT_IN_NEW 65535  OVERWRITE

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
