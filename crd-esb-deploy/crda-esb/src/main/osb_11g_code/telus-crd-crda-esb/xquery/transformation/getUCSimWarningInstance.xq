(:: pragma bea:global-element-parameter parameter="$body" element="ref:getReferenceDataResponse" location="../../wsdls/refpds/ReferencePDSDataServiceRequestResponse_v1_0.xsd" ::)
(:: pragma bea:local-element-return type="ref:getReferenceDataResponse/ref:return/ref:RefEntity/ref:Instance" location="../../wsdls/refpds/ReferencePDSDataServiceRequestResponse_v1_0.xsd" ::)

declare namespace ref = "http://xmlschema.tmi.telus.com/srv/ERM/RefPds/ReferencePDSDataServiceRequestResponse_v1_0";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/getSimInstance/";

declare function xf:getSimInstance(
	$body as element(ref:getReferenceDataResponse),
    $name as xs:string)
    as element()*
{   
 let $aRefEntity := $body/ref:return/ref:RefEntity[@name='UC_MATCH_SIMULATOR_WARNINGS']
 let $instanceList := $aRefEntity/ref:Instance
 for $aInstance in $instanceList
 return 
 	if($aInstance/ref:Input[@code='SIMULATOR_KEY']/ref:value=$name)
 		then
         $aInstance
    else
    	()

};

declare variable $body as element(ref:getReferenceDataResponse) external;
declare variable $name as xs:string external;

xf:getSimInstance($body,$name)