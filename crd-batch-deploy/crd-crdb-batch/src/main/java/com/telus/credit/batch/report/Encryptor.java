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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.crypto.impl.pilot.PilotCryptoImpl;

import com.telus.credit.batch.util.CommonConstants;
import com.telus.credit.batch.util.StringUtil;

public class Encryptor
{
    private String m_encryptionFieldIndexes;
    private ArrayList m_listOfFieldIndexes;
    private PilotCryptoImpl m_cryptoImpl;
    private Log m_log = LogFactory.getLog(getClass());
    
    public String getEncryptionFieldIndexes()
    {
        return this.m_encryptionFieldIndexes;
    }
    
    public void setEncryptionFieldIndexes ( String encryptionFieldIndexes)
    {
        this.m_encryptionFieldIndexes = encryptionFieldIndexes;
    }

    public PilotCryptoImpl getCryptoImpl()
    {
        return this.m_cryptoImpl;
    }
    
    public void setCryptoImpl( PilotCryptoImpl cryptoImpl)
    {
        this.m_cryptoImpl = cryptoImpl;
    }
    
    public void init()
    {
        m_listOfFieldIndexes = StringUtil.parseString(m_encryptionFieldIndexes, ",");       
    }
    
    public String encryptFields ( String recordLine ) throws ModuleException
    {
        try{
            ArrayList recordFields = StringUtil.parseString(recordLine, CommonConstants.FIELD_DELIMITER );
            for (int i=0; i < m_listOfFieldIndexes.size(); i++ )
            {
                Object obj = m_listOfFieldIndexes.get( i ); 
                int fieldIndex = Integer.parseInt( (String)obj);
                String field_str = null;
                if ( fieldIndex+1 <= recordFields.size())
                 field_str = ( String )recordFields.get(fieldIndex);
                if ( field_str == null || "".equals(field_str))
                    continue;
                
                String strDecrypted = new String( m_cryptoImpl.encrypt( field_str ));
                recordFields.set(fieldIndex, strDecrypted);
            }
            
            StringBuffer outputStr = new StringBuffer();
            int flsListSize = recordFields.size();
            for ( int i=0 ; i< flsListSize ; i++)
            {
                String str = (String) recordFields.get(i);
                switch ( i ) {
                    case 0:
                        str = StringUtil.justifiedStrGenerator( str, "0", 9, true );
                        break;
                    case 1:
                        str = StringUtil.justifiedStrGenerator( str, " ", 10, false );
                        break;
                    case 2:
                        str = StringUtil.justifiedStrGenerator( str, " ", 2, false );
                        break;
                    case 3:
                        str = StringUtil.justifiedStrGenerator( str, " ", 50, false );
                        break;
                    case 4:
                        str = StringUtil.justifiedStrGenerator( str, " ", 10, false );
                        break;
                    case 5:
                        str = StringUtil.justifiedStrGenerator( str, " ", 9, false );
                        break;
                }
                outputStr.append( str );
            }
            return outputStr.toString();
        }
        catch (Exception e)
        {
            m_log.error("Error in decryption!" , e);
            throw new ModuleException ("Error in decryption!" , e);
        }
    }
}
