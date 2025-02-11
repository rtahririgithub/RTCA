(:: pragma bea:global-element-parameter parameter="$assessCreditWorthinessResponse1" element="ns2:assessCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns2:overrideCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/assessCreditWorthinessResponse_overrideCreditWorthinessResponse/";

declare function xf:assessCreditWorthinessResponse_overrideCreditWorthinessResponse($assessCreditWorthinessResponse1 as element(ns2:assessCreditWorthinessResponse))
    as element(ns2:overrideCreditWorthinessResponse) {
        <ns2:overrideCreditWorthinessResponse>
            <ns2:overridenCreditWorthiness>
                <ns1:customerID>{ data($assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:customerID) }</ns1:customerID>
                {
                    for $creditAssessmentId in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:creditAssessmentId
                    return
                        <ns1:creditAssessmentId>{ data($creditAssessmentId) }</ns1:creditAssessmentId>
                }
                {
                    for $creditValueCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:creditValueCd
                    return
                        <ns1:creditValueCd>{ data($creditValueCd) }</ns1:creditValueCd>
                }
                {
                    for $assessmentMessageCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:assessmentMessageCd
                    return
                        <ns1:assessmentMessageCd>{ data($assessmentMessageCd) }</ns1:assessmentMessageCd>
                }
                {
                    for $productCategoryQualification in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:productCategoryQualification
                    return
                        <ns1:productCategoryQualification>{ $productCategoryQualification/@* , $productCategoryQualification/node() }</ns1:productCategoryQualification>
                }
                {
                    for $fraudIndicatorCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:fraudIndicatorCd
                    return
                        <ns1:fraudIndicatorCd>{ data($fraudIndicatorCd) }</ns1:fraudIndicatorCd>
                }
                {
                    for $fraudMessageCdList in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:fraudMessageCdList
                    return
                        <ns1:fraudMessageCdList>{ data($fraudMessageCdList) }</ns1:fraudMessageCdList>
                }
                {
                    for $creditAssessmentTypeCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:creditAssessmentTypeCd
                    return
                        <ns1:creditAssessmentTypeCd>{ data($creditAssessmentTypeCd) }</ns1:creditAssessmentTypeCd>
                }
                {
                    for $creditAssessmentSubTypeCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:creditAssessmentSubTypeCd
                    return
                        <ns1:creditAssessmentSubTypeCd>{ data($creditAssessmentSubTypeCd) }</ns1:creditAssessmentSubTypeCd>
                }
                {
                    for $creditAssessmentCreationDate in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:creditAssessmentCreationDate
                    return
                        <ns1:creditAssessmentCreationDate>{ data($creditAssessmentCreationDate) }</ns1:creditAssessmentCreationDate>
                }
                {
                    for $decisionCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns1:decisionCd
                    return
                        <ns1:decisionCd>{ data($decisionCd) }</ns1:decisionCd>
                }
                {
                    for $updateCreditWorthinessErrorCd in $assessCreditWorthinessResponse1/ns2:assessedCreditWorthiness/ns2:updateCreditWorthinessErrorCd
                    return
                        <ns2:updateCreditWorthinessErrorCd>{ data($updateCreditWorthinessErrorCd) }</ns2:updateCreditWorthinessErrorCd>
                }
            </ns2:overridenCreditWorthiness>
        </ns2:overrideCreditWorthinessResponse>
};

declare variable $assessCreditWorthinessResponse1 as element(ns2:assessCreditWorthinessResponse) external;

xf:assessCreditWorthinessResponse_overrideCreditWorthinessResponse($assessCreditWorthinessResponse1)