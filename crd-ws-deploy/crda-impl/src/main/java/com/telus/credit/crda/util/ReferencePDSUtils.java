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

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.erm.referenceods.domain.ReferenceMessageDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

import java.util.Collection;
import java.util.List;

public class ReferencePDSUtils {
    private static ReferencePDSUtils m_referenceODSUtils;

    private ReferencePDSUtils() {

    }

    public static ReferencePDSUtils instanceOf() {
        if (m_referenceODSUtils == null) {
            m_referenceODSUtils = new ReferencePDSUtils();
        }
        return m_referenceODSUtils;
    }


    public void validate(List<ReferencePDSValidateUnit> validationArray) throws EnterpriseCreditAssessmentPolicyException {
        for (ReferencePDSValidateUnit refUnit : validationArray) {
            validate(refUnit);
        }
    }

    public void validate(ReferencePDSValidateUnit refUnit)
            throws EnterpriseCreditAssessmentPolicyException {
        String codeValue = (
                refUnit == null ||
                        refUnit.getCodeValue() == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR : refUnit.getCodeValue().trim();

        if (EnterpriseCreditAssessmentConsts.EMPTY_STR.equalsIgnoreCase(codeValue) && refUnit.isRequired()) {
            throw new EnterpriseCreditAssessmentPolicyException("Attribute: " + refUnit.getFieldName() + " cannot be null.",
                    (refUnit.getExceptionCode() == null ? EnterpriseCreditAssessmentExceptionCodes.GENERAL_MISING_MANDATORY_FIELD_EXCEPTION : refUnit.getExceptionCode()));
        }
        if (!EnterpriseCreditAssessmentConsts.EMPTY_STR.equalsIgnoreCase(codeValue)) {
            if (!isValidCode(refUnit.getCodeValue(), refUnit.getRefTable())) {
                throw new EnterpriseCreditAssessmentPolicyException(
                        "Value: " + refUnit.getCodeValue()
                                + " does not exist in refTable:" + refUnit.getRefTable()
                                + " for attribute: " + refUnit.getFieldName(),
                        (refUnit.getExceptionCode() == null ? EnterpriseCreditAssessmentExceptionCodes.REFPDS_VALIDATION_FAILED_EXCEPTION : refUnit.getExceptionCode()));

            }
        }
    }

    /**
     * <p><b>Description</b> Validates code by looking it up in the codes table. </p>
     *
     * @param codeValue
     * @param category
     * @return true if the code is valid; false otherwise.
     */
    public boolean isValidCode(String codeValue, String category) {
        boolean foundMatch = false;

        Collection<ReferenceDecode> codeList = ReferencePdsAccess.getView(category, "EN");
        for (ReferenceDecode refDecode : codeList) {
            if (refDecode.getCode().equals(codeValue)) {
                foundMatch = true;
                break;
            }
        }
        return foundMatch;
    }

    public String getMessage(String messageId) {
        ReferenceMessageDecode refMsg = ReferencePdsAccess.getMessage(messageId, ReferencePdsAccess.LANG_EN);
        return (refMsg != null ? refMsg.getText() : null);
    }

    public Collection<ReferenceDecode> getValidCode( String category) {
    	return ReferencePdsAccess.getView(category, "EN");
    }
}
