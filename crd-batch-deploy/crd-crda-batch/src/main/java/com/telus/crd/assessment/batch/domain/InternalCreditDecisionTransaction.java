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

public class InternalCreditDecisionTransaction
{
    private String m_carId;
    private String m_creditValCd;
    private String m_decisionCd;
    private String m_productCatBoltOn;
    private String m_assessmentMsgCd;
    private String m_resultDetailCd;
    private String m_prodQualInd;
    private String m_credApprovedProdCatCd;
    private String m_fraudMsgCd;
    
    public InternalCreditDecisionTransaction(){
        m_carId = "";
        m_creditValCd = "";
        m_decisionCd = "";
        m_productCatBoltOn = "";
        m_assessmentMsgCd = "";
        m_resultDetailCd = "";
        m_prodQualInd = "";
        m_credApprovedProdCatCd = "";
        m_fraudMsgCd = "";
    }
    
    public String getCarId()
    {
        return m_carId;
    }


    public void setCarId(String carId)
    {
        m_carId = carId;
    }


    public String getCreditValCd()
    {
        return m_creditValCd;
    }


    public void setCreditValCd(String creditValCd)
    {
        m_creditValCd = creditValCd;
    }


    public String getDecisionCd()
    {
        return m_decisionCd;
    }


    public void setDecisionCd(String decisionCd)
    {
        m_decisionCd = decisionCd;
    }


    public String getProductCatBoltOn()
    {
        return m_productCatBoltOn;
    }


    public void setProductCatBoltOn(String productCatBoltOn)
    {
        m_productCatBoltOn = productCatBoltOn;
    }


    public String getAssessmentMsgCd()
    {
        return m_assessmentMsgCd;
    }


    public void setAssessmentMsgCd(String assessmentMsgCd)
    {
        m_assessmentMsgCd = assessmentMsgCd;
    }


    public String getResultDetailCd()
    {
        return m_resultDetailCd;
    }


    public void setResultDetailCd(String resultDetailCd)
    {
        m_resultDetailCd = resultDetailCd;
    }


    public String getProdQualInd()
    {
        return m_prodQualInd;
    }


    public void setProdQualInd(String prodQualInd)
    {
        m_prodQualInd = prodQualInd;
    }


    public String getCredApprovedProdCatCd()
    {
        return m_credApprovedProdCatCd;
    }


    public void setCredApprovedProdCatCd(String credApprovedProdCatCd)
    {
        m_credApprovedProdCatCd = credApprovedProdCatCd;
    }

    public String getFraudMsgCd()
    {
        return m_fraudMsgCd;
    }

    public void setFraudMsgCd(String fraudMsgCd)
    {
        m_fraudMsgCd = fraudMsgCd;
    }
    
    
}
