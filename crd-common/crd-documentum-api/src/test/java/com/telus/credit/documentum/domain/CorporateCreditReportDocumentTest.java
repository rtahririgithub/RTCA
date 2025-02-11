/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.documentum.domain;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class CorporateCreditReportDocumentTest {
	
	@Test
	public void testGetMetadataAttrNames() {
		CorporateCreditReportDocument doc = new CorporateCreditReportDocument("file.ext");
		doc.setJurisdictionCode("AB");
		doc.setAddressLine("some address");
		List<String> attrNames = doc.getMetadataAttrNames();
		Assert.assertEquals(9, attrNames.size());
		Assert.assertEquals("AB", doc.getValueForAttribute("crd_jurcode"));
		Assert.assertEquals("some address", doc.getValueForAttribute("crd_addr_line"));
		Assert.assertNull(doc.getValueForAttribute("doesn't exist"));
	}

}
