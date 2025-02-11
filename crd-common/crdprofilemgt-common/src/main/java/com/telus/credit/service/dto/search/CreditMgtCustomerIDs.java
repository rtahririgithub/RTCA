/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.service.dto.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * 
 * <p><b>Description: </b> Container for CreditMgtCustomerID objects.</p>
 *
 * <p><br><b>Revision History: </b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="55%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */

public class CreditMgtCustomerIDs implements Set
{


    /**
     * <code>m_set</code> that holds CreditMgtCustomerID objects.
     */
    private Set m_set;



    /**
     * CONSTRUCTOR
     */
    public CreditMgtCustomerIDs()
    {
        m_set = new HashSet();
    }


    /**
     * <p><b>Description</b> Adds new CreditMgtCustomerID object</p>
     * @param cid represents customerId
     * @return true if this CreditMgtCustomerIDs changed as a result of the call;
     * false otherwise. 
     */
    public boolean add(CreditMgtCustomerID cid)
    {
        return m_set.add( cid );
    }


    /**
     * <p><b>Description</b> Checks whether CreditMgtCustomerIDs contains aCustomerId</p>
     * @param aCustomerId
     * @return true if customerId is present; false otherwise.  
     */
    public boolean containsId(int aCustomerId)
    {
        for ( Iterator it = m_set.iterator(); it.hasNext(); ) {
            int customerId = ((CreditMgtCustomerID) it.next()).getId()
                    .intValue();
            if ( aCustomerId == customerId ) {
                return true;
            }
        }
        return false;
    }


    /**
     * <p><b>Description: </b> Checks whether the object of this class contains unprocessed
     * customer ids</p>
     * @return true if there are unprocessed customer ids; false otherwise.
     */
    public boolean containsUnprocessedId()
    {
        for ( Iterator i = m_set.iterator(); i.hasNext(); ) {
            CreditMgtCustomerID customerID = (CreditMgtCustomerID) i.next();
            if ( !customerID.isProcessed() ) {
                return true;
            }
        }
        return false;
    }


    /**
     * <p><b>Description</b> Return all customerIds.</p>
     * @return array of customerIds.  
     */
    public Integer[] getIds()
    {
        Integer[] cids = new Integer[m_set.size()];
        int j = 0;
        for ( Iterator i = m_set.iterator(); i.hasNext(); ) {
            CreditMgtCustomerID customerID = (CreditMgtCustomerID) i.next();
            cids[j++] = customerID.getId();
        }
        return cids;
    }


    /**
     * <p><b>Description</b> Adds a collection of CreditMgtCustomerID objects.</p>
     * @param cids collection of CreditMgtCustomerID objects
     * @return true if this CreditMgtCustomerIDs changed as a result of the call;
     * false otherwise.
     */
    public boolean addAll(Collection cids)
    {
        for ( Iterator it = cids.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if ( !(o instanceof CreditMgtCustomerID) ) {
                throw new IllegalArgumentException();
            }
        }
        return m_set.addAll( cids );
    }

    /**
     * <p><b>Description</b> Returns iterator.</p>
     * @return iterator  
     */
    public Iterator iterator()
    {
        return m_set.iterator();
    }


    /* (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object o)
    {
        return m_set.add( o );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#clear()
     */
    public void clear()
    {
        m_set.clear();
    }


    /* (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o)
    {
        return m_set.contains( o );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c)
    {
        return m_set.containsAll( c );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty()
    {
        return m_set.isEmpty();
    }


    /* (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o)
    {
        return m_set.remove( o );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c)
    {
        return m_set.removeAll( c );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c)
    {
        return m_set.retainAll( c );
    }


    /* (non-Javadoc)
     * @see java.util.Collection#size()
     */
    public int size()
    {
        return m_set.size();
    }


    /* (non-Javadoc)
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray()
    {
        return m_set.toArray();
    }


    /* (non-Javadoc)
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] objects)
    {
        return m_set.toArray( objects );
    }


    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "CreditMgtCustomerIDs {" );
        for ( Iterator i = m_set.iterator(); i.hasNext(); ) {
            CreditMgtCustomerID customerID = (CreditMgtCustomerID) i.next();
            buf.append( customerID.toString() );
        }
        buf.append( "} " );
        return buf.toString();
    }
}

