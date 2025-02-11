#!/usr/bin/ksh

# Filename: rtca_pre_drive_active_customer_sort.sh
# Function: remove duplicate ban   
#	           
####################################### 
if [ $# -eq 2 ]; then
    CRDA_ACTIVE_CUSTOMER=$1
    CRDA_ACTIVE_CUSTOMER_SORTED=$2
else
    echo ""
    echo "usage:rtca_pre_ar1_deposit_request_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CRDA_ACTIVE_CUSTOMER 65535 "|" 
    /FIELDS customer_id 1: - 1: en
    /FIELDS total_amount   2:  - 2:

    /KEY customer_id 
    /OUTFILE $CRDA_ACTIVE_CUSTOMER_SORTED OVERWRITE
     
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
