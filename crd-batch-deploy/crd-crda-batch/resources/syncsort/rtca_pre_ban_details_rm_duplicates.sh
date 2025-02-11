#!/usr/bin/ksh

# Filename: rtca_pre_customer_ban_rm_duplicates.sh
# Function: remove duplicates with customer and ban as keys  
#	           
####################################### 
if [ $# -eq 2 ]; then
        CUSTOMER_BAN_DETAILS=$1
        CUSTOMER_BAN_DETAILS_NODUPLICATES=$2
else
    echo ""
    echo "usage:rtca_pre_customer_ban_rm_duplicates.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTOMER_BAN_DETAILS 65535 "|" 
    /FIELDS CUST_ID 1: - 1: en 
    /FIELDS BAN     2: - 2: en
    /FIELDS BAN_STATUS 3: - 3:
    /FIELDS BAN_ST_DATE 4: - 4:
    /FIELDS BMS_ID     5: - 5:
    /FIELDS TOTAL_BILLED_AMOUNT 6: - 6:
    
    /DERIVEDFIELD count 1 en 10 
    /KEYS CUST_ID, BAN 
    /SUMMARIZE TOTAL count  
    /OUTFILE $CUSTOMER_BAN_DETAILS_NODUPLICATES OVERWRITE
     
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
