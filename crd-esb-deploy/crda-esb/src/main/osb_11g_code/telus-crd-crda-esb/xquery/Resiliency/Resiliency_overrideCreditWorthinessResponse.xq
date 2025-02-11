(:: pragma bea:schema-type-parameter parameter="$consumerCreditProfile1" type="ns1:ConsumerCreditProfile" location="../../wsdls/WirelineCreditProfileManagementTypes_v2_1.xsd" ::)
(:: pragma bea:global-element-return element="ns2:overrideCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/Resiliency_overrideCreditWorthinessResponse/";

declare function xf:Resiliency_overrideCreditWorthinessResponse(
$consumerCreditProfile1 as element())
    as element(ns2:overrideCreditWorthinessResponse) {
        <ns2:overrideCreditWorthinessResponse>
            {
                let $creditWorthiness := $consumerCreditProfile1/ns1:creditWorthiness
                return
                    <ns2:overridenCreditWorthiness>
                        <ns1:customerID>{ data($creditWorthiness/ns1:customerID) }</ns1:customerID>
                        {
                            for $creditAssessmentId in $creditWorthiness/ns1:creditAssessmentId
                            return
                                <ns1:creditAssessmentId>{ data($creditAssessmentId) }</ns1:creditAssessmentId>
                        }
                        {
                            for $creditValueCd in $creditWorthiness/ns1:creditValueCd
                            return
                                <ns1:creditValueCd>{ data($creditValueCd) }</ns1:creditValueCd>
                        }
                        <ns1:assessmentMessageCd>ASMT_BYPASSED</ns1:assessmentMessageCd>
                        {
                            for $productCategoryQualification in $creditWorthiness/ns1:productCategoryQualification
                            return
                                <ns1:productCategoryQualification>
                                    {
                                        for $productCategoryList in $productCategoryQualification/ns0:productCategoryList
                                        return
                                            <ns0:productCategoryList>
                                                {
                                                    for $categoryCd in $productCategoryList/ns0:categoryCd
                                                    return
                                                        <ns0:categoryCd>{ data($categoryCd) }</ns0:categoryCd>
                                                }
                                                {
                                                    for $qualified in $productCategoryList/ns0:qualified
                                                    return
                                                        <ns0:qualified>{ data($qualified) }</ns0:qualified>
                                                }
                                            </ns0:productCategoryList>
                                    }
                                    <ns0:boltOnInd>{ data($productCategoryQualification/ns0:boltOnInd) }</ns0:boltOnInd>
                                </ns1:productCategoryQualification>
                        }
                        {
                            for $fraudIndicatorCd in $creditWorthiness/ns1:fraudIndicatorCd
                            return
                                <ns1:fraudIndicatorCd>{ data($fraudIndicatorCd) }</ns1:fraudIndicatorCd>
                        }
                        {
                            for $fraudMessageCdList in $creditWorthiness/ns1:fraudMessageCdList
                            return
                                <ns1:fraudMessageCdList>{ data($fraudMessageCdList) }</ns1:fraudMessageCdList>
                        }
                        {
                            for $creditAssessmentTypeCd in $creditWorthiness/ns1:creditAssessmentTypeCd
                            return
                                <ns1:creditAssessmentTypeCd>{ data($creditAssessmentTypeCd) }</ns1:creditAssessmentTypeCd>
                        }
                        {
                            for $creditAssessmentSubTypeCd in $creditWorthiness/ns1:creditAssessmentSubTypeCd
                            return
                                <ns1:creditAssessmentSubTypeCd>{ data($creditAssessmentSubTypeCd) }</ns1:creditAssessmentSubTypeCd>
                        }
                        {
                            for $creditAssessmentCreationDate in $creditWorthiness/ns1:creditAssessmentCreationDate
                            return
                                <ns1:creditAssessmentCreationDate>{ data($creditAssessmentCreationDate) }</ns1:creditAssessmentCreationDate>
                        }
                        {
                            for $decisionCd in $creditWorthiness/ns1:decisionCd
                            return
                                <ns1:decisionCd>{ data($decisionCd) }</ns1:decisionCd>
                        }
                        {
                            for $reportIndicator in $consumerCreditProfile1/ns1:reportIndicator
                            return
                                <ns2:creditBureauReportInd>{ data($reportIndicator) }</ns2:creditBureauReportInd>
                        }
                    </ns2:overridenCreditWorthiness>
            }
        </ns2:overrideCreditWorthinessResponse>
};

declare variable $consumerCreditProfile1 as element() external;

xf:Resiliency_overrideCreditWorthinessResponse($consumerCreditProfile1)