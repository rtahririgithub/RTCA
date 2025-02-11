package com.telus.credit.crda.domain.delegate.dcn;

import com.fico.telus.blaze.creditCommon.CreditAssessmentStrategies;
import com.fico.telus.blaze.creditCommon.UnifiedCreditAssessmentResult;

public class CreditAssessmentResultWrapper extends com.telus.credit.domain.common.CreditAssessmentResult{

	protected CreditAssessmentStrategies  m_creditAssessmentStrategies;
	protected UnifiedCreditAssessmentResult m_UnifiedCreditAssessmentResult;
	
	public UnifiedCreditAssessmentResult getUnifiedCreditAssessmentResultCopy() {
		return m_UnifiedCreditAssessmentResult;
	}

	public void setUnifiedCreditAssessmentResultCopy(UnifiedCreditAssessmentResult aUnifiedCreditAssessmentResult) {
		this.m_UnifiedCreditAssessmentResult = aUnifiedCreditAssessmentResult;
	}

	public CreditAssessmentStrategies getCreditAssessmentStrategies() {
		return m_creditAssessmentStrategies;
	}

	public void setCreditAssessmentStrategies(
			CreditAssessmentStrategies mCreditAssessmentStrategies) {
		m_creditAssessmentStrategies = mCreditAssessmentStrategies;
	}
   
	
}
