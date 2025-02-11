#!/usr/bin/ksh

# Filename: rtca_pre_agency_request.sh 
# Function: pre-process exp_CL9_AGENCY_REQUEST.* file
#           to retrieve agency details.
#
#######################################
if [ $# -eq 2 ]; then
    CL9_AGENCY_REQUEST=$1
    CL9_AGENCY_REQUEST_OUTPUT=$2

else
    echo ""
    echo "usage:rtca_pre_agency_request.sh  [input file1] [output file] " 

    exit 1
fi


syncsort << EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /INFILE $CL9_AGENCY_REQUEST  "|" 
    /FIELDS AGENCY_CD 2: - 2:
    /FIELDS BAN 7: - 7: ASCII 
    /FIELDS AGENCY_DATE 10: - 10:   
    /FIELDS AGENCY_YEAR  10:1 - 10:4 
    /FIELDS AGENCY_MONTH 10:5 - 10:6
    /FIELDS AGENCY_DAY   10:7 - 10:
    /FIELDS AGENCY_AMOUNT 12: - 12: en
    /FIELDS AGENCY_STATUS  15: - 15: 
    /CONDITION agencyDateNotNull (AGENCY_DATE MT /[0-9]+/ )
    /DERIVEDFIELD AGEN_DT AGENCY_YEAR + "-" + AGENCY_MONTH + "-" + AGENCY_DAY
    /DERIVEDFIELD AGENCY_DT IF agencyDateNotNull THEN AGEN_DT  ELSE AGENCY_DATE
    /KEYS BAN, AGENCY_DATE desc  

    /CONDITION filter (AGENCY_STATUS = "P" OR AGENCY_STATUS = "E" )
    /INCLUDE filter 
 
    /OUTFILE $CL9_AGENCY_REQUEST_OUTPUT  OVERWRITE
    /REFORMAT BAN, AGENCY_AMOUNT, AGENCY_CD, AGENCY_STATUS, AGENCY_DT  
    /COLLATINGSEQUENCE DEFAULT ASCII

    /SILENT
    /WARNINGS 100
    /END
EOF


return $?
