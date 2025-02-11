(:: pragma bea:global-element-parameter parameter="$getDepositPaymentListByAccountListResponse1" element="ns1:getDepositPaymentListByAccountListResponse" location="../../wsdls/GetDepositPaymentListByAccountListSplitJoinRequestResponse_v1_0.xsd" ::)
(:: pragma bea:global-element-return element="ns0:searchDepositPaymentByCustomerIDResponse" location="../../wsdls/SearchDepositPaymentByCustomerIDRequestResponse_v1_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v9";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/getDepositPaymentListByAccountListSplitJoinRequestResponse_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/BillingAccountMgmt/DepositManagementServiceRequestResponse_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/SearchDepositPaymentByCustomerIDRequestResponse_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/getDepositPaymentListByAccountListResponse_searchDepositPaymentByCustomerIDResponse/";

declare function xf:getDepositPaymentListByAccountListResponse_searchDepositPaymentByCustomerIDResponse($getDepositPaymentListByAccountListResponse1 as element(ns1:getDepositPaymentListByAccountListResponse))
    as element(ns0:searchDepositPaymentByCustomerIDResponse) {
        <ns0:searchDepositPaymentByCustomerIDResponse>
            <searchDepositResponseList>
                {
                    for $searchDepositResponse in $getDepositPaymentListByAccountListResponse1/DepositPaymentList/ns3:searchDepositResponse
                    return
                        <ns3:searchDepositResponse>{ $searchDepositResponse/@* , $searchDepositResponse/node() }</ns3:searchDepositResponse>
                }
            </searchDepositResponseList>
        </ns0:searchDepositPaymentByCustomerIDResponse>
};

declare variable $getDepositPaymentListByAccountListResponse1 as element(ns1:getDepositPaymentListByAccountListResponse) external;

xf:getDepositPaymentListByAccountListResponse_searchDepositPaymentByCustomerIDResponse($getDepositPaymentListByAccountListResponse1)