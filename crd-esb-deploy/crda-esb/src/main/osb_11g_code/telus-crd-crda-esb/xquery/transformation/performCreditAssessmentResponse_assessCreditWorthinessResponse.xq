(:: pragma bea:global-element-parameter parameter="$performCreditAssessmentResponse1" element="ns0:performCreditAssessmentResponse" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns3:assessCreditWorthinessResponse" location="../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/EnterpriseCreditAssessmentTypes_v2";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/performCreditAssessmentResponse_assessCreditWorthinessResponse/";

declare function xf:performCreditAssessmentResponse_assessCreditWorthinessResponse($performCreditAssessmentResponse1 as element(ns0:performCreditAssessmentResponse))
    as element(ns3:assessCreditWorthinessResponse) {
        <ns3:assessCreditWorthinessResponse>
            <ns3:assessedCreditWorthiness>
            	<ns2:customerID>{ data($performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:customerID) }</ns2:customerID>
                <ns2:creditAssessmentId>{ data($performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditAssessmentID) }</ns2:creditAssessmentId>
                <ns2:creditValueCd>{ data($performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:creditValueCd) }</ns2:creditValueCd>
                <ns2:creditRiskLevel>{ data($performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:creditRiskLevel) }</ns2:creditRiskLevel>
                <ns2:assessmentMessageCd>{ data($performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:assessmentMessageCd) }</ns2:assessmentMessageCd>
                {
                    for $productCategoryQualification in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:productCategoryQualification
                    return
                        <ns2:productCategoryQualification>
                            {
                                for $productCategoryList in $productCategoryQualification/ns1:productCategoryList
                                return
                                    <ns1:productCategoryList>
                                        {
                                            for $categoryCd in $productCategoryList/ns1:categoryCd
                                            return
                                                <ns1:categoryCd>{ data($categoryCd) }</ns1:categoryCd>
                                        }
                                        {
                                            for $qualified in $productCategoryList/ns1:qualified
                                            return
                                                <ns1:qualified>{ data($qualified) }</ns1:qualified>
                                        }
                                    </ns1:productCategoryList>
                            }
                            <ns1:boltOnInd>{ data($productCategoryQualification/ns1:boltOnInd[1]) }</ns1:boltOnInd>
                        </ns2:productCategoryQualification>
                }
                {
                    for $fraudIndicatorCd in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:fraudIndicatorCd
                    return
                        <ns2:fraudIndicatorCd>{ data($fraudIndicatorCd) }</ns2:fraudIndicatorCd>
                }
                {
                    for $fraudMessageCdList in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:fraudMessageCdList
                    return
                        <ns2:fraudMessageCdList>{ data($fraudMessageCdList) }</ns2:fraudMessageCdList>
                }
                {
                    for $creditAssessmentTypeCd in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditAssessmentTypeCd
                    return
                        <ns2:creditAssessmentTypeCd>{ data($creditAssessmentTypeCd) }</ns2:creditAssessmentTypeCd>
                }
                {
                    for $creditAssessmentSubTypeCd in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditAssessmentSubTypeCd
                    return
                        <ns2:creditAssessmentSubTypeCd>{ data($creditAssessmentSubTypeCd) }</ns2:creditAssessmentSubTypeCd>
                }
                {
                    for $creditAssessmentDate in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditAssessmentDate
                    return
                        <ns2:creditAssessmentCreationDate>{ data($creditAssessmentDate) }</ns2:creditAssessmentCreationDate>
                }
                {
                    for $decisionCd in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditDecisioningResult/ns1:decisionCd
                    return
                        <ns2:decisionCd>{ data($decisionCd) }</ns2:decisionCd>
                }
                {
                    for $creditBureauReportInd in $performCreditAssessmentResponse1/ns0:creditAssessmentTransactionResult/ns4:creditBureauReportInd
                    return
                        <ns3:creditBureauReportInd>{ data($creditBureauReportInd) }</ns3:creditBureauReportInd>
                }
            </ns3:assessedCreditWorthiness>
        </ns3:assessCreditWorthinessResponse>
};

declare variable $performCreditAssessmentResponse1 as element(ns0:performCreditAssessmentResponse) external;

xf:performCreditAssessmentResponse_assessCreditWorthinessResponse($performCreditAssessmentResponse1)