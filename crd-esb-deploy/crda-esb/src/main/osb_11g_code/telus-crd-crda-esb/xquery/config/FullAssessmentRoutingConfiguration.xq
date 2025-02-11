<FullAssessmentRoutingConfiguration>

	<CreditAssessmentSubType value="AUTO_ASSESSMENT" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/UnifiedCreditCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="AUTO_ASSESSMENT" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	
	<CreditAssessmentSubType value="MANUAL_ASSESSMENT" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="MANUAL_ASSESSMENT" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>	
	
	<CreditAssessmentSubType value="REOPEN_ASSESSMENT" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="REOPEN_ASSESSMENT" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
		
	<CreditAssessmentSubType value="NEW_ACC_ASSESSMENT" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="NEW_ACC_ASSESSMENT" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>	
	
	<CreditAssessmentSubType value="MONTHLY_CVUD" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="MONTHLY_CVUD" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/AnyCustomerAssessment_PS</Service>
	</CreditAssessmentSubType>
		
	<CreditAssessmentSubType value="GET_BUREAU_DATA" UCDormantFlagValue="OFF">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/GetBureuaData_PS</Service>
	</CreditAssessmentSubType>
	<CreditAssessmentSubType value="GET_BUREAU_DATA" UCDormantFlagValue="ON">
		<Service>telus-crd-crda-esb/proxy/assessCrdWorthiness/GetBureuaData_PS</Service>
	</CreditAssessmentSubType>	
	
</FullAssessmentRoutingConfiguration>