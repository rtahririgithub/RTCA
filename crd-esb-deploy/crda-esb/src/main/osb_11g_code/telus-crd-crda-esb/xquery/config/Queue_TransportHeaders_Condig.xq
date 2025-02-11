<QueueTransportHeaders>
	<MessageType MessageTypeNAME="COMMON" 					WCPPRXY_VERSION_PROPERTY="WCPPRXY_v2_0" 			ARFM_APPLICATION_ID="WCPPRXY-CMDB-ID-9343"		ARFM_RESOURCE_ID="Risk Management/Collections/CreditManagement"/>
	<MessageType MessageTypeNAME="TRB" 						Q_MSG_TYPE="ASSESSCREDITWORTHINESS_REQUEST_V2.0" 	ARFM_SERVICE_NAME="[Service:WLNCreditProfileManagementProxyService_v2_0:Operation=assessCreditWorthiness]"/>
	<MessageType MessageTypeNAME="UPDATECREDITWORTHINESS" 	Q_MSG_TYPE="UPDATE_CREDITWORTHINESS_REQUEST_V2.0" 	ARFM_SERVICE_NAME="[Service:WLNCreditProfileManagementProxyService_v2_0 calling WLNCreditProfileDataManagementService ;Operation=updateCreditWorthiness]"/>
	<MessageType MessageTypeNAME="UPDATECUSTOMERODS" 		Q_MSG_TYPE="UPDATE_CUSTOMER_ODS_REQUEST_V2.0" 		ARFM_SERVICE_NAME="[Service:WLNCreditProfileManagementProxyService_v2_0 calling ConsumerCustomerManagementService ;Operation=get/updateCustomer]"/>
	
</QueueTransportHeaders>