#!/usr/bin/ksh

# Filename: rtca_mug_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    INPUT_FILE=$1
    SORTED_FILE=$2
else
    echo ""
    echo "usage:rtca_mug_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $INPUT_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
   
    /KEY customer_id
 
 
    /OUTFILE $SORTED_FILE OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
