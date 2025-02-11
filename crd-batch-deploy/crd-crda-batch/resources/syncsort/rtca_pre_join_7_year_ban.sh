#!/usr/bin/ksh

# Filename: rtca_pre_join_7_year.sh
# Function: join drive file with pre-processed billing  file
#           to retrieve all the active customers with open or closed account. 
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_REF_BILLING_FILTED1=$1   
    CUSTOMER_REF_BILLING_FILTED2=$2
    CUSTOMER_BAN_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_7_year.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/
   /join unpaired leftside only
    /INFILE $CUSTOMER_REF_BILLING_FILTED1 65535 "|"
    /FIELDS customer_id 1: - 1: en
    /JOINKEYS sorted  customer_id

    /INFILE $CUSTOMER_REF_BILLING_FILTED2 "|"
    /FIELDS cust_id 1: - 1: en 
    /JOINKEYS  sorted cust_id


    /OUTFILE $CUSTOMER_BAN_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:customer_id

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
