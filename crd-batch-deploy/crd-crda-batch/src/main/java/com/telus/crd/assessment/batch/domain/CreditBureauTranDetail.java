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

/**
 * <Replace this with a short description of the class.> 
 * 
 * @author x136675
 */
public class CreditBureauTranDetail
{
    private String m_carId;
    private String m_detailCd;
    private String m_code;
    private String m_val;
    
    public String getCarId()
    {
        return m_carId;
    }
    public void setCarId(String carId)
    {
        m_carId = carId;
    }
    public String getDetailCd()
    {
        return m_detailCd;
    }
    public void setDetailCd(String detailCd)
    {
        m_detailCd = detailCd;
    }

    public String getCode()
    {
        return m_code;
    }
    public void setCode(String code)
    {
        m_code = code;
    }
    public String getVal()
    {
        return m_val;
    }
    public void setVal(String value)
    {
        m_val = value;
    }
}
