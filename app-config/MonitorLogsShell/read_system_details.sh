#!/bin/bash	
echo "******************************* read ./config/system_details.txt****************************************************"
while read aLine
do
  prop_name=`echo $aLine | cut -d: -f1`
  prop_value=`echo $aLine | cut -d: -f2`
  if [[ $prop_name = "System" ]];then
     system_to_monitor=${prop_value}
  elif [[ $prop_name = "Environment" ]];then
      env_to_monitor=${prop_value}
  elif [[ $prop_name = "MonitorInterval" ]];then
      monitor_every_x_minutes=${prop_value}
      ((monitor_every_x_seconds=${monitor_every_x_minutes}*60))
  elif [[ $prop_name = "MonitoringOS" ]];then
      unix_server=${prop_value}
  elif [[ $prop_name = "EMailFrom" ]];then
      email_from=${prop_value}
  elif [[ $prop_name = "Mode" ]];then
      working_mode=${prop_value}
  elif [[ "$aLine" = "#*" ]];then
      echo "Ignore comments: $aLine" >> ${log_file}
  else
     echo "Invalid Property: $prop_name" >> ${log_file}
  fi
done <"./config/system_details.txt"
#echo "system_to_monitor=$system_to_monitor"


