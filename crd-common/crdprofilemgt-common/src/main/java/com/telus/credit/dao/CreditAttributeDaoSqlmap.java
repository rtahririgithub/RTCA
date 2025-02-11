/**
 * 
 */
package com.telus.credit.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditIDCard;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;

/**
 * @author x122365
 *
 */
public class CreditAttributeDaoSqlmap extends AbstractSqlMapDao implements
		CreditAttributeDao {
	
	private static Log log = LogFactory.getLog(CreditAttributeDaoSqlmap.class);
	
	protected final static String STAT_ID_INSERT_CREDIT_ATTRIBUTE = "credit_attribute.insert_credit_attribute";
	
	protected final static String STAT_ID_GET_ATTRIBUTES_BY_CREDIT_PROFILE_ID = "credit_attribute.get_attributes_by_credit_profile_id";
	
	protected final static String STAT_ID_DELETE_ATTRIBUTE = "credit_attribute.delete_attribute";

	/* (non-Javadoc)
	 * @see com.telus.credit.dao.CreditAttributeDao#getCreditAttributes(java.lang.Long)
	 */
	@Override
	public List<CreditAttribute> getCreditAttributes(Long creditProfileId)
			throws ObjectNotFoundException {
		
		return queryForList( STAT_ID_GET_ATTRIBUTES_BY_CREDIT_PROFILE_ID, creditProfileId );
		
	}

	/* (non-Javadoc)
	 * @see com.telus.credit.dao.CreditAttributeDao#insertCreditAttribute(com.telus.credit.domain.CreditAttribute, java.lang.String)
	 */
	@Override
	public void insertCreditAttribute(CreditAttribute creditAttribute, String userId) 
			throws DuplicateKeyException {

		Map creditAttributeMap = new HashMap();
		creditAttributeMap.put( "credit_attribute", creditAttribute );
		creditAttributeMap.put( "userId", userId );
        insert( STAT_ID_INSERT_CREDIT_ATTRIBUTE, creditAttributeMap );

	}

	/* (non-Javadoc)
	 * @see com.telus.credit.dao.CreditAttributeDao#updateCreditAttribute(com.telus.credit.domain.CreditAttribute, java.lang.String)
	 */
	@Override
	public void updateCreditAttribute(CreditAttribute creditAttribute, String userId) 
	throws ConcurrencyConflictException 
			 {
		try {
			try {
				expireAttribute(creditAttribute, userId);
			} catch (ConcurrencyConflictException e) {
				if (e.getMessage().contains("No records updated for statement: credit_attribute.delete_attribute")) {
	        		// do nothing, it's a normal scenario
	        		log.info("Credit Attribute not existing for Credit Profile ID " + creditAttribute.getCreditProfileId());
	        	}
			}
			insertCreditAttribute(creditAttribute, userId);
		} catch(DuplicateKeyException e) {
			throw new ConcurrencyConflictException( e );
		}

	}
	
	private void expireAttribute(CreditAttribute creditAttribute, String userId)
    throws ConcurrencyConflictException
	{
		Map map = new HashMap();
		map.put( "credit_attribute", creditAttribute );
		map.put( "userId", userId );
		update( STAT_ID_DELETE_ATTRIBUTE, map );
	}

}
