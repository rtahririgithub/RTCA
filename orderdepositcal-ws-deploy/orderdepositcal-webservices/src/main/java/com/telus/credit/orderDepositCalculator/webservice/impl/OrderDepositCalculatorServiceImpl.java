
package com.telus.credit.orderDepositCalculator.webservice.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fico.telus.blaze.depositCalculator.OrderData;
import com.fico.telus.blaze.creditCommon.DepositData;
import com.fico.telus.blaze.depositCalculator.DepositCalculatorRequest;
import com.fico.telus.blaze.depositCalculator.DepositCalculationResult;
import com.fico.telus.blaze.creditCommon.ProductEquipment;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentRequest;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentResult;
import com.fico.telus.blaze.depositCalculator.DepositRequest;
import com.fico.telus.blaze.depositCalculator.DepositResult;
import com.fico.telus.rtca.blaze.RuleServicesBean;
import com.fico.telus.rtca.blaze.RuleServicesException;
import com.fico.telus.rtca.blaze.XmlUtils;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.orderDepositCalculator.webservice.OrderDepositCalculatorServiceV10PortType;
import com.telus.credit.orderDepositCalculator.webservice.PolicyException;
import com.telus.credit.orderDepositCalculator.webservice.ServiceException;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDataBr;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDepositCalculationDto;
import com.telus.credit.orderDepositCalculator.webservice.util.Util;
import com.telus.credit.orderDepositCalculator.common.domain.AssignedOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.BaseProduct;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentProduct;
import com.telus.credit.orderDepositCalculator.common.domain.DepositItem;
import com.telus.credit.orderDepositCalculator.common.domain.DepositItemList;
import com.telus.credit.orderDepositCalculator.common.domain.EquipmentCategory;
import com.telus.credit.orderDepositCalculator.common.domain.EquipmentCategoryQualificationList;
import com.telus.credit.orderDepositCalculator.common.domain.PayChannelNumberList;
import com.telus.credit.orderDepositCalculator.common.domain.PendingOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.PendingProduct;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResult;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResultList;
import com.telus.credit.orderDepositCalculator.dao.DepositCalculationTransactionDao;
import com.telus.credit.orderDepositCalculator.domain.CalculateDeposit;
import com.telus.credit.orderDepositCalculator.domain.CalculateDepositResponse;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationList;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationListResponse;
import com.telus.credit.wirelineCreditProfileManagement.domain.CreditWorthiness;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.orderDepositCalculator.webservice.util.RequestResponseUtil;
import com.telus.credit.util.SummarizeARDepositItemList;
import com.telus.credit.enterpriseCommon.domain.AuditInfo;
import com.telus.framework.config.ConfigContext;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.math.Money;
import com.telus.framework.validation.ValidationResult;
import com.telus.credit.orderDepositCalculator.common.domain.OrderDataDetail;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDepositProperties;




public class OrderDepositCalculatorServiceImpl
    implements OrderDepositCalculatorServiceV10PortType
{
	private static final Logger log = LogManager.getLogger(OrderDepositCalculatorServiceImpl.class.getName());
	public final static Integer DEFAULT_RISK_LEVEL_NUM=9000;
	
	@Autowired
	private CreditProfileDao m_creditProfileDao;
	
	@Autowired
	private RuleServicesBean m_ruleServiceBean;
	
	@Autowired
	private DozerBeanMapper m_dozerMapper;
	
	@Autowired
	private DepositCalculationTransactionDao m_depositCalculationTrxDao;
	
	@Autowired
	private OrderDataBr m_orderDataBr;
	
    public OrderDepositCalculatorServiceImpl() {
    }

    /**
     * 
     * @param parameters
     * @return
     *     returns com.telus.credit.orderDepositCalculator.domain.CalculateDepositResponse
     * @throws ServiceException
     * @throws PolicyException
     */
    @Override
    public CalculateDepositResponse calculateDeposit(CalculateDeposit parameters)
        throws PolicyException, ServiceException
    {   	
    	if ( log.isDebugEnabled()) {
				log.debug("Entering calculateDeposit........ ");
				}
   
   	   //validate auditInfo
   	    AuditInfo auditInfo=parameters.getAuditInfo();
  
    	if ((auditInfo==null) || (auditInfo.getUserId()==null) || ("".equals(auditInfo.getUserId()))
   	    	|| (auditInfo.getOriginatorApplicationId()==null) || ("".equals(auditInfo.getOriginatorApplicationId())))
    	 {
   			String errorMsg="Invalid AuditInfo. User id and Originator Application Id are required.";
    		log.error(errorMsg);
   			throw new PolicyException( "calculateDeposit: Policy Exception-Invalid AuditInfo",
					ExceptionHandler.createPolicyException(ExceptionHandler.VALIDATION_EXCEPTION,errorMsg));
       	   }
    	
    	//validate applicationID
    	String appID=parameters.getApplicationID();
    	if ((appID==null) || ("".equals(appID)))
         {
       			String errorMsg="applicationID is required.";
        		log.error(errorMsg);
       			throw new PolicyException( "calculateDeposit: Policy Exception-Invalid applicationID",
    					ExceptionHandler.createPolicyException(ExceptionHandler.VALIDATION_EXCEPTION,errorMsg));
           	   }
    	
    	
        // Get userid and source application Id from audit info.
   	     String userId=auditInfo.getUserId();
		//String srcAppId = auditInfo.getOriginatorApplicationId();
   	   
   	    long customerId=0;
    	OrderDataDetail srcOrderData=parameters.getOrderData();
    	if ( (srcOrderData !=null) && (log.isDebugEnabled()))
    	  {
    		 customerId=srcOrderData.getCustomerID();
    		 log.debug("Customer id is.........." + customerId);
		     try {
				    log.error( "Customer id: "+ customerId +" , Calculate Deposit input xml: " + RequestResponseUtil.convertToXml( parameters ));
				} catch (JAXBException e) {
					log.error("ignore error in converting request xml...."+ customerId +" , the error is  "+ e);
				}
    	    }
    		
    	String calculationTypeCD=parameters.getCalculationTypeCd();
    	if (calculationTypeCD != null) calculationTypeCD=calculationTypeCD.toUpperCase();
    	//OrderDataBr orderDataBr = OrderDataBr.getInstance();

		// Validate Order Data from input, throw the first validation error if any
		ValidationResult validationResult = m_orderDataBr.validate(calculationTypeCD, srcOrderData);

		if (!validationResult.isValid()) {
			log.error("Validation result: OrderData is invalid");
			String[] errorKeys = validationResult.labels();
			for (int i = 0; i < errorKeys.length; i++) {
				log.error("Error key " + i + ": " + errorKeys[i]);
			}
			//throw new PolicyException( "calculateDeposit: Policy Exception-Data Validation Error",
					//ExceptionHandler.createPolicyException(validationResult.errors()[0].getLabel(), validationResult.errors()[0].getDetailResult().toString()));
			throw new PolicyException( "calculateDeposit: Policy Exception-Data Validation Error",
			        //ExceptionHandler.createPolicyException(errorKeys[0], validationResult.errors()[0].getDetailResult().toString()));
			        ExceptionHandler.createPolicyException(ExceptionHandler.VALIDATION_EXCEPTION, errorKeys[0]));
			
		}
		
		if ( log.isDebugEnabled()) {
			log.debug("Validation Passed : Order Data is valid");
			log.debug("Order id is.........." + srcOrderData.getOrderID());
			log.debug("Customer id is.........." + srcOrderData.getCustomerID());
			log.debug("Order id Source System code is.........." + srcOrderData.getOrderIdSourceSystemCd());
			}
		
		//long customerId=parameters.getOrderData().getCustomerID();
        long payChannelNumber=0;
        
		//log pay channel list
		PayChannelNumberList payChannelNumList=parameters.getCustomerActivePayChannelNumberList();
		List<Long> payChannelList;
		
		if (payChannelNumList !=null) 
		  {
			      payChannelList=payChannelNumList.getPayChannelNum();	 
			      if (!payChannelList.isEmpty())
			       {
			    	 for (Iterator<Long> ittt= payChannelList.iterator(); ittt.hasNext();)
			    	 {
			    		 payChannelNumber = ittt.next();
			    		 if ( log.isDebugEnabled()) {
			    				log.debug("Customer [" + customerId + "] Active Pay Channel Number List is " + payChannelNumber);
			    				}
			    	    }
			    	 
			        }
		  }

    	CreditProfile creditProfile=new CreditProfile();
    	CreditValue  creditValue=null;
    	String creditValueCD=null;
    	String decisionCD=null;
    	Integer riskNum=null;
    	String orderIdSourceSystemCD="";
    	String orderId="";
    	BigDecimal totalPaidDepositAmount=new BigDecimal("0");
    	DepositData ficoInputDepositData=new DepositData();
    	
    	CalculateDepositResponse responseDepositCalulationResult =new CalculateDepositResponse();
    	DepositCalculatorRequest depositCalculatorRequest = new DepositCalculatorRequest();
        //set request type
    	depositCalculatorRequest.setDepositRequestType(OrderDepositCalculatorConst.DEPOSIT_CALCULATOR_TYPE);
    	//set calculation type
    	depositCalculatorRequest.setCalculationTypeCd(calculationTypeCD);
    	
    	try {
    		//get credit profile
			creditProfile=m_creditProfileDao.getCreditProfile(new Long(customerId).intValue());
			creditValue=creditProfile.getCreditValue();
			
			/* 
			 * get creditValueCD and decesionCDs
			 * Since CreditValue and Deposit decision code are retrieved by Order deposit calculator WebService 
			 * ( is not part of the input from end user ) and fico has its own validation logic , 
			 * Order deposit calculator Webservice will not validate these 2 attributes
			 * 
			 * 
			 * added credit risk level in WLNUC CR, it is retrieved from db and defined as number(4) there, could be null for some customers
		     */
			if (creditValue !=null ) 
			{ 
				creditValueCD=creditValue.getCreditValueCode();
				decisionCD=creditValue.getCreditDecisionCd();
				//added credit risk level in WLNUC CR
				riskNum=creditValue.getRiskLevelNum();
				if ( log.isDebugEnabled()) {
    				log.debug("Customer's credit value is " + creditValueCD);
    				log.debug("Customer's decision code is " + decisionCD);
    				log.debug("Customer's risk number is " + riskNum);
    				}
			}  
			
			depositCalculatorRequest.setCreditValueCd(creditValueCD);
			depositCalculatorRequest.setDecisionCd(decisionCD);
			//added credit risk level in WLNUC CR
			depositCalculatorRequest.setCreditRiskLevel(riskNum);
			
			//Mapper mapper = new DozerBeanMapper();
			
			//get orderIdSourceSystemCD, e.g.OMS/Wireline Sales
			orderIdSourceSystemCD=srcOrderData.getOrderIdSourceSystemCd();
			orderId=srcOrderData.getOrderID();
			
			//map to fico OrderData object
			 OrderData ficoOrderData= new OrderData();
			 m_dozerMapper.map(srcOrderData, ficoOrderData);
			 depositCalculatorRequest.setOrderData(ficoOrderData);
			 
			 //Get Summarized deposit from enabler for FINAL type calculation
			 if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
				{
				//fico DepositData 
				 DepositItemList depositItemList=srcOrderData.getDepositItemList();
				 if (depositItemList !=null)
				 {
			          if (log.isDebugEnabled()) {
							log.debug("DepositItemList from Enabler is ........ ");
						}
				      ficoInputDepositData=getSummarizedDeposit(depositItemList);
				      totalPaidDepositAmount=ficoInputDepositData.getDepositPaid();
				    }
				 depositCalculatorRequest.setDepositData(ficoInputDepositData);
				  }
			 
			//calling fico............	
			 log.error("Customer id: "+ customerId + ", Rule Services Bean: (calculateDeposit) input: " + XmlUtils.convertToXml( depositCalculatorRequest ) );
			 DepositCalculationResult depositCalculationResult = (DepositCalculationResult) m_ruleServiceBean.calculateDeposit(customerId, depositCalculatorRequest);
			 log.error( "Customer id: "+ customerId + ", Calculate Deposit Result: " + XmlUtils.convertToXml( depositCalculationResult ));			    

			 			  
			 //mapping deposit calculation result from fico to web service 
			 m_dozerMapper.map(depositCalculationResult, responseDepositCalulationResult);
			 
			 //set customer id as fico doesn't return customer id
			 responseDepositCalulationResult.setCustomerID(customerId);
			 
			 //set orphans objects in web service response, orderID and BigDecimal depositOnHandAmt
			 responseDepositCalulationResult.setOrderID(orderId);
			 responseDepositCalulationResult.setDepositOnHandAmt(totalPaidDepositAmount);
			 
			 Long depositCalculationTrnID=null;
			 
			 //persist data(order deposit transactions, product instances and pay channels) for FINAL TYPE
			 if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
			 {  
			   //persist Deposit Calculation Transaction
			   OrderDepositCalculationDto orderDepositCalculationDto=new OrderDepositCalculationDto();
			   
			   //setOrderDepositCalculationDto
			   //from input
			   orderDepositCalculationDto.setCustomerID(customerId);
			   orderDepositCalculationDto.setOrderID(orderId);
			   orderDepositCalculationDto.setOrderMasterSrcID(orderIdSourceSystemCD);
			   orderDepositCalculationDto.setDecisionCD(decisionCD);
			   //added credit risk level in WLNUC CR
			   orderDepositCalculationDto.setRiskLevelNum(riskNum);
			   orderDepositCalculationDto.setApplicationID(appID);
			   orderDepositCalculationDto.setChannelID(parameters.getChannelID());
			   orderDepositCalculationDto.setUserID(userId);
	           //from calculation,ficoInputDepositData
			   orderDepositCalculationDto.setTotalDepositPaidAmt(ficoInputDepositData.getDepositPaid());
			   orderDepositCalculationDto.setLastDepositPaidDate(Util.asDate(ficoInputDepositData.getMostRecentDepositPaidDate()));
			   orderDepositCalculationDto.setTotalDepositPendingAmt(ficoInputDepositData.getDepositPending());
			   orderDepositCalculationDto.setLastDepositPendingDate(Util.asDate(ficoInputDepositData.getMostRecentDepositPendingDate()));
			   orderDepositCalculationDto.setTotalDepositReleaseAmt(ficoInputDepositData.getDepositReleased());
			   orderDepositCalculationDto.setLastDepositReleaseDate(Util.asDate(ficoInputDepositData.getMostRecentDepositReleaseDate()));
			    //?source
			   orderDepositCalculationDto.setDepositOnHandAmt(totalPaidDepositAmount);
				
				//from fico return
				orderDepositCalculationDto.setDepositAdjustmentAmt(depositCalculationResult.getDepositAdjustmentAmt());
				orderDepositCalculationDto.setTotalAssessedDepositAmt(depositCalculationResult.getTotalDepositAmt());
				orderDepositCalculationDto.setCalculationResultMsgCD(depositCalculationResult.getCalculationResultMessageCd());
				orderDepositCalculationDto.setCalculationResultReasonCD(depositCalculationResult.getCalculationResultReasonCd());

			   //calling dao to persist
			   depositCalculationTrnID=m_depositCalculationTrxDao.insertDepositCalculationTransaction(orderDepositCalculationDto);
			   
			   
			 /*
			  * set orphans objects in web service response, productNameCd and monthlyChargeAmt in current product list
			  * get assessed deposit amount for each current product instance and persist current order products if FINAL
              *
			  */ 
			   //CurrentOrderProductList
			   List<ProductDepositResult> newProductDepositResult=new ArrayList<ProductDepositResult>();
			   ProductDepositResultList newProductDepositResultList=new ProductDepositResultList();
			   CurrentOrderProductList currentOrderProductList=srcOrderData.getCurrentOrderProductList();
			   ProductDepositResultList productDepositResultList=responseDepositCalulationResult.getProductDepositResultList();
			   if ( currentOrderProductList != null ) 
		              {
					      if ( log.isDebugEnabled() )  log.debug("checking current order product instance......");
					      newProductDepositResult=insertCurrentOrderProductInstance(currentOrderProductList,productDepositResultList,depositCalculationTrnID,userId, calculationTypeCD);
					      newProductDepositResultList.setProductDepositResult(newProductDepositResult);
					      responseDepositCalulationResult.setProductDepositResultList(newProductDepositResultList);
			        }
			   
			   //persist pending and assigned order products if FINAL
			   //if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD)) {
				 //PendingOrderProductList
				 PendingOrderProductList pendingOrderProductList=srcOrderData.getPendingOrderProductList();	 
				 if ( pendingOrderProductList != null ) 
		              {
					     if ( log.isDebugEnabled() )  log.debug("inserting pending order product instance......");
					     List<BaseProduct> invoicedOrderProductList= Util.castList(BaseProduct.class, pendingOrderProductList.getPendingProduct());
					     insertInvoicedOrderProductInstance(invoicedOrderProductList,depositCalculationTrnID,OrderDepositCalculatorConst.PENDING_ORDER_PRODUCT_STATUS,userId);
			        }
				 
				 //AssignedOrderProductList
				 AssignedOrderProductList assignedOrderProductList=srcOrderData.getAssignedOrderProductList();	 
				 if ( assignedOrderProductList != null ) 
		              {
					     if ( log.isDebugEnabled() )  log.debug("inserting assigned order product instance......");
					     List<BaseProduct> invoicedOrderProductList= Util.castList(BaseProduct.class, assignedOrderProductList.getAssignedProduct());
					     insertInvoicedOrderProductInstance(invoicedOrderProductList,depositCalculationTrnID,OrderDepositCalculatorConst.ASSIGNED_ORDER_PRODUCT_STATUS,userId);
			        }
			    } //persist closing

		} catch (ObjectNotFoundException notFoundEx) {	
			throw new PolicyException( "calculateDeposit: Policy Exception-Unable to get credit Profile",
					ExceptionHandler.createPolicyException(notFoundEx));
	     }catch (MappingException mape){
	    	 throw new ServiceException( "calculateDeposit: Dozer Mapping Exception",
	        			ExceptionHandler.createServiceException(mape), mape );
		 }catch (RuleServicesException e) {
				throw new ServiceException( "calculateDeposit: FICO Rule Service Exception",
						ExceptionHandler.createServiceException(e), e );					
		 }catch (DuplicateKeyException dupe){
	    	 throw new ServiceException( "calculateDeposit: DuplicateKeyException",
	        			ExceptionHandler.createServiceException(dupe), dupe );
		 }catch( Throwable e ){
			 throw new ServiceException( "calculateDeposit: Service Exception",
	        			ExceptionHandler.createServiceException(e), e );
		}
    	
    	try {
			log.error( "Customer id: "+ customerId + " , Calculate Deposit output xml: " + RequestResponseUtil.convertToXml( responseDepositCalulationResult ));
		} catch (JAXBException e) {
			log.error("ignore error in converting response xml...."+ customerId + " , the error is " + e);
		}
    	
    	if ( log.isDebugEnabled()) {
			log.debug("Exiting calculateDeposit........ ");		
			}
        return responseDepositCalulationResult;
    }
    
    /**
     * 
     * <p>
	 * <b>Description </b> Summarise Deposit Amounts, Dates from Enabler.
	 * </p>
	 * 
     * @param com.telus.credit.orderDepositCalculator.common.domain.DepositItemList
     * @return
     *     returns com.telus.credit.orderDepositCalculator.common.domain.DepositItemList
     */
    protected DepositData getSummarizedDeposit(DepositItemList depositItemList)
    {
    	 if (log.isDebugEnabled()) {
				log.debug("Entering getSummarizedDeposit........ ");
				
			}
    	 
    	DepositData ficoDepositData =new DepositData(); 
    	List<DepositItem> depositItems=depositItemList.getDepositItem(); 
    	List<com.telus.credit.domain.deposit.DepositItem>
    	    creditDepositItems =new ArrayList<com.telus.credit.domain.deposit.DepositItem>();
    	com.telus.credit.domain.deposit.DepositItem creditDepositItem = null;
    	
    	if (depositItems != null && depositItems.size()> 0) 
    	  {
    		 for (DepositItem depositItem : depositItems) 
    		   {
    			  creditDepositItem = new com.telus.credit.domain.deposit.DepositItem();
    			  m_dozerMapper.map(depositItem, creditDepositItem);
    			  creditDepositItems.add(creditDepositItem);
    		     }  

    	    com.telus.credit.domain.deposit.DepositData orderDepositData=SummarizeARDepositItemList.summarizeARDepositDetail(creditDepositItems); 
    	  
    	    
    	    if (log.isDebugEnabled()) {
    	    	log.debug("Summarized AR deposit details........ ");
				log.debug("Total Deposit Paid........ " + orderDepositData.getDepositPaid());
				log.debug("Most Recent Deposit Paid Date........ " + orderDepositData.getMostRecentDepositPaidDate());
				log.debug("Total Deposit Pending........ " + orderDepositData.getDepositPending());
				log.debug("Most Recent Deposit Pending Date........ " + orderDepositData.getMostRecentDepositPendingDate());
				log.debug("Total Deposit Released........ " + orderDepositData.getDepositReleased());
				log.debug("Most Recent Deposit Release Date........ " + orderDepositData.getMostRecentDepositReleaseDate());
    	      }
    	    
    	    ficoDepositData.setDepositPaid(orderDepositData.getDepositPaid());
    		ficoDepositData.setMostRecentDepositPaidDate(orderDepositData.getMostRecentDepositPaidDate());
    		   
    		ficoDepositData.setDepositPending(orderDepositData.getDepositPending());
    		ficoDepositData.setMostRecentDepositPendingDate(orderDepositData.getMostRecentDepositPendingDate());
    		   
    		ficoDepositData.setDepositReleased(orderDepositData.getDepositReleased());
    		ficoDepositData.setMostRecentDepositReleaseDate(orderDepositData.getMostRecentDepositReleaseDate());
    		
    	  }
		   
    	
		   
	    if (log.isDebugEnabled()) {
					log.debug("Exiting getSummarizedDeposit........ ");
					
			}
		return ficoDepositData;
             
       }
    
    
	 private List<ProductDepositResult> insertCurrentOrderProductInstance(CurrentOrderProductList currentOrderProductList,ProductDepositResultList productDepositResultList,Long depositCalculationTrnID,String userID, String calculationTypeCd)
	            throws DuplicateKeyException
	  {
		Map productInstanceMap; 
		Long productInstanceID;
		List<ProductDepositResult> rtnDepositResultList=new ArrayList<ProductDepositResult>();
		
		List<ProductDepositResult> depositResultList=productDepositResultList.getProductDepositResult();
		ProductDepositResult productDepositResult=new ProductDepositResult();
		BigDecimal assessedDepositAmt;
		
		List<CurrentProduct> productList=currentOrderProductList.getCurrentProduct();
	    CurrentProduct currentProduct=new CurrentProduct();
	    
	    if (!productList.isEmpty())
	    {
	    	for (Iterator<CurrentProduct> it = productList.iterator(); it.hasNext();) 
	      	  {
	    		 currentProduct=it.next();
	    		 assessedDepositAmt=new BigDecimal("0");
	    		 
	    	     
	    		 //get assesssed deposit amount for each product
	    		 if (!depositResultList.isEmpty())
	    	     {
	    			for (Iterator<ProductDepositResult> itt = depositResultList.iterator(); itt.hasNext();) 
	    			{
	    				productDepositResult=itt.next();
	    				if (log.isDebugEnabled()) {
	    					log.debug("Comparing FICO Desposit Results with Current Order Product Instance........ ");
	    				    log.debug("Service Type Code........ "+productDepositResult.getServiceTypeCd());
	    				    log.debug("Assigned Product id........ "+productDepositResult.getAssignedProductID());
	    				    log.debug("Assessed Deposit Amount........ "+productDepositResult.getAssessedDepositAmt());
	    				}
	    				if ( currentProduct.getAssignedProductID().equalsIgnoreCase(productDepositResult.getAssignedProductID())
	    					&& currentProduct.getServiceTypeCd().equalsIgnoreCase(productDepositResult.getServiceTypeCd())
	    					)
	    				 {
	    					assessedDepositAmt = productDepositResult.getAssessedDepositAmt();
	    				    //set MonthlyChargeAmt and ProductNameCd for service return
	    				    productDepositResult.setMonthlyChargeAmt(currentProduct.getTotalRecurringChargeAmt());
	    				    productDepositResult.setProductNameCd(currentProduct.getProductName());
	    				    //roll back service type code for single line credit restrict
	    				    productDepositResult.setServiceTypeCd(
	    				    		(OrderDepositCalculatorConst.FICO_SING_RESTRICT).equals(productDepositResult.getServiceTypeCd()) 
	    				    		? OrderDepositCalculatorConst.SING 
	    				    		: productDepositResult.getServiceTypeCd());
	    				    if (log.isDebugEnabled()) 
	    				      {
	    				    	log.debug("Matching found in current order product from input............ ");
	    				    	log.debug("Assigned Product id is....>>> " + productDepositResult.getServiceTypeCd() );
	    				    	log.debug("Service Type Code is....>>> " + productDepositResult.getServiceTypeCd() );
	    				        }
	    				    //adding to web service response
	    				    rtnDepositResultList.add(productDepositResult);
	    				  }//if closing
	    			  
	    			  }//for closing
	    	        }
                 
	    		 if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCd))
	    		 {
		    		 //prepare data for dao
	    			 if ( log.isDebugEnabled() )  log.debug("inserting pending order product instance......");
		    		 productInstanceMap=new HashMap();
		    		 productInstanceMap.put("orderProductStatus", OrderDepositCalculatorConst.CURRENT_ORDER_PRODUCT_STATUS);
		    		 productInstanceMap.put("currentProduct", currentProduct);
		    		 productInstanceMap.put("depositCalculationTrnID", depositCalculationTrnID);
		    		 productInstanceMap.put("assessedDepositAmt", assessedDepositAmt);
		    		 productInstanceMap.put("userID", userID);
		    		 productInstanceID =m_depositCalculationTrxDao.insertCurrentOrderProductInstance(productInstanceMap);
		    		 
		    		 
		    		 //persist product pay channel
		    		 PayChannelNumberList payChannelNumList=currentProduct.getPayChannelNumberList();
		    		 if (payChannelNumList !=null)
		    		   { 
		    			 m_depositCalculationTrxDao.insertProductPayChannel(payChannelNumList, productInstanceID, userID );
		    			   }
	    		  }//if closing for persisting current product instance for FINAL
	      	   }
	    	}
	    
	       return rtnDepositResultList;
	    }//method closing
	 


	 private void insertInvoicedOrderProductInstance(List<BaseProduct> invoicedOrderProductList,Long depositCalculationTrnID,String orderProductStatus,String userID)
	            throws DuplicateKeyException
	      	  {
	    			Map productInstanceMap; 
	    			Long productInstanceID;
	    			
	    	       // List<BaseProduct> productList=invoicedOrderProductList.getPendingProduct();
	    			BaseProduct baseProduct=new BaseProduct();
	    	 	    
	    	 	    if (!invoicedOrderProductList.isEmpty())
	    	 	    {
	    	 	    	for (Iterator<BaseProduct> it = invoicedOrderProductList.iterator(); it.hasNext();) 
	    	 	      	  {
	    	 	    		baseProduct=it.next();
	    	 	    		 //to do
	    	 	    		 productInstanceMap=new HashMap();
	    	 	    		 productInstanceMap.put("orderProductStatus", orderProductStatus);
	    	 	    		 productInstanceMap.put("baseProduct", baseProduct);
	    	 	    		 productInstanceMap.put("depositCalculationTrnID", depositCalculationTrnID);
	    	 	    		 productInstanceMap.put("userID", userID);
	    	 	    		 productInstanceID = m_depositCalculationTrxDao.insertInvoicedOrderProductInstance(productInstanceMap);
	    	 	    		 
	    	 	    		 PayChannelNumberList payChannelNumList=baseProduct.getPayChannelNumberList();
	    	 	    		 if (payChannelNumList !=null)
	    	 	    		   { 
	    	 	    			m_depositCalculationTrxDao.insertProductPayChannel(payChannelNumList, productInstanceID, userID );
	    	 	    			   }
	    	 	      	   }
	    	 	    
	    	 	    	}
	}//method closing  
	 

    /**
     * 
     * @param parameters
     * @return
     *     returns com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationListResponse
     * @throws ServiceException
     * @throws PolicyException
     */
    @Override
    public GetEquipmentQualificationListResponse getEquipmentQualificationList(GetEquipmentQualificationList parameters)
        throws PolicyException, ServiceException
    {
        
    	long customerId=0;
	    String creditValueCDDao=null;
	    String decisionCDDao=null;
	    Integer riskNumDao=null;
	    
	    
    	if (parameters !=null) {
    		 customerId=parameters.getCustomerID();
    	     try {
			   log.error( "Customer ID : "+ customerId + " , getEquipmentQualificationList input xml: " + RequestResponseUtil.convertToXml( parameters));
		     } catch (JAXBException e) {
			   log.error("ignore error in converting getEquipmentQualificationList request xml...."+ e);
		     }
    	}
    	
    	if ( parameters == null 
    		 || parameters.getCustomerID()==0) {
    	//	 || parameters.getCreditWorthiness() == null
    	//	 || parameters.getCreditWorthiness().getCreditValueCd() == null
    	//	 || parameters.getCreditWorthiness().getCreditValueCd().trim().length() == 0 
    	//	 || parameters.getCreditWorthiness().getDecisionCd() == null
    	//	 || parameters.getCreditWorthiness().getDecisionCd().trim().length() == 0 ) 
    		throw new PolicyException("getEquipmentQualificationList Validation Exception", 
    				ExceptionHandler.createPolicyException ( ExceptionHandler.VALIDATION_EXCEPTION,
    														"getEquipmentQualificationList Validation Exception: "
    														+ composeValidationMessage( 
    																new ValidationCondition( (parameters == null), "GetEquipmentQualificationList not populated" ), 
    																new ValidationCondition( ( parameters != null && parameters.getCustomerID() == 0 ),
    	    																						 "Customer ID not populated." )
    																//new ValidationCondition( ( parameters != null && parameters.getCreditWorthiness() == null ),
    																//						 "Credit Worthiness not populated." ),
    																//new ValidationCondition( (parameters != null &&	parameters.getCreditWorthiness() != null
    																//						  && (parameters.getCreditWorthiness().getCreditValueCd() == null 
    																//							  || parameters.getCreditWorthiness().getCreditValueCd().trim().length() == 0)),
    																//						  "Credit value code is not populated." ),
    																//new ValidationCondition( (parameters != null &&	parameters.getCreditWorthiness() != null
    																//						 && ( parameters.getCreditWorthiness().getDecisionCd() == null
    																//			    		 || parameters.getCreditWorthiness().getDecisionCd().trim().length() == 0 ) ),
    																//			    		 "Decision code is not populated." )
    																) ) );
    	}
    	
    try {
    	    CreditProfile creditProfile=m_creditProfileDao.getCreditProfile(new Long(customerId).intValue());
		    CreditValue  creditValue=creditProfile.getCreditValue();

		
		/* 
		 * credit risk level is retrieved from dao and defined as number(4) there, could be null for some customers
	     */
		if (creditValue !=null ) { 
			creditValueCDDao=creditValue.getCreditValueCode();
			decisionCDDao=creditValue.getCreditDecisionCd();
			//added credit risk level in WLNUC CR
			riskNumDao=creditValue.getRiskLevelNum();
		
			log.error("Customer's credit value from DAO  is " + creditValueCDDao);
			log.error("Customer's decision code from DAO is " + decisionCDDao);
			log.error("Customer's risk number from DAO is " + riskNumDao);
		    } 
		
         } catch (ObjectNotFoundException notFoundEx) {	
			throw new PolicyException( "getEquipmentQualificationList: Policy Exception-Unable to get credit Profile" + customerId ,
					ExceptionHandler.createPolicyException(notFoundEx));
         }
		

    	
    	GetEquipmentQualificationListResponse result = new GetEquipmentQualificationListResponse();
        CreditWorthiness creditWorthiness=parameters.getCreditWorthiness();
        DepositEquipmentRequest depositRequest = new DepositEquipmentRequest();
        
		depositRequest.setDepositRequestType(OrderDepositCalculatorConst.DEPOSIT_EQUIPMENT_TYPE);
		
		depositRequest.setDecisionCd( decisionCDDao !=null ? decisionCDDao : (creditWorthiness !=null ? creditWorthiness.getDecisionCd() : ""));
		depositRequest.setCreditValueCd(creditValueCDDao !=null ? creditValueCDDao : (creditWorthiness !=null ? creditWorthiness.getCreditValueCd() : ""));
		//added credit risk level in WLNUC CR
		depositRequest.setCreditRiskLevel(riskNumDao !=null ? riskNumDao : (creditWorthiness !=null ? creditWorthiness.getCreditRiskLevel() : DEFAULT_RISK_LEVEL_NUM));
		
        try {
        	log.error("Customer id: "+ parameters.getCustomerID() + ", Rule Services Bean: (getEquipmentQualificationList) input: " + XmlUtils.convertToXml( depositRequest ) );
        	DepositEquipmentResult depositEquipmentResult = (DepositEquipmentResult) m_ruleServiceBean.calculateDeposit(parameters.getCustomerID(), depositRequest);
			log.error( "Customer id: "+ parameters.getCustomerID() + ", getEquipmentQualificationList Result: " + XmlUtils.convertToXml( depositEquipmentResult ));			    
        	
        	if ( depositEquipmentResult != null &&
        		 depositEquipmentResult.getProductEquipmentQualification() != null ) {
        		if ( log.isDebugEnabled() ) {
        			log.debug("Equipment Qualification  for customer: " + parameters.getCustomerID() );
        		}
        		EquipmentCategoryQualificationList equipmentQualList = new EquipmentCategoryQualificationList();
        		for ( ProductEquipment productEquipment: depositEquipmentResult.getProductEquipmentQualification().getProductEquipmentList() ) {
        			EquipmentCategory equipmentCategory = new EquipmentCategory();
        			equipmentCategory.setProductCd(productEquipment.getProductCd());
        			equipmentCategory.setMaxCount(productEquipment.getMaxNumberOfEquipment());
        			log.debug( "Product Code: " + equipmentCategory.getProductCd() + ", Max Equipment: " + equipmentCategory.getMaxCount() );
        			equipmentQualList.getEquipmentCategory().add( equipmentCategory );
        		}
        		result.setEquipmentCategoryQualificationList(equipmentQualList);
        	}
        	else {
        		log.error("No Equipment Qualification data returned by FICO for customer: " + parameters.getCustomerID() );
        	}	
		} catch (RuleServicesException e) {
			throw new ServiceException( "getEquipmentQualificationList: Service Exception",
			ExceptionHandler.createServiceException(e), e );
		}
        catch ( Exception e) {
        	throw new ServiceException( "getEquipmentQualificationList: Service Exception",
        			ExceptionHandler.createServiceException(e), e );
        }
        
     	try {
			log.error( "Customer ID : " + customerId + " , getEquipmentQualificationList output xml: " + RequestResponseUtil.convertToXml( result ));
		} catch (JAXBException e) {
			log.error("ignore error in converting getEquipmentQualificationList response xml...."+ e);
		}
     	
        return result;
    }

    /**
     * Validation condition 
     *
     */
    public class ValidationCondition {
    	public boolean m_condition;
    	public String m_message;
    	
    	public ValidationCondition( boolean cond, String vMessage ) {
    		m_condition = cond;
    		m_message = vMessage;
    	}
    }
    
    /**
     * Compose validation message
     * @param validationConditions
     * @return validation message
     */
    public String composeValidationMessage( ValidationCondition... validationConditions) {
    	StringBuffer result = new StringBuffer();
    	for ( ValidationCondition vc: validationConditions ) {
    		if ( vc.m_condition ) {
    			result.append( vc.m_message );
    		}
    	}
    	return result.toString();
    }
    
    /**
     * 
     * @param parameters
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse
     * @throws ServiceException
     * @throws PolicyException
     */
    @Override
    public PingResponse ping(Ping parameters)
        throws PolicyException, ServiceException
    {
        StringBuffer version=new StringBuffer("OrderDepositCalculatorService_V1.0");
        String fw_buildDate = ConfigContext.getProperty("fw_buildDate");
    	String fw_buildLabel = ConfigContext.getProperty("fw_buildLabel");
    	version.append(" [buildDate=" + fw_buildDate  +"]").append(" [buildLabel=" + fw_buildLabel  +"]");
		try {
			
			version.append(" [ficoPing: ").append(m_ruleServiceBean.ping()).append(" ,]");
		} catch (RuleServicesException e) {
			log.error(this.getClass(),e); 	
			version.append("[ ").append(e.getStackTrace()).append(" ]");
		}
       
        PingResponse response=new PingResponse();
        response.setVersion(version.toString());
        return response;
    }

}
