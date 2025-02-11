(:: pragma bea:global-element-parameter parameter="$assessCreditWorthiness1" element="ns3:assessCreditWorthiness" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns3:assessCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/dormantCreditWorthiness/";

declare function xf:dormantCreditWorthiness($assessCreditWorthiness1 as element(ns3:assessCreditWorthiness))
    as element(ns3:assessCreditWorthinessResponse) {
        <ns3:assessCreditWorthinessResponse>
            <ns3:assessedCreditWorthiness>
            	<ns2:customerID>{$assessCreditWorthiness1/ns3:assessCreditWorthinessRequest/ns3:customerID/text()}</ns2:customerID>
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
                
                <ns2:creditAssessmentTypeCd>{ data($assessCreditWorthiness1/ns3:assessCreditWorthinessRequest/ns3:creditAssessmentTypeCd) }</ns2:creditAssessmentTypeCd>
                <ns2:creditAssessmentSubTypeCd>{ data($assessCreditWorthiness1/ns3:assessCreditWorthinessRequest/ns3:creditAssessmentSubTypeCd) }</ns2:creditAssessmentSubTypeCd>
                {
                    for $timestamp in $assessCreditWorthiness1/ns3:auditInfo/ns1:timestamp
                    return
                        <ns2:creditAssessmentCreationDate>{ data($timestamp) }</ns2:creditAssessmentCreationDate>
                }
                <ns3:creditBureauReportInd>false</ns3:creditBureauReportInd>
            </ns3:assessedCreditWorthiness>
        </ns3:assessCreditWorthinessResponse>
};

declare variable $assessCreditWorthiness1 as element(ns3:assessCreditWorthiness) external;

xf:dormantCreditWorthiness($assessCreditWorthiness1)