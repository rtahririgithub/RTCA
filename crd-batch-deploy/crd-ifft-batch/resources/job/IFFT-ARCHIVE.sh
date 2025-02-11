G_COL_IFFT_BOX=$1
G_COL_GG_SFTP=$2
G_ENV=$3
#mkdir $G_COL_GG_BOX/archive/inbox
#mkdir $G_COL_GG_BOX/archive/outbox
GZIP_SUFFIX=.gz
mv $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.DAT "$G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.DAT`date +%y%m%d`"
gzip -S $GZIP_SUFFIX $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.DAT*
mv  $G_COL_IFFT_BOX/outbox/IMAGINE_DAILY.DAT* $G_COL_IFFT_BOX/archive/




