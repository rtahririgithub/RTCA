#!/usr/bin/ksh

echo "Step 8b: Sorting the file from step 8a."

syncsort << EOF
   /INFILE "$1" 65535
	/FIELDS BILLING_ACCOUNT_ID 1 character 18 
	/FIELDS WTN 19 character 12
   /KEYS WTN

   /CONDITION badrecord (BILLING_ACCOUNT_ID = "")
   /OMIT badrecord

   /OUTFILE "$2" 65535 OVERWRITE
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?
