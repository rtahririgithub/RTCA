#!/usr/bin/ksh

# Filename: rtca_mug_cust_sort.sh 
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
    
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id 
    /SUMMARIZE TOTAL count  
    /OUTFILE $FILE_NODUPLICATES OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
