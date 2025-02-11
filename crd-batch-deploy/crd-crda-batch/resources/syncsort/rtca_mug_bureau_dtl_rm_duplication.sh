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
    /FIELDS req_id 2: - 2: en
    /FIELDS tran_id 3: - 3: en    
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id, req_id, tran_id 
    /SUMMARIZE TOTAL count  
    /STABLE
    /OUTFILE $FILE_NODUPLICATES OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
