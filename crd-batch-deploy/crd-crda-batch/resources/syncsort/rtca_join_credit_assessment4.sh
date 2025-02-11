#!/usr/bin/ksh

# Filename: crda_join_credit_assessment.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 3 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_INPUT_FILE2=$2
        CRDA_OUTPUT_FILE=$3
        
else
    echo ""
    echo "usage:crda_join_credit_assessment.sh  [input file1] [input file2]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortmemory}
${sortworkspace}

/* Join report*/
/JOIN UNPAIRED LEFTSIDE
/INFILE $CRDA_INPUT_FILE1 65535 "|"
/FIELDS req_id 1: - 1: ASCII 
/FIELDS customer_id 2: - 2: 
/FIELDS customer_st_cd 3: - 3:
/FIELDS create_user_id 4: - 4: 
/FIELDS data_source_id 5: - 5: 
/FIELDS create_ts 6: - 6:
/FIELDS cat_type_cd 7: - 7:
/FIELDS cat__sub_type_cd 8: - 8:
/FIELDS customer_ct_dt 9: - 9:
/FIELDS bypass 10: - 10:
/FIELDS f_nm 11: - 11:
/FIELDS m_nm 12: - 12: 
/FIELDS l_nm 13: - 13:
/FIELDS address_l1 14: - 14: 
/FIELDS address_l2 15: - 15: 
/FIELDS address_l3 16: - 16: 
/FIELDS address_l4 17: - 17: 
/FIELDS address_l5 18: - 18:
/FIELDS city 19: - 19:
/FIELDS prov 20: - 20:
/FIELDS country 21: - 21:
/FIELDS post_cd 22: - 22:
/FIELDS sub_pv_cd 23: - 23:
/FIELDS cust_type_cd 24: - 24:
/FIELDS report_sc 25: - 25:
/FIELDS sin 26: - 26:
/FIELDS dl 27: - 27:
/FIELDS dl_pv 28: - 28:
/FIELDS passport 29: - 29:
/FIELDS passport_ct 30: - 30:
/FIELDS pov_id 31: - 31:
/FIELDS pov_pv 32: - 32: 
/FIELDS health_id 33: - 33:
/FIELDS health_pv 34: - 34:
/FIELDS employm 35: - 35:
/FIELDS resident 36: - 36:
/FIELDS prim_cr 37: - 37:
/FIELDS second_cr 38: - 38:
/FIELDS legal_care 39: - 39:
/FIELDS dob 40: - 40:
/FIELDS sim 41: - 41:
/FIELDS crd_score 42: - 42:
/FIELDS crd_class 43: - 43: 
/FIELDS crd_decision 44: - 44:
/FIELDS crd_msg 45: - 45:
/FIELDS create_date 46: - 46:
/FIELDS coll_segment 47: - 47:
/FIELDS scorecard_id 48: - 48:
/FIELDS cycles_delinq 49: - 49:
/FIELDS credit_vu 50: - 50:
/FIELDS pd_bolt_on 51: - 51:
/FIELDS ass_msg 52: - 52:
/FIELDS fraud 53: - 53:
/FIELDS risk_lvl_num 54: - 54:
/FIELDS prd1 55: - 55:
/FIELDS prd2 56: - 56:
/FIELDS prd3 57: - 57:
/FIELDS prd4 58: - 58:
/FIELDS prd5 59: - 59:

/JOINKEYS req_id

/INFILE $CRDA_INPUT_FILE2 "|"
/FIELDS req_id1 1: - 1: 
/FIELDS risk1 2: - 2:
/FIELDS risk2 3: - 3:
/FIELDS risk3 4: - 4:
/FIELDS risk4 5: - 5:
/FIELDS risk5 6: - 6:
/FIELDS risk6 7: - 7:
/FIELDS risk7 8: - 8:
/FIELDS risk8 9: - 9:
/FIELDS risk9 10: - 10:
/JOINKEYS req_id1


/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE  
/REFORMAT leftside:create_ts,create_date,req_id,customer_id,customer_st_cd,create_user_id,data_source_id,cat_type_cd,cat__sub_type_cd,customer_ct_dt,bypass,f_nm,m_nm,l_nm,address_l1,address_l2,address_l3, address_l4,address_l5,city,prov,country,post_cd,sub_pv_cd,cust_type_cd,report_sc,employm,resident,prim_cr,second_cr,legal_care,dob,sim,crd_score,crd_class,crd_decision,crd_msg, credit_vu,pd_bolt_on, ass_msg, coll_segment,scorecard_id,cycles_delinq, fraud, risk_lvl_num, prd1,prd3,prd4,rightside:risk1,risk2,risk3,risk4,risk5,risk6,risk7,risk8,risk9 

/STATISTICS
/WARNINGS 100
/END

EOF

return $?
