#!/usr/bin/ksh

echo "Step 7: Process the PIC ADMIN feed file."

syncsort << EOF
   /INFILE "$1" 65535
   /KEYS recordlayout1.WTN
   
   /OUTFILE "$2" OVERWRITE
   /REFORMAT recordlayout1.WTN, recordlayout1.PIC_CODE
   /COLLATINGSEQUENCE DEFAULT ASCII
   /RECORDLAYOUT recordlayout1 {BTN CHARACTER 10, WTN CHARACTER 10, PIC_CODE CHARACTER 5}
   
   /SILENT
   /END
EOF

return $?