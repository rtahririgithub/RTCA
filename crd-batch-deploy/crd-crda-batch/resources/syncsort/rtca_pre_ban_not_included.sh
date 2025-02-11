#!/usr/bin/ksh

# Filename: rtca_pre_ban_not_included.sh
# Function: join drive file with result ban driver file
#           to retrieve all the customers bans that are not included. 
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_DRIVER_FILTE=$1   
    CUSTOMER_BANDETAIL_FILE=$2
    CUSTOMER_BAN_NOT_INCLUDED=$3
        
else
    echo ""
    echo "usage:rtca_pre_ban_not_included.sh [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join report*/
    /join unpaired leftside only
    /INFILE $CUSTOMER_DRIVER_FILTE 65535 "|"
    /FIELDS ban_id 1: - 1: en
    /JOINKEYS  ban_id

    /INFILE $CUSTOMER_BANDETAIL_FILE 65535 "|"
    /FIELDS ban 2: - 2: en
    /JOINKEYS ban 


    /OUTFILE $CUSTOMER_BAN_NOT_INCLUDED 65535  OVERWRITE

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
