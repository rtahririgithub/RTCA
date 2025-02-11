#!/bin/bash	

output_efile=./temp/notify_email.html
rm $output_efile

breakline=$'\n'
. read_system_details.sh
#Read ./config/notify_groups.txt"

	
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

done < "./config/notify_groups.txt"


	
	error_report_time=`date '+%Y-%m-%d %H:%M:%S'`
	content_type_boundary="CONTENT_TYPE_BOUNDARY"
	 
	 IFS=':'
	
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
	
	 html_start="<html><body>"
	 html_header="<h4>${system_to_monitor} Error Report at ${error_report_time} for errors occurred in the past ${monitor_every_x_minutes} minutes:</h4>"
	 
	valign="valign=\"top\" "
	 html_table_start="<table >"
	 html_table_start="${html_table_start}<tr align=\"left\" ><td ${valign}>&nbsp;Systems Affected:&nbsp;</td><td ${valign}>${systems_affected}</td></tr>"
	 html_table_start="${html_table_start}<tr align=\"left\" ><td ${valign}>&nbsp;Environment:&nbsp;</td><td ${valign}>${env_to_monitor}</td></tr></table><br><br>"
	 html_table_start="${html_table_start}<table border=\"1\"><tr align=\"left\" ><th>Domain</th><th>Server</th><th>System Affected</th><th>Error Details</th><th>logFile</th></tr>"

	html_table_end="</table><br>"
	html_end="</body></html>"
	
	html_doc=""
	html_doc="$html_doc  ${breakline}  $html_start"
	html_doc="$html_doc  ${breakline}  $html_header"	
	html_doc="$html_doc  ${breakline}  $html_table_start"
	
	html_doc="$html_doc  ${breakline}  ${exception_table}"
	
	html_doc="$html_doc  ${breakline} ${html_table_end}"
	html_doc="$html_doc  ${breakline} ${html_end}"



	echo "${html_doc}"   >> ${output_efile}
	echo "output_efile="$output_efile

	IFS=','	
	cat ${output_efile} | /usr/sbin/sendmail  ${email_from}
	echo "**************EMAIL SENT *******************************"