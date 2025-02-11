xquery version "1.0" encoding "Cp1252";
(:: pragma  parameter="$FaultMsg" type="xs:anyType" ::)
(:: pragma  parameter="$BodyMsg" type="xs:anyType" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/util/createErrorStr/";

declare function xf:createErrorStr(
	$FaultMsg as element(*)?,
    $BodyMsg as element(*)?)
    as xs:string? {


concat(
'Error details :' 

,($FaultMsg//*[local-name()='faultcode'])[1]

,'  '
,($FaultMsg//*[local-name()='faultstring'])[1]

,'  '
,($FaultMsg//*[local-name()='errorCode'])[1]

,'  '
,($FaultMsg//*[local-name()='reason'])[1]

,'  '
,($FaultMsg//*[local-name()='faultstring'])[1]

,'  '
,($FaultMsg//*[local-name()='PolicyException'])[1]//*[local-name()='errorCode']

,'  '
,($FaultMsg//*[local-name()='PolicyException'])[1]//*[local-name()='errorMessage']

,'  '
,($FaultMsg//*[local-name()='ServiceException'])[1]//*[local-name()='errorCode']

,'  '
,($FaultMsg//*[local-name()='ServiceException'])[1]//*[local-name()='errorMessage']
,'  '
,($FaultMsg//*[local-name()='ServiceException'])[1]//*[local-name()='faultcode']
,'  '
,($FaultMsg//*[local-name()='ServiceException'])[1]//*[local-name()='faultstring']

,' [ location:   '
,' node='
,($FaultMsg//*[local-name()='location'])[1]//*[local-name()='node']
,' ,pipeline='
,($FaultMsg//*[local-name()='location'])[1]//*[local-name()='pipeline']
,' ,stage='
,($FaultMsg//*[local-name()='location'])[1]//*[local-name()='stage']
,' ,path='
,($FaultMsg//*[local-name()='location'])[1]//*[local-name()='path']
,' ]   '

,($BodyMsg//*[local-name()='faultcode'])[1]

,'  '
,($BodyMsg//*[local-name()='faultstring'])[1]

,'  '
,($BodyMsg//*[local-name()='errorCode'])[1]

,'  '
,($BodyMsg//*[local-name()='reason'])[1]

,'  '
,($BodyMsg//*[local-name()='faultstring'])[1]

,'  '
,($BodyMsg//*[local-name()='PolicyException'])[1]//*[local-name()='errorCode']

,'  '
,($BodyMsg//*[local-name()='PolicyException'])[1]//*[local-name()='errorMessage']

,'  '
,($BodyMsg//*[local-name()='ServiceException'])[1]//*[local-name()='errorCode']

,'  '
,($BodyMsg//*[local-name()='ServiceException'])[1]//*[local-name()='errorMessage']
,'  '
,($BodyMsg//*[local-name()='ServiceException'])[1]//*[local-name()='faultcode']
,'  '
,($BodyMsg//*[local-name()='ServiceException'])[1]//*[local-name()='faultstring']

,' [ location:   '
,' node='
,($BodyMsg//*[local-name()='location'])[1]//*[local-name()='node']
,' ,pipeline='
,($BodyMsg//*[local-name()='location'])[1]//*[local-name()='pipeline']
,' ,stage='
,($BodyMsg//*[local-name()='location'])[1]//*[local-name()='stage']
,' ,path='
,($BodyMsg//*[local-name()='location'])[1]//*[local-name()='path']
,' ]   '
)


};

declare variable $FaultMsg as element(*)? external;
declare variable $BodyMsg as element(*)? external;

xf:createErrorStr($FaultMsg, $BodyMsg)