xquery version "2004-draft" encoding "Cp1252";
(:: pragma bea:schema-type-parameter parameter="$refpdsAccountInstance" type="ref:BusinessRuleInstanceType" location="../../wsdls/refpds/ReferencePDSDataServiceRequestResponse_v1_0.xsd" ::)
(:: pragma bea:schema-type-parameter parameter="$refpdsWarningInstance" type="ref:BusinessRuleInstanceType" location="../../wsdls/refpds/ReferencePDSDataServiceRequestResponse_v1_0.xsd" ::)
(:: pragma bea:schema-type-return type="ent:WlsUnifiedCreditSearchResult" location="../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/Create_WlsUnifiedCreditSearchResult_by_UCSim/";
declare namespace ref = "http://xmlschema.tmi.telus.com/srv/ERM/RefPds/ReferencePDSDataServiceRequestResponse_v1_0";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ent = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace cre = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";

declare function xf:Create_WlsUnifiedCreditSearchResult_by_UCSim(
	$refpdsAccountInstance as element(),
    $refpdsWarningInstance as element())
    as element() {

        <ent:WlsUnifiedCreditSearchResult>           
		    <ent:matchFound>
		        {
		            if ($refpdsAccountInstance/ref:Output[@code='MATCH_FOUND_IND']/ref:value='Y')
		            then <ent:indicator>true</ent:indicator>
		            else (<ent:indicator>false</ent:indicator>)
		        }
		    <ent:reasonCd>{fn:data($refpdsAccountInstance/ref:Output[@code='MATCH_REASON_CD']/ref:value)}</ent:reasonCd>
	    </ent:matchFound>
                
       {
            if ($refpdsAccountInstance/ref:Output[@code='MATCH_FOUND_IND']/ref:value='Y' and $refpdsAccountInstance/ref:Output[@code='LINE_OF_BUSINESS']/ref:value)
            then <ent:lineOfBusiness>{fn:data($refpdsAccountInstance/ref:Output[@code='LINE_OF_BUSINESS']/ref:value)}</ent:lineOfBusiness>
            else ()
        }
        <ent:unifiedCreditDormantInd>false</ent:unifiedCreditDormantInd>
        {
        if($refpdsAccountInstance/ref:Output[@code='MATCH_FOUND_IND']/ref:value='Y')
        then
        <ent:matchedAccount>
        <ent:accountData>
            <ent:billingAccountNumber>{fn:data($refpdsAccountInstance/ref:Output[@code='BAN']/ref:value)}</ent:billingAccountNumber>
            <ent:accountType>{fn:data($refpdsAccountInstance/ref:Output[@code='ACCOUNT_TYPE']/ref:value)}</ent:accountType>
            <ent:accountSubType>{fn:data($refpdsAccountInstance/ref:Output[@code='ACCOUNT_SUBTYPE']/ref:value)}</ent:accountSubType>
            <ent:brandId>{fn:data($refpdsAccountInstance/ref:Output[@code='BRAND_ID']/ref:value)}</ent:brandId>
            <ent:accountCreationDate>{fn:data($refpdsAccountInstance/ref:Output[@code='ACCOUNT_CREATION_DT']/ref:value)}</ent:accountCreationDate>
            {
                if ($refpdsAccountInstance/ref:Output[@code='START_SERVICE_DT']/ref:value)
                then <ent:startServiceDate>{fn:data($refpdsAccountInstance/ref:Output[@code='START_SERVICE_DT']/ref:value)}</ent:startServiceDate>
                else ()
            }
            <ent:accountStatus>{fn:data($refpdsAccountInstance/ref:Output[@code='ACCOUNT_STATUS_CD']/ref:value)}</ent:accountStatus>
            <ent:accountStatusDate>{fn:data($refpdsAccountInstance/ref:Output[@code='ACCOUNT_STATUS_DT']/ref:value)}</ent:accountStatusDate>
            {
                if ($refpdsAccountInstance/ref:Output[@code='STATUS_ACTIVITY_CD']/ref:value)
                then <ent:statusActivityCode>{fn:data($refpdsAccountInstance/ref:Output[@code='STATUS_ACTIVITY_CD']/ref:value)}</ent:statusActivityCode>
                else ()
            }
            {
                if ($refpdsAccountInstance/ref:Output[@code='STATUS_ACTIVITY_RSN_CD']/ref:value)
                then <ent:statusActivityReasonCode>{fn:data($refpdsAccountInstance/ref:Output[@code='STATUS_ACTIVITY_RSN_CD']/ref:value)}</ent:statusActivityReasonCode>
                else ()
            }
        </ent:accountData>
        <ent:creditWorthinessData>
            {
                if ($refpdsAccountInstance/ref:Output[@code='RISK_LEVEL_NUM']/ref:value)
                then <ent:riskLevelNumber>{fn:data($refpdsAccountInstance/ref:Output[@code='RISK_LEVEL_NUM']/ref:value)}</ent:riskLevelNumber>
                else ()
            }
            {
                if ($refpdsAccountInstance/ref:Output[@code='RISK_LEVEL_DECISION_CD']/ref:value)
                then <ent:riskLevelDecisionCd>{fn:data($refpdsAccountInstance/ref:Output[@code='RISK_LEVEL_DECISION_CD']/ref:value)}</ent:riskLevelDecisionCd>
                else ()
            }
            <ent:creditClassCd>{fn:data($refpdsAccountInstance/ref:Output[@code='CREDIT_CLASS_CODE']/ref:value)}</ent:creditClassCd>
        </ent:creditWorthinessData>
          {
           if($refpdsWarningInstance/ref:Output)
           then
		<ent:warningHistoryList>

 		{
		 if ($refpdsWarningInstance/ref:Output[@code='WARNING_CATEGORY']/ref:value)
		 then
		    <ent:warningCategoryCd>{fn:data($refpdsWarningInstance/ref:Output[@code='WARNING_CATEGORY']/ref:value)}</ent:warningCategoryCd>
		  else()
		 }
		 {
		 if ($refpdsWarningInstance/ref:Output[@code='WARNING_CODE']/ref:value)
		 then
		    <ent:warningCd>{fn:data($refpdsWarningInstance/ref:Output[@code='WARNING_CODE']/ref:value)}</ent:warningCd>
		  else()
		 }
		 {
		 if ($refpdsWarningInstance/ref:Output[@code='WARNING_TYPE']/ref:value)
		 then
		    <ent:warningTypeCd>{fn:data($refpdsWarningInstance/ref:Output[@code='WARNING_TYPE']/ref:value)}</ent:warningTypeCd>
		  else()
		 }	
		 
		 {
		 if ($refpdsWarningInstance/ref:Output[@code='WARNING_STATUS']/ref:value)
		 then
		    <ent:warningStatusCd>{fn:data($refpdsWarningInstance/ref:Output[@code='WARNING_STATUS']/ref:value)}</ent:warningStatusCd>
		  else()
		 }			

		 </ent:warningHistoryList>
              else()
             }
        <ent:accountFinancialHistory>
            {
                if ($refpdsAccountInstance/ref:Output[@code='DELINQUENT_IND']/ref:value='Y')
                then <ent:delinquentInd>true</ent:delinquentInd>
                else (
                <ent:delinquentInd>false</ent:delinquentInd>
                )
            }
        </ent:accountFinancialHistory>
        {
            if ($refpdsAccountInstance/ref:Output[@code='BUREAU_SOURCE_CD']/ref:value)
            then 
                <ent:creditBureauResult>
                    {
                        if ($refpdsAccountInstance/ref:Output[@code='BUREAU_SOURCE_CD']/ref:value)
                        then <ent:reportSourceCd>{fn:data($refpdsAccountInstance/ref:Output[@code='BUREAU_SOURCE_CD']/ref:value)}</ent:reportSourceCd>
                        else ()
                    }
                    {
                        if ($refpdsAccountInstance/ref:Output[@code='BUREAU_ERROR_CD']/ref:value)
                        then <ent:errorCd>{fn:data($refpdsAccountInstance/ref:Output[@code='BUREAU_ERROR_CD']/ref:value)}</ent:errorCd>
                        else ()
                    }
                    {
                        if ($refpdsAccountInstance/ref:Output[@code='BUREAU_DECISION_CD']/ref:value)
                        then 
                            <ent:adjudicationResult>
                                {
                                    if ($refpdsAccountInstance/ref:Output[@code='BUREAU_DECISION_CD']/ref:value)
                                    then 
                                        <ent:creditDecision>
                                            <cre:creditDecisionCd>{fn:data($refpdsAccountInstance/ref:Output[@code='BUREAU_DECISION_CD']/ref:value)}</cre:creditDecisionCd>
                                            <cre:creditDecisionMessage></cre:creditDecisionMessage>
                                        </ent:creditDecision>
                                    else ()
                                }
                            </ent:adjudicationResult>
                        else ()
                    }
                    {
                        if ($refpdsAccountInstance/ref:Output[@code='BUREAU_DECISION_DT']/ref:value)
                        then <ent:creationDate>{fn:data($refpdsAccountInstance/ref:Output[@code='BUREAU_DECISION_DT']/ref:value)}</ent:creationDate>
                        else ()
                    }
                </ent:creditBureauResult>
            else ()
        }
        </ent:matchedAccount>
        else()
        }
    </ent:WlsUnifiedCreditSearchResult>  
};

declare variable $refpdsAccountInstance as element() external;
declare variable $refpdsWarningInstance as element() external;

xf:Create_WlsUnifiedCreditSearchResult_by_UCSim($refpdsAccountInstance,
    $refpdsWarningInstance)