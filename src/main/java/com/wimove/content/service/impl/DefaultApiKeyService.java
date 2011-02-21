/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 18 Dec 2009
 * mccalv
 * DefaultApiKeyService
 * 
 */
package com.wimove.content.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.wimove.content.exception.WimoveException;
import com.wimove.content.persistence.ApiKeyRepository;
import com.wimove.content.service.ApiKeyService;

/**
 * @author mccalv
 * 
 */
public class DefaultApiKeyService implements ApiKeyService {

	private ApiKeyRepository apiKeyRepository;

	@Transactional(readOnly = true)
	public void checkValidApiKey(String apiKey) {
		if (apiKeyRepository.getApiKey(apiKey)==null) {
			throw new WimoveException(
					WimoveException.ExceptionType.INVALID_API_KEY,
					"The api key " + apiKey + " is not valid");
		}

	}

	/**
	 * @param apiKeyRepository
	 *            the apiKeyRepository to set
	 */
	public void setApiKeyRepository(ApiKeyRepository apiKeyRepository) {
		this.apiKeyRepository = apiKeyRepository;
	}

}
