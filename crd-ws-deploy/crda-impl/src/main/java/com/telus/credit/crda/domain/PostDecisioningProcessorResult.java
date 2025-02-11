package com.telus.credit.crda.domain;

public class PostDecisioningProcessorResult {
    long m_CRAID;
    String m_PostDecisioningProcessorResultCd;

    public long getCRAID() {
        return m_CRAID;
    }

    public void setCRAID(long mCRAID) {
        m_CRAID = mCRAID;
    }

    public String getPostDecisioningProcessorResultCd() {
        return m_PostDecisioningProcessorResultCd;
    }

    public void setPostDecisioningProcessorResultCd(
            String mPostDecisioningProcessorResultCd) {
        m_PostDecisioningProcessorResultCd = mPostDecisioningProcessorResultCd;
    }


}
