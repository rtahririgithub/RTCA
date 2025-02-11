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

package com.telus.credit.batch.report;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;

/**
 * Base class of Module that handles the initialization of a batch job.
 * The batch job can be initialized in either:
 * <ul>
 * <li><code>NORMAL</code> mode,</li>
 * <li><code>RESTART</code> mode with no saved restore state, or</li>
 * <li><code>RESTART</code> mode with saved restore state.</li>
 * </ul>
 *
 * <pre>
 * Lifecycle of batch job is:
 * 1. launch()
 * 2. restoreState() (if state was previously saved)
 * 3. hasNext() (loop 3,4,5 until return false)
 *    3.1. init() (a. calls either: initNormalMode() 
 *                              or: initRestartMode()
 *                 b. called only once)
 *    3.2. next() (call to see if there is a next)
 *    3.3. last() (called after last item has been executed)
 * 4. execute()
 * 5. getStateForRestart() (if checkpoints enabled & reached)
 * 6. getSummary()
 * 7. onExit()
 * </pre>
 * 
 * @author Trevor Baker (x107579)
 */
public abstract class BaseModule implements Module
{
    protected Log m_log = LogFactory.getLog(getClass());
    
    private boolean m_initialized;
    private boolean m_checkpointReached;

    private Properties m_restoreState;


    /**
     * A no-op launch.
     * 
     * @param  batchContext  the batch context.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    public void launch(BatchContext batchContext) throws ModuleException
    {
        // no-op
    }


    /**
     * Saves the restore state to be used later in the initialization of the
     * batch job.
     * 
     * @param  state  the previous run state.
     * 
     * @throws  ModuleException  if any error occurred.
     * 
     * @see  #initRestartMode(Properties)
     */
    public void restoreState(Properties state) throws ModuleException
    {
        m_restoreState = state;
    }


    /**
     * This <code>hasNext()</code> initializes the module, checks to see if
     * there is a next item to {@link Module#execute()} on, and allows
     * sub-classes to do an operation after the last
     * 
     * @return  <code>true</code> iff there is another item to iterate on.
     * 
     * @throws  ModuleException  if any error occurred.
     * 
     * @see  #init()
     * @see  #next()
     * @see  #last()
     */
    public boolean hasNext() throws ModuleException
    {
        if( !m_initialized )
        {
            init();
            m_initialized = true;
        }

        boolean hasNext = next();
        if( !hasNext )
        {
            last();
        }

        return hasNext;
    }
    

    /**
     * Called whenever a checkpoint has been reached to save state in the
     * event of a restart.
     * 
     * @return  an empty Properties object for sub-classes to store restart state.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    public Properties getStateForRestart() throws ModuleException
    {
        m_checkpointReached = true;

        return new Properties();
    }


    /**
     * Called when the batch job is finished to save audit summary info.
     * 
     * @return  an empty Properties object for sub-classes to store summary info.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    public Properties getSummary() throws ModuleException
    {
        return new Properties();
    }


    /**
     * Called when the batch job ends.
     * 
     * @param  success  if the batch job completed successfully.
     * 
     * @return  The appropriate return code based on <code>success</code>.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    public int onExit(boolean success) throws ModuleException
    {
        return success? RETURN_CODE_SUCCESS : RETURN_CODE_FAILURE;
    }


    /**
     * @return  <code>true</code> iff a checkpoint was reached in current job run.
     */
    public boolean isCheckpointReached()
    {
        return m_checkpointReached;
    }


    /**
     * @return  <code>true</code> iff the {@link #init()} method was called.
     */
    public boolean isInitialized()
    {
        return m_initialized;
    }


    /**
     * @return  <code>true</code> iff batch job is running in restart mode
     *          with restored state from a previous job run.
     */
    public boolean isRestart()
    {
        return m_restoreState != null;
    }


    /**
     * Initializes the batch job accordingly to the run mode.
     * Must be called only once.
     * 
     * @throws  ModuleException  if the batch job cannot be initialized.
     * 
     * @see  #hasNext()
     * @see  #isInitialized()
     * @see  #initNormalMode()
     * @see  #initRestartMode(Properties)
     */
    protected void init() throws ModuleException
    {
        if( isRestart() )
        {
            initRestartMode(m_restoreState);
        }
        else
        {
            initNormalMode();
        }
    }


    /**
     * Sub-classes must override this method to handle a <code>NORMAL</code>,
     * job run or a <code>RESTART</code> job run that did not save any state
     * in the previous job run.
     * 
     * @throws  ModuleException  if the batch job cannot be initialized.
     */
    protected abstract void initNormalMode() throws ModuleException;


    /**
     * Sub-classes must override this method to handle a <code>RESTART</code>
     * job run that saved state in the previous job run, which needs to be
     * restored.
     * 
     * @param  restoreState  the previous job run's saved state.
     * 
     * @throws  ModuleException  if the batch job cannot be initialized.
     */
    protected abstract void initRestartMode(Properties restoreState) throws ModuleException;

    
    /**
     * @return  <code>true</code> iff there is another item to iterate on.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    protected abstract boolean next() throws ModuleException;


    /**
     * A no-op operation ran after the last {@link Module#execute()} is called.
     * 
     * @throws  ModuleException  if any error occurred.
     */
    protected void last() throws ModuleException
    {
        // no-op
    }
}
