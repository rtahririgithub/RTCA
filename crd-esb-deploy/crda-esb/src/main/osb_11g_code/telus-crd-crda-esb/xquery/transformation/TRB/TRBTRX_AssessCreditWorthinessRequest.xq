(:: pragma bea:global-element-parameter parameter="$tRB_TRX1" element="TRB_TRX" location="../../../wsdls/TRB_TRX_OPEN_REOPEN_BA.xsd" ::)
(:: pragma bea:global-element-return element="ns1:assessCreditWorthiness" location="../../../wsdls/WLNCreditProfileManagementProxyServiceRequestResponse_v2_0.xsd" ::)

declare namespace ns1 = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2";
declare namespace ns0 = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7";
declare namespace xf = "http://tempuri.org/telus-crd-crda-esb/xquery/transformation/TRB/TRBTRX_AssessCreditWorthinessRequest/";

declare function xf:TRBTRX_AssessCreditWorthinessRequest($tRB_TRX1 as element(TRB_TRX))
    as element(ns1:assessCreditWorthiness) {
        <ns1:assessCreditWorthiness>
            <ns1:assessCreditWorthinessRequest xsi:type="wln:AssessCreditWorthinessRequest" xmlns:wln="http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WLNCreditProfileManagementProxyServiceRequestResponse_v2" >
                <ns1:creditAssessmentTypeCd>FULL_ASSESSMENT</ns1:creditAssessmentTypeCd>
                
                <ns1:creditAssessmentSubTypeCd>
                    {
                        if (data($tRB_TRX1/HEADER/@TransactionCode) = "REOPENBA" ) then
                            ("REOPEN_ASSESSMENT")
                        else 
                            "NEW_ACC_ASSESSMENT" (:OPENBA:)
                    }
                </ns1:creditAssessmentSubTypeCd>       
                <ns1:customerID>
                    {
                        if (data($tRB_TRX1/HEADER/@EntityType) = "CUSTOMER" ) then
                            data($tRB_TRX1/HEADER/@EntityId)
                        else 						
						   if (data($tRB_TRX1/HEADER/@TransactionCode) = "REOPENPCN" ) then
								data($tRB_TRX1/DATA/OpenBillingArrangement/BillingArrangement/BillingArrangementContext/CustomerNo)
							else 
								data($tRB_TRX1/DATA/OpenPayChannel/PayChannel/PayChannelContext/CustomerNo)					
							 
                    }
                </ns1:customerID>  

                 
                <ns1:applicationID>1047</ns1:applicationID>                
                <ns1:lineOfBusiness>WIRELINE</ns1:lineOfBusiness>
            </ns1:assessCreditWorthinessRequest>
            <ns1:auditInfo>
				<ns0:userId>SYS-BDSReclass-MDB</ns0:userId>
                <ns0:channelOrganizationId>1047</ns0:channelOrganizationId>
                <ns0:originatorApplicationId>1047</ns0:originatorApplicationId>                
                {
                    for $EffectiveDate in $tRB_TRX1/HEADER/@EffectiveDate
                    return
                        <ns0:timestamp>{ data($EffectiveDate) }</ns0:timestamp>
                }
            </ns1:auditInfo>
        </ns1:assessCreditWorthiness>
};

declare variable $tRB_TRX1 as element(TRB_TRX) external;

xf:TRBTRX_AssessCreditWorthinessRequest($tRB_TRX1)