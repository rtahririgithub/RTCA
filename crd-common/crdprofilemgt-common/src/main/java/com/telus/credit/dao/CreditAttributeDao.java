/**
 * 
 */
package com.telus.credit.dao;

import java.util.List;

import com.telus.credit.domain.CreditAttribute;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 * @author x122365
 *
 */
public interface CreditAttributeDao {
	
	public List<CreditAttribute> getCreditAttributes(Long creditProfileId) 
		throws ObjectNotFoundException;
	
	public void insertCreditAttribute(CreditAttribute creditAttribute, String userId)
		throws DuplicateKeyException;
	
	public void updateCreditAttribute(CreditAttribute creditAttribute, String userId)
		throws ConcurrencyConflictException;

}
