<QueueTransportHeaders>
			<COMMON>
				<WCPPRXY_VERSION_PROPERTY>WCPPRXY_v2_0</WCPPRXY_VERSION_PROPERTY>
				<ARFM_APPLICATION_ID>WCPPRXY-CMDB-ID-9343</ARFM_APPLICATION_ID>
				<ARFM_RESOURCE_ID>Risk Management/Collections/CreditManagement</ARFM_RESOURCE_ID>
			</COMMON>
			
			
			<TRB>
				<Q_MSG_TYPE>ASSESSCREDITWORTHINESS_REQUEST_V2.0</Q_MSG_TYPE>
				<Q_NAME>WCPMPXY_INCOMMING_TRB_QUEUE</Q_NAME>				 
				<ARFM_SERVICE_NAME>[Service:WLNCreditProfileManagementProxyService_v2_0:Operation=assessCreditWorthiness]</ARFM_SERVICE_NAME>
			</TRB>
			
			<CREDITVALUE>
				<Q_MSG_TYPE>UPDATE_CUSTOMER_ODS_REQUEST_V2.0</Q_MSG_TYPE>
				<ARFM_SERVICE_NAME>[Service:WLNCreditProfileManagementProxyService_v2_0 calling ConsumerCustomerManagementService ;Operation=get/updateCustomer]</ARFM_SERVICE_NAME>
			</CREDITVALUE>

			<CREDITWORTHINESS>
				<Q_MSG_TYPE>UPDATE_CREDITWORTHINESS_REQUEST_V2.0</Q_MSG_TYPE>
				<ARFM_SERVICE_NAME>[Service:WLNCreditProfileManagementProxyService_v2_0 calling WLNCreditProfileDataManagementService ;Operation=updateCreditWorthiness]</ARFM_SERVICE_NAME>
			</CREDITWORTHINESS>		
</QueueTransportHeaders>

(: comment
  $QueueTransportHeaders/COMMON/WCPPRXY_VERSION_PROPERTY/text()
  $QueueTransportHeaders/COMMON/ARFM_APPLICATION_ID/text()
  $QueueTransportHeaders/COMMON/ARFM_RESOURCE_ID/text()
  $QueueTransportHeaders/COMMON/ARFM_RESOURCE_ID/text() 
  
  $QueueTransportHeaders/TRB/Q_MSG_TYPE/text()
  $QueueTransportHeaders/TRB/SENDER/text()
  $QueueTransportHeaders/TRB/FALLOUT_SENDER/text()
  $QueueTransportHeaders/TRB/ARFM_SERVICE_NAME/text()
  
  $QueueTransportHeaders/CREDITVALUE/Q_MSG_TYPE/text()
  $QueueTransportHeaders/CREDITVALUE/SENDER/text()
  $QueueTransportHeaders/CREDITVALUE/FALLOUT_SENDER/text()
  $QueueTransportHeaders/CREDITVALUE/ARFM_SERVICE_NAME/text()  
  
:)