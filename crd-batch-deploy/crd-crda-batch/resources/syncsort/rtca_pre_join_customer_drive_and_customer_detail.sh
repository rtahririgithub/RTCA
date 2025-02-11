#!/usr/bin/ksh

# Filename: rtca_pre_join_customer_drive_and_customer_detail.sh
# Function: join drive file with pre-processed cods customer  file
#           to retrieve all the active customer details. 
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_DRIVER_FILE=$1
    CUSTOMER_ODS_REF_CUSTOMER=$2
    CUSTOMER_DETAILS=$3
        
else
    echo ""
    echo "usage: rtca_pre_join_customer_drive_and_customer_detail.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/

    /INFILE $CUSTOMER_DRIVER_FILE 65535 "|"
    /FIELDS customer_id 1: - 1: en 
    /JOINKEYS  sorted customer_id

    /INFILE $CUSTOMER_ODS_REF_CUSTOMER "|"
    /FIELDS cust_id 1: - 1: en 
    /FIELDS cust_mast_sr_id 2:  - 2:
    /FIELDS cust_title 3:  - 3:
    /FIELDS first_name   4:  - 4:
    /FIELDS middle_name   5:  - 5:
    /FIELDS last_name 6: - 6:
    /FIELDS name_suffix 7: - 7:
    /FIELDS create_dt 8: - 8:
    /FIELDS cust_type 9: -9:
    /FIELDS cust_sub_type 10: - 10:
    /FIELDS cust_status_cd 11: - 11:
    /FIELDS revenue_seg_cd  12: - 12:
    /FIELDS employee_ind 13: -13:
    /JOINKEYS  sorted cust_id

    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)
    /DERIVEDFIELD CURRENT_DAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en


    /OUTFILE $CUSTOMER_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:customer_id, rightside:cust_mast_sr_id, cust_title, first_name, middle_name, last_name, name_suffix, create_dt, cust_type, cust_sub_type, cust_status_cd, revenue_seg_cd, employee_ind, CURRENT_DAY 

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
