#!/usr/bin/ksh

#
# Notes:
# &1: Step 1 -> MASTER_DIFFERENCES.DAT
# &2: Step 2 -> TARGET_DIFFERENCES.DAT
# &3: Step 3 -> CHANGE_RECORDS.DAT.DAT
# &4: Output    -> NDS_FEED_FILE_TARGET_MANIFESTED.DAT
#
# cat 6.10 6.12 6.04 6.02 > OMS_DATA.DAT
#

echo "Step 6.13: Concatenating the OMS products into one file."

cat $1 $2 $3  > $4 
rm $1
rm $2
rm $3
rm $5
return $?
