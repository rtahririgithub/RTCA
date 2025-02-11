#!/usr/bin/ksh

# Filename: rtca_pre_customer_detail_rm_duplicates.sh
# Function: remove duplicated customers   
#	           
####################################### 
if [ $# -eq 2 ]; then
    CRDA_INPUT_FILE=$1
    CRDA_OUTPUT_FILE=$2
else
    echo ""
    echo "usage:rtca_pre_customer_detail_rm_duplicates.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CRDA_INPUT_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
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

    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id 
    /SUMMARIZE TOTAL count  
    /OUTFILE $CRDA_OUTPUT_FILE OVERWRITE
     
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
