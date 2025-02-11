#!/usr/bin/ksh

# Filename: rtca_mug_cust_dtl_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    CUST_DTL_FILE=$1
    CUST_DTL_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_cust_dtl_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUST_DTL_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS pdate 13: - 13: en
   
    /KEY customer_id, pdate  
      
    /OUTFILE $CUST_DTL_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
