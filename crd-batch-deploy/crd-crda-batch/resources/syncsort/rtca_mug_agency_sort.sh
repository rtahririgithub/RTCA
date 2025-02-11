#!/usr/bin/ksh

# Filename: rtca_mug_agency_sort.sh 
# Function: sort     
#	          
####################################### 
if [ $# -eq 2 ]; then
    AGENCY_FILE=$1
    AGENCY_FILE_SORTED=$2
else
    echo ""
    echo "usage:rtca_mug_agency_sort.sh  [input file] [output file] "
    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $AGENCY_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban 2: - 2: en
    /FIELDS pdate 7: - 7: en
    /DERIVEDFIELD count 1 en 10 
    /KEY customer_id, ban, pdate desc 
     
    /OUTFILE $AGENCY_FILE_SORTED OVERWRITE
    
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
