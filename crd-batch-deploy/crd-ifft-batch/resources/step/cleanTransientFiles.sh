#!/usr/bin/ksh

echo "--Cleaning out the transient files."

rm $1/*
rm $2/HEADER_FILE.DAT
rm $2/HEADER_FILE_NIK.DAT
rm $2/NDS_FEED_FILE_TARGET_MANIFESTED.DAT
rm $2/NDS_FEED_FILE_TARGET_MANIFESTED_NIK.DAT
rm $2/FOOTER_FILE.DAT
rm $2/FOOTER_FILE_NIK.DAT

return $?
