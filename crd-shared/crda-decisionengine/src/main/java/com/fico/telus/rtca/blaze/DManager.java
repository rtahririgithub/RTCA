//
// Blaze Advisor Server Deployment.
// Created by the Blaze Advisor Generate Deployment Wizard
//
// Copyright (1997-2011),Fair Isaac Corporation. All Rights Reserved.
// 
//

package com.fico.telus.rtca.blaze;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blazesoft.server.deploy.manager.NdDeploymentManager;
import com.blazesoft.server.deploy.manager.NdDeploymentManagerException;

//==> Import Application specific code here

/** 
 *	This class simply starts up the Deployment manager which can be used either 
 *	to start a local or remote deployment manager.
 */
public class DManager	
{

    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog(DManager.class);

    private 		NdDeploymentManager m_dManager;

    /** 
     *	Constructs a deployment manager out of the specified
     *	configuration.
     *
     *	@param	dManagerConfig	The name of the configuration file used to 
     *							configure the deployment manager. If null, a 
     *							default will be used
     */
    public DManager(String dManagerConfig)
	throws RuleServicesException
    {
	try {
	    String deploymentManagerConfigStr = dManagerConfig != null ? dManagerConfig : "./RTCA_RuleServiceServer.dmanager";
	    log.info("Deployment Manager Configuration: " + deploymentManagerConfigStr);
	    m_dManager = NdDeploymentManager.createDeploymentManager(deploymentManagerConfigStr);
	}
	catch (NdDeploymentManagerException e ) {
		e.printStackTrace();
	    RuleServicesExceptionHandler.throwServiceException( e );
	}


    }
    
    /**
     * Stops the deployment manager
     */
    public void shutdown()
	throws NdDeploymentManagerException
    {
	m_dManager.shutdown();
    }
    
    /**
     *	Creates and starts the deployment manager. Used when servers connect to 
     *	it remotely.
     *	It is expected that this class is invoked with, in the command line, the
     *	path to an accessible deployment manager configuration file.
     */
    public static void main(String[] args)
    {
	String dManagerConfig = null;
	dManagerConfig = (((args != null) && (args.length > 0)) ? args[0] : null);
	
	try{
	    DManager dm = new DManager(dManagerConfig);
	    System.out.println("Press Enter to shut down the Deployment Manager");
	    System.in.read();
	    dm.shutdown();
	    System.out.println("It is now safe to close this command window");
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	}
	catch (NdDeploymentManagerException e) {
	    e.printStackTrace();
	    System.exit(-1);
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
