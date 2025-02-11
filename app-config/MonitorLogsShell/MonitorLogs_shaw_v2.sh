#!/bin/bash
set -x
rm -f ./logs/*.log
rm -f ./temp/*.*
log_file=./logs/MonitorLogs.`date +%Y%m%d%H%M`.log
echo "Start Monitoring" > $log_file
# set the Internal Field Separator to a pipe symbol
IFS='|'
# file name
file=./config/log_files_to_monitor.txt

system_to_monitor=""
env_to_monitor=""
monitor_every_x_minutes=15
monitor_every_x_seconds=900
email_from=""
unix_server=""
working_mode=""
# Build Notification Group Hash Table
while read notify_group_file
do
 if [[ "$notify_group_file" = "" ]]; then
  continue
 fi
 notify_group_name=`echo $notify_group_file | cut -d: -f1`
 notify_group_email_list=`echo $notify_group_file | cut -d: -f2`
 notify_var_name=notify_${notify_group_name}
 eval ${notify_var_name}=${notify_group_email_list}
 eval notify_group_email_list_value=\$$notify_var_name
 echo "Read Email Group: $notify_var_name=${notify_group_email_list_value}" >> ${log_file}
done < "./config/notify_groups.txt"
while read f
do
  prop_name=`echo $f | cut -d: -f1`
  prop_value=`echo $f | cut -d: -f2`
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
  elif [[ "$f" = "#*" ]];then
      echo "Ignore comments: $f" >> ${log_file}
  else
     echo "Invalid Property: $prop_name" >> ${log_file}
  fi
done <"./config/system_details.txt"

# use while loop to read domain and ip
while read f
do
 if [[ "$f" = "#*" ]]; then
    continue
 fi
 domain=`echo $f | cut -d, -f1`
 server=`echo $f | cut -d, -f2`
 filename=`echo $f | cut -d, -f3`
 varPrefix=`echo $f | cut -d, -f4`
 echo "Looking into file: ${filename}" >> ${log_file}
 cpVarName=${varPrefix}_cp
 if [ ! -f "$filename" ]; then
   eval cpValue=0
   continue
 fi
 eval ${cpVarName}=`wc -l ${filename} | awk '{print $1}'`
 eval cpValue=\$$cpVarName
 echo "`date '+%Y-%m-%d %H:%M:%S'`: Initialized ${cpVarName}=${cpValue}" >> ${log_file}
done <"$file"
while :; do
    notify=false
    mail_attachments=""
    systems_affected=""
    notify_groups=""
    exception_table=""
    IFS='|'
    while read f
    do
      if [[ "$f" = "#*" ]]; then
       echo "Comment line for log_files_to_monitor.txt, ignored." >> ${log_file}
       continue
      fi
      if [[ "$f" = "" ]]; then
        echo "Empty line for log_files_to_monitor.txt, ignored." >> ${log_file}
	continue
      fi
      domain=`echo $f | awk -F"," '{print $1}'`
      server=`echo $f | awk -F"," '{print $2}'`
      filename=`echo $f | awk -F"," '{print $3}'`
      varPrefix=`echo $f | awk -F"," '{print $4}'`
      exception_file=`echo $f | awk -F"," '{print $5}'`
      cpVarName=${varPrefix}_cp
      eval cpValue=\$$cpVarName
      echo "`date '+%Y-%m-%d %H:%M:%S'`: domain=${domain} server=${server} filename=${filename} varPrefix=${varPrefix} exception_file=${exception_file} cpVarName=${cpVarName} cpValue=${cpValue}" >> ${log_file}
      if [ ! -f "$filename" ]; then
       echo "$filename is not a valid file name, ignored." >> ${log_file}
       continue
      fi
      log_length=`wc -l ${filename} | awk '{print $1}'`

      if [[ $log_length -lt ${cpValue} ]];then
	  echo "Readjusting ${cpVarName} pointer due to new day roll over of log files" >> ${log_file}
	  eval ${cpVarName}=0
	  eval cpValue=\$$cpVarName
      fi
      ((tail_length=${log_length}-${cpValue}))
      tail -${tail_length} ${filename} > temp/${varPrefix}_log.log
      echo "`date '+%Y-%m-%d %H:%M:%S'`: ${cpVarName}=${cpValue} log_length=${log_length} tail_length=${tail_length}" >> ${log_file}
      while read e
      do
        if [[ "$e" = "#*" ]];then
          echo "Comment line ignored in $e" >> ${log_file}
          continue
        fi
	if [[ "$e" = "" ]]; then
	    echo "Empty line for ./config/${exception_file}, ignored." >> ${log_file}
	    continue
        fi
	exception_text=`echo "$e" | awk -F"," '{print $1}'`
	exception_message=`echo "$e" | awk -F"," '{print $2}'`
	exception_system=`echo "$e" | awk -F"," '{print $3}'`
	exception_team=`echo "$e" | awk -F"," '{print $4}'`
	exception_notify_group=`echo "$e" | awk -F"," '{print $5}'`
	exception_severity=`echo "$e" | awk -F"," '{print $6}'`
	

	
	exception_error=`grep "${exception_text}" temp/${varPrefix}_log.log`	
	echo "grep ${exception_text} temp/${varPrefix}_log.log" >> ${log_file}
	if [[ "$exception_text" = "WCDAP_ERROR" ]]; then
		 echo "grep -A100000 <WCDAP_ERROR> temp/${varPrefix}_log.log | grep -B100000 </WCDAP_ERROR> temp/${varPrefix}_log.log" >> ${log_file}
	    exception_error=`grep -A100000 "<WCDAP_ERROR>" temp/${varPrefix}_log.log | grep -B100000 "</WCDAP_ERROR>"`
    fi	
	
	echo "exception_text=$exception_text" >> ${log_file}
	echo "exception_message=$exception_message" >> ${log_file}
	echo "exception_system=$exception_system" >> ${log_file}
	echo "exception_team=$exception_team" >> ${log_file}
	echo "exception_notify_group=$exception_notify_group" >> ${log_file}
	echo "exception_severity=$exception_severity"	 >> ${log_file}
	echo "exception_message =$exception_message" >> ${log_file}
	echo "exception_error =$exception_error" >> ${log_file}
	if [[ $? -eq 0 ]];then
	    log_to_attach=${varPrefix}_${RANDOM}.log
	    if [[ "$unix_server" = "Solaris" ]];then
		begin_log=`grep -n "${exception_text}" temp/${varPrefix}_log.log | awk -F: '{print $1}' | head -1`
                file_length=`wc -l temp/${varPrefix}_log.log | awk '{print $1}'`
                ((begin_log_start=${file_length}-${begin_log}+20))
                tail -${begin_log_start} temp/${varPrefix}_log.log | head -80 > ./temp/${log_to_attach}
	    else
	      grep -A 120 -B 30 "${exception_text}" temp/${varPrefix}_log.log > ./temp/${log_to_attach}.temp
	      cat ./temp/${log_to_attach}.temp | head -90 > ./temp/${log_to_attach}
	    fi
	    exception_ignore=false
            exception_exception=`echo $e | awk -F"," '{print $7}'`
	    if [[ "$exception_exception" != "" ]]; then
              grep ${exception_exception} ./temp/${log_to_attach}
              if [[ $? -eq 0 ]];then
		echo "Ignoring notification for exception: ${exception_exception}" >> ${log_file}
                exception_ignore=true
	      fi
	    fi
	  if [ "$exception_ignore" = "false" ];then
            echo "Preparing the exception row for exception: ${exception_message}" >> ${log_file}
	    exception_error_t=`echo ${exception_error} | head -2`
	    exception_row="<tr><td>${domain}</td><td>${server}</td><td>${exception_system}</td><td>${exception_severity}</td><td>${exception_team}</td><td>${exception_message}</td><td>${exception_error_t}</td><td>${log_to_attach}</td></tr>"
	    exception_table="${exception_table}${exception_row}"
	    if [ "$systems_affected" = "" ]; then
		systems_affected=${exception_system}
	    elif [[ $systems_affected = *${exception_system}* ]]; then
               echo "Systems affected ${exception_system} is already included in ${systems_affected}" >> ${log_file}
            else
		systems_affected="${systems_affected},${exception_system}"
            fi
	    if [ "$notify_groups" = "" ];then
               notify_groups=${exception_notify_group}
	    elif [[ $notify_groups = *${exception_notify_group}* ]]; then
		echo "Notify Group: ${exception_notify_group} is already included in ${notify_groups}" >> ${log_file}
            else
               notify_groups="${notify_groups}:${exception_notify_group}"
            fi
	    if [ "$mail_attachments" = "" ];then
		mail_attachments=${log_to_attach}
	    else
		mail_attachments="${mail_attachments},${log_to_attach}"
            fi
	    echo "mail_attachments=${mail_attachments}" >> ${log_file}
	    notify=true
          fi
	fi
      done <"./config/${exception_file}"
      eval ${cpVarName}=$log_length
    done <"$file"
    if [ "$notify" = "true" ];then
	 error_report_time=`date '+%Y-%m-%d %H:%M:%S'`
         echo "${error_report_time}: Exceptions found, sending notification..." >> ${log_file}
	 output_efile=./temp/notify_email.html
	 content_type_boundary="CONTENT_TYPE_BOUNDARY"
	 echo "From: ${email_from}" > ${output_efile}
	 IFS=':'
	 email_to=""
	 #set -A notify_group_array ${notify_groups}
	 notify_group_array=${notify_groups}
	 for notify_grp in "${notify_group_array[@]}";
	 do
	   notify_var_name=notify_${notify_grp}
	   eval notify_group_email_list_value=\$$notify_var_name
	   echo "Notify group email: ${notify_var_name}=${notify_group_email_list_value}" >> ${log_file}
	   if [ "$notify_group_email_list_value" = "" ]; then
	       echo "No Mailing List for group $notify_grp."  >> ${log_file}
	       continue
           fi
	   if [ "$email_to" = "" ]; then
	      email_to=${notify_group_email_list_value}
           else
	      email_to="${email_to},${notify_group_email_list_value}"
           fi
	 done
	 echo "To: ${email_to}" >> ${output_efile}
	 echo "Subject: ${system_to_monitor} Monitoring ALERT: Systems Affected: ${systems_affected}, Environment: ${env_to_monitor}" >> ${output_efile}
	 echo "MIME-Version: 1.0" >> ${output_efile}
	 echo "Content-Type: multipart/mixed; boundary=\"${content_type_boundary}\"" >> ${output_efile}
	 echo "Content-Disposition: inline" >> ${output_efile}
	 echo "" >> ${output_efile}
	 echo "--${content_type_boundary}" >> ${output_efile}
	 echo "Content-Type: text/html; charset=\"us-ascii\"" >> ${output_efile}
	 echo "Content-Disposition: inline" >> ${output_efile}
	 echo "" >> ${output_efile}
	 html_body="<html><body><h4>${system_to_monitor} Error Report at ${error_report_time} for errors occurred in the past ${monitor_every_x_minutes} minutes:</h4>"
	 html_body="${html_body}<table>"
	 html_body="${html_body}<tr><td>&nbsp;Systems Affected:&nbsp;</td><td>${systems_affected}</td></tr>"
	 html_body="${html_body}<tr><td>&nbsp;Environment:&nbsp;</td><td>${env_to_monitor}</td></tr></table><br><br>"
	 html_body="${html_body}<table border=\"1\"><tr><th>Domain</th><th>Server</th><th>System Affected</th><th>Severity</th><th>Team(s) to investigate</th><th>Error Message</th><th>Error Details</th><th>Attached log file</th></tr>"
	 echo "${html_body}" >> ${output_efile}
	 echo "${exception_table}" >> ${output_efile}
	 end_html="</table><br>Please refer to the attachments corresponding to the Errors reported above.<br></body></html>"
	 echo "${end_html}" >> ${output_efile}
	 
	 IFS=','
	 #set -A attachment_array ${mail_attachments}
	 attachment_array=${mail_attachments}
	 echo  "mail_attachments=${mail_attachments}" >> ${log_file}
	 
	 
	 IFS=',' read -r -a attachment_array <<< "$mail_attachments"
	 echo  "attachment_array=${attachment_array}" >> ${log_file}
	 for attachment in "${attachment_array[@]}";
	 do
	   echo  "attachment=$attachment" >> ${log_file}
	   echo "" >> ${output_efile}
	   echo "--${content_type_boundary}" >> ${output_efile}
	   echo "Content-Type: text/plain; charset=US-ASCII" >> ${output_efile}
	   echo "Content-Disposition: attachement; filename=${attachment}" >> ${output_efile}
	   echo "" >> ${output_efile}
	   cat ./temp/${attachment} >> ${output_efile}
	 done
	 if [[ "$working_mode" = "Production" ]];then
	   echo  "output_efile = ${output_efile}" >> ${log_file}
	   echo  "cat ${output_efile} | /usr/sbin/sendmail  ${email_from}" >> ${log_file}
	   #cat ${output_efile} | /usr/sbin/sendmail -tf ${email_from}
	   cat ${output_efile} | /usr/sbin/sendmail  ${email_from}
     fi
    fi
    rm -f ./temp/*.*
    echo "`date '+%Y-%m-%d %H:%M:%S'`: sleep for ${monitor_every_x_minutes} minutes" >> ${log_file}
    sleep ${monitor_every_x_seconds}
done
