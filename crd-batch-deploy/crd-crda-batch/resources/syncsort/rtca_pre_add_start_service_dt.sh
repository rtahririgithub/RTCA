#!/usr/bin/ksh

# Filename: rtca_pre_add_start_service_dt.sh
# Function: add START_SERVICE_DATE
#
#######################################
if [ $# -eq 3 ]; then
    CRUSTOMER_BAN_DETAILS_1=$1
    CUSTODS_REF_SERVICE_DT=$2
    CRUSTOMER_BAN_DETAILS=$3

else
    echo ""
    echo "usage:rtca_pre_add_start_service_dt.sh [input file1] [input file2] [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input file: CRUSTOMER_BAN_DETAILS_1: CUST_ID|BAN|BAN_STATUS|ST_DT|BSID|TOTAL_AMOUNT|DT */
    /join unpaired left
    /INFILE $CRUSTOMER_BAN_DETAILS_1 65535 "|"
    /FIELDS CUST_ID 1: - 1: en
    /FIELDS BAN 2: - 2: en
    /FIELDs BAN_STATUS 3: - 3:
    /FIELDs ST_DT 4: - 4:
    /FIELDs BSID 5: - 5:
    /FIELDs TOTAL_AMOUNT 6: - 6:
    /FIELDs DT 7: - 7:

    /JOINKEYS CUST_ID, BAN

    /* CRDA_SERVICEDT_*.dat: CUST_ID1|BAN1|START_SERVICE_DATE */
    /INFILE $CUSTODS_REF_SERVICE_DT "|"
    /FIELDS CUST_ID1 1: - 1: en
    /FIELDS BAN1 2: - 2: en
    /FIELDS START_SERVICE_DATE 3: - 3:

    /JOINKEYS CUST_ID1, BAN1

    /OUTFILE $CRUSTOMER_BAN_DETAILS 65535 OVERWRITE
    /REFORMAT leftside:CUST_ID, BAN, BAN_STATUS, ST_DT, BSID, TOTAL_AMOUNT, DT, rightside:START_SERVICE_DATE

    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
