#!/usr/bin/ksh

# Filename: rtca_pre_drive_file_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    DRIVER_FILE=$1
    DRIVER_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_driver_file_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $DRIVER_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS pdate 3: - 3: en
   
    /KEY customer_id, pdate descending 
    
    
    /OUTFILE $DRIVER_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
