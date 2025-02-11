xquery version "1.0" encoding "Cp1252";
(:: pragma bea:global-element-parameter parameter="$soapbody" element="soap-env:Body" location="soap-env.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$soapbodyFault" element="soap-env:Body" location="soap-env.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$osbfault" element="ctx:fault" location="MessageContext.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$osbinbound" element="ctx:endpoint" location="MessageContext.xsd" ::)
(:: pragma bea:global-element-return element="soap-env:Fault" location="soap-env.xsd" ::)
 
declare namespace xf = "http://rubix.nl/common/Soap11Fault/";
declare namespace soap-env = "http://schemas.xmlsoap.org/soap/envelope/";
declare namespace ctx = "http://www.bea.com/wli/sb/context";
declare namespace tp = "http://www.bea.com/wli/sb/transports";
declare namespace http = "http://www.bea.com/wli/sb/transports/http";
declare namespace con1 = "http://www.bea.com/wli/sb/stages/transform/config";


 
declare function xf:SoapFault(
$soapbody as element(soap-env:Body)?,
$soapbodyFault as element(soap-env:Fault)?,
$osbfault as element(ctx:fault)?,
$osbinbound as element(ctx:endpoint)?)

as element(soap-env:Fault)
{
 
<soap-env:Fault>
	{
	if ($osbfault/ctx:errorCode="BEA-382505")  (: BEA-382505 represents "Validate action validation failed"  :)	
	then <faultcode xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">soap-env:Client</faultcode>	
	else <faultcode xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">soap-env:Server</faultcode>
	}
	{
	if ($soapbodyFault)
	then 
		<faultstring>Error in operation: {$osbinbound/ctx:service/ctx:operation/text()}.
		customerID : {$soapbody//*[local-name()='customerID']}. 
		Faultstring : {$soapbodyFault/faultstring/text()}
		</faultstring>
	else 
		<faultstring>
			Error in operation: {$osbinbound/ctx:service/ctx:operation/text()}
			customerID : {$soapbody//*[local-name()='customerID']}. 	
		</faultstring>
	
	}

 
	{
	if( fn:exists($osbfault//*[local-name()='PolicyException']) )
	then 
		<detail>
			{$osbfault//*[local-name()='PolicyException']}
			{$osbfault//*[local-name()='exception']}
		</detail>
	else 
		if( fn:exists($osbfault//*[local-name()='ServiceException']) )
		then 
			<detail>
				{$osbfault//*[local-name()='ServiceException']}
				{$osbfault//*[local-name()='exception']}
			</detail>
		else 	
			if( fn:exists($soapbodyFault//*[local-name()='PolicyException']) )
			then 
				<detail>
					{$soapbodyFault//*[local-name()='PolicyException']}
					{$soapbodyFault//*[local-name()='exception']}
				</detail>
			else 	
				if( fn:exists($soapbodyFault//*[local-name()='ServiceException']) )
				then 
					<detail>
						{$soapbodyFault//*[local-name()='ServiceException']}
						{$soapbodyFault//*[local-name()='exception']}
					</detail>
				else 					
					if( fn:exists($osbfault//*[local-name()='ReceivedFaultDetail']) )
						then 
							<detail>{$osbfault//*[local-name()='ReceivedFaultDetail']//*[local-name()='detail']/*}</detail>
						else 
						<detail>{$osbfault}</detail>
						(:  create a serviceexception with  :)
						
						
	}

</soap-env:Fault>

};

declare variable $soapbody as element(soap-env:Body)? external;
declare variable $soapbodyFault as element(soap-env:Fault)? external;
declare variable $osbfault as element(ctx:fault)? external;
declare variable $osbinbound as element(ctx:endpoint)? external;

xf:SoapFault(
$soapbody,
$soapbodyFault,
$osbfault,
$osbinbound)