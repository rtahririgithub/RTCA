#!/usr/bin/ksh

# Filename: rtca_pre_join_ban_details_and_ar1_deposit_request.sh
# Function: join drive file with ar1 pre processed  file
#           to retrieve all the deposit details 
#
#######################################
if [ $# -eq 3 ]; then
    CRUSTOMER_BAN_DETAILS=$1
    AR1_DEPOSIT_REQUEST_PROCESSED=$2
    DEPOSIT_REQUEST_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_ban_details_and_ar1_deposit_request.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/

    /INFILE $CRUSTOMER_BAN_DETAILS 65535 "|"
    /FIELDS CUSTOMER_ID 1: - 1: en
    /FIELDS BAN   2:  - 2: en
   
    
    /JOINKEYS ban 

    /INFILE $AR1_DEPOSIT_REQUEST_PROCESSED "|"
    /FIELDS BAN1             1: - 1: en
    /FIELDS DEPOSIT_ID       2:  - 2:
    /FIELDS ORDER_ID         3:  - 3:
    /FIELDS REQUEST_AMOUNT   4: - 4: en
    /FIELDS REQUEST_DATE     5: - 5:
    /FIELDS REQUEST_REASON   6:  - 6:
    /FIELDS DUE_DATE           7:  - 7:
    /FIELDS DEPOSIT_PAID_AMOUNT     8:  - 8:
    /FIELDS PAID_DATE           9: - 9:
    /FIELDS INTEREST_AMOUNT     10:  - 10:
    /FIELDS CANCEL_DATE        11: - 11:
    /FIELDS CANCEL_AMOUNT       12:  - 12:
    /FIELDS CANCEL_REASON    13: - 13:
    /FIELDS RELEASE_DATE       14: - 14:
    /FIELDS RELEASE_AMOUNT   15: -15:
    /FIELDS RELEASE_METHOD   16: - 16:
    /FIELDS RELEASE_REASON   17: - 17:
    
    /JOINKEYS BAN1 

    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)
    /DERIVEDFIELD CURRENT_DAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en


    /OUTFILE $DEPOSIT_REQUEST_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:CUSTOMER_ID, BAN, rightside:REQUEST_AMOUNT, REQUEST_DATE, DEPOSIT_ID, ORDER_ID,  REQUEST_REASON, DUE_DATE, DEPOSIT_PAID_AMOUNT,  PAID_DATE, INTEREST_AMOUNT, CANCEL_DATE,  CANCEL_AMOUNT, CANCEL_REASON,  RELEASE_DATE, RELEASE_AMOUNT, RELEASE_METHOD, RELEASE_REASON, CURRENT_DAY 

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
