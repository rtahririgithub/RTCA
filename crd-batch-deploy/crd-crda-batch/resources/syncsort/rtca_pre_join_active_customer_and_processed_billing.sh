#!/usr/bin/ksh

# Filename: rtca_pre_join_active_customer_and_processed_billing.sh
# Function: join drive file with pre-processed billing  file
#           to retrieve all the active customers with open or closed account. 
#
#######################################
if [ $# -eq 3 ]; then
    TCM_CUSTOMER_DRIVE_FILE=$1
    CUSTOMER_REF_BILLING_FILTED=$2
    CUSTOMER_BAN_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_active_customer_and_processed_billing.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/

    /INFILE $TCM_CUSTOMER_DRIVE_FILE 65535 "|"
    /FIELDS customer_id 1: - 1: en
    /FIELDS total_billed_amount 2: - 2:
    /JOINKEYS  customer_id

    /INFILE $CUSTOMER_REF_BILLING_FILTED "|"
    /FIELDS cust_id 1: - 1: en 
    /FIELDS ban   2:  - 2:
    /FIELDS ban_status 3: - 3:
    /FIELDS ban_status_date 4: - 4:
    /FIELDS bsid 5: - 5:
    /FIELDS cust_tp 6: -6:
    /JOINKEYS  cust_id

    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)
    /DERIVEDFIELD CURRENT_DAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en

    /OUTFILE $CUSTOMER_BAN_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:customer_id, rightside:ban, ban_status, ban_status_date, bsid, leftside: total_billed_amount, CURRENT_DAY

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
