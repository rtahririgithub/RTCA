package com.telus.crd.assessment.batch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.crd.assessment.batch.domain.AbstractBillingAccountRecord;
import com.telus.crd.assessment.batch.domain.AbstractCustomerBureauRecord;
import com.telus.crd.assessment.batch.domain.AbstractCustomerRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountAgencyRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountCollectionRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountDepositRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileFraudRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileRecord;
import com.telus.crd.assessment.batch.domain.CustomerRecord;
import com.telus.formletters.framework.batch.io.RecordCollector;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauDtlRecord;
                                    

final class MonthlyUpDownRecordCollector implements RecordCollector<AbstractCustomerRecord>
{
	private static final Log s_log = LogFactory.getLog(MonthlyUpDownRecordCollector.class);
	
    public Long customerId;
    public List<CustomerRecord> customers;
    public List<CustomerCreditBureauRecord> customerCreditBureaus;
    public List<CustomerCreditBureauDtlRecord> customerCreditBureausDtl;
    public List<CustomerCreditProfileRecord> customerCreditProfiles;
    public List<CustomerCreditProfileFraudRecord> customerCreditProfilesFraud;
    public List<BillingAccountRecord> billingAccounts;
    public List<BillingAccountAgencyRecord> billingAccountAgencies;
    public List<BillingAccountCollectionRecord> billingAccountCollections;
    public List<BillingAccountDepositRecord> billingAccountDeposits;
    public Set<Long> billingAccountNums;
    public Set<String> scoreNms;
    public Set<Integer> riskInds;
    public Set<String> fraudNms;
    public Set<String> prFraudCds;

    @Override
    public int collect(AbstractCustomerRecord obj)
    {
        return (int )(obj.getCustomerId() - customerId);
    }


    public void init()
    {
        billingAccountNums = new HashSet<Long>();
        addBillingAccountNums(billingAccountAgencies);        
        addBillingAccountNums(billingAccountCollections);        
        addBillingAccountNums(billingAccounts);
        addBillingAccountNums(billingAccountDeposits);
        
        scoreNms = new HashSet<String>();
        addScoreNms(customerCreditBureausDtl);
        riskInds = new HashSet<Integer>();
        addRiskInds(customerCreditBureausDtl);
        fraudNms = new HashSet<String>();
        addFraudNms(customerCreditBureausDtl);
        prFraudCds = new HashSet<String>();
        addPrFraudCds(customerCreditProfilesFraud);
    }


    private <T extends AbstractBillingAccountRecord> void addBillingAccountNums(List<T> list)
    {
        if( list != null )
        {
            for( T item : list )
            {
                billingAccountNums.add(item.getBillingAccountNum());
            }
        }
    }
    private <T extends CustomerCreditBureauDtlRecord> void addScoreNms(List<T> list)
    {
        if( list != null )
        {
            for( T item : list )
            {
                scoreNms.add(item.getScoreName());
            }
        }
    }
    private <T extends CustomerCreditBureauDtlRecord> void addRiskInds(List<T> list)
    {
        if( list != null )
        {
            for( T item : list )
            {
            	riskInds.add(item.getRiskIndType());
            }
        }
    }
    private <T extends CustomerCreditBureauDtlRecord> void addFraudNms(List<T> list)
    {
        if( list != null )
        {
            for( T item : list )
            {
                fraudNms.add(item.getFraudCd());
            }
        }
    }
    private <T extends CustomerCreditProfileFraudRecord> void addPrFraudCds(List<T> list)
    {
        if( list != null )
        {
            for( T item : list )
            {
            	prFraudCds.add(item.getFraudCd());
            }
        }
    }
}
