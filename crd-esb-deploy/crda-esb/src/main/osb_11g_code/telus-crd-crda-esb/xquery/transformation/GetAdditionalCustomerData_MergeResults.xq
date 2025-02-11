(:: pragma bea:global-element-parameter parameter="$searchDepositPaymentByCustomerIDResponse1" element="ns0:searchDepositPaymentByCustomerIDResponse" location="../../wsdls/SearchDepositPaymentByCustomerIDRequestResponse_v1_0.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getCustomerCollectionDataResponse1" element="ns12:getCustomerCollectionDataResponse" location="../../wsdls/WLNCollectionManagementServiceRequestResponse_v1_0.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getCreditProfileByCustomerIdResponse1" element="ns1:getCreditProfileByCustomerIdResponse" location="../../wsdls/WLNCreditProfileManagementServiceRequestResponse_v2_0.xsd" ::)
(:: pragma bea:global-element-parameter parameter="$getCustomerWithContactsResponse1" element="ns3:getCustomerWithContactsResponse" location="../../wsdls/ConsumerCustomerMgmtSvcRequestResponse_Local.xsd" ::)
(:: pragma bea:global-element-return element="ns10:getAdditionalCustomerDataResponse" location="../../wsdls/GetAdditionalCustomerDataSplitJoinRequestResponse_v2_0.xsd" ::)

declare namespace ns14 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerSubDomain_v3";
declare namespace ns15 = "http://xmlschema.tmi.telus.com/srv/CMO/BillingAccountMgmt/DepositManagementServiceRequestResponse_v1";
declare namespace ns9 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CustomerCommon_v3";
declare namespace ns5 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v3";
declare namespace ns12 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCollectionManagementServiceRequestResponse_v1";
declare namespace ns13 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v9";
declare namespace ns6 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1";
declare namespace ns7 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v2";
declare namespace ns10 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoinRequestResponse_v2";
declare namespace ns8 = "http://xmlschema.tmi.telus.com/xsd/Product/ProductInstance/Customer_Product_Instance_Sub_Domain_v3";
declare namespace ns11 = "http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditDepositTypes_v1";
declare namespace ns2 = "http://xmlschema.tmi.telus.com/xsd/Customer/CustomerBill/Customer_Billing_Sub_Domain_v2";
declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementServiceRequestResponse_v2";
declare namespace ns4 = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1";
declare namespace ns3 = "http://xmlschema.tmi.telus.com/srv/CMO/InformationMgmt/ConsumerCustomerMgmtSvcRequestResponse_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/SearchDepositPaymentByCustomerIDRequestResponse_v1";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/GetAdditionalCustomerData_MergeResults/";

declare function xf:GetAdditionalCustomerData_MergeResults($searchDepositPaymentByCustomerIDResponse1 as element(ns0:searchDepositPaymentByCustomerIDResponse),
    $getCustomerCollectionDataResponse1 as element(ns12:getCustomerCollectionDataResponse),
    $getCreditProfileByCustomerIdResponse1 as element(ns1:getCreditProfileByCustomerIdResponse),
    $getCustomerWithContactsResponse1 as element(ns3:getCustomerWithContactsResponse))
    as element(ns10:getAdditionalCustomerDataResponse) {
        <ns10:getAdditionalCustomerDataResponse>
            {
                for $fullCustomer in $getCustomerWithContactsResponse1/fullCustomer
                return
                    <ns10:creditCustomerInfo>
                        <ns6:customerID>{ data($fullCustomer/customerId) }</ns6:customerID>
                        <ns6:personName>
                            <ns6:firstName>{ data($fullCustomer/nameList[1]/firstName) }</ns6:firstName>
                            <ns6:lastName>{ data($fullCustomer/nameList[1]/lastName) }</ns6:lastName>
                            {
                                for $middleName in $fullCustomer/nameList[1]/middleName
                                return
                                    <ns6:middleName>{ data($middleName) }</ns6:middleName>
                            }
                            {
                                for $nameSuffixCode in $fullCustomer/nameList[1]/nameSuffixCode
                                return
                                    <ns6:nameSuffix>{ data($nameSuffixCode) }</ns6:nameSuffix>
                            }
                            {
                                for $salutationCode in $fullCustomer/nameList[1]/salutationCode
                                return
                                    <ns6:title>{ data($salutationCode) }</ns6:title>
                            }
                        </ns6:personName>
                        <ns6:customerCreationDate>{ data($fullCustomer/customerCreationDate) }</ns6:customerCreationDate>
                        {
                            for $customerMasterSourceId in $fullCustomer/customerMasterSourceId
                            return
                                <ns6:customerMasterSourceID>{ data($customerMasterSourceId) }</ns6:customerMasterSourceID>
                        }
                        <ns6:customerStatusCd>{ data($fullCustomer/customerStatusCode) }</ns6:customerStatusCd>
                        <ns6:customerTypeCd>{ data($fullCustomer/customerTypeCode) }</ns6:customerTypeCd>
                        {
                            for $customerSubTypeCode in $fullCustomer/customerSubTypeCode
                            return
                                <ns6:customerSubTypeCd>{ data($customerSubTypeCode) }</ns6:customerSubTypeCd>
                        }
                        {
                            for $contactIndividualList in $fullCustomer/contactIndividualList,
                                $telecomContactList in $contactIndividualList/telecomContactList
                            return
                                let $__nullable := ( $contactIndividualList )
                                return
                                    if (fn:boolean($__nullable))
                                    then
                                        <ns6:telecomContactList>
                                            {
                                                for $telecomContactId in $telecomContactList/telecomContactId
                                                return
                                                    <ns6:telecomContactId>{ data($telecomContactId) }</ns6:telecomContactId>
                                            }
                                            {
                                                for $telecomContactTypeCode in $telecomContactList/telecomContactTypeCode
                                                return
                                                    <ns6:telecomContactTypeCode>{ data($telecomContactTypeCode) }</ns6:telecomContactTypeCode>
                                            }
                                            {
                                                for $telecomContactSubTypeCode in $telecomContactList/telecomContactSubTypeCode
                                                return
                                                    <ns6:telecomContactSubTypeCode>{ data($telecomContactSubTypeCode) }</ns6:telecomContactSubTypeCode>
                                            }
                                            {
                                                for $telephoneNumber in $telecomContactList/telephoneNumber
                                                return
                                                    <ns6:telephoneNumber>{ data($telephoneNumber) }</ns6:telephoneNumber>
                                            }
                                            {
                                                for $telephoneExtensionNumber in $telecomContactList/telephoneExtensionNumber
                                                return
                                                    <ns6:telephoneExtensionNumber>{ data($telephoneExtensionNumber) }</ns6:telephoneExtensionNumber>
                                            }
                                            {
                                                for $lastUpdate in $telecomContactList/lastUpdate
                                                return
                                                    <ns6:lastUpdate>{ data($lastUpdate) }</ns6:lastUpdate>
                                            }
                                        </ns6:telecomContactList>
                                    else
                                        ()
                        }
                        <ns6:revenueSegmentCd>{ $fullCustomer/marketSegmentList[marketSegmentTypeCode='VALS'][1]/marketSegmentCode/text() }</ns6:revenueSegmentCd>
                        <ns6:employeeInd>
                            {
                                if (fn:empty( data($getCustomerWithContactsResponse1/fullCustomer/teamMemberId/text()) )) then
                                    ("false")
                                else 
                                    "true"
                            }
</ns6:employeeInd>
                    </ns10:creditCustomerInfo>
            }
            {
                let $creditProfile := $getCreditProfileByCustomerIdResponse1/ns1:creditProfile
                return
                    <ns10:creditProfileData>
                        {
                            for $creditProfileID in $creditProfile/ns6:creditProfileID
                            return
                                <ns6:creditProfileID>{ data($creditProfileID) }</ns6:creditProfileID>
                        }
                        <ns6:customerID>{ data($creditProfile/ns6:customerID) }</ns6:customerID>
                        {
                            for $creditIdentification in $creditProfile/ns6:creditIdentification
                            return
                                <ns6:creditIdentification>{ $creditIdentification/@* , $creditIdentification/node() }</ns6:creditIdentification>
                        }
                        {
                            for $creditAddress in $creditProfile/ns6:creditAddress
                            return
                                <ns6:creditAddress>
                                    {
                                        for $creditAddressTypeCd in $creditAddress/ns6:creditAddressTypeCd
                                        return
                                            <ns6:creditAddressTypeCd>{ data($creditAddressTypeCd) }</ns6:creditAddressTypeCd>
                                    }
                                    {
                                        for $addressTypeCd in $creditAddress/ns6:addressTypeCd
                                        return
                                            <ns6:addressTypeCd>{ data($addressTypeCd) }</ns6:addressTypeCd>
                                    }
                                    <ns6:addressLineOne>{ data($creditAddress/ns6:addressLineOne) }</ns6:addressLineOne>
                                    {
                                        for $addressLineTwo in $creditAddress/ns6:addressLineTwo
                                        return
                                            <ns6:addressLineTwo>{ data($addressLineTwo) }</ns6:addressLineTwo>
                                    }
                                    {
                                        for $addressLineThree in $creditAddress/ns6:addressLineThree
                                        return
                                            <ns6:addressLineThree>{ data($addressLineThree) }</ns6:addressLineThree>
                                    }
                                    {
                                        for $addressLineFour in $creditAddress/ns6:addressLineFour
                                        return
                                            <ns6:addressLineFour>{ data($addressLineFour) }</ns6:addressLineFour>
                                    }
                                    {
                                        for $addressLineFive in $creditAddress/ns6:addressLineFive
                                        return
                                            <ns6:addressLineFive>{ data($addressLineFive) }</ns6:addressLineFive>
                                    }
                                    <ns6:cityName>{ data($creditAddress/ns6:cityName) }</ns6:cityName>
                                    <ns6:provinceCd>{ data($creditAddress/ns6:provinceCd) }</ns6:provinceCd>
                                    <ns6:countryCd>{ data($creditAddress/ns6:countryCd) }</ns6:countryCd>
                                    <ns6:postalCd>{ data($creditAddress/ns6:postalCd) }</ns6:postalCd>
                                </ns6:creditAddress>
                        }
                        <ns6:applicationProvinceCd>{ data($creditProfile/ns6:applicationProvinceCd) }</ns6:applicationProvinceCd>
                        {
                            for $personalInfo in $creditProfile/ns6:personalInfo
                            return
                                <ns6:personalInfo>
                                    {
                                        for $employmentStatusCd in $personalInfo/ns6:employmentStatusCd
                                        return
                                            <ns6:employmentStatusCd>{ data($employmentStatusCd) }</ns6:employmentStatusCd>
                                    }
                                    {
                                        for $residencyCd in $personalInfo/ns6:residencyCd
                                        return
                                            <ns6:residencyCd>{ data($residencyCd) }</ns6:residencyCd>
                                    }
                                    {
                                        for $creditCheckConsentCd in $personalInfo/ns6:creditCheckConsentCd
                                        return
                                            <ns6:creditCheckConsentCd>{ data($creditCheckConsentCd) }</ns6:creditCheckConsentCd>
                                    }
                                    {
                                        for $birthDate in $personalInfo/ns6:birthDate
                                        return
                                            <ns6:birthDate>{ data($birthDate) }</ns6:birthDate>
                                    }
                                    {
                                        for $underLegalCareCd in $personalInfo/ns6:underLegalCareCd
                                        return
                                            <ns6:underLegalCareCd>{ data($underLegalCareCd) }</ns6:underLegalCareCd>
                                    }
                                    {
                                        for $provinceOfCurrentResidenceCd in $personalInfo/ns6:provinceOfCurrentResidenceCd
                                        return
                                            <ns6:provinceOfCurrentResidenceCd>{ data($provinceOfCurrentResidenceCd) }</ns6:provinceOfCurrentResidenceCd>
                                    }
                                </ns6:personalInfo>
                        }
                        {
                            for $creditCardCd in $creditProfile/ns6:creditCardCd
                            return
                                <ns6:creditCardCd>
                                    {
                                        for $primaryCreditCardCd in $creditCardCd/ns6:primaryCreditCardCd
                                        return
                                            <ns6:primaryCreditCardCd>{ data($primaryCreditCardCd) }</ns6:primaryCreditCardCd>
                                    }
                                    {
                                        for $secondaryCreditCardCd in $creditCardCd/ns6:secondaryCreditCardCd
                                        return
                                            <ns6:secondaryCreditCardCd>{ data($secondaryCreditCardCd) }</ns6:secondaryCreditCardCd>
                                    }
                                </ns6:creditCardCd>
                        }
                        {
                            for $creditProfileStatusCd in $creditProfile/ns6:creditProfileStatusCd
                            return
                                <ns6:creditProfileStatusCd>{ data($creditProfileStatusCd) }</ns6:creditProfileStatusCd>
                        }
                        <ns6:businessLastUpdateTimestamp>
                            {
                                if (fn:empty($creditProfile/ns6:businessLastUpdateTimestamp/text())) then
                                    (data($getCustomerWithContactsResponse1/fullCustomer/customerCreationDate))
                                else 
                                    data($creditProfile/ns6:businessLastUpdateTimestamp)
                            }
</ns6:businessLastUpdateTimestamp>
                        {
                            for $bypassMatchIndicator in $creditProfile/ns6:bypassMatchIndicator
                            return
                                <ns6:bypassMatchIndicator>{ data($bypassMatchIndicator) }</ns6:bypassMatchIndicator>
                        }
                        {
                            for $formatCd in $creditProfile/ns6:formatCd
                            return
                                <ns6:formatCd>{ data($formatCd) }</ns6:formatCd>
                        }
                        {
                            for $methodCd in $creditProfile/ns6:methodCd
                            return
                                <ns6:methodCd>{ data($methodCd) }</ns6:methodCd>
                        }
                        {
                            for $customerGuarantor in $creditProfile/ns6:customerGuarantor
                            return
                                <ns6:customerGuarantor>
                                    <ns6:id>{ data($customerGuarantor/ns6:id) }</ns6:id>
                                    <ns6:guarantorCustomerID>{ data($customerGuarantor/ns6:guarantorCustomerID) }</ns6:guarantorCustomerID>
                                    <ns6:guarantorCreditProfileID>{ data($customerGuarantor/ns6:guarantorCreditProfileID) }</ns6:guarantorCreditProfileID>
                                    <ns6:expiryDate>{ data($customerGuarantor/ns6:expiryDate) }</ns6:expiryDate>
                                    <ns6:guaranteedAmt>{ data($customerGuarantor/ns6:guaranteedAmt) }</ns6:guaranteedAmt>
                                    {
                                        for $guarantorFullName in $customerGuarantor/ns6:guarantorFullName
                                        return
                                            <ns6:guarantorFullName>{ data($guarantorFullName) }</ns6:guarantorFullName>
                                    }
                                    {
                                        for $guarantorPhoneNum in $customerGuarantor/ns6:guarantorPhoneNum
                                        return
                                            <ns6:guarantorPhoneNum>{ data($guarantorPhoneNum) }</ns6:guarantorPhoneNum>
                                    }
                                    {
                                        for $referenceNum in $customerGuarantor/ns6:referenceNum
                                        return
                                            <ns6:referenceNum>{ data($referenceNum) }</ns6:referenceNum>
                                    }
                                    {
                                        for $commentTxt in $customerGuarantor/ns6:commentTxt
                                        return
                                            <ns6:commentTxt>{ data($commentTxt) }</ns6:commentTxt>
                                    }
                                </ns6:customerGuarantor>
                        }
                        {
                            for $commentTxt in $creditProfile/ns6:commentTxt
                            return
                                <ns6:commentTxt>{ data($commentTxt) }</ns6:commentTxt>
                        }
                        {
                            for $creditWorthiness in $creditProfile/ns7:creditWorthiness
                            return
                                <ns7:creditWorthiness>
                                    <ns7:customerID>{ data($creditWorthiness/ns7:customerID) }</ns7:customerID>
                                    {
                                        for $creditAssessmentId in $creditWorthiness/ns7:creditAssessmentId
                                        return
                                            <ns7:creditAssessmentId>{ data($creditAssessmentId) }</ns7:creditAssessmentId>
                                    }
                                    {
                                        for $creditValueCd in $creditWorthiness/ns7:creditValueCd
                                        return
                                            <ns7:creditValueCd>{ data($creditValueCd) }</ns7:creditValueCd>
                                    }
                                    {
                                        for $creditRiskLevel in $creditWorthiness/ns7:creditRiskLevel
                                        return
                                            <ns7:creditRiskLevel>{ data($creditRiskLevel) }</ns7:creditRiskLevel>
                                    }                                    
                                    
                                    {
                                        for $assessmentMessageCd in $creditWorthiness/ns7:assessmentMessageCd
                                        return
                                            <ns7:assessmentMessageCd>{ data($assessmentMessageCd) }</ns7:assessmentMessageCd>
                                    }
                                    {
                                        for $productCategoryQualification in $creditWorthiness/ns7:productCategoryQualification
                                        return
                                            <ns7:productCategoryQualification>{ $productCategoryQualification/@* , $productCategoryQualification/node() }</ns7:productCategoryQualification>
                                    }
                                    {
                                        for $fraudIndicatorCd in $creditWorthiness/ns7:fraudIndicatorCd
                                        return
                                            <ns7:fraudIndicatorCd>{ data($fraudIndicatorCd) }</ns7:fraudIndicatorCd>
                                    }
                                    {
                                        for $fraudMessageCdList in $creditWorthiness/ns7:fraudMessageCdList
                                        return
                                            <ns7:fraudMessageCdList>{ data($fraudMessageCdList) }</ns7:fraudMessageCdList>
                                    }
                                    {
                                        for $creditAssessmentTypeCd in $creditWorthiness/ns7:creditAssessmentTypeCd
                                        return
                                            <ns7:creditAssessmentTypeCd>{ data($creditAssessmentTypeCd) }</ns7:creditAssessmentTypeCd>
                                    }
                                    {
                                        for $creditAssessmentSubTypeCd in $creditWorthiness/ns7:creditAssessmentSubTypeCd
                                        return
                                            <ns7:creditAssessmentSubTypeCd>{ data($creditAssessmentSubTypeCd) }</ns7:creditAssessmentSubTypeCd>
                                    }
                                    {
                                        for $creditAssessmentCreationDate in $creditWorthiness/ns7:creditAssessmentCreationDate
                                        return
                                            <ns7:creditAssessmentCreationDate>{ data($creditAssessmentCreationDate) }</ns7:creditAssessmentCreationDate>
                                    }
                                    {
                                        for $decisionCd in $creditWorthiness/ns7:decisionCd
                                        return
                                            <ns7:decisionCd>{ data($decisionCd) }</ns7:decisionCd>
                                    }
                                </ns7:creditWorthiness>
                        }
                        <ns7:reportIndicator>
                            {
                                if (fn:empty($getCreditProfileByCustomerIdResponse1/ns1:creditProfile/ns7:reportIndicator/text())) then
                                    ("false")
                                else 
                                    $getCreditProfileByCustomerIdResponse1/ns1:creditProfile/ns7:reportIndicator/text()
                            }
</ns7:reportIndicator>
                        {
                            for $creditValueEffectiveDate in $creditProfile/ns7:creditValueEffectiveDate
                            return
                                <ns7:creditValueEffectiveDate>{ data($creditValueEffectiveDate) }</ns7:creditValueEffectiveDate>
                        }
                        {
                            for $firstCreditAssessmentDate in $creditProfile/ns7:firstCreditAssessmentDate
                            return
                                <ns7:firstCreditAssessmentDate>{ data($firstCreditAssessmentDate) }</ns7:firstCreditAssessmentDate>
                        }
                        {
                            for $latestCreditAssessmentDate in $creditProfile/ns7:latestCreditAssessmentDate
                            return
                                <ns7:latestCreditAssessmentDate>{ data($latestCreditAssessmentDate) }</ns7:latestCreditAssessmentDate>
                        }
                    </ns10:creditProfileData>
            }
            {
                let $customerCollectionData := $getCustomerCollectionDataResponse1/ns12:customerCollectionData
                return
                    <ns10:customerCollectionData>
                        {
                            for $activeAccountsCollectionInd in $customerCollectionData/ns4:activeAccountsCollectionInd
                            return
                                <ns4:activeAccountsCollectionInd>{ data($activeAccountsCollectionInd) }</ns4:activeAccountsCollectionInd>
                        }
                        {
                            for $latestCollectionStartDate in $customerCollectionData/ns4:latestCollectionStartDate
                            return
                                <ns4:latestCollectionStartDate>{ data($latestCollectionStartDate) }</ns4:latestCollectionStartDate>
                        }
                        {
                            for $latestCollectionEndDate in $customerCollectionData/ns4:latestCollectionEndDate
                            return
                                <ns4:latestCollectionEndDate>{ data($latestCollectionEndDate) }</ns4:latestCollectionEndDate>
                        }
                        {
                            for $collectionScore in $customerCollectionData/ns4:collectionScore
                            return
                                <ns4:collectionScore>{ data($collectionScore) }</ns4:collectionScore>
                        }
                        {
                            for $numberOfAccountsInAgency in $customerCollectionData/ns4:numberOfAccountsInAgency
                            return
                                <ns4:numberOfAccountsInAgency>{ data($numberOfAccountsInAgency) }</ns4:numberOfAccountsInAgency>
                        }
                        {
                            for $accountsInAgencyBalance in $customerCollectionData/ns4:accountsInAgencyBalance
                            return
                                <ns4:accountsInAgencyBalance>{ data($accountsInAgencyBalance) }</ns4:accountsInAgencyBalance>
                        }
                        {
                            for $latestAgencyAssignmentDate in $customerCollectionData/ns4:latestAgencyAssignmentDate
                            return
                                <ns4:latestAgencyAssignmentDate>{ data($latestAgencyAssignmentDate) }</ns4:latestAgencyAssignmentDate>
                        }
                        {
                            for $involuntaryCancelledAccounts in $customerCollectionData/ns4:involuntaryCancelledAccounts
                            return
                                <ns4:involuntaryCancelledAccounts>{ data($involuntaryCancelledAccounts) }</ns4:involuntaryCancelledAccounts>
                        }
                        {
                            for $involuntaryCancelledAccountsBalance in $customerCollectionData/ns4:involuntaryCancelledAccountsBalance
                            return
                                <ns4:involuntaryCancelledAccountsBalance>{ data($involuntaryCancelledAccountsBalance) }</ns4:involuntaryCancelledAccountsBalance>
                        }
                        {
                            for $latestInvoluntaryCancelledDate in $customerCollectionData/ns4:latestInvoluntaryCancelledDate
                            return
                                <ns4:latestInvoluntaryCancelledDate>{ data($latestInvoluntaryCancelledDate) }</ns4:latestInvoluntaryCancelledDate>
                        }
                        {
                            for $numberOfNSFCheques in $customerCollectionData/ns4:numberOfNSFCheques
                            return
                                <ns4:numberOfNSFCheques>{ data($numberOfNSFCheques) }</ns4:numberOfNSFCheques>
                        }
                    </ns10:customerCollectionData>
            }
            <ns10:depositItemList>
                {
                    for $searchDepositResponse  in ($searchDepositPaymentByCustomerIDResponse1/searchDepositResponseList/ns15:searchDepositResponse)  
                    return
                        for $depositItem  in ($searchDepositResponse/output/depositItemList/depositItem)  
                        return
                            (<ns11:depositItem>
                            <ns11:depositID>{ data($depositItem/depositID) }</ns11:depositID>
                            <ns11:dueDate>{ data($depositItem/dueDate) }</ns11:dueDate>
                            <ns11:depositDesignationID>{ data($depositItem/depositDesignationID) }</ns11:depositDesignationID>
                            <ns11:requestAmount>{ data($depositItem/requestAmount) }</ns11:requestAmount>
                            <ns11:requestDate>{ data($depositItem/requestDate) }</ns11:requestDate>
                            <ns11:requestReasonCd>{ data($depositItem/requestReasonCd) }</ns11:requestReasonCd>
                            <ns11:requestReasonTxt>{ data($depositItem/requestReasonTxt) }</ns11:requestReasonTxt>
                            <ns11:paidAmount>{ data($depositItem/paidAmount) }</ns11:paidAmount>
                            <ns11:paidDate>{ data($depositItem/paidDate) }</ns11:paidDate>
                            <ns11:cancelledAmount>{ data($depositItem/cancelledAmount) }</ns11:cancelledAmount>
                            <ns11:cancelDate>{ data($depositItem/cancelDate) }</ns11:cancelDate>
                            <ns11:cancelReasonCd>{ data($depositItem/cancelReasonCd) }</ns11:cancelReasonCd>
                            <ns11:cancelReasonTxt>{ data($depositItem/cancelReasonTxt) }</ns11:cancelReasonTxt>
                            <ns11:releasedAmount>{ data($depositItem/releasedAmount) }</ns11:releasedAmount>
                            <ns11:releaseDate>{ data($depositItem/releaseDate) }</ns11:releaseDate>
                            <ns11:releaseReasonCd>{ data($depositItem/releaseReasonCd) }</ns11:releaseReasonCd>
                            <ns11:releaseReasonTxt>{ data($depositItem/releaseReasonTxt) }</ns11:releaseReasonTxt>
                            <ns11:releaseMethodCd>{ data($depositItem/releaseMethodCd) }</ns11:releaseMethodCd>
                            <ns11:releaseMethodTxt>{ data($depositItem/releaseMethodTxt) }</ns11:releaseMethodTxt>
                            <ns11:interestAmount>{ data($depositItem/interestAmount) }</ns11:interestAmount>
                            <ns11:accountID>{ data($depositItem/accountID) }</ns11:accountID>
                            </ns11:depositItem>)
                }
</ns10:depositItemList>
        </ns10:getAdditionalCustomerDataResponse>
};

declare variable $searchDepositPaymentByCustomerIDResponse1 as element(ns0:searchDepositPaymentByCustomerIDResponse) external;
declare variable $getCustomerCollectionDataResponse1 as element(ns12:getCustomerCollectionDataResponse) external;
declare variable $getCreditProfileByCustomerIdResponse1 as element(ns1:getCreditProfileByCustomerIdResponse) external;
declare variable $getCustomerWithContactsResponse1 as element(ns3:getCustomerWithContactsResponse) external;

xf:GetAdditionalCustomerData_MergeResults($searchDepositPaymentByCustomerIDResponse1,
    $getCustomerCollectionDataResponse1,
    $getCreditProfileByCustomerIdResponse1,
    $getCustomerWithContactsResponse1)