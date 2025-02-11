#!/usr/bin/ksh

# Filename: rtca_pre_ar1_rm_duplicates.sh
# Function: remove duplicate ban   
#	           
####################################### 
if [ $# -eq 2 ]; then
    CL9_AGENCY_REQUEST_PROCESSED=$1
    CL9_AGENCY_REQUEST_NODUPLICATES=$2
else
    echo ""
    echo "usage:rtca_pre_ar1_rm_duplicates.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CL9_AGENCY_REQUEST_PROCESSED 65535 "|" 
    /FIELDS CUSTOMER_ID 1: - 1: en 
    /FIELDS BAN 2: - 2: en 
    /FIELDS AGENCY_AMOUNT 3: - 3: 
    /FIELDS AGENCY_CD 4: - 4:
    /FIELDS AGENCY_STATUS  5: - 5:
    /FIELDS AGENCY_DATE  6: - 6:
    /DERIVEDFIELD count 1 en 10 
    /KEY CUSTOMER_ID, BAN 
    /SUMMARIZE TOTAL count  
    /OUTFILE $CL9_AGENCY_REQUEST_NODUPLICATES OVERWRITE
     
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
