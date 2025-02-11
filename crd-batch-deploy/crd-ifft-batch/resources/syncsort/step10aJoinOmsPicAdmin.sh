#!/usr/bin/ksh

echo "Step 10a: Joining the files from steps 6 and 7."

syncsort << EOF
   /JOIN unpaired rightside
   /INFILE "$1" 65535
   /JOINKEYS SORTED recordlayout1.WTN
   /INFILE "$2" 65535
   /JOINKEYS SORTED recordlayout2.WTN

   /OUTFILE "$3" 65535 OVERWRITE
   /REFORMAT LEFTSIDE:recordlayout1.WTN, RIGHTSIDE:recordlayout2.WTN, LEFTSIDE:recordlayout1.LSP_NAME, RIGHTSIDE:recordlayout2.PIC_CODE, LEFTSIDE:recordlayout1.NON_PUB_INDICATOR
   /COLLATINGSEQUENCE DEFAULT ASCII

   /RECORDLAYOUT recordlayout1 { WTN CHARACTER 10, LSP_NAME CHARACTER 10, NON_PUB_INDICATOR CHARACTER 1}
   /RECORDLAYOUT recordlayout2 { WTN CHARACTER 10, PIC_CODE CHARACTER 5}

   /SILENT
   /END
EOF

return $?
