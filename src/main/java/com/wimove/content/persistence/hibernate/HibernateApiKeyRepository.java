/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 27 Nov 2009
 * mccalv
 * HibernateApiKeyRepository
 * 
 */
package com.wimove.content.persistence.hibernate;

import com.wimove.content.domain.ApiKey;
import com.wimove.content.persistence.ApiKeyRepository;

/**
 * @author mccalv
 * 
 */
public class HibernateApiKeyRepository extends HibernateBaseRepository
		implements ApiKeyRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.ApiKeyRepository#getApiKey(java.lang.String
	 * )
	 */

	
	public ApiKey getApiKey(String hash) {
		return (ApiKey) sessionFactory.getCurrentSession().get(ApiKey.class,
				hash);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.ApiKeyRepository#saveOrUpdate(com.wimove
	 * .content.domain.ApiKey)
	 */
	@Override
	public void saveOrUpdate(ApiKey apiKey) {
		// TODO Auto-generated method stub

	}

}
