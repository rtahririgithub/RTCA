#!/usr/bin/ksh

#  ================================================================  
#  merge.sh					                     
#  ****************************************************************  
#  merge non-grouped customers with grouped customers				
#  2005-11-11 Lei Fan Initial version
#  =================================================================    
 
cat $PROCESSBOX/extract/sbi/singleCustomers.dat $PROCESSBOX/extract/sbi/groupCustomer.dat>$PROCESSBOX/extract/sbi/personList.dat <<EOF
EOF
