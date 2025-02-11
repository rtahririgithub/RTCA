#!/usr/bin/ksh

#  ================================================================ */
#  getBANFromCustomerODS.srt                                        */
#  **************************************************************** */
#  Desc: This SS shell joins cutomer ids from unMergeList with      */
#        BILLING ACCOUNT file from cutomerODS to get customer id-   */
#        billing account number mapping information. This is for    */
#        performance tuning due to defect 37882--retriving data     */
#        from extract instead of calling API                        */
#                                                                   */
#  input  file      - unMergeCustomerList.dat                       */
#  input  file      - CUSTODS_BILLING_ACCOUNT.dat                   */
#  Output file      - cutomerIDBanMapping.dat                       */
#                                                                   */
#  2007-06-27   Lei Fan x089748 init                                */
#  ================================================================ */
if [ $# -eq 3 ]; then
    CODS_BILLING_ACCOUNT_DATA=$1
    UNMERGEED_CUSTOMER=$2 
    BAN_MAPPING=$3
else
    echo ""
    echo "usage: getBANFromCustomerODS.sh [CUSTODS_REF_BILLING_ACCOUNT.DAT]"
    echo ""
    exit 1
fi
echo "UNMERGEED_CUSTOMER is $2" 
syncsort <<-!EOF
${sortstatistic}
${sortworkspace}

/* Added because the file estimate in production is greater than 1 G */
/MEMORY 256 megabytes

/INFILE $UNMERGEED_CUSTOMER 
/FIELDS CUSTOMER_ID1 1: -1: EN
/JOINKEYS CUSTOMER_ID1

/INFILE $CODS_BILLING_ACCOUNT_DATA 3239
/JOINKEYS CUSTOMER_ID2
/FIELDS BILLING_ACCOUNT_NUMBER  37 EN 9
/FIELDS CUSTOMER_ID2 188 EN 9

/DERIVEDFIELD delim "|"

/OUTFILE $BAN_MAPPING OVERWRITE
/REFORMAT LEFTSIDE:CUSTOMER_ID1,delim,RIGHTSIDE:BILLING_ACCOUNT_NUMBER

/WARNINGS 100
/END
!EOF

return $?
