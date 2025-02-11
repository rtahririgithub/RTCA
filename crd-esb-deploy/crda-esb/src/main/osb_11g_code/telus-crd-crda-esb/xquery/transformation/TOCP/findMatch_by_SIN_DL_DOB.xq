xquery version "1.0" encoding "Cp1252";
(:: pragma bea:schema-type-parameter parameter="$ecrda_FullCreditAssessmentRequest" type="ecrda:FullCreditAssessmentRequest" location="../../../wsdls/EnterpriseCreditAssessmentServiceRequestResponse_v2_1.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$fmsr_findMatchingSearchResultList" element="fmsr:findMatchingSearchResultList" location="../../../wsdls/FindMatchingSearchResult.xsd" ::)
(:: pragma bea:global-element-return element="fmsr:findMatchingSearchResultList" location="../../../wsdls/FindMatchingSearchResult.xsd" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/TOCP/findMatch_by_SIN_DL_DOB/";
declare namespace cred = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ecrda = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditAssessmentServiceRequestResponse_v2";
declare namespace wir = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace fmsr = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WCPMProxyMDMSearchResult";

declare function xf:findMatch_by_SIN_DL_DOB(
	$ecrda_FullCreditAssessmentRequest as element(),
    $fmsr_findMatchingSearchResultList as element(fmsr:findMatchingSearchResultList),
	$constants as element()
    )
    as element(fmsr:findMatchingSearchResultList) {
   <fmsr:findMatchingSearchResultList>
    
    
    {
      let $driverLicenseNumberRequest:= fn:upper-case($ecrda_FullCreditAssessmentRequest/ecrda:creditProfileData/cred:creditIdentification/cred:driverLicense/cred:driverLicenseNum/text())
      let $sinRequest:= $ecrda_FullCreditAssessmentRequest/ecrda:creditProfileData/cred:creditIdentification/cred:sin/text()
      let $birthDateRequest:=$ecrda_FullCreditAssessmentRequest/ecrda:creditProfileData/cred:personalInfo/cred:birthDate/text()
      let $birthDateRequestYear:=fn:year-from-date($birthDateRequest)
	  let $birthDateRequestMonth:=fn:month-from-date($birthDateRequest)
	  let $birthDateRequestDay:=fn:day-from-date($birthDateRequest) 
	  
      for $findMatchingSearchResult in $fmsr_findMatchingSearchResultList/fmsr:findMatchingSearchResult
        
        let $driverLicenseList:= $findMatchingSearchResult/fmsr:customerData/fmsr:creditIdentification/fmsr:driverLicense
        let $dl_matchFound := 
        ( 
        	if( exists
        		( 
        		$findMatchingSearchResult/fmsr:customerData/fmsr:creditIdentification/fmsr:driverLicense[fmsr:driverLicenseNumber=$driverLicenseNumberRequest]
        	 	)
        	 )
          	then    (fn:true()) 
		    else    (fn:false())             
        )
        
        let $socialInsuranceNum:= fn:upper-case($findMatchingSearchResult/fmsr:customerData/fmsr:creditIdentification/fmsr:socialInsuranceNum/text())
        let $sin_matchFound := ( fn:compare($sinRequest,$socialInsuranceNum) eq 0 )
 
		(: MDM return birthdate sometimes in date and sometimes in datetime format. xquery 1 doesn't allow casting hence we had to convert birthdate to string and then to date :)  
        let $birthDateStr:=substring($findMatchingSearchResult/fmsr:customerData/fmsr:birthDate/text(), 1,10)
        let $birthDate_matchFound := 
        ( 
          if(not(string-length($birthDateStr)=0))
          then    
           (
		          if( 
		          	$birthDateRequestYear = fn:year-from-date(xs:date($birthDateStr)) and
		          	$birthDateRequestMonth = fn:month-from-date(xs:date($birthDateStr)) and
		          	$birthDateRequestDay = fn:day-from-date(xs:date($birthDateStr)) 
		          	
		          ) 
		          then    (fn:true()) 
		          else    (fn:false())
		    )
          else (fn:false()) 
	
		)          


       
         
        let $customerMatchReasonCd := 
        ( 
          if( $dl_matchFound and $sin_matchFound and $birthDate_matchFound) 
          then    ($constants/constant[@value="MATCH_BY_DL_SIN_DOB"]/text()) 
          else 
          (
            if( $dl_matchFound and $sin_matchFound ) 
            then    ($constants/constant[@value="MATCH_BY_DL_SN"]/text()) 
            else
            (
              if( $dl_matchFound and $birthDate_matchFound ) 
              then  ($constants/constant[@value="MATCH_BY_DL_DOB"]/text())
              else
              (
                if( $sin_matchFound and $birthDate_matchFound ) 
                then  ($constants/constant[@value="MATCH_BY_SN_DOB"]/text())
                else
                (
                  if( $dl_matchFound  ) 
                  then   ($constants/constant[@value="MATCH_BY_DL"]/text())
                  else
                  (
                    if( $sin_matchFound  ) 
                    then   ($constants/constant[@value="MATCH_BY_SN"]/text())
                    else
                    (
                      if( $birthDate_matchFound ) 
                      then    ($constants/constant[@value="MATCH_BY_DOB"]/text())
                      else
                      ()                    
                    )                  
                  )                 
                )              
              )            
            )
          )
        ) 

         let $matchFoundInd := 
        ( 
          if( $dl_matchFound or $sin_matchFound or $birthDate_matchFound) 
          then  fn:true()
          else  (fn:false())
        )    

        return 
if ($ecrda_FullCreditAssessmentRequest)
then       
        if( $matchFoundInd )
        then 
        
        <fmsr:findMatchingSearchResult>
            {
                if ($findMatchingSearchResult/fmsr:customerData)
                then 
                    <fmsr:customerData>
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:sourceSystemID)
                            then <fmsr:sourceSystemID>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:sourceSystemID)}</fmsr:sourceSystemID>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:customerId)
                            then <fmsr:customerId>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:customerId)}</fmsr:customerId>
                            else ()
                        }
                        <fmsr:customerCreationDate>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:customerCreationDate)}</fmsr:customerCreationDate>
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:customerStatusCd)
                            then <fmsr:customerStatusCd>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:customerStatusCd)}</fmsr:customerStatusCd>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:customerTypeCd)
                            then <fmsr:customerTypeCd>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:customerTypeCd)}</fmsr:customerTypeCd>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:customerSubTypeCd)
                            then <fmsr:customerSubTypeCd>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:customerSubTypeCd)}</fmsr:customerSubTypeCd>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:creditIdentification)
                            then 
                            $findMatchingSearchResult/fmsr:customerData/fmsr:creditIdentification/*                               
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:firstName)
                            then <fmsr:firstName>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:firstName)}</fmsr:firstName>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:lastName)
                            then <fmsr:lastName>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:lastName)}</fmsr:lastName>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:middleName)
                            then <fmsr:middleName>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:middleName)}</fmsr:middleName>
                            else ()
                        }
                        {
                            if ($findMatchingSearchResult/fmsr:customerData/fmsr:birthDate)
                            then <fmsr:birthDate>{fn:data($findMatchingSearchResult/fmsr:customerData/fmsr:birthDate)}</fmsr:birthDate>
                            else ()
                        }
                    {
                        <fmsr:customerMatchReasonCd>{$customerMatchReasonCd}</fmsr:customerMatchReasonCd>

                    }                        
                    </fmsr:customerData>
                else ()
            }
            {
                for $accountData in $findMatchingSearchResult/fmsr:accountData
                return 
                <fmsr:accountData>
                    {
                        if ($accountData/fmsr:billingMasterSourceId)
                        then <fmsr:billingMasterSourceId>{fn:data($accountData/fmsr:billingMasterSourceId)}</fmsr:billingMasterSourceId>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:billingAccountNumber)
                        then <fmsr:billingAccountNumber>{fn:data($accountData/fmsr:billingAccountNumber)}</fmsr:billingAccountNumber>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:brandId)
                        then <fmsr:brandId>{fn:data($accountData/fmsr:brandId)}</fmsr:brandId>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:accountType)
                        then <fmsr:accountType>{fn:data($accountData/fmsr:accountType)}</fmsr:accountType>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:accountSubType)
                        then <fmsr:accountSubType>{fn:data($accountData/fmsr:accountSubType)}</fmsr:accountSubType>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:accountCreationDate)
                        then <fmsr:accountCreationDate>{fn:data($accountData/fmsr:accountCreationDate)}</fmsr:accountCreationDate>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:startServiceDate)
                        then <fmsr:startServiceDate>{fn:data($accountData/fmsr:startServiceDate)}</fmsr:startServiceDate>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:statusCode)
                        then <fmsr:statusCode>{fn:data($accountData/fmsr:statusCode)}</fmsr:statusCode>
                        else ()
                    }
                    {
                        if ($accountData/fmsr:statusDate)
                        then <fmsr:statusDate>{fn:data($accountData/fmsr:statusDate)}</fmsr:statusDate>
                        else ()
                    }

                </fmsr:accountData>
            }
        </fmsr:findMatchingSearchResult>
        else
         ()
else
 ()
   
    
    }
   
    </fmsr:findMatchingSearchResultList> 
};

declare variable $ecrda_FullCreditAssessmentRequest as element() external;
declare variable $fmsr_findMatchingSearchResultList as element(fmsr:findMatchingSearchResultList) external;
declare variable $constants as element() external;
xf:findMatch_by_SIN_DL_DOB(
	$ecrda_FullCreditAssessmentRequest,
    $fmsr_findMatchingSearchResultList,
    $constants)