xquery version "1.0" encoding "Cp1252";
(:: pragma bea:global-element-parameter parameter="$searchDepositPaymentByCustomerIDResponse1" element="ns0:searchDepositPaymentByCustomerIDResponse" location="../../wsdls/SearchDepositPaymentByCustomerIDRequestResponse_v1_0.xsd" ::)
(:: pragma bea:global-element-return element="ns1:getDepositPaymentListByAccountListResponse" location="../../wsdls/GetDepositPaymentListByAccountListSplitJoinRequestResponse_v1_0.xsd" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/GetDepositPaymentListByAccountList_MergeResult/";
declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v9";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/getDepositPaymentListByAccountListSplitJoinRequestResponse_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/BillingAccountMgmt/DepositManagementServiceRequestResponse_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/SearchDepositPaymentByCustomerIDRequestResponse_v1";

declare function xf:GetDepositPaymentListByAccountList_MergeResult($searchDepositPaymentByCustomerIDResponse1 as element(ns0:searchDepositPaymentByCustomerIDResponse))
    as element(ns1:getDepositPaymentListByAccountListResponse) {
        <ns1:getDepositPaymentListByAccountListResponse/>
};

declare variable $searchDepositPaymentByCustomerIDResponse1 as element(ns0:searchDepositPaymentByCustomerIDResponse) external;

xf:GetDepositPaymentListByAccountList_MergeResult($searchDepositPaymentByCustomerIDResponse1)