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

package com.telus.credit.crda.util;

public class ReferencePDSValidateUnit {
    private String m_exceptionCode;

    private String m_fieldName;
    private String m_codeValue;
    private String m_refTable;
    private boolean m_required;

    public ReferencePDSValidateUnit(String codeValue, String refTable, boolean required, String fieldName, String exceptionCode) {
        m_fieldName = fieldName;
        m_codeValue = codeValue;
        m_refTable = refTable;
        m_required = required;
        m_exceptionCode = exceptionCode;
    }

    /**
     * @return Returns the m_exceptionCode.
     */
    public String getExceptionCode() {
        return m_exceptionCode;
    }

    /**
     * @param m_exceptionCode The m_exceptionCode to set.
     */
    public void setExceptionCode(String exceptionCode) {
        this.m_exceptionCode = exceptionCode;
    }

    /**
     * @return Returns the m_fieldName.
     */
    public String getFieldName() {
        return m_fieldName;
    }

    /**
     * @param m_fieldName The m_fieldName to set.
     */
    public void setFieldName(String fieldName) {
        this.m_fieldName = fieldName;
    }

    /**
     * @return Returns the m_codeValue.
     */
    public String getCodeValue() {
        return m_codeValue;
    }

    /**
     * @param m_codeValue The m_codeValue to set.
     */
    public void setCodeValue(String codeValue) {
        this.m_codeValue = codeValue;
    }

    /**
     * @return Returns the m_refTable.
     */
    public String getRefTable() {
        return m_refTable;
    }

    /**
     * @param m_refTable The m_refTable to set.
     */
    public void setRefTable(String refTable) {
        this.m_refTable = refTable;
    }

    /**
     * @return Returns the m_required.
     */
    public boolean isRequired() {
        return m_required;
    }

    /**
     * @param m_required The m_required to set.
     */
    public void setRequired(boolean required) {
        this.m_required = required;
    }

}
