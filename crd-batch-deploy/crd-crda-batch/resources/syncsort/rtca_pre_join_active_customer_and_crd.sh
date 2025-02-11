#!/usr/bin/ksh

# Filename: rtca_pre_join_drv_active_crd.sh
# Function: join active customer file crd customer file
#           to retrieve the active customer with right
#           credit values. 
#
#######################################
if [ $# -eq 3 ]; then
    CUSODS_REF_BILLING_ACCOUNT_OPEN=$1
    CUSTOMER_CREDIT_PROFILE_FILTERED=$2
    CUSODS_REF_BILLING_ACCOUNT_OPEN_CREDIT_VALUE_FILTERED_DRIVER_FILE=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_drv_active_crd.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/

    /INFILE $CUSODS_REF_BILLING_ACCOUNT_OPEN 65535 "|"
    /FIELDS customer_id 1: - 1: en 
    /FIELDS total_billed_amount 2: - 2:
    /JOINKEYS SORTED customer_id

    /INFILE $CUSTOMER_CREDIT_PROFILE_FILTERED "|"
    /FIELDS cust_id 1: - 1: en 
    /FIELDS crd_v   2:  - 2:
    /JOINKEYS SORTED cust_id


    /OUTFILE $CUSODS_REF_BILLING_ACCOUNT_OPEN_CREDIT_VALUE_FILTERED_DRIVER_FILE 65535  OVERWRITE
    /REFORMAT leftside:customer_id,total_billed_amount

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
