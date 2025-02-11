(:: pragma bea:global-element-parameter parameter="$overrideCreditWorthiness1" element="ns3:overrideCreditWorthiness" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns3:overrideCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/dormant/dormantOverrideCreditWorthinessResponse/";

declare function xf:dormantOverrideCreditWorthinessResponse($overrideCreditWorthiness1 as element(ns3:overrideCreditWorthiness))
    as element(ns3:overrideCreditWorthinessResponse) {
   
      <ns3:overrideCreditWorthinessResponse>
            <ns3:overridenCreditWorthiness>
            	<ns2:customerID>{$overrideCreditWorthiness1/ns3:overrideCreditWorthinessRequest/ns3:customerID/text()}</ns2:customerID>
                <ns2:creditAssessmentId>00000</ns2:creditAssessmentId>
                <ns2:creditValueCd>R</ns2:creditValueCd>
                <ns2:assessmentMessageCd>DFINC01</ns2:assessmentMessageCd>
                <ns2:productCategoryQualification>
                
                    <ns0:productCategoryList>
                        <ns0:categoryCd>SL</ns0:categoryCd>
                        <ns0:qualified>true</ns0:qualified>
                    </ns0:productCategoryList>

                    <ns0:productCategoryList>
                        <ns0:categoryCd>DSL</ns0:categoryCd>
                        <ns0:qualified>false</ns0:qualified>
                    </ns0:productCategoryList>
                   
                    <ns0:productCategoryList>
                        <ns0:categoryCd>TTV</ns0:categoryCd>
                        <ns0:qualified>false</ns0:qualified>
                    </ns0:productCategoryList>
                                                        
                 <ns0:boltOnInd>false</ns0:boltOnInd>
                </ns2:productCategoryQualification>
                
                <ns2:creditAssessmentTypeCd>{$overrideCreditWorthiness1/ns3:overrideCreditWorthinessRequest/ns3:creditAssessmentTypeCd/text()}</ns2:creditAssessmentTypeCd>
                <ns2:creditAssessmentSubTypeCd>{$overrideCreditWorthiness1/ns3:overrideCreditWorthinessRequest/ns3:creditAssessmentSubTypeCd/text()}</ns2:creditAssessmentSubTypeCd>
                {
                    for $timestamp in $overrideCreditWorthiness1/ns3:auditInfo/ns1:timestamp
                    return
                        <ns2:creditAssessmentCreationDate>{ data($timestamp) }</ns2:creditAssessmentCreationDate>
                }
                <ns3:creditBureauReportInd>false</ns3:creditBureauReportInd>
            </ns3:overridenCreditWorthiness>
        </ns3:overrideCreditWorthinessResponse>
        

};

declare variable $overrideCreditWorthiness1 as element(ns3:overrideCreditWorthiness) external;

xf:dormantOverrideCreditWorthinessResponse($overrideCreditWorthiness1)