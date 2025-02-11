#!/usr/bin/ksh

# Filename: rtca_mug_cust_rm_duplicates.sh 
# Function: remove duplicated    
#	        
####################################### 
if [ $# -eq 2 ]; then
    INPUT_FILE=$1
    FILE_NODUPLICATES=$2
else
    echo ""
    echo "usage:rtca_mug_cust_rm_duplicates.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $INPUT_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS prf_id 2: - 2: en
    /FIELDS fraud_cd 3: - 3:    
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id, fraud_cd 
    /SUMMARIZE TOTAL count  
    /STABLE
    /OUTFILE $FILE_NODUPLICATES OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
