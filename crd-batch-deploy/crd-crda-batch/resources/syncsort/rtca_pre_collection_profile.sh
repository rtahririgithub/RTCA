#!/usr/bin/ksh

#e Filename: rtca_pre_collection_profile.sh
# Function: join file from tcm with db extract
# 
# New TRT_CRDA_PROFILE_HISTORY.DAT format (RTCA1.6)
# customer_id, ban, ba_score, score_card_id, score_dt, NSF, cycle_delinquent, coll_seg, st_dt, bc_dt
#######################################
if [ $# -eq 3 ]; then
    COLLECTION_DB_EXTRACT=$1
    TCM_RESULT=$2
    COLLECTION_DETAILS=$3
        
else
    echo ""
    echo "usage:rtca_pre_collection_profile.sh  [input file1] [input file2]  [output file] "

    exit 1
fi

syncsort <<-!EOF
    ${sortstatistic}
    ${sortmemory}
    ${sortworkspace}

    /* Join input files*/
    /JOIN UNPAIRED LEFTSIDE
    /INFILE $COLLECTION_DB_EXTRACT 65535 "|"
    /FIELDS customer_id 1: - 1: en 
    /FIELDS ban 2: - 2: en
    /FIELDS OBSERVATION_PROFILE_ID 3: - 3:
    /FIELDS collection_ind 4: - 4:
    /FIELDS start_dt 5: - 5:
    /FIELDS end_dt 6: - 6:
    /FIELDS status_cd 7: - 7: 
    /FIELDS inv_ceased_ind 8: - 8:
    /FIELDS dt 9: - 9:

    /JOINKEYS customer_id, ban

    /INFILE $TCM_RESULT "|"	
	/FIELDS customer_id1 1: - 1: en
    /FIELDS ban1 2: - 2: en
    /FIELDS ba_score 3: - 3:
    /FIELDS score_card_id 4: - 4:
    /FIELDS score_dt 5: - 5:    
    /FIELDS nsf 6: - 6:
    /FIELDS cycle_delinquent 7: - 7:
    /FIELDS coll_seg 8: - 8:
    /FIELDS st_dt 9: - 9:
	/FIELDS bc_dt 10: - 10:
	
    /JOINKEYS customer_id1, ban1

    /OUTFILE $COLLECTION_DETAILS 65535  OVERWRITE
    /REFORMAT leftside:customer_id, ban, collection_ind, start_dt, end_dt, status_cd, rightside:ba_score, nsf, score_dt, score_card_id, cycle_delinquent, coll_seg, leftside:inv_ceased_ind, dt
	
    /STATISTICS
    /WARNINGS 100
    /END

EOF

return $?
