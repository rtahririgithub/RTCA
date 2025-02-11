#!/usr/bin/ksh

echo "Step 10b: Sorting the file from step 10a."

syncsort << EOF
   /INFILE "$1" 65535
	/FIELDS WTN 1 character 10
   /KEYS WTN

   /OUTFILE "$2" 65535 OVERWRITE
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?
