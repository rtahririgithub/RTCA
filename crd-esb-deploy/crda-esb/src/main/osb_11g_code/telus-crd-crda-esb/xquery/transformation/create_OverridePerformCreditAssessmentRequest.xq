(:: pragma bea:global-element-parameter parameter="$overrideCreditWorthiness1" element="ns8:overrideCreditWorthiness" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getAdditionalCustomerDataResponse1" element="ns0:getAdditionalCustomerDataResponse" location="../../wsdls/GetAdditionalCustomerDataSplitJoinRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns1:performCreditAssessment" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditDepositTypes_v1";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoinRequestResponse_v2";
declare namespace ns5 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns6 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerSubDomain_v3";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/create_OverridePerformCreditAssessmentRequest/";
declare namespace ns7 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns8 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";

declare function xf:create_OverridePerformCreditAssessmentRequest($overrideCreditWorthiness1 as element(ns8:overrideCreditWorthiness),
    $getAdditionalCustomerDataResponse1 as element(ns0:getAdditionalCustomerDataResponse))
    as element(ns1:performCreditAssessment) {
        <ns1:performCreditAssessment>
          <ns1:creditAssessmentRequest xsi:type="ent:OverrideCreditAssessmentRequest">
            <ns1:creditAssessmentTypeCd>{ data($overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:creditAssessmentTypeCd) }</ns1:creditAssessmentTypeCd>
            <ns1:creditAssessmentSubTypeCd>{ data($overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:creditAssessmentSubTypeCd) }</ns1:creditAssessmentSubTypeCd>
            <ns1:customerID>{ data($overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:customerID) }</ns1:customerID>
            <ns1:applicationID>{ data($overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:applicationID) }</ns1:applicationID>
            {
                for $channelID in $overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:channelID
                return
                    <ns1:channelID>{ data($channelID) }</ns1:channelID>
            }
            <ns1:lineOfBusiness>{ data($overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:lineOfBusiness) }</ns1:lineOfBusiness>
            {
                for $commentTxt in $overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:commentTxt
                return
                    <ns1:commentTxt>{ data($commentTxt) }</ns1:commentTxt>
            }
            {
                let $creditCustomerInfo := $getAdditionalCustomerDataResponse1/ns0:creditCustomerInfo
                return
                    <ns1:creditCustomerInfo>{ $creditCustomerInfo/* }</ns1:creditCustomerInfo>
            }
            {
                let $creditProfileData := $getAdditionalCustomerDataResponse1/ns0:creditProfileData
                return
                    <ns1:creditProfileData>{ $creditProfileData/* }</ns1:creditProfileData>
            }
            {
                for $customerCollectionData in $getAdditionalCustomerDataResponse1/ns0:customerCollectionData
                return
                    <ns1:customerCollectionData>{ $customerCollectionData/* }</ns1:customerCollectionData>
            }
            {
                for $depositItemList in $getAdditionalCustomerDataResponse1/ns0:depositItemList
                return
                    <ns1:depositItemList>{ $depositItemList/* }</ns1:depositItemList>
            }
            {
                for $newFraudIndictorCd in $overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:newFraudIndictorCd
                return
                    <ns1:newCreditValueCd>{ data($newFraudIndictorCd) }</ns1:newCreditValueCd>
            }
            {
                for $newCreditValueCd in $overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:newCreditValueCd
                return
                    <ns1:newFraudIndicatorCd>{ data($newCreditValueCd) }</ns1:newFraudIndicatorCd>
            }
            {
                for $newCreditCheckConsentCd in $overrideCreditWorthiness1/ns8:overrideCreditWorthinessRequest/ns8:newCreditCheckConsentCd
                return
                    <ns1:newCreditCheckConsentCd>{ data($newCreditCheckConsentCd) }</ns1:newCreditCheckConsentCd>
            }
            
            </ns1:creditAssessmentRequest>
            {
                let $auditInfo := $overrideCreditWorthiness1/ns8:auditInfo
                return
                    <ns1:auditInfo>{ $auditInfo/* }</ns1:auditInfo>
            }
        </ns1:performCreditAssessment>
};

declare variable $overrideCreditWorthiness1 as element(ns8:overrideCreditWorthiness) external;
declare variable $getAdditionalCustomerDataResponse1 as element(ns0:getAdditionalCustomerDataResponse) external;

xf:create_OverridePerformCreditAssessmentRequest($overrideCreditWorthiness1,
    $getAdditionalCustomerDataResponse1)