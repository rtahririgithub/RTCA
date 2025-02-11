#!/usr/bin/ksh

# Filename: rtca_mug_ban_dtl_sort.sh 
# Function: remove duplicated    
#	        
####################################### 
if [ $# -eq 2 ]; then
    BAN_DTL_FILE=$1
    BAN_DTL_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_ban_dtl_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $BAN_DTL_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban 2: - 2: en
    /FIELDS pdate 7: - 7: en
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id, ban, pdate desc 
      
    /OUTFILE $BAN_DTL_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
