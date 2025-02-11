#!/usr/bin/ksh

# Filename: rtca_mug_bureau_dtl_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    BUREAU_DTL_FILE=$1
    BUREAU_DTL_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_bureau_dtl_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $BUREAU_DTL_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS prf_id 2: - 2: en
    /FIELDS fraud_cd 3: - 3: 
    /FIELDS pdate 4: - 4: en
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id, fraud_cd, pdate DESC
 
     
    /OUTFILE $BUREAU_DTL_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
