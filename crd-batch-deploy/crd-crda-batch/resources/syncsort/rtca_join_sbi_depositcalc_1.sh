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
/JOIN UNPAIRED LEFT
/FIELDS file1_orderDepId 1: - 1:
/FIELDS file1_serviceTypCd 2: - 2:
/JOINKEY file1_orderDepId, file1_serviceTypCd

/INFILE $CRDA_INPUT_file2 65535 "|"
/FIELDS file2_orderDepId 1: - 1:
/FIELDS file2_serviceTypCd 2: - 2:
/FIELDS file2_rentedEquipCount 3: - 3:
/JOINKEY file2_orderDepId, file2_serviceTypCd

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT LEFTSIDE:file1_orderDepId,file1_serviceTypCd,RIGHTSIDE:file2_rentedEquipCount

/STATISTICS
/WARNINGS 100
/END

EOF


return $?