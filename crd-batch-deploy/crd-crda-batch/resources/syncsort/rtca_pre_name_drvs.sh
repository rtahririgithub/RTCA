#!/usr/bin/ksh

# Filename: rtca_pre_name_drvs.sh 
# Function: move files to passbox   
#	         
####################################### 
if [ $# -eq 4 ]; then
    CUSTOMER_DRIVE_FILE=$1
    CUSTOMER_BAN_DRIVE_FILE=$2
    CUSTOMER_DRIVE_FILE_PASS=$3
    CUSTOMER_BAN_DRIVE_PASS=$4
else
    echo ""
    echo "usage:rtca_pre_name_drvs.sh  [input file] [output file] "
    exit 1
fi
cp $2 $4
syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CUSTOMER_DRIVE_FILE 65535 "|" 
    /FIELDS customer_id 1: - 1: en
    /FIELDS amt 2: - 2: 
    /FIELDS pdate 3: - 3: en
    /KEY customer_id 

    /STABLE
    /OUTFILE $CUSTOMER_DRIVE_FILE_PASS OVERWRITE
    /REFORMAT customer_id 
    /STATISTICS
    /WARNINGS 100
    /END

!EOF 

return $?
