#!/bin/ksh
env_to_monitor=""
log_file_prefix="/shared/logs/PR"
output_file="./log_files_to_monitor.txt"

while read f
do
  prop_name=`echo $f | cut -d: -f1`
  prop_value=`echo $f | cut -d: -f2`
  if [[ $prop_name = "Environment" ]];then
      env_to_monitor=${prop_value}
  elif [[ $prop_name = "LogFilePreFix" ]];then
      log_file_prefix=${prop_value}
  fi
done <"./log_files_to_monitor.prop"

echo "server,log_file_location,attachment_prefix,exception_file" > ${output_file}

while read f
do
 if [[ "$f" = "#*" ]];then
    echo" ignore comments"
    continue
 fi
 IFS='|'
  service=`echo $f | cut -d, -f1`
  domain=`echo $f | cut -d, -f2`
  exception_file=`echo $f | cut -d, -f3`
  servers=`echo $f | cut -d, -f4`
  log_file_suffix=`echo $f | cut -d, -f5`
 IFS=':'
 for server in $servers
 do
     echo -e "${domain},${server},${log_file_prefix}/${server}/${log_file_suffix},${env_to_monitor}_${service}_${server},${exception_file}" >> ${output_file}
 done
done <"./log_files_to_monitor_template.txt"
