#!/usr/bin/ksh

# Filename: rtca_mug_prf_sort.sh 
# Function: sort     
#	          
####################################### 
if [ $# -eq 2 ]; then
    PRF_FILE=$1
    PRF_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_prf_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $PRF_FILE 65535  
    /FIELDS prf_id 1 en 18   
    /FIELDS customer_id 19 en 9
    /FIELDS fild1 28 character 2027   
    /FIELDS pdate 2055 en 8 
     
    /KEY customer_id,  pdate desc 
     
    /OUTFILE $PRF_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
