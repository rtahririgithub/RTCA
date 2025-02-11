/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 

package com.telus.credit.wlnprfldmgt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.framework.config.ConfigContext;

import com.telus.credit.domain.CreditMgtBrHelper;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import org.springframework.test.context.ContextConfiguration;


@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-wlnCrdDMgmt.properties")
@ContextConfiguration("classpath:test-refpds-spring.xml")
public class TestReferencePDSUtil
{  
    
    private static final Log log = LogFactory
    .getLog(TestReferencePDSUtil.class);

    @Test
    public void test_RefPDS() {
	try {
	    String prop = ConfigContext.getProperty("connections/webServices/refpds/endpointAddress");
	    System.out.println("Ref pds connection: " + prop );
	    CreditMgtBrHelper.isValidCode( "FULL_ASSESSMENT",  "CAR_TYP" );
	    System.out.println("validation completed: " + "prop: " + prop );
	}
	catch ( Exception e ) {
            e.printStackTrace();
        }
	
    }


}
*/