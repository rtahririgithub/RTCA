#!/usr/bin/ksh

# Filename: rtca_mug_cv_update_sort.sh 
# Function: sort    
#	        
####################################### 
if [ $# -eq 2 ]; then
    CV_UPDATE_FILE=$1
    CV_UPDATE_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_cv_update_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CV_UPDATE_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS pdate 4: - 4: en
   
    /KEY customer_id, pdate DESC
 
 
    /OUTFILE $CV_UPDATE_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
