#!/usr/bin/ksh

# Filename: crda_join_credit_assessment.sh
# Function:joing report data 
#
#######################################
if [ $# -eq 2 ]; then
        CRDA_INPUT_FILE1=$1
        CRDA_OUTPUT_FILE=$2
        
else
    echo ""
    echo "usage:crda_join_credit_assessment.sh  [input file1]  [output file] | noskip"

    echo "      optional noskip is used so that the first row is processed."
    exit 1
fi

syncsort <<-!EOF
${sortstatistic}
${sortmemory}
${sortworkspace}

/* Join report*/

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
/FIELDS credit_vu 38: - 38:
/FIELDS pd_bolt_on 39: - 39:
/FIELDS ass_msg  40: - 40:
/FIELDS fraud 41: - 41:
/FIELDS prd1 42: - 42:
/FIELDS prd3 43: - 43: 
/FIELDS prd4 44: - 44:
/FIELDS risk1 45: - 45:
/FIELDS risk2 46: - 46:
/FIELDS risk3 47: - 47:
/FIELDS risk4 48: - 48:
/FIELDS risk5 49: - 49:
/FIELDS risk6 50: - 50:
/FIELDS risk7 51: - 51:
/FIELDS risk8 52: - 52:
/FIELDS risk9 53: - 53:
/FIELDS car_credit_value 54: - 54:
/FIELDS car_fraud_code 55: - 55:
/FIELDS cp_credit_value 56: - 56:
/FIELDS cp_fraud_code 57: - 57:
/FIELDS ovrd_current_credit_value 58: - 58:
/FIELDS ovrd_requested_credit_value 59: - 59:
/FIELDS ovrd_current_fraud_cd 60: - 60:
/FIELDS ovrd_requested_cd 61: - 61:
/FIELDS ovrd_current_consent_cd 62: - 62:
/FIELDS ovrd_requested_consent_cd 63: - 63:

/KEY req_id
/SUMMARIZE
/STABLE

/OUTFILE $CRDA_OUTPUT_FILE 65535 OVERWRITE "," 

/STATISTICS
/WARNINGS 100
/END

EOF

return $?
