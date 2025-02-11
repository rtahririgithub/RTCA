(:: pragma bea:global-element-parameter parameter="$assessCreditWorthiness1" element="wcpmpxy:assessCreditWorthiness" location="../../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ecrda:performCreditAssessment" location="../../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_0.xsd" ::)


declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/external/create_PerformGetBureauDataCreditAssessmentRequest/";

declare namespace crdcmn = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace wcpmpxy = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace wcpm = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ecrda = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";

declare function xf:create_PerformGetBureauDataCreditAssessmentRequest(
	$assessCreditWorthiness1 as element(wcpmpxy:assessCreditWorthiness)
) as element(ecrda:performCreditAssessment) {

        <ecrda:performCreditAssessment >
            <ecrda:creditAssessmentRequest xsi:type="ent:FullCreditAssessmentRequest">
                <ecrda:creditAssessmentTypeCd>{ data($assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:creditAssessmentTypeCd) }</ecrda:creditAssessmentTypeCd>
                <ecrda:creditAssessmentSubTypeCd>{ data($assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:creditAssessmentSubTypeCd) }</ecrda:creditAssessmentSubTypeCd>
                <ecrda:customerID>{ data($assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:customerID) }</ecrda:customerID>
                <ecrda:applicationID>{ data($assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:applicationID) }</ecrda:applicationID>
                {
                    for $channelID in $assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:channelID
                    return
                        <ecrda:channelID>{ data($channelID) }</ecrda:channelID>
                }
                <ecrda:lineOfBusiness>{ data($assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:lineOfBusiness) }</ecrda:lineOfBusiness>
                {
                    for $commentTxt in $assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:commentTxt
                    return
                        <ecrda:commentTxt>{ data($commentTxt) }</ecrda:commentTxt>
                }
                
                               
	          	{
	                let $creditCustomerInfo := $assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:creditCustomerInfo
	                return
	                    <ecrda:creditCustomerInfo>{ $creditCustomerInfo/* }</ecrda:creditCustomerInfo>
	            } 
           		{
                let $creditProfileData := $assessCreditWorthiness1/wcpmpxy:assessCreditWorthinessRequest/wcpmpxy:creditProfileData
                return
                    <ecrda:creditProfileData>
                        {
                            for $creditProfileID in $creditProfileData/crdcmn:creditProfileID
                            return
                                <crdcmn:creditProfileID>{ data($creditProfileID) }</crdcmn:creditProfileID>
                        }
                        <crdcmn:customerID>{ data($creditProfileData/crdcmn:customerID) }</crdcmn:customerID>
                        {
                            for $creditIdentification in $creditProfileData/crdcmn:creditIdentification
                            return
                                <crdcmn:creditIdentification>{ $creditIdentification/@* , $creditIdentification/node() }</crdcmn:creditIdentification>
                        }
                        {
                            for $creditAddress in $creditProfileData/crdcmn:creditAddress
                            return
                                <crdcmn:creditAddress>{ $creditAddress/@* , $creditAddress/node() }</crdcmn:creditAddress>
                        }
                        <crdcmn:applicationProvinceCd>{ data($creditProfileData/crdcmn:applicationProvinceCd) }</crdcmn:applicationProvinceCd>
                        {
                            for $personalInfo in $creditProfileData/crdcmn:personalInfo
                            return
                                <crdcmn:personalInfo>{ $personalInfo/@* , $personalInfo/node() }</crdcmn:personalInfo>
                        }
                        {
                            for $creditCardCd in $creditProfileData/crdcmn:creditCardCd
                            return
                                <crdcmn:creditCardCd>{ $creditCardCd/@* , $creditCardCd/node() }</crdcmn:creditCardCd>
                        }
                        {
                            for $creditProfileStatusCd in $creditProfileData/crdcmn:creditProfileStatusCd
                            return
                                <crdcmn:creditProfileStatusCd>{ data($creditProfileStatusCd) }</crdcmn:creditProfileStatusCd>
                        }
                       <crdcmn:businessLastUpdateTimestamp>
                            {
                                if ((fn:empty($creditProfileData/crdcmn:businessLastUpdateTimestamp)  )) 
                                
                                then   
                                	fn:current-dateTime()                                 
                                else 
                                    data($creditProfileData/crdcmn:businessLastUpdateTimestamp)
                            }
						</crdcmn:businessLastUpdateTimestamp>                              
                        {
                            for $bypassMatchIndicator in $creditProfileData/crdcmn:bypassMatchIndicator
                            return
                                <crdcmn:bypassMatchIndicator>{ data($bypassMatchIndicator) }</crdcmn:bypassMatchIndicator>
                        }
                        {
                            for $formatCd in $creditProfileData/crdcmn:formatCd
                            return
                                <crdcmn:formatCd>{ data($formatCd) }</crdcmn:formatCd>
                        }
                        {
                            for $methodCd in $creditProfileData/crdcmn:methodCd
                            return
                                <crdcmn:methodCd>{ data($methodCd) }</crdcmn:methodCd>
                        }
                        {
                            for $customerGuarantor in $creditProfileData/crdcmn:customerGuarantor
                            return
                                <crdcmn:customerGuarantor>{ $customerGuarantor/@* , $customerGuarantor/node() }</crdcmn:customerGuarantor>
                        }
                        {
                            for $commentTxt in $creditProfileData/crdcmn:commentTxt
                            return
                                <crdcmn:commentTxt>{ data($commentTxt) }</crdcmn:commentTxt>
                        }                                             
                        {
                            for $reportIndicator in $creditProfileData/wcpm:reportIndicator
                            return
                                <wcpm:reportIndicator>{ data($reportIndicator) }</wcpm:reportIndicator>
                        }
                        {
                            for $creditValueEffectiveDate in $creditProfileData/wcpm:creditValueEffectiveDate
                            return
                                <wcpm:creditValueEffectiveDate>{ data($creditValueEffectiveDate) }</wcpm:creditValueEffectiveDate>
                        }
                        {
                            for $firstCreditAssessmentDate in $creditProfileData/wcpm:firstCreditAssessmentDate
                            return
                                <wcpm:firstCreditAssessmentDate>{ data($firstCreditAssessmentDate) }</wcpm:firstCreditAssessmentDate>
                        }
                        {
                            for $latestCreditAssessmentDate in $creditProfileData/wcpm:latestCreditAssessmentDate
                            return
                                <wcpm:latestCreditAssessmentDate>{ data($latestCreditAssessmentDate) }</wcpm:latestCreditAssessmentDate>
                        }
                    </ecrda:creditProfileData>
            	}
	                          
                
            </ecrda:creditAssessmentRequest>
            {
                let $auditInfo := $assessCreditWorthiness1/wcpmpxy:auditInfo
                return
                    <ecrda:auditInfo>{ $auditInfo/* }</ecrda:auditInfo>
            }
        </ecrda:performCreditAssessment>
};

declare variable $assessCreditWorthiness1 as element(wcpmpxy:assessCreditWorthiness) external;

xf:create_PerformGetBureauDataCreditAssessmentRequest($assessCreditWorthiness1)