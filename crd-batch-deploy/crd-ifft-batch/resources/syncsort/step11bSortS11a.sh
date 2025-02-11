#!/usr/bin/ksh

echo "Step 11b: Sorting the file from step 11a."

syncsort << EOF
   /INFILE "$1" 65535
	/FIELDS BILLING_ACCOUNT_ID 1 character 18
   /KEYS BILLING_ACCOUNT_ID

   /OUTFILE "$2" 65535 OVERWRITE
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?
