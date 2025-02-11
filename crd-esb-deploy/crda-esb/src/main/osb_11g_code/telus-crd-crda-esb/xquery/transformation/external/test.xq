(:: pragma bea:global-element-return element="ns0:updateCustomer" location="../../../wsdls/ConsumerCustomerMgmtSvcRequestResponse_Local.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerSubDomain_v3";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v4";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CustomerCommon_v3";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/InformationMgmt/ConsumerCustomerMgmtSvcRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/external/test/";

declare function xf:test($creditValueCode1 as xs:string)
    as element(ns0:updateCustomer) {
        <ns0:updateCustomer>
            <customer>
                <creditValueCode>{ $creditValueCode1 }</creditValueCode>
            </customer>
        </ns0:updateCustomer>
};

declare variable $creditValueCode1 as xs:string external;

xf:test($creditValueCode1)