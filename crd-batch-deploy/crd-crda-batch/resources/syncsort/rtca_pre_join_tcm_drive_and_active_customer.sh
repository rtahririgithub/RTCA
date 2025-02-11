#!/usr/bin/ksh

# Filename: rtca_pre_join_tcm_drive_and_active_customer.sh
# Function: join drive file from trt with billing active customer file
#           to retrieve all the active customer  
#
#######################################
if [ $# -eq 3 ]; then
    TCM_DRIVE_FILE=$1
    CUSODS_REF_BILLING_ACCOUNT_OPEN=$2
    CUSODS_REF_BILLING_ACCOUNT_OPEN_DRIVER_FILE=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_tcm_drive_and_active_customer.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/

    /INFILE $TCM_DRIVE_FILE 65535 "|"
    /FIELDS ban 1: - 1: en
    /FIELDS total_billed_amount 2: - 2:

    /JOINKEYS  ban

    /INFILE $CUSODS_REF_BILLING_ACCOUNT_OPEN "|"
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban1 2: - 2: en
    /JOINKEYS ban1 


    /OUTFILE $CUSODS_REF_BILLING_ACCOUNT_OPEN_DRIVER_FILE 65535  OVERWRITE
    /REFORMAT rightside:customer_id, leftside:total_billed_amount

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
