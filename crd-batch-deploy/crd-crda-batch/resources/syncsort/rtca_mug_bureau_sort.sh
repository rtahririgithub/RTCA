#!/usr/bin/ksh

# Filename: rtca_mug_bureau_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    BUREAU_FILE=$1
    BUREAU_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_bureau_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $BUREAU_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS pdate 19: - 19: en
   
    /KEY customer_id, pdate DESC
 
 
    /OUTFILE $BUREAU_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
