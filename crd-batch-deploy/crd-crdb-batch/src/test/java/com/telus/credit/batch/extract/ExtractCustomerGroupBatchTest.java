/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 */
package com.telus.credit.batch.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.telus.credit.batch.util.CustomerGroup;

import junit.framework.TestCase;
//import junit.textui.TestRunner;

/**
 * 
 * <p>
 * <b>Description: </b>  This function . 
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Lei Fan(x089748)
 *  
 */
public class ExtractCustomerGroupBatchTest extends TestCase {

	private ExtractCustomerGroupBatch extractBatch;
	
	public ExtractCustomerGroupBatchTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		extractBatch=new ExtractCustomerGroupBatch();
		String[] c1={"p1"};
	    String[] c2={"p1","p3"};
		String[] c3={"p3"};
		String[] c4={"p3"};
		 
		HashMap m_customerMap=new HashMap();
		m_customerMap.put("c1",c1);
		m_customerMap.put("c2",c2);
		m_customerMap.put("c3",c3);
		m_customerMap.put("c4",c4);
		extractBatch.setCustomerMap(m_customerMap);
		
		String[] p1={"c1","c2"};
		String[] p3={"c2","c3","c4"};
		HashMap m_profileMap=new HashMap();
		m_profileMap.put("p1",p1);
		m_profileMap.put("p3",p3);
		extractBatch.setProfileMap(m_profileMap);
	
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testRun() {
		
		ArrayList groupList=new ArrayList();
	    String customerId;
        
        HashSet m_processedResultSet=new HashSet();
        extractBatch.setProcessedResultSet(m_processedResultSet);
        Iterator m_iteratorCustomerId;
        CustomerGroup group;
        
		/*Loop through all customer id in the customerMap*/
		for(m_iteratorCustomerId=extractBatch.getCustomerMap().keySet().iterator();m_iteratorCustomerId.hasNext();)
		{   
		  customerId=((String)m_iteratorCustomerId.next());
		  m_processedResultSet=extractBatch.getProcessedResultSet();
		  if ((m_processedResultSet.isEmpty())||(!m_processedResultSet.contains(customerId)))
		 {  
		  	  group=new CustomerGroup();
		  	  extractBatch.setGroup(group);
			  group.setProfileCount(0);
			  groupList.add(group);
			  extractBatch.findRecursive(group,customerId);
			 
		  }
		}
		assertEquals(2,extractBatch.getGroup().getProfileCount());
	}
	
}	
