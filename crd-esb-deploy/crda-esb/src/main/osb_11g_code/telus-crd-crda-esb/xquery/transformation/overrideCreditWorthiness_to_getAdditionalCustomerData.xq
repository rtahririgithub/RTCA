(:: pragma bea:global-element-parameter parameter="$overrideCreditWorthiness1" element="ns2:overrideCreditWorthiness" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns0:getAdditionalCustomerData" location="../../wsdls/GetAdditionalCustomerDataSplitJoinRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoinRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/overrideCreditWorthiness_to_getAdditionalCustomerData/";

declare function xf:overrideCreditWorthiness_to_getAdditionalCustomerData($overrideCreditWorthiness1 as element(ns2:overrideCreditWorthiness))
    as element(ns0:getAdditionalCustomerData) {
        <ns0:getAdditionalCustomerData>
            <ns0:assessCreditWorthinessRequest>
                <ns2:creditAssessmentTypeCd>{ data($overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:creditAssessmentTypeCd) }</ns2:creditAssessmentTypeCd>
                <ns2:creditAssessmentSubTypeCd>{ data($overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:creditAssessmentSubTypeCd) }</ns2:creditAssessmentSubTypeCd>
                <ns2:customerID>{ data($overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:customerID) }</ns2:customerID>
                {
                    for $commentTxt in $overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:commentTxt
                    return
                        <ns2:commentTxt>{ data($commentTxt) }</ns2:commentTxt>
                }
                <ns2:applicationID>{ data($overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:applicationID) }</ns2:applicationID>
                {
                    for $channelID in $overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:channelID
                    return
                        <ns2:channelID>{ data($channelID) }</ns2:channelID>
                }
                <ns2:lineOfBusiness>{ data($overrideCreditWorthiness1/ns2:overrideCreditWorthinessRequest/ns2:lineOfBusiness) }</ns2:lineOfBusiness>
            </ns0:assessCreditWorthinessRequest>
            {
                let $auditInfo := $overrideCreditWorthiness1/ns2:auditInfo
                return
                    <ns0:auditInfo>
                        {
                            for $userId in $auditInfo/ns1:userId
                            return
                                <ns1:userId>{ data($userId) }</ns1:userId>
                        }
                        {
                            for $userTypeCode in $auditInfo/ns1:userTypeCode
                            return
                                <ns1:userTypeCode>{ data($userTypeCode) }</ns1:userTypeCode>
                        }
                        {
                            for $salesRepresentativeId in $auditInfo/ns1:salesRepresentativeId
                            return
                                <ns1:salesRepresentativeId>{ data($salesRepresentativeId) }</ns1:salesRepresentativeId>
                        }
                        {
                            for $channelOrganizationId in $auditInfo/ns1:channelOrganizationId
                            return
                                <ns1:channelOrganizationId>{ data($channelOrganizationId) }</ns1:channelOrganizationId>
                        }
                        {
                            for $outletId in $auditInfo/ns1:outletId
                            return
                                <ns1:outletId>{ data($outletId) }</ns1:outletId>
                        }
                        {
                            for $originatorApplicationId in $auditInfo/ns1:originatorApplicationId
                            return
                                <ns1:originatorApplicationId>{ data($originatorApplicationId) }</ns1:originatorApplicationId>
                        }
                        {
                            for $correlationId in $auditInfo/ns1:correlationId
                            return
                                <ns1:correlationId>{ data($correlationId) }</ns1:correlationId>
                        }
                        {
                            for $timestamp in $auditInfo/ns1:timestamp
                            return
                                <ns1:timestamp>{ data($timestamp) }</ns1:timestamp>
                        }
                    </ns0:auditInfo>
            }
        </ns0:getAdditionalCustomerData>
};

declare variable $overrideCreditWorthiness1 as element(ns2:overrideCreditWorthiness) external;

xf:overrideCreditWorthiness_to_getAdditionalCustomerData($overrideCreditWorthiness1)