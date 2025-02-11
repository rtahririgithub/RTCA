#!/usr/bin/ksh

#
# Notes:
# &1: PIC AB data file 
# &2: PIC BC data file 
# &3: Output    -> PIC data file 
#
#

echo "Step 0701: Concatenating the PIC data files one file."
echo $1
echo $2
echo $3
cat $1 $2  > $3

return $?
