xquery version "2004-draft";
(:: pragma bea:schema-type-parameter parameter="$wlsUnifiedCreditSearchResult1" type="ns0:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)
(:: pragma bea:schema-type-return type="ns0:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)

declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/Format_WlsUnifiedCreditSearchResult/";

declare function Format_WlsUnifiedCreditSearchResult1($wlsUnifiedCreditSearchResult1 as element())
    as element() {
       <ns0:unifiedCreditSearchResult xsi:type="ent:WlsUnifiedCreditSearchResult">
            <ns0:matchFound>
                {
                    for $indicator in $wlsUnifiedCreditSearchResult1/ns0:matchFound/ns0:indicator
                    return
                        <ns0:indicator>{ data($indicator) }</ns0:indicator>
                }
                {
                    for $reasonCd in $wlsUnifiedCreditSearchResult1/ns0:matchFound/ns0:reasonCd
                    return
                        <ns0:reasonCd>{ data($reasonCd) }</ns0:reasonCd>
                }
            </ns0:matchFound>
       {
            if ($wlsUnifiedCreditSearchResult1/ns0:lineOfBusiness)
            then <ns0:lineOfBusiness>{ data($wlsUnifiedCreditSearchResult1/ns0:lineOfBusiness) }</ns0:lineOfBusiness>
            else ()
        }
            <ns0:unifiedCreditDormantInd>{ data($wlsUnifiedCreditSearchResult1/ns0:unifiedCreditDormantInd) }</ns0:unifiedCreditDormantInd>
            {
            if($wlsUnifiedCreditSearchResult1/ns0:matchFound/ns0:indicator='true')
            then
            <ns0:matchedAccount>
                <ns0:accountData>
                    <ns0:billingAccountNumber>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:billingAccountNumber) }</ns0:billingAccountNumber>
                    <ns0:accountType>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:accountType) }</ns0:accountType>
                    <ns0:accountSubType>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:accountSubType) }</ns0:accountSubType>
                    <ns0:brandId>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:brandId) }</ns0:brandId>
                    <ns0:accountCreationDate>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:accountCreationDate) }</ns0:accountCreationDate>
                    {
                        for $startServiceDate in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:startServiceDate
                        return
                            <ns0:startServiceDate>{ data($startServiceDate) }</ns0:startServiceDate>
                    }
                    <ns0:accountStatus>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:accountStatus) }</ns0:accountStatus>
                    <ns0:accountStatusDate>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:accountStatusDate) }</ns0:accountStatusDate>
                    {
                        for $statusActivityCode in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:statusActivityCode
                        return
                            <ns0:statusActivityCode>{ data($statusActivityCode) }</ns0:statusActivityCode>
                    }
                    {
                        for $statusActivityReasonCode in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountData/ns0:statusActivityReasonCode
                        return
                            <ns0:statusActivityReasonCode>{ data($statusActivityReasonCode) }</ns0:statusActivityReasonCode>
                    }
                </ns0:accountData>
                <ns0:creditWorthinessData>
                    {
                        for $riskLevelNumber in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditWorthinessData/ns0:riskLevelNumber
                        return
                            <ns0:riskLevelNumber>{ data($riskLevelNumber) }</ns0:riskLevelNumber>
                    }
                    {
                        for $riskLevelDecisionCd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditWorthinessData/ns0:riskLevelDecisionCd
                        return
                            <ns0:riskLevelDecisionCd>{ data($riskLevelDecisionCd) }</ns0:riskLevelDecisionCd>
                    }
                    <ns0:creditClassCd>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditWorthinessData/ns0:creditClassCd) }</ns0:creditClassCd>
                </ns0:creditWorthinessData>
                {
                if($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:warningHistoryList)
                then
                <ns0:warningHistoryList>
                    <ns0:warningCategoryCd>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:warningHistoryList[1]/ns0:warningCategoryCd) }</ns0:warningCategoryCd>
                    <ns0:warningCd>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:warningHistoryList[1]/ns0:warningCd) }</ns0:warningCd>
                    {
                        for $warningTypeCd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:warningHistoryList[1]/ns0:warningTypeCd
                        return
                            <ns0:warningTypeCd>{ data($warningTypeCd) }</ns0:warningTypeCd>
                    }
                    {
                        for $warningStatusCd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:warningHistoryList[1]/ns0:warningStatusCd
                        return
                            <ns0:warningStatusCd>{ data($warningStatusCd) }</ns0:warningStatusCd>
                    }
                </ns0:warningHistoryList>
                else()
                }
                {
                if($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountFinancialHistory)
                then
                <ns0:accountFinancialHistory>
                    {
                        for $delinquentInd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:accountFinancialHistory/ns0:delinquentInd
                        return
                            <ns0:delinquentInd>{ data($delinquentInd) }</ns0:delinquentInd>
                    }
                </ns0:accountFinancialHistory>
                else()
                }
                {
                if($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult)
                then
                <ns0:creditBureauResult>
                    {
                        for $reportSourceCd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult/ns0:reportSourceCd
                        return
                            <ns0:reportSourceCd>{ data($reportSourceCd) }</ns0:reportSourceCd>
                    }
                    {
                        for $errorCd in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult/ns0:errorCd
                        return
                            <ns0:errorCd>{ data($errorCd) }</ns0:errorCd>
                    }
                    <ns0:adjudicationResult>
                        <ns0:creditDecision>
                            <ns1:creditDecisionCd>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult/ns0:adjudicationResult/ns0:creditDecision/ns1:creditDecisionCd) }</ns1:creditDecisionCd>
                            <ns1:creditDecisionMessage>{ data($wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult/ns0:adjudicationResult/ns0:creditDecision/ns1:creditDecisionMessage) }</ns1:creditDecisionMessage>
                        </ns0:creditDecision>
                    </ns0:adjudicationResult>
                    {
                        for $creationDate in $wlsUnifiedCreditSearchResult1/ns0:matchedAccount/ns0:creditBureauResult/ns0:creationDate
                        return
                            <ns0:creationDate>{ data($creationDate) }</ns0:creationDate>
                    }
                </ns0:creditBureauResult>
                else()
                }
            </ns0:matchedAccount>
            else()
            }
        </ns0:unifiedCreditSearchResult>
};

declare variable $wlsUnifiedCreditSearchResult1 as element() external;

Format_WlsUnifiedCreditSearchResult1($wlsUnifiedCreditSearchResult1)