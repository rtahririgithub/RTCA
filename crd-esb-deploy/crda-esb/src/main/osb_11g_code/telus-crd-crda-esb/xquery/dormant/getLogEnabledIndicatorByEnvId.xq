declare namespace ref = "http://xmlschema.tmi.telus.com/srv/ERM/RefPds/ReferencePDSDataServiceRequestResponse_v1_0";

declare variable $body as element() external;
declare variable $envId as xs:string external;

for $o in $body/ref:getReferenceDataResponse/ref:return/ref:RefEntity[@name='CREDIT_OPERATION_PARM']/ref:Instance
where
$o/ref:Input[@code='APPL_ID']/ref:value='EnterpriseCrdAssmtWs' and
$o/ref:Input[@code='PARM_NM']/ref:value='WLN_WCDAP_LOGGING_ENABLED' and
$o/ref:Input[@code='ENVIR_NM']/ref:value=$envId
return data($o/ref:Output[@code='PARM_VALUE_STR']/ref:value)