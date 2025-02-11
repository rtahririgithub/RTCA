#!/usr/bin/ksh

# Filename: rtca_pre_ban_not_included_details_cv.sh
# Function: join drive file from trt with billing  customer crd file
#           to retrieve all the  customer details 
#
#######################################
if [ $# -eq 3 ]; then
    TCM_DRIVE_FILE=$1
    CUS_CRD_FILE=$2
    DRIVER_CRD_FILE=$3
        
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
    /JOIN UNPAIRED LEFTSIDE
    /INFILE $TCM_DRIVE_FILE "|"
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban 2: - 2: en
    /FIELDS banstatus 3: - 3: 
    /JOINKEYS  customer_id 

    /INFILE $CUS_CRD_FILE "|" 
    /FIELDS cust_id 1: - 1: en 
    /FIELDS crd_v   2:  - 2:
    /JOINKEYS  cust_id

    /OUTFILE $DRIVER_CRD_FILE 65535  OVERWRITE
    /REFORMAT leftside:customer_id, ban, banstatus, rightside:crd_v

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
