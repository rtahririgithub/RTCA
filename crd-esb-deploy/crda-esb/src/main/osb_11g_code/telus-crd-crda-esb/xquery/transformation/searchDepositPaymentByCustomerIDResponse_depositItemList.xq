(:: pragma bea:global-element-parameter parameter="$searchDepositPaymentByCustomerIDResponse1" element="ns1:searchDepositPaymentByCustomerIDResponse" location="../../wsdls/SearchDepositPaymentByCustomerIDRequestResponse_v1_0.xsd" ::)
(:: pragma bea:local-element-return type="ns0:getAdditionalCustomerDataResponse/ns0:depositItemList" location="../../wsdls/GetAdditionalCustomerDataSplitJoinRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditDepositTypes_v1";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/SearchDepositPaymentByCustomerIDRequestResponse_v1";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/srv/CMO/BillingAccountMgmt/DepositManagementServiceRequestResponse_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v9";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoinRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/searchDepositPaymentByCustomerIDResponse_depositItemList/";

declare function xf:searchDepositPaymentByCustomerIDResponse_depositItemList($searchDepositPaymentByCustomerIDResponse1 as element(ns1:searchDepositPaymentByCustomerIDResponse))
    as element() {
        let $depositItemList := $searchDepositPaymentByCustomerIDResponse1/searchDepositResponseList/ns4:searchDepositResponse[1]/depositItemList
        return
            <ns0:depositItemList>
                {
                    for $depositItem in $depositItemList/depositItem
                    return
                        <ns2:depositItem>
                            <ns2:depositID>{ data($depositItem/depositID) }</ns2:depositID>
                            <ns2:dueDate>{ data($depositItem/dueDate) }</ns2:dueDate>
                            <ns2:depositDesignationID>{ data($depositItem/depositDesignationID) }</ns2:depositDesignationID>
                            <ns2:requestAmount>{ data($depositItem/requestAmount) }</ns2:requestAmount>
                            <ns2:requestDate>{ data($depositItem/requestDate) }</ns2:requestDate>
                            <ns2:requestReasonCd>{ data($depositItem/requestReasonCd) }</ns2:requestReasonCd>
                            <ns2:requestReasonTxt>{ data($depositItem/requestReasonTxt) }</ns2:requestReasonTxt>
                            <ns2:paidAmount>{ data($depositItem/paidAmount) }</ns2:paidAmount>
                            <ns2:paidDate>{ data($depositItem/paidDate) }</ns2:paidDate>
                            <ns2:cancelledAmount>{ data($depositItem/cancelledAmount) }</ns2:cancelledAmount>
                            <ns2:cancelDate>{ data($depositItem/cancelDate) }</ns2:cancelDate>
                            <ns2:cancelReasonCd>{ data($depositItem/cancelReasonCd) }</ns2:cancelReasonCd>
                            <ns2:cancelReasonTxt>{ data($depositItem/cancelReasonTxt) }</ns2:cancelReasonTxt>
                            <ns2:releasedAmount>{ data($depositItem/releasedAmount) }</ns2:releasedAmount>
                            <ns2:releaseDate>{ data($depositItem/releaseDate) }</ns2:releaseDate>
                            <ns2:releaseReasonCd>{ data($depositItem/releaseReasonCd) }</ns2:releaseReasonCd>
                            <ns2:releaseReasonTxt>{ data($depositItem/releaseReasonTxt) }</ns2:releaseReasonTxt>
                            <ns2:releaseMethodCd>{ data($depositItem/releaseMethodCd) }</ns2:releaseMethodCd>
                            <ns2:releaseMethodTxt>{ data($depositItem/releaseMethodTxt) }</ns2:releaseMethodTxt>
                            <ns2:interestAmount>{ data($depositItem/interestAmount) }</ns2:interestAmount>
                            <ns2:accountID>{ data($depositItem/accountID) }</ns2:accountID>
                        </ns2:depositItem>
                }
            </ns0:depositItemList>
};

declare variable $searchDepositPaymentByCustomerIDResponse1 as element(ns1:searchDepositPaymentByCustomerIDResponse) external;

xf:searchDepositPaymentByCustomerIDResponse_depositItemList($searchDepositPaymentByCustomerIDResponse1)