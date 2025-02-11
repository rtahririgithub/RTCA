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
	 let $birthDateRequest:=$ecrda_FullCreditAssessmentRequest/ecrda:creditProfileData/cred:personalInfo/cred:birthDate/text()
      let $birthDateRequestYear:=fn:year-from-date($birthDateRequest)
	  let $birthDateRequestMonth:=fn:month-from-date($birthDateRequest)
	  let $birthDateRequestDay:=fn:day-from-date($birthDateRequest) 
	  
      for $findMatchingSearchResult in $fmsr_findMatchingSearchResultList/fmsr:findMatchingSearchResult
        

 		
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

        return 
        
        <fmsr:findMatchingSearchResult>
        {
		$birthDate_matchFound	
		
		}
        </fmsr:findMatchingSearchResult>


   
    
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