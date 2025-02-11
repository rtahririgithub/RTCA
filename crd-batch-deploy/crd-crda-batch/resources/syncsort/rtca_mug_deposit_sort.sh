#!/usr/bin/ksh

# Filename: rtca_mug_deposit_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    DEPOSIT_FILE=$1
    DEPOSIT_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_deposit_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $DEPOSIT_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban 2: - 2: en
    /FIELDS pdate 19: - 19: en

    /KEY customer_id, ban, pdate desc 
   
    /OUTFILE $DEPOSIT_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
