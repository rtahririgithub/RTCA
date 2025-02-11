#!/usr/bin/ksh

#  ================================================================  
#  merge.sh					                     
#  ****************************************************************  
#  merge non-grouped customers with grouped customers				
#  2005-11-11 Lei Fan Initial version
#  =================================================================    
 
cat $PROCESSBOX/extract/singleCustomers.dat $PROCESSBOX/extract/groupCustomer.dat>$PROCESSBOX/extract/personList.dat <<EOF
EOF