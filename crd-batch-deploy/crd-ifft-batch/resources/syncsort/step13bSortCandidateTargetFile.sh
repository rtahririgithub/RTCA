#!/usr/bin/ksh

echo "Step 13b: Sorting the candidate target file on the WTN"

syncsort << EOF
   /INFILE "$1" 65535
	/FIELDS WTN 2 character 10
   /KEYS WTN

   /OUTFILE "$2" 65535 OVERWRITE
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?
