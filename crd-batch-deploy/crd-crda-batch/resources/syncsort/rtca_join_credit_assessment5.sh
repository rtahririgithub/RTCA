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
/FIELDS create_ts  1: - 1: 
/FIELDS create_date 2: - 2: 
/FIELDS req_id 3: - 3: ASCII 
/FIELDS customer_id 4: - 4: 
/FIELDS customer_st_cd 5: - 5: 
/FIELDS create_user_id 6: - 6:
/FIELDS data_source_id 7: - 7:
/FIELDS cat_type_cd  8: - 8:
/FIELDS cat__sub_type_cd  9: - 9:
/FIELDS customer_ct_dt  10: - 10:
/FIELDS bypass  11: - 11:
/FIELDS f_nm 12: - 12: 
/FIELDS m_nm 13: - 13:
/FIELDS l_nm   14: - 14: 
/FIELDS address_l1  15: - 15: 
/FIELDS address_l2  16: - 16: 
/FIELDS address_l3 17: - 17: 
/FIELDS address_l4 18: - 18:
/FIELDS address_l5 19: - 19:
/FIELDS city 20: - 20:
/FIELDS prov 21: - 21:
/FIELDS country 22: - 22:
/FIELDS post_cd 23: - 23:
/FIELDS sub_pv_cd  24: - 24:
/FIELDS cust_type_cd 25: - 25:
/FIELDS report_sc 26: - 26:
/FIELDS employm  27: - 27:
/FIELDS resident 28: - 28:
/FIELDS prim_cr 29: - 29:
/FIELDS second_cr  30: - 30:
/FIELDS legal_care 31: - 31:
/FIELDS dob  32: - 32: 
/FIELDS sim 33: - 33:
/FIELDS crd_score 34: - 34:
/FIELDS crd_class 35: - 35:
/FIELDS crd_decision 36: - 36:
/FIELDS crd_msg  37: - 37:

/FIELDS coll_segment 38: - 38:
/FIELDS scorecard_id 39: - 39:
/FIELDS cycles_delinq 40: - 40:

/FIELDS credit_vu 41: - 41:
/FIELDS pd_bolt_on 42: - 42:
/FIELDS ass_msg  43: - 43:
/FIELDS fraud 44: - 44:

/FIELDS risk_lvl_num 45: - 45:

/FIELDS prd1 46: - 46:
/FIELDS prd3 47: - 47: 
/FIELDS prd4 48: - 48:
/FIELDS risk1 49: - 49:
/FIELDS risk2 50: - 50:
/FIELDS risk3 51: - 51:
/FIELDS risk4 52: - 52:
/FIELDS risk5 53: - 53:
/FIELDS risk6 54: - 54:
/FIELDS risk7 55: - 55:
/FIELDS risk8 56: - 56:
/FIELDS risk9 57: - 57:
/JOINKEYS req_id

/INFILE $CRDA_INPUT_FILE2 "|"
/FIELDS cust_id1 1: - 1: 
/FIELDS req_id1 2: - 2: ASCII 
/FIELDS create_dt 3: - 3:
/FIELDS car_type 4: - 4:
/FIELDS car_sub_type 5: - 5:
/FIELDS car_asmt_msg_cd 6: - 6:
/FIELDS car_credit_value 7: - 7:
/FIELDS car_fraud_code 8: - 8:
/FIELDS cp_credit_value 9: - 9:
/FIELDS cp_fraud_code 10: - 10:
/FIELDS ovrd_current_credit_value 11: - 11:
/FIELDS ovrd_requested_credit_value 12: - 12:
/FIELDS ovrd_current_fraud_cd 13: - 13:
/FIELDS ovrd_requested_cd 14: - 14:
/FIELDS ovrd_current_consent_cd 15: - 15:
/FIELDS ovrd_requested_consent_cd 16: - 16:

/JOINKEYS req_id1


/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE  
/REFORMAT leftside:create_ts,create_date,req_id,customer_id,customer_st_cd,create_user_id,data_source_id,cat_type_cd,cat__sub_type_cd,customer_ct_dt,bypass,f_nm,m_nm,l_nm,address_l1,address_l2,address_l3, address_l4,address_l5,city,prov,country,post_cd,sub_pv_cd,cust_type_cd,report_sc,employm,resident,prim_cr,second_cr,legal_care,dob,sim,crd_score,crd_class,crd_decision,crd_msg, credit_vu,pd_bolt_on, ass_msg, coll_segment,scorecard_id,cycles_delinq, fraud, risk_lvl_num,  prd1,prd3,prd4, risk1,risk2,risk3,risk4,risk5,risk6,risk7,risk8,risk9, rightside:car_credit_value,car_fraud_code,cp_credit_value,cp_fraud_code,ovrd_current_credit_value,ovrd_requested_credit_value,ovrd_current_fraud_cd,ovrd_requested_cd,ovrd_current_consent_cd,ovrd_requested_consent_cd   

/STATISTICS
/WARNINGS 100
/END

EOF

return $?
