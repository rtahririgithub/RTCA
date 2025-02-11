xquery version "1.0" encoding "Cp1252";
(:: pragma bea:schema-type-parameter parameter="$getBureuaReportCreditWorthinessRequest1" type="ns4:GetBureuaReportCreditWorthinessRequest" location="../../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:schema-type-return type="ns0:FullCreditAssessmentRequest" location="../../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_0.xsd" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/external/test_creditprofileData/";
declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerSubDomain_v3";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";

declare function xf:test_creditprofileData($getBureuaReportCreditWorthinessRequest1 as element())
    as element() {
        <ns0:FullCreditAssessmentRequest/>
};

declare variable $getBureuaReportCreditWorthinessRequest1 as element() external;

xf:test_creditprofileData($getBureuaReportCreditWorthinessRequest1)