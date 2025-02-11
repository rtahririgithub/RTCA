(:: pragma bea:local-element-parameter parameter="$creditAssessmentTransactionResult1" type="ns0:performCreditAssessmentResponse/ns0:creditAssessmentTransactionResult" location="../../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:local-element-parameter parameter="$auditInfo1" type="ns5:assessCreditWorthiness/ns5:auditInfo" location="../../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-return element="ns2:updateCreditWorthiness" location="../../../wsdls/WLNCreditProfileDataManagementServiceRequestResponse_v1_3.xsd" ::)

declare namespace ns2 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileDataManagementServiceRequestResponse_v1";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v6";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace ns5 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns6 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/EnterpriseCreditAssessmentTypes_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/external/create_UpdateCreditWorthiness_Request/";

declare function xf:create_UpdateCreditWorthiness_Request($creditAssessmentTransactionResult1 as element(),
    $auditInfo1 as element())
    as element(ns2:updateCreditWorthiness) {
        <ns2:updateCreditWorthiness>
            {
                let $creditAssessmentTransactionResult := $creditAssessmentTransactionResult1
                return
                    <ns2:creditAssessmentTransactionResult>
                        <ns6:creditAssessmentID>{ data($creditAssessmentTransactionResult/ns6:creditAssessmentID) }</ns6:creditAssessmentID>
                        <ns6:customerID>{ data($creditAssessmentTransactionResult/ns6:customerID) }</ns6:customerID>
                        {
                            for $creditAssessmentDate in $creditAssessmentTransactionResult/ns6:creditAssessmentDate
                            return
                                <ns6:creditAssessmentDate>{ data($creditAssessmentDate) }</ns6:creditAssessmentDate>
                        }
                        {
                            for $creditAssessmentTypeCd in $creditAssessmentTransactionResult/ns6:creditAssessmentTypeCd
                            return
                                <ns6:creditAssessmentTypeCd>{ data($creditAssessmentTypeCd) }</ns6:creditAssessmentTypeCd>
                        }
                        {
                            for $creditAssessmentSubTypeCd in $creditAssessmentTransactionResult/ns6:creditAssessmentSubTypeCd
                            return
                                <ns6:creditAssessmentSubTypeCd>{ data($creditAssessmentSubTypeCd) }</ns6:creditAssessmentSubTypeCd>
                        }
                        {
                            for $creditAssessmentStatus in $creditAssessmentTransactionResult/ns6:creditAssessmentStatus
                            return
                                <ns6:creditAssessmentStatus>{ data($creditAssessmentStatus) }</ns6:creditAssessmentStatus>
                        }
                        {
                            for $creditAssessmentStatusReasonCd in $creditAssessmentTransactionResult/ns6:creditAssessmentStatusReasonCd
                            return
                                <ns6:creditAssessmentStatusReasonCd>{ data($creditAssessmentStatusReasonCd) }</ns6:creditAssessmentStatusReasonCd>
                        }
                        {
                            for $creditAssessmentStatusDate in $creditAssessmentTransactionResult/ns6:creditAssessmentStatusDate
                            return
                                <ns6:creditAssessmentStatusDate>{ data($creditAssessmentStatusDate) }</ns6:creditAssessmentStatusDate>
                        }
                        {
                            for $creditDecisioningResult in $creditAssessmentTransactionResult/ns6:creditDecisioningResult
                            return
                                <ns6:creditDecisioningResult>{ $creditDecisioningResult/@* , $creditDecisioningResult/node() }</ns6:creditDecisioningResult>
                        }
                        {
                            for $creditAssessmentDataSourceCd in $creditAssessmentTransactionResult/ns6:creditAssessmentDataSourceCd
                            return
                                <ns6:creditAssessmentDataSourceCd>{ data($creditAssessmentDataSourceCd) }</ns6:creditAssessmentDataSourceCd>
                        }
                        {
                            for $creditBureauReportInd in $creditAssessmentTransactionResult/ns6:creditBureauReportInd
                            return
                                <ns6:creditBureauReportInd>{ data($creditBureauReportInd) }</ns6:creditBureauReportInd>
                        }
                        {
                            for $creditBureauReportSourceCd in $creditAssessmentTransactionResult/ns6:creditBureauReportSourceCd
                            return
                                <ns6:creditBureauReportSourceCd>{ data($creditBureauReportSourceCd) }</ns6:creditBureauReportSourceCd>
                        }
                        {
                            for $depositAmt in $creditAssessmentTransactionResult/ns6:depositAmt
                            return
                                <ns6:depositAmt>{ data($depositAmt) }</ns6:depositAmt>
                        }
                        {
                            for $creditScoreCd in $creditAssessmentTransactionResult/ns6:creditScoreCd
                            return
                                <ns6:creditScoreCd>{ data($creditScoreCd) }</ns6:creditScoreCd>
                        }
                        {
                            for $creditScoreTypeCd in $creditAssessmentTransactionResult/ns6:creditScoreTypeCd
                            return
                                <ns6:creditScoreTypeCd>{ data($creditScoreTypeCd) }</ns6:creditScoreTypeCd>
                        }
                        {
                            for $creditClass in $creditAssessmentTransactionResult/ns6:creditClass
                            return
                                <ns6:creditClass>{ data($creditClass) }</ns6:creditClass>
                        }
                        {
                            for $creditDecisionCd in $creditAssessmentTransactionResult/ns6:creditDecisionCd
                            return
                                <ns6:creditDecisionCd>{ $creditDecisionCd/@* , $creditDecisionCd/node() }</ns6:creditDecisionCd>
                        }
                        {
                            for $userID in $creditAssessmentTransactionResult/ns6:userID
                            return
                                <ns6:userID>{ data($userID) }</ns6:userID>
                        }
                        {
                            for $channelID in $creditAssessmentTransactionResult/ns6:channelID
                            return
                                <ns6:channelID>{ data($channelID) }</ns6:channelID>
                        }
                        {
                            for $commentTxt in $creditAssessmentTransactionResult/ns6:commentTxt
                            return
                                <ns6:commentTxt>{ data($commentTxt) }</ns6:commentTxt>
                        }
                    </ns2:creditAssessmentTransactionResult>
            }
            {
                let $auditInfo := $auditInfo1
                return
                    <ns2:auditInfo>
                        {
                            for $userId in $auditInfo/ns3:userId
                            return
                                <ns1:userId>{ data($userId) }</ns1:userId>
                        }
                        {
                            for $userTypeCode in $auditInfo/ns3:userTypeCode
                            return
                                <ns1:userTypeCode>{ data($userTypeCode) }</ns1:userTypeCode>
                        }
                        {
                            for $salesRepresentativeId in $auditInfo/ns3:salesRepresentativeId
                            return
                                <ns1:salesRepresentativeId>{ data($salesRepresentativeId) }</ns1:salesRepresentativeId>
                        }
                        {
                            for $channelOrganizationId in $auditInfo/ns3:channelOrganizationId
                            return
                                <ns1:channelOrganizationId>{ data($channelOrganizationId) }</ns1:channelOrganizationId>
                        }
                        {
                            for $outletId in $auditInfo/ns3:outletId
                            return
                                <ns1:outletId>{ data($outletId) }</ns1:outletId>
                        }
                       <ns1:originatorApplicationId>
                            {
                                if ( fn:empty($auditInfo/ns3:originatorApplicationId/text())  )                                 
                                then   
                                	("6102")                             
                                else 
                                    data($auditInfo/ns3:originatorApplicationId/text())
                            }
						</ns1:originatorApplicationId>      
                        {
                            for $correlationId in $auditInfo/ns3:correlationId
                            return
                                <ns1:correlationId>{ data($correlationId) }</ns1:correlationId>
                        }
                        {
                            for $timestamp in $auditInfo/ns3:timestamp
                            return
                                <ns1:timestamp>{ data($timestamp) }</ns1:timestamp>
                        }
                    </ns2:auditInfo>
            }
        </ns2:updateCreditWorthiness>
};

declare variable $creditAssessmentTransactionResult1 as element() external;
declare variable $auditInfo1 as element() external;

xf:create_UpdateCreditWorthiness_Request($creditAssessmentTransactionResult1,
    $auditInfo1)