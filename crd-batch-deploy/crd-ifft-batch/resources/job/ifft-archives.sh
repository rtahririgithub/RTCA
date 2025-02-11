G_COL_IFFT_BOX=$1
G_COL_GG_SFTP=$2
G_ENV=$3
GZIP_SUFFIX=.gz
mv $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.dat "$G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.dat`date +%Y%m%d`"
gzip -S $GZIP_SUFFIX $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.dat*
cp $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.dat* $G_COL_GG_SFTP/$G_ENV/outbox/leads/
gzip -S $GZIP_SUFFIX $G_COL_IFFT_BOX/outbox/SCODS_FCustomer_DFRAUD_T*.dat

mv  $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.dat* $G_COL_IFFT_BOX/archive/
mv $G_COL_IFFT_BOX/outbox/SCODS_FCustomer_DFRAUD_T*.dat* $G_COL_IFFT_BOX/archive/
#rm $G_COL_GG_SFTP/$G_ENV/outbox/ifft/IMAGINE_DAILY.dat*
rm $G_COL_IFFT_BOX/outbox/SCODS_FCustomer_DFRAUD_T*.done

return $?




