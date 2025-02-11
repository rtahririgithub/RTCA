#!/usr/bin/ksh

#
# Notes:
# &1: Step 6.10 -> OMS_LD_DATA.DAT
# &2: Step 6.12 -> OMS_SR_DATA.DAT
# &3: Step 6.04 -> OMS_EXTRACT_CLOB_SING_PARSED.DAT
# &4: Step 6.02 -> OMS_EXTRACT_CLOB_CC_PARSED.DAT
# &5: Output    -> OMS_DATA.DAT
#
# cat 6.10 6.12 6.04 6.02 > OMS_DATA.DAT
#

echo "Step 6.13: Concatenating the OMS products into one file."

cat $1 $2 $3 $4 > $5

return $?