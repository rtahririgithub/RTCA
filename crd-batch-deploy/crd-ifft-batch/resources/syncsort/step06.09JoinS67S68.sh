#!/usr/bin/ksh

echo "Step 6.9: Joining the files from steps 6.7 and 6.8."

syncsort << EOF
   /INFILE "$1" 65535
   /JOINKEYS LeftHandFile_RecordLayout.DECODE_ID
   /INFILE "$2" 65535
   /JOINKEYS RightHandFile_RecordLayout.DECODE_ID

   /OUTFILE "$3" OVERWRITE
   /REFORMAT LEFTSIDE:__Field_1, RIGHTSIDE:__Field_2
   /COLLATINGSEQUENCE DEFAULT ASCII

   /RECORDLAYOUT LeftHandFile_RecordLayout { DISCRETE_CODE CHARACTER 10, DECODE_ID CHARACTER 32}
   /RECORDLAYOUT RightHandFile_RecordLayout { DECODE_ID CHARACTER 32, CAPTION CHARACTER 10}
   /DERIVEDFIELD __Field_1 LeftHandFile_RecordLayout.DISCRETE_CODE CHARACTER 10
   /DERIVEDFIELD __Field_2 RightHandFile_RecordLayout.CAPTION CHARACTER 10

   /SILENT
   /END
EOF

return $?