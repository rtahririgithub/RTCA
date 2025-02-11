<Asmtconfig>
	<Type typename="FULL_ASSESSMENT">
		<SubType subtypename="AUTO_ASSESSMENT"    isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="false" isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="GET_BUREAU_DATA"    isCreditProfileDataRequired="false" isCustomerDataRequired="false" isCollectionDataRequired="false" isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/GetBureuaData_PS" 			operation="assessCreditWorthiness"/>
		<SubType subtypename="MONTHLY_CVUD"       isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="NEW_ACC_ASSESSMENT" isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  isAsynchCaller="true" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="REOPEN_ASSESSMENT"  isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  isAsynchCaller="true" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="MANUAL_ASSESSMENT"  isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
	</Type>
	<Type typename="OVRD_ASSESSMENT">
		<SubType subtypename="MANUAL_OVERRIDE"           isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="UNMERGED"                  isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="CANCEL_DEPOSIT_INV"        isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="DEPOSIT_DOWNGRADE"         isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
	</Type>
	<Type typename="AUDIT">
		<SubType subtypename="BUREAU_CONSENT" isCreditProfileDataRequired="true" isCustomerDataRequired="true" isCollectionDataRequired="false" isDepositDataRequied="false" service="proxy/externalPS/EnterpriseCreditAssessmentServicePS" operation="performCreditAssessment"/>
	</Type>
</Asmtconfig>

 (: comment : original value
 
 <Asmtconfig>
	<Type typename="FULL_ASSESSMENT">
		<SubType subtypename="AUTO_ASSESSMENT"    isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="false" isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="GET_BUREAU_DATA"    isCreditProfileDataRequired="false" isCustomerDataRequired="false" isCollectionDataRequired="false" isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/GetBureuaData_PS" 			operation="assessCreditWorthiness"/>
		<SubType subtypename="MONTHLY_CVUD"       isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="NEW_ACC_ASSESSMENT" isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  isAsynchCaller="true" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="REOPEN_ASSESSMENT"  isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  isAsynchCaller="true" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
		<SubType subtypename="MANUAL_ASSESSMENT"  isCreditProfileDataRequired="true"  isCustomerDataRequired="true"  isCollectionDataRequired="true"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="assessCreditWorthiness"/>
	</Type>
	<Type typename="OVRD_ASSESSMENT">
		<SubType subtypename="MANUAL_OVERRIDE"           isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="UNMERGED"                  isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="CANCEL_DEPOSIT_INV"        isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="true"  service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
		<SubType subtypename="DEPOSIT_DOWNGRADE"         isCreditProfileDataRequired="true"   isCustomerDataRequired="true"   isCollectionDataRequired="false"  isDepositDataRequied="false" service="telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS"  operation="overrideCreditWorthiness"/>
	</Type>
	<Type typename="AUDIT">
		<SubType subtypename="BUREAU_CONSENT" isCreditProfileDataRequired="false" isCustomerDataRequired="false" isCollectionDataRequired="false" isDepositDataRequied="false" service="proxy/externalPS/EnterpriseCreditAssessmentServicePS" operation="performCreditAssessment"/>
	</Type>
</Asmtconfig>
 :)