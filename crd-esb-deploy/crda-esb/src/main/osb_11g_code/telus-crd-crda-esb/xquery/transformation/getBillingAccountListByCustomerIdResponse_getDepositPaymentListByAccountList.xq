(:: pragma bea:global-element-parameter parameter="$getBillingAccountListByCustomerIdResponse1" element="ns1:getBillingAccountListByCustomerIdResponse" location="../../wsdls/ConsumerBillingAccountMgmtSvcRequestResponse_v1_1.xsd" ::)
(:: pragma bea:global-element-return element="ns2:getDepositPaymentListByAccountList" location="../../wsdls/GetDepositPaymentListByAccountListSplitJoinRequestResponse_v1_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/getDepositPaymentListByAccountListSplitJoinRequestResponse_v1";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/InformationMgmt/ConsumerBillingAccountMgmtSvcRequestResponse_v1";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CustomerCommon_v3";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v3";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Customer/CustomerBill/Customer_Billing_Sub_Domain_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/getBillingAccountListByCustomerIdResponse_getDepositPaymentListByAccountList/";

declare function xf:getBillingAccountListByCustomerIdResponse_getDepositPaymentListByAccountList($getBillingAccountListByCustomerIdResponse1 as element(ns1:getBillingAccountListByCustomerIdResponse))
    as element(ns2:getDepositPaymentListByAccountList) {
        <ns2:getDepositPaymentListByAccountList>
            {
                for $billingAccountList in $getBillingAccountListByCustomerIdResponse1/billingAccountList
                return
                    <billingAccountList>{ $billingAccountList/@* , $billingAccountList/node() }</billingAccountList>
            }
        </ns2:getDepositPaymentListByAccountList>
};

declare variable $getBillingAccountListByCustomerIdResponse1 as element(ns1:getBillingAccountListByCustomerIdResponse) external;

xf:getBillingAccountListByCustomerIdResponse_getDepositPaymentListByAccountList($getBillingAccountListByCustomerIdResponse1)