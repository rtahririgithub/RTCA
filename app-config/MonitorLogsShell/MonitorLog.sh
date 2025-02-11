#!/bin/bash
#set -x
rm -f ./logs/*.log
rm -f ./temp/*.*
. read_system_details.sh

log_file=./logs/MonitorLogs.`date +%Y%m%d%H%M`.log
IFS='|'
log_files_to_monitor=./config/log_files_to_monitor.txt

system_to_monitor=""
env_to_monitor=""
monitor_every_x_minutes=15
monitor_every_x_seconds=900
email_from=""
unix_server=""
working_mode=""
 
# use while loop to read domain and ip
echo "******************************* read $log_files_to_monitor file****************************************************"
while read aLine_of_log_files_to_monitor
do
	 if [[ "$aLine_of_log_files_to_monitor" = "#*" ]]; then
	    continue
	 fi
	 domain=`echo $aLine_of_log_files_to_monitor | cut -d, -f1`
	 server=`echo $aLine_of_log_files_to_monitor | cut -d, -f2`
	 filename=`echo $aLine_of_log_files_to_monitor | cut -d, -f3`
	 varPrefix=`echo $aLine_of_log_files_to_monitor | cut -d, -f4`
	 
	 echo "Looking into file: ${filename}" >> ${log_file}
	 cpVarName=${varPrefix}_cp
	 if [ ! -f "$filename" ]; then
	   eval totalNumOfLineInLogFile=0
	   continue
	 fi
	#count number of lines in the log file
	 eval ${cpVarName}=`wc -l ${filename} | awk '{print $1}'`
	 eval totalNumOfLineInLogFile=\$$cpVarName
	 echo "`date '+%Y-%m-%d %H:%M:%S'`: Initialized ${cpVarName}=${totalNumOfLineInLogFile}" >> ${log_file}
	
	 if [[ $log_length -lt ${cpValue} ]];then
		echo "Readjusting ${cpVarName} pointer due to new day roll over of log files" >> ${log_file}
		eval ${cpVarName}=0
		eval cpValue=\$$cpVarName
	 fi
	#echo "domain=$domain"
	#echo "server=$server"
	#echo "filename=$filename"
	#echo "varPrefix=$varPrefix"
	#echo "domain=$domain"
	#echo "cpVarName=$cpVarName"
	#echo "totalNumOfLineInLogFile=$totalNumOfLineInLogFile"
done <"$log_files_to_monitor"

echo "*******************************Finished reading $log_files_to_monitor file****************************************************"


while :; do
    notify=false
    mail_attachments=""
    systems_affected=""
    notify_groups=""
    exception_table=""
    IFS='|'
    while read a_log_files_to_monitor
    do
      if [[ "$a_log_files_to_monitor" = "#*" ]]; then
       echo "Comment line for log_files_to_monitor.txt, ignored." >> ${log_file}
       continue
      fi
      if [[ "$a_log_files_to_monitor" = "" ]]; then
        echo "Empty line for log_files_to_monitor.txt, ignored." >> ${log_file}
		continue
      fi
      domain=`echo $a_log_files_to_monitor | awk -F"," '{print $1}'`
      server=`echo $a_log_files_to_monitor | awk -F"," '{print $2}'`
      filename=`echo $a_log_files_to_monitor | awk -F"," '{print $3}'`
      varPrefix=`echo $a_log_files_to_monitor | awk -F"," '{print $4}'`
      exception_file=`echo $a_log_files_to_monitor | awk -F"," '{print $5}'`
      cpVarName=${varPrefix}_cp
      eval cpValue=\$$cpVarName
      echo "`date '+%Y-%m-%d %H:%M:%S'`: domain=${domain} server=${server} filename=${filename} varPrefix=${varPrefix} exception_file=${exception_file} cpVarName=${cpVarName} cpValue=${cpValue}" >> ${log_file}
     if [ ! -f "$filename" ]; then
       continue
      fi
      log_length=`wc -l ${filename} | awk '{print $1}'`
      if [[ $log_length -lt ${cpValue} ]];then
		  eval ${cpVarName}=0
		  eval cpValue=\$$cpVarName
      fi
     ((tail_length=${log_length}-${cpValue})) 
    #get log data since last time
	tailResultFile="temp/${varPrefix}_log.log"
	if [[ $tail_length -lt 1 ]];then
		continue
	else
	    tail -${tail_length} ${filename} > $tailResultFile  
	fi
	#read a line from exception_file and search in $tailResultFile
	#echo "*************************************************"
	while read exception_file_line
	do
		[[ "$exception_file_line" =~ ^[[:space:]]*# ]] && continue
		exception_text=`echo "$exception_file_line" | awk -F"," '{print $1}'`
		exception_system=`echo "$exception_file_line" | awk -F"," '{print $3}'`
		exception_error=`grep "${exception_text}" $tailResultFile`			
		if [[ "$exception_error" != "" ]];then
		    exception_row="<tr><th>${domain}</th><th>${server}</th><th>${exception_system}</th><th>${exception_error}</th><th>${filename}</th></tr>"
		    exception_table="${exception_table}${exception_row}"		    
		fi
	done <"./config/${exception_file}"
	#echo "*************************************************"
	
eval ${cpVarName}=$log_length  
done <"$log_files_to_monitor" #while read aLine_of_log_files_to_monitor

   
if [[ "$exception_table" != "" ]];then
	. emailFunctions.sh	
fi
rm -f ./temp/*.log
monitor_every_x_seconds=10 #900
echo "sleep ${monitor_every_x_seconds}"
sleep ${monitor_every_x_seconds}
done    