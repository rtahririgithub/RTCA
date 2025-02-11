#!/usr/bin/ksh

# Filename: crda_join_sbi_extract_1.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_file1=$1
        CRDA_OUTPUT_FILE=$2
        
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
/FIELDS file1_orderDepId 1: - 1:
/FIELDS file1_serviceTypCd 2: - 2:
/FIELDS file1_reqrentedEquipCount 3: - 3:EN
/FIELDS file1_reqPurchasedCount 4: - 4:EN
/FIELDS file1_ownedCount 5: - 5:EN
/FIELDS file1_rentedCount 6: - 6:EN
/KEYS file1_orderDepId, file1_serviceTypCd

/CONDITION isDSL(file1_serviceTypCd = "HSIC" )

/SUMMARIZE TOTAL file1_reqrentedEquipCount , TOTAL file1_reqPurchasedCount , TOTAL file1_ownedCount , TOTAL file1_rentedCount

/INCLUDE isDSL
/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE
/REFORMAT file1_orderDepId,file1_serviceTypCd,file1_reqrentedEquipCount,file1_reqPurchasedCount,file1_ownedCount,file1_rentedCount

/STATISTICS
/WARNINGS 100
/END

EOF


return $?
