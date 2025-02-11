#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 3 ]; then
        CRDA_INPUT_file1=$1
        CRDA_INPUT_file2=$2
        CRDA_OUTPUT_FILE=$3
        
else
    echo ""
    echo "usage:crda_join_sbi_extract.sh  [input file1] [input file1]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortworkspace}
/MEMORY 32 megabytes

/INFILE $CRDA_INPUT_file1 65535 "|"
/JOIN UNPAIRED LEFTSIDE
/FIELDS file1_orderDepId 1: - 1:
/FIELDS file1_serviceTypCd 2: - 2:
/FIELDS file1_reqRentedCount 3: - 3:en
/FIELDS file1_reqPurchased 4: - 4:en
/FIELDS file1_ownedCount 5: - 5:en

/JOINKEY file1_orderDepId, file1_serviceTypCd

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_orderDepId 1: - 1:
/FIELDS file2_serviceTypCd 2: - 2:
/FIELDS file2_rentedCount 3: - 3:en

/JOINKEY file2_orderDepId, file2_serviceTypCd

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT leftside:file1_orderDepId,file1_serviceTypCd, file1_reqRentedCount , file1_reqPurchased , file1_ownedCount , RIGHTSIDE:file2_rentedCount

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
