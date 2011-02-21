/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 27 Nov 2009
 * mccalv
 * ApiKeyRepository
 * 
 */
package com.wimove.content.persistence;

import com.wimove.content.domain.ApiKey;

/**
 * @author mccalv
 *
 */
public interface ApiKeyRepository {

	/**
	 * 
	 * @param apiKey
	 */
	void saveOrUpdate(ApiKey apiKey);
	
	
	/**
	 * 
	 * @param hash
	 * @return
	 */
	ApiKey getApiKey(String hash);


}
