(:: pragma bea:global-element-parameter parameter="$getCustomerResponse1" element="ns0:getCustomerResponse" location="../../../wsdls/ConsumerCustomerMgmtSvcRequestResponse_v2_3.xsd" ::)
(:: pragma bea:local-element-parameter parameter="$auditInfo1" type="ns6:assessCreditWorthiness/ns6:auditInfo" location="../../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns0:updateCustomer" location="../../../wsdls/ConsumerCustomerMgmtSvcRequestResponse_Local.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v4";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerSubDomain_v3";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/InformationMgmt/ConsumerCustomerMgmtSvcRequestResponse_v2";
declare namespace ns5 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns6 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/external/create_UpdateCustomerRequest/";
declare namespace ns7 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CustomerCommon_v3";

declare function xf:create_UpdateCustomerRequest($getCustomerResponse1 as element(ns0:getCustomerResponse),
    $creditValueCode1 as xs:string,
    $auditInfo1 as element())
    as element(ns0:updateCustomer) {
        <ns0:updateCustomer>
            {
                let $customer := $getCustomerResponse1/customer
                return
                    <customer>
                        {
                            for $customerId in $customer/customerId
                            return
                                <customerId>{ data($customerId) }</customerId>
                        }
                        {
                            for $externalKeyList in $customer/externalKeyList
                            return
                                <externalKeyList>{ $externalKeyList/@* , $externalKeyList/node() }</externalKeyList>
                        }
                        {
                            for $addressList in $customer/addressList
                            return
                                <addressList>{ $addressList/@* , $addressList/node() }</addressList>
                        }
                        {
                            for $nameList in $customer/nameList
                            return
                                <nameList>{ $nameList/@* , $nameList/node() }</nameList>
                        }
                        {
                            for $billCycleCode in $customer/billCycleCode
                            return
                                <billCycleCode>{ data($billCycleCode) }</billCycleCode>
                        }
                        {
                            for $commentText in $customer/commentText
                            return
                                <commentText>{ data($commentText) }</commentText>
                        }
                        {
                            for $customerCreationDate in $customer/customerCreationDate
                            return
                                <customerCreationDate>{ data($customerCreationDate) }</customerCreationDate>
                        }
                        {
                            for $customerMasterSourceId in $customer/customerMasterSourceId
                            return
                                <customerMasterSourceId>{ data($customerMasterSourceId) }</customerMasterSourceId>
                        }
                        {
                            for $customerStatusCode in $customer/customerStatusCode
                            return
                                <customerStatusCode>{ data($customerStatusCode) }</customerStatusCode>
                        }
                        {
                            for $customerSubTypeCode in $customer/customerSubTypeCode
                            return
                                <customerSubTypeCode>{ data($customerSubTypeCode) }</customerSubTypeCode>
                        }
                        {
                            for $customerTypeCode in $customer/customerTypeCode
                            return
                                <customerTypeCode>{ data($customerTypeCode) }</customerTypeCode>
                        }
                        {
                            for $defaultPostpaidPayChannelId in $customer/defaultPostpaidPayChannelId
                            return
                                <defaultPostpaidPayChannelId>{ data($defaultPostpaidPayChannelId) }</defaultPostpaidPayChannelId>
                        }
                        {
                            for $newBillCycleCode in $customer/newBillCycleCode
                            return
                                <newBillCycleCode>{ data($newBillCycleCode) }</newBillCycleCode>
                        }
                        <creditValueCode>{ $creditValueCode1 }</creditValueCode>
                        {
                            for $marketSegmentList in $customer/marketSegmentList
                            return
                                <marketSegmentList>{ $marketSegmentList/@* , $marketSegmentList/node() }</marketSegmentList>
                        }
                        <customerPIN>{ data($customer/customerPIN) }</customerPIN>
                        {
                            for $hearingImpairedInd in $customer/hearingImpairedInd
                            return
                                <hearingImpairedInd>{ data($hearingImpairedInd) }</hearingImpairedInd>
                        }
                        {
                            for $visuallyImpairedInd in $customer/visuallyImpairedInd
                            return
                                <visuallyImpairedInd>{ data($visuallyImpairedInd) }</visuallyImpairedInd>
                        }
                        {
                            for $teamMemberId in $customer/teamMemberId
                            return
                                <teamMemberId>{ data($teamMemberId) }</teamMemberId>
                        }
                        {
                            for $teamMemberConcessionCode in $customer/teamMemberConcessionCode
                            return
                                <teamMemberConcessionCode>{ data($teamMemberConcessionCode) }</teamMemberConcessionCode>
                        }
                        {
                            for $lastUpdateTimestamp in $customer/lastUpdateTimestamp
                            return
                                <lastUpdateTimestamp>{ data($lastUpdateTimestamp) }</lastUpdateTimestamp>
                        }
                        {
                            for $brandId in $customer/brandId
                            return
                                <brandId>{ data($brandId) }</brandId>
                        }
                    </customer>
            }
            {
                let $auditInfo := $auditInfo1
                return
                    <auditInfo>
                        {
                            for $userId in $auditInfo/ns2:userId
                            return
                                <ns1:userId>{ data($userId) }</ns1:userId>
                        }
                        {
                            for $userTypeCode in $auditInfo/ns2:userTypeCode
                            return
                                <ns1:userTypeCode>{ data($userTypeCode) }</ns1:userTypeCode>
                        }
                        {
                            for $salesRepresentativeId in $auditInfo/ns2:salesRepresentativeId
                            return
                                <ns1:salesRepresentativeId>{ data($salesRepresentativeId) }</ns1:salesRepresentativeId>
                        }
                        {
                            for $channelOrganizationId in $auditInfo/ns2:channelOrganizationId
                            return
                                <ns1:channelOrganizationId>{ data($channelOrganizationId) }</ns1:channelOrganizationId>
                        }
                        {
                            for $outletId in $auditInfo/ns2:outletId
                            return
                                <ns1:outletId>{ data($outletId) }</ns1:outletId>
                        }
                        {
                            for $originatorApplicationId in $auditInfo/ns2:originatorApplicationId
                            return
                                <ns1:originatorApplicationId>{ data($originatorApplicationId) }</ns1:originatorApplicationId>
                        }
                        {
                            for $correlationId in $auditInfo/ns2:correlationId
                            return
                                <ns1:correlationId>{ data($correlationId) }</ns1:correlationId>
                        }
                        {
                            for $timestamp in $auditInfo/ns2:timestamp
                            return
                                <ns1:timestamp>{ data($timestamp) }</ns1:timestamp>
                        }
                    </auditInfo>
            }
        </ns0:updateCustomer>
};

declare variable $getCustomerResponse1 as element(ns0:getCustomerResponse) external;
declare variable $creditValueCode1 as xs:string external;
declare variable $auditInfo1 as element() external;

xf:create_UpdateCustomerRequest($getCustomerResponse1,
    $creditValueCode1,
    $auditInfo1)