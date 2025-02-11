#!/usr/bin/ksh

echo "Step 6.14: Sorting the file from step 6.13."

syncsort << EOF
   /INFILE "$1" 65535
   /FIELDS WTN 1 CHARACTER 10
   /KEYS WTN

   /OUTFILE "$2" 65535 OVERWRITE
   /COLLATINGSEQUENCE DEFAULT ASCII

   /SILENT
   /END
EOF

return $?