package com.telus.credit.crda.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.erm.referenceods.domain.ReferenceMessageDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

@RunWith(TelusJUnitClassRunner.class)
 
@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/test-telus-crd-crda-impl-spring.xml")
public class ReferencePDSUtilsTest {
 
	@Test 
	public void testReferencePDSUtils() throws EnterpriseCreditAssessmentPolicyException{
        try {
			List<ReferencePDSValidateUnit> validationArray = new ArrayList<ReferencePDSValidateUnit>();

			String creditAssessmentTypeCd = "FULL_ASSESSMENT";
			validationArray.add(new ReferencePDSValidateUnit(creditAssessmentTypeCd,
			        EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_TYP,
			        true,
			        "Credit Assessment Type",
			        EnterpriseCreditAssessmentExceptionCodes.ASMT_TYPE_VALIDATION_EXCEPTION));

			 ReferencePDSUtils.instanceOf().validate(validationArray);
			 
			 ReferenceMessageDecode refMsg = ReferencePdsAccess.getMessage("", ReferencePdsAccess.LANG_EN);
			 
			 
		} catch (Exception e) {
	 
			e.printStackTrace();
		}		
		
	}
	
	@Test 
	public void testgetMessagefromREFPD() throws EnterpriseCreditAssessmentPolicyException{
        try {
   
			 ReferenceMessageDecode refMsg = ReferencePdsAccess.getMessage("CRDA-TEXT-002", ReferencePdsAccess.LANG_EN);
			 System.out.println("CRDA-TEXT-002 text = " + refMsg.getText());
			  refMsg = ReferencePdsAccess.getMessage("CRDA-TEXT-003", ReferencePdsAccess.LANG_EN);
			 System.out.println("CRDA-TEXT-003 text = " + refMsg.getText());
			  refMsg = ReferencePdsAccess.getMessage("CRDA-TEXT-004", ReferencePdsAccess.LANG_EN);
			 System.out.println("CRDA-TEXT-004 text = " + refMsg.getText());
			  refMsg = ReferencePdsAccess.getMessage("CRDA-TEXT-005", ReferencePdsAccess.LANG_EN);
			// System.out.println("CRDA-TEXT-005 text = " + refMsg.getText());
			  refMsg = ReferencePdsAccess.getMessage("CRDA-TEXT-006", ReferencePdsAccess.LANG_EN);
			 //System.out.println("CRDA-TEXT-006 text = " + refMsg.getText());
			 
			 
			 
		} catch (Exception e) {
	 
			e.printStackTrace();
		}		
		
	}
}
