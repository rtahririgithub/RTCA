#!/usr/bin/ksh

# Filename: rtca_pre_ar1_deposit_request_sort.sh
# Function: remove duplicate ban   
#	           
####################################### 
if [ $# -eq 2 ]; then
    CRDA_AR1_DEPOSIT_REQUEST=$1
    CRDA_AR1_DEPOSIT_REQUEST_SORTED=$2
else
    echo ""
    echo "usage:rtca_pre_ar1_deposit_request_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CRDA_AR1_DEPOSIT_REQUEST 65535 "|" 
    /FIELDS customer_id 1: - 1: en
    /FIELDS ban 2: - 2: en 
    /FIELDS deposit_amount   3:  - 3:
    /FIELDS paid_amount   4:  - 4:
    /FIELDS paid_date 5: - 5:
    /FIELDS cancel_date 6: - 6:
    /FIELDS release_date 7: - 7:
    /FIELDS release_amount 8: - 8:

    /KEY customer_id, ban 
    /OUTFILE $CRDA_AR1_DEPOSIT_REQUEST_SORTED OVERWRITE
     
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
