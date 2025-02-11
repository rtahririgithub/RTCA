xquery version "2004-draft";
(:: pragma bea:schema-type-parameter parameter="$wlsUnifiedCreditSearchResult1" type="ns0:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)
(:: pragma bea:schema-type-return type="ns0:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)

declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/Format_WlsUnifiedCreditSearchResult/";

declare function xf:Format_WlsUnifiedCreditSearchResult($wlsUnifiedCreditSearchResult1 as element())
    as element() {
       <ns0:unifiedCreditSearchResult xsi:type="ent:WlsUnifiedCreditSearchResult">
          {$wlsUnifiedCreditSearchResult1/*}
        </ns0:unifiedCreditSearchResult>
};

declare variable $wlsUnifiedCreditSearchResult1 as element() external;

xf:Format_WlsUnifiedCreditSearchResult($wlsUnifiedCreditSearchResult1)