(:: pragma bea:schema-type-parameter parameter="$wlsUnifiedCreditSearchResult" type="wlsUCSearchResult:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getAccountByAccountNumberResponse" element="ais:getAccountByAccountNumberResponse" location="../../wsdls/ais/AccountInformationServiceRequestResponse_v3_5.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getCreditWorthinessResponse" element="wcdm:getCreditWorthinessResponse" location="../../wsdls/wcdm/WLSCreditManagementSvcRequestResponse_v2_1.xsd" ::)
(:: pragma bea:schema-type-return type="wlsUCSearchResult:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)

declare namespace wlsUCSearchResult = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace cre = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace wir = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelessCreditTypes_v2";
declare namespace cus = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v3";
declare namespace wcdm = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLSCreditManagementSvcRequestResponse_v2";
declare namespace ais = "http://xmlschema.tmi.telus.com/srv/CMO/InformationMgmt/AccountInformationServiceRequestResponse_v3";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/add_UC_AdditionalInfo_to_topmatchAccount/";
declare namespace ent = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v9";
declare namespace acc = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/AccountInformationTypes_v2";

declare function xf:add_UC_AdditionalInfo_to_topmatchAccount($wlsUnifiedCreditSearchResult as element(),
    $getAccountByAccountNumberResponse as element(ais:getAccountByAccountNumberResponse),
    $getCreditWorthinessResponse as element(wcdm:getCreditWorthinessResponse))
    as element() {
        <wlsUCSearchResult:WlsUnifiedCreditSearchResult>
            <wlsUCSearchResult:matchFound>
                {
                    for $indicator in $wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchFound/wlsUCSearchResult:indicator
                    return
                        <wlsUCSearchResult:indicator>{ data($indicator) }</wlsUCSearchResult:indicator>
                }
                {
                    for $reasonCd in $wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchFound/wlsUCSearchResult:reasonCd
                    return
                        <wlsUCSearchResult:reasonCd>{ data($reasonCd) }</wlsUCSearchResult:reasonCd>
                }
            </wlsUCSearchResult:matchFound>
            {
                for $dataInquiryErrorCodeList in $wlsUnifiedCreditSearchResult/wlsUCSearchResult:dataInquiryErrorCodeList
                return
                    <wlsUCSearchResult:dataInquiryErrorCodeList>{ data($dataInquiryErrorCodeList) }</wlsUCSearchResult:dataInquiryErrorCodeList>
            }
            <wlsUCSearchResult:lineOfBusiness>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:lineOfBusiness) }</wlsUCSearchResult:lineOfBusiness>
            <wlsUCSearchResult:unifiedCreditDormantInd>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:unifiedCreditDormantInd) }</wlsUCSearchResult:unifiedCreditDormantInd>
            <wlsUCSearchResult:matchedAccount>
                <wlsUCSearchResult:accountData>
                    <wlsUCSearchResult:billingAccountNumber>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchedAccount/wlsUCSearchResult:accountData/wlsUCSearchResult:billingAccountNumber) }</wlsUCSearchResult:billingAccountNumber>
                    <wlsUCSearchResult:accountType>{ data($getAccountByAccountNumberResponse/ais:account/acc:accountType) }</wlsUCSearchResult:accountType>
                    <wlsUCSearchResult:accountSubType>{ data($getAccountByAccountNumberResponse/ais:account/acc:accountSubType) }</wlsUCSearchResult:accountSubType>
                    <wlsUCSearchResult:brandId>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchedAccount/wlsUCSearchResult:accountData/wlsUCSearchResult:brandId) }</wlsUCSearchResult:brandId>
                    <wlsUCSearchResult:accountCreationDate>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchedAccount/wlsUCSearchResult:accountData/wlsUCSearchResult:accountCreationDate) }</wlsUCSearchResult:accountCreationDate>
                    <wlsUCSearchResult:startServiceDate>{ data($getAccountByAccountNumberResponse/ais:account/acc:startServiceDate)}</wlsUCSearchResult:startServiceDate>
                    <wlsUCSearchResult:accountStatus>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchedAccount/wlsUCSearchResult:accountData/wlsUCSearchResult:accountStatus) }</wlsUCSearchResult:accountStatus>
                    <wlsUCSearchResult:accountStatusDate>{ data($wlsUnifiedCreditSearchResult/wlsUCSearchResult:matchedAccount/wlsUCSearchResult:accountData/wlsUCSearchResult:accountStatusDate) }</wlsUCSearchResult:accountStatusDate>
                    <wlsUCSearchResult:statusActivityCode>{ data($getAccountByAccountNumberResponse/ais:account/acc:statusActivityCode) }</wlsUCSearchResult:statusActivityCode>
                    <wlsUCSearchResult:statusActivityReasonCode>{ data($getAccountByAccountNumberResponse/ais:account/acc:statusActivityReasonCode)}</wlsUCSearchResult:statusActivityReasonCode>
                </wlsUCSearchResult:accountData>
                <wlsUCSearchResult:creditWorthinessData>
                    <wlsUCSearchResult:riskLevelNumber>{ data($getCreditWorthinessResponse/wcdm:creditWorthiness/wir:creditProgram/wir:riskLevelNumber) }</wlsUCSearchResult:riskLevelNumber>
                    <wlsUCSearchResult:riskLevelDecisionCd>{ data($getCreditWorthinessResponse/wcdm:creditWorthiness/wir:creditProgram/wir:riskLevelDecisionCd) }</wlsUCSearchResult:riskLevelDecisionCd>
                    <wlsUCSearchResult:creditClassCd>{ data($getCreditWorthinessResponse/wcdm:creditWorthiness/wir:creditProgram/wir:creditClassCd) }</wlsUCSearchResult:creditClassCd>
                </wlsUCSearchResult:creditWorthinessData>
                {
                    for $warningHistoryList in $getCreditWorthinessResponse/wcdm:warningHistoryList
                    return
                        <wlsUCSearchResult:warningHistoryList>
                            <wlsUCSearchResult:warningCategoryCd>{ data($warningHistoryList/wir:warningCategoryCd) }</wlsUCSearchResult:warningCategoryCd>
                            <wlsUCSearchResult:warningCd>{ data($warningHistoryList/wir:warningCd) }</wlsUCSearchResult:warningCd>
                            <wlsUCSearchResult:warningTypeCd>{ data($warningHistoryList/wir:warningTypeCd) }</wlsUCSearchResult:warningTypeCd>
                            <wlsUCSearchResult:warningStatusCd>{ data($warningHistoryList/wir:warningStatusCd) }</wlsUCSearchResult:warningStatusCd>
                        </wlsUCSearchResult:warningHistoryList>
                }
                <wlsUCSearchResult:accountFinancialHistory>
                    <wlsUCSearchResult:delinquentInd>{ data($getAccountByAccountNumberResponse/ais:account/acc:financialHistory/cus:delinquentInd) }</wlsUCSearchResult:delinquentInd>
                </wlsUCSearchResult:accountFinancialHistory>
                <wlsUCSearchResult:creditBureauResult></wlsUCSearchResult:creditBureauResult>
                <wlsUCSearchResult:totalSubscribers>
                    <wlsUCSearchResult:totalActiveSubscribersNumber>{ count($getAccountByAccountNumberResponse/ais:account/acc:productSubscriberList/acc:activeSubscriber) }</wlsUCSearchResult:totalActiveSubscribersNumber>
                    <wlsUCSearchResult:totalReservedSubscribersNumber>{ count($getAccountByAccountNumberResponse/ais:account/acc:productSubscriberList/acc:reservedSubscriber) }</wlsUCSearchResult:totalReservedSubscribersNumber>
                    <wlsUCSearchResult:totalSuspendedSubscribersNumber>{ count($getAccountByAccountNumberResponse/ais:account/acc:productSubscriberList/acc:suspendedSubscriber) }</wlsUCSearchResult:totalSuspendedSubscribersNumber>
                </wlsUCSearchResult:totalSubscribers>
            </wlsUCSearchResult:matchedAccount>
        </wlsUCSearchResult:WlsUnifiedCreditSearchResult>
};

declare variable $wlsUnifiedCreditSearchResult as element() external;
declare variable $getAccountByAccountNumberResponse as element(ais:getAccountByAccountNumberResponse) external;
declare variable $getCreditWorthinessResponse as element(wcdm:getCreditWorthinessResponse) external;

xf:add_UC_AdditionalInfo_to_topmatchAccount(
	$wlsUnifiedCreditSearchResult,
    $getAccountByAccountNumberResponse,
    $getCreditWorthinessResponse)