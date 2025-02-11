#!/usr/bin/ksh

# Filename: rtca_pre_customer_drive_rm_duplicates.sh
# Function: remove duplicated customers   
#	    in the customer drive file      
####################################### 
if [ $# -eq 2 ]; then
    CUSTOMER_DRIVE_FILE=$1
    CUSTOMER_DRIVE_FILE_NODUPLICATES=$2
else
    echo ""
    echo "usage:rtca_pre_customer_drive_rm_duplicates.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTOMER_DRIVE_FILE 65535 "|" 
    /FIELDS CUSTOMER_ID 1: - 1: en
    /DERIVEDFIELD count 1 en 10 
    /KEY CUSTOMER_ID 
    /SUMMARIZE TOTAL count  
    /OUTFILE $CUSTOMER_DRIVE_FILE_NODUPLICATES OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
