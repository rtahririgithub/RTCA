#!/usr/bin/ksh

# Filename: rtca_pre_ban_total_amt.sh
# Function: join drive file with drive total  file
#           to get total for each of the accounts. 
#
#######################################
if [ $# -eq 3 ]; then
    TCM_CUSTOMER_DRIVE_FILE=$2
    CUSTOMER_REF_BILLING_FILTED=$1
    CUSTOMER_BAN_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_ban_total_amt.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/

    /JOIN UNPAIRED LEFTSIDE
    /INFILE $CUSTOMER_REF_BILLING_FILTED "|"
    /FIELDS cust_id 1: - 1: en 
    /FIELDS ban   2:  - 2: en
    /FIELDS ban_status 3: - 3:
    /FIELDS ban_status_date 4: - 4:
    /FIELDS bsid 5: - 5:
    /FIELDS tp 6: - 6:
    /FIELDS tm 7: - 7:
    /JOINKEYS ban 

    /INFILE $TCM_CUSTOMER_DRIVE_FILE 65535 "|"
    /FIELDS ban1 1: - 1: en
    /FIELDS total_billed_amount 2: - 2:
    /JOINKEYS ban1

    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)
    /DERIVEDFIELD CURRENT_DAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en

    /OUTFILE $CUSTOMER_BAN_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:cust_id, ban, ban_status, ban_status_date, bsid, rightside:total_billed_amount, CURRENT_DAY

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
