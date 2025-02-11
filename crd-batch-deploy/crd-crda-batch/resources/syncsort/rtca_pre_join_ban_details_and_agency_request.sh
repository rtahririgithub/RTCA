#!/usr/bin/ksh

# Filename: rtca_pre_join_ban_details_and_agency_request.sh
# Function: join drive file with agency pre processed  file
#           to retrieve all the agency details 
#
#######################################
if [ $# -eq 3 ]; then
    CUSTOMER_BAN_DETAILS=$1
    CL9_AGENCY_REQUEST_PROCESSED=$2
    AGENCY_REQUEST_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_join_ban_details_and_agency_request.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/
       
    /INFILE $CUSTOMER_BAN_DETAILS 65535 "|"
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban   2:  - 2: en
    
    /JOINKEYS ban 

    /INFILE $CL9_AGENCY_REQUEST_PROCESSED "|"
    /FIELDS ban1 1: - 1: en 
    /FIELDS agency_amount   2:  - 2:
    /FIELDS agency_cd   3:  - 3:
    /FIELDS agency_status   4:  - 4:
    /FIELDS agency_date 5: - 5:

    /JOINKEYS ban1 


    /DERIVEDFIELD CURRENT_DT TODAY (YEARMM0DD0)  
    /DERIVEDFIELD CURRENT_DAY CURRENT_DT  EXTRACT /([0-9]+)/ '\1' en

    /OUTFILE $AGENCY_REQUEST_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:customer_id, ban, rightside:agency_amount, agency_cd, agency_status, agency_date, CURRENT_DAY 

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
