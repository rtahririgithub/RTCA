xquery version "1.0" encoding "Cp1252";
(:: pragma bea:global-element-parameter parameter="$fmsr_FindMatchingSearchResultList" element="fmsr:findMatchingSearchResultList" location="../../../wsdls/FindMatchingSearchResult.xsd" ::)
(:: pragma bea:schema-type-return type="ecrda:WlsUnifiedCreditSearchResult" location="../../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)


declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/TOCP/test/";
declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ecrda = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace fmsr = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WCPMProxyMDMSearchResult";

declare function convert_findMatchingSearchResultList_to_WlsUnifiedCreditSearchResult(
$fmsr_FindMatchingSearchResultList as element(fmsr:findMatchingSearchResultList)) as element() {

    <ecrda:WlsUnifiedCreditSearchResult>
    
        <ecrda:matchFound>        
 
           {
                if ($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult)
                then 
                  <ecrda:indicator>true</ecrda:indicator>
                else (
                  <ecrda:indicator>false</ecrda:indicator>
                )
            }                      
           
            {
                if ($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:customerData/fmsr:customerMatchReasonCd)
                then 
                	<ecrda:reasonCd>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:customerData/fmsr:customerMatchReasonCd)}</ecrda:reasonCd>
                else (
                	<ecrda:reasonCd>{$fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResultIndicator/fmsr:reasonCd/text()}</ecrda:reasonCd>
                )
            }
        </ecrda:matchFound>
        <ecrda:dataInquiryErrorCodeList></ecrda:dataInquiryErrorCodeList>
           {
                if ($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult)
                then 
		         <ecrda:lineOfBusiness>WIRELESS</ecrda:lineOfBusiness>
                else ()
            }                      
        <ecrda:unifiedCreditDormantInd>false</ecrda:unifiedCreditDormantInd>
{        
      if ($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult)
      then         
        <ecrda:matchedAccount>
            <ecrda:accountData>
                <ecrda:billingAccountNumber>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:billingAccountNumber)}</ecrda:billingAccountNumber>
                <ecrda:accountType>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:accountType)}</ecrda:accountType>
                <ecrda:accountSubType>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:accountSubType)}</ecrda:accountSubType>
                <ecrda:brandId>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:brandId)}</ecrda:brandId>
                <ecrda:accountCreationDate>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:accountCreationDate)}</ecrda:accountCreationDate>
                {
                    if ($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:startServiceDate)
                    then <ecrda:startServiceDate>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:startServiceDate)}</ecrda:startServiceDate>
                    else ()
                }
                <ecrda:accountStatus>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:statusCode)}</ecrda:accountStatus>
                <ecrda:accountStatusDate>{fn:data($fmsr_FindMatchingSearchResultList/fmsr:findMatchingSearchResult/fmsr:accountData[1]/fmsr:statusDate)}</ecrda:accountStatusDate>

            </ecrda:accountData>

        </ecrda:matchedAccount>
     else ()
}
    </ecrda:WlsUnifiedCreditSearchResult>
};

declare variable $fmsr_FindMatchingSearchResultList as element() external;

convert_findMatchingSearchResultList_to_WlsUnifiedCreditSearchResult($fmsr_FindMatchingSearchResultList)