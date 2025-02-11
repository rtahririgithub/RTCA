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

package com.telus.crd.assessment.batch.domain;

public class OrderProduct
{
    private String m_orderDepId;
    private String m_serviceTypCd;
    private String m_reqRentedEquipCount;
    private String m_purchasedCount;
    private String m_rentedEquipCount;
    
    public String getOrderDepId()
    {
        return m_orderDepId;
    }
    public void setOrderDepId(String orderDepId)
    {
        m_orderDepId = orderDepId;
    }
    public String getServiceTypCd()
    {
        return m_serviceTypCd;
    }
    public void setServiceTypCd(String serviceTypCd)
    {
        m_serviceTypCd = serviceTypCd;
    }
    public String getReqRentedEquipCount()
    {
        return m_reqRentedEquipCount;
    }
    public void setReqRentedEquipCount(String reqRentedEquipCount)
    {
        m_reqRentedEquipCount = reqRentedEquipCount;
    }
    public String getPurchasedCount()
    {
        return m_purchasedCount;
    }
    public void setPurchasedCount(String purchasedCount)
    {
        m_purchasedCount = purchasedCount;
    }
    public String getRentedEquipCount()
    {
        return m_rentedEquipCount;
    }
    public void setRentedEquipCount(String rentedEquipCount)
    {
        m_rentedEquipCount = rentedEquipCount;
    }
    
    
}
