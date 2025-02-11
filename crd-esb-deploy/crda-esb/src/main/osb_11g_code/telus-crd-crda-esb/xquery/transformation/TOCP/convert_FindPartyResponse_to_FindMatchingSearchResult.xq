xquery version "1.0" encoding "Cp1252";
(:: pragma bea:global-element-parameter parameter="$fparty_findPartyResponse" element="fparty:findPartyResponse" location="../../../wsdls/TOCP/FindParty_v2_0.wsdl" ::)
(:: pragma bea:global-element-return element="fmsr:findMatchingSearchResultList" location="../../../wsdls/FindMatchingSearchResult.xsd" ::)

declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/TOCP/convert_FindPartyResponse_to_FindMatchingSearchResult/";
declare namespace ns2 = "http://www.ibm.com/telecom/common/schema/party/v3_0";
declare namespace ns1 = "http://www.ibm.com/telecom/common/schema/party_extensions/v3_0";
declare namespace ns4 = "http://www.ibm.com/telecom/templates/schema/party_extensions/v3_0";
declare namespace ns3 = "http://www.ibm.com/telecom/common/schema/mtosi/v3_0";
declare namespace fmsr = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WCPMProxyMDMSearchResult";
declare namespace fparty = "http://www.ibm.com/xmlns/prod/websphere/fabric/2009/12/telecom/operations/inventory/types/schema/FindParty";
declare namespace ns5 = "http://www.ibm.com/telecom/common/schema/base/v3_0";
declare namespace par = "http://www.ibm.com/xmlns/prod/websphere/fabric/2009/12/telecom/operations/common/schema/PartyMessage";
declare namespace ns7 = "http://www.ibm.com/telecom/templates/schema/party_role_extensions/v3_0";
declare namespace ns8 = "http://www.ibm.com/telecom/common/schema/base_extensions/v3_0";

declare function xf:convert_FindPartyResponse_to_FindMatchingSearchResult
(
	$fparty_findPartyResponse as element(fparty:findPartyResponse),
	$constants as element()
)
    as element(fmsr:findMatchingSearchResultList) {

 <fmsr:findMatchingSearchResultList>
    {
        let $party_List:=  $fparty_findPartyResponse/par:party                
        for $party in $party_List
          let $party_CharacteristicValue_List:=  $party/CharacteristicValue   
          
          let $customerTypeCd :=$party/CharacteristicValue[ Characteristic/Name = 'prtyTyp' ]/Value/text()     
   
          
          let $firstName :=$party/CharacteristicValue[ Characteristic/Name = 'firstName'  ]/Value/text()
	  	  let $middleName :=$party/CharacteristicValue[ Characteristic/Name = 'middleName'  ]/Value/text()
          let $lastName :=$party/CharacteristicValue[ Characteristic/Name = 'lastName'  ]/Value/text()
		  
          let $birthDate :=$party/CharacteristicValue[ Characteristic/Name = 'birthDate'  ]/Value/text()
          let $customerCreationDate :=$party/CharacteristicValue[ Characteristic/Name = 'partyCreationDate'  ]/Value/text()

          let $CUSTOMER_ID_PartyIdentification :=$party/ComponentParty/PartyIdentification[ CharacteristicValue/Characteristic/Name = 'altIdTyp' and CharacteristicValue/Value='CUSTOMER_ID' ]
          let $customerId:= $CUSTOMER_ID_PartyIdentification/CharacteristicValue[Characteristic/Name='altIdVal']/Value/text()
          
          let $DL_PartyIdentification :=$party/ComponentParty/PartyIdentification[ CharacteristicValue/Characteristic/Name = 'altIdTyp' and CharacteristicValue/Value='DL' ]

          

 
          let $SN_PartyIdentification :=$party/ComponentParty/PartyIdentification[ CharacteristicValue/Characteristic/Name = 'altIdTyp' and CharacteristicValue/Value='SIN' ]
          let $socialInsuranceNum:= $SN_PartyIdentification[1]/CharacteristicValue[Characteristic/Name='altIdVal']/Value/text()
          
 


          return     
	<fmsr:findMatchingSearchResult>
		<fmsr:customerData>
			<!--<fmsr:sourceSystemID>{$sourceSystemID}</fmsr:sourceSystemID>-->
			<fmsr:customerId>{$customerId}</fmsr:customerId>
			<fmsr:customerCreationDate>{$customerCreationDate}</fmsr:customerCreationDate>
			<fmsr:customerStatusCd>{$customerCreationDate}</fmsr:customerStatusCd>
			<fmsr:customerTypeCd>{$customerTypeCd}</fmsr:customerTypeCd>
			<fmsr:customerSubTypeCd/>
			<fmsr:creditIdentification>
			{
			for $aDL_PartyIdentification in $DL_PartyIdentification
			 let $driverLicenseNumber:= $aDL_PartyIdentification/CharacteristicValue[Characteristic/Name='altIdVal']/Value/text()
             let $driverLicenseNumber_provStateCd:= $aDL_PartyIdentification[1]/CharacteristicValue[Characteristic/Name='provStateCd']/Value/text() 
			return
				<fmsr:driverLicense>
					<fmsr:driverLicenseNumber>{$driverLicenseNumber}</fmsr:driverLicenseNumber>
					<fmsr:provinceCd>{$driverLicenseNumber_provStateCd}</fmsr:provinceCd>
					<fmsr:expiryDate/>
				</fmsr:driverLicense>
			}
				<fmsr:socialInsuranceNum>{$socialInsuranceNum}</fmsr:socialInsuranceNum>
				<fmsr:creditCardTokenTxt/>
				<fmsr:creditCardFirstSixNum/>
				<fmsr:creditCardLastFourNum/>
				<fmsr:healthCardNumber/>
				<fmsr:passport>
					<fmsr:passportNumber/>
					<fmsr:countryCd/>
				</fmsr:passport>
				<fmsr:provincialIdCard>
					<fmsr:provincialId/>
					<fmsr:provinceCd/>
				</fmsr:provincialIdCard>
			</fmsr:creditIdentification>
			<fmsr:firstName>{$firstName}</fmsr:firstName>
			<fmsr:lastName>{$lastName}</fmsr:lastName>
			<fmsr:middleName>{$middleName}</fmsr:middleName>
                        <fmsr:birthDate>{$birthDate}</fmsr:birthDate>
		</fmsr:customerData>
    {
      let $CustomerAccount_PartyIdentification_List :=$party/ComponentParty/PartyIdentification[ Specification/Type = 'CustomerAccount' ]
      for $CustomerAccount_PartyIdentification in $CustomerAccount_PartyIdentification_List
          let $billingMasterSourceId:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctMsrtSrcId']/Value/text()
		  let $sourceSystemID:= $billingMasterSourceId
		  let $billingAccountNumber:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctNum']/Value/text()
          let $brandId:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='brandId']/Value/text()         
		 
          
          let $accountTypeSubType:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctSubtypCd']/Value/text()
          let $accountType:= substring($accountTypeSubType, 1, 1)
          let $accountSubType:= substring($accountTypeSubType, 2, 1)

		  
		  let $accountCreationDate:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctOpenDt']/Value/text()	
		  let $startServiceDate:=$accountCreationDate

          let $statusCode:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctStatusCd']/Value/text()	  
		 
          let $statusDate:= $CustomerAccount_PartyIdentification/CharacteristicValue[Characteristic/Name='acctStatusDate']/Value/text()
          return                     
		<fmsr:accountData>
			<fmsr:billingMasterSourceId>{$billingMasterSourceId}</fmsr:billingMasterSourceId>
			<fmsr:billingAccountNumber>{$billingAccountNumber}</fmsr:billingAccountNumber>
			<fmsr:brandId>{$brandId}</fmsr:brandId>
			<fmsr:accountType>{$accountType}</fmsr:accountType>
			<fmsr:accountSubType>{$accountSubType}</fmsr:accountSubType>
			{
			if($statusCode="T")
				then <fmsr:accountCreationDate>{$statusDate}</fmsr:accountCreationDate>
			else(
				<fmsr:accountCreationDate>{$accountCreationDate}</fmsr:accountCreationDate>
			)
			}
			<fmsr:startServiceDate>{$startServiceDate}</fmsr:startServiceDate>
			<fmsr:statusCode>{$statusCode}</fmsr:statusCode>
			<fmsr:statusDate>{$statusDate}</fmsr:statusDate>
			<fmsr:matchReasonCd/>
		</fmsr:accountData>
          }
	</fmsr:findMatchingSearchResult>

    }
    
	
	{	
	     let $party_List2:=  $fparty_findPartyResponse/par:party
        let $findMatchingSearchResultListInd := 
        ( 
          if( $party_List2) 
          then  fn:true()
          else  (fn:false())
        )
        let $findMatchingSearchResultIndicatorReasonCd := 
        ( 
          if( $party_List2) 
          then  ""
          else  ($constants/constant[@value="NO_RESULT_FROM_MDM_AFTER_CONVERSION"]/text())
        ) 	     
	     return   
	     <fmsr:findMatchingSearchResultIndicator>
		    <fmsr:indicator>{$findMatchingSearchResultListInd}</fmsr:indicator>
			<fmsr:reasonCd>{$findMatchingSearchResultIndicatorReasonCd}</fmsr:reasonCd>
		</fmsr:findMatchingSearchResultIndicator>  
	} 	
	 
	
    </fmsr:findMatchingSearchResultList>
    
};

declare variable $fparty_findPartyResponse as element(fparty:findPartyResponse) external;
declare variable $constants as element() external;
xf:convert_FindPartyResponse_to_FindMatchingSearchResult($fparty_findPartyResponse,$constants)