/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 18 Dec 2009
 * mccalv
 * DefaultApiKeyService
 * 
 */
package com.closertag.smartmove.server.content.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import com.closertag.smartmove.server.content.exception.WimoveException;
import com.closertag.smartmove.server.content.persistence.ApiKeyRepository;
import com.closertag.smartmove.server.content.service.ApiKeyService;

/**
 * @author mccalv
 * 
 */
public class DefaultApiKeyService implements ApiKeyService {

	private ApiKeyRepository apiKeyRepository;

	private static String SERVER_TOKEN = "SMARTMOVE_SERVER_25_05_2010_er312";

	private String generateHash(String key) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			key += SERVER_TOKEN;
			// start fresh
			md.reset();
			md.update(key.getBytes());
			byte[] bytes = md.digest();
			// buffer to write the md5 hash to
			StringBuffer buff = new StringBuffer();
			for (int l = 0; l < bytes.length; l++) {
				String hx = Integer.toHexString(0xFF & bytes[l]);
				// make sure the hex string is correct if 1 character
				if (hx.length() == 1)
					buff.append("0");
				buff.append(hx);
			}
			return buff.toString().trim();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(readOnly = true)
	public void checkValidApiKey(HttpServletRequest req, String apiKey) {

		String referer = req.getHeader("Referer");
		if (referer != null) {
			// check if it's a relative URL and make sure we end with a slash
			if (!referer.startsWith("http") && !referer.endsWith("/"))
				referer = referer + "/";
			// get the key for the host used by the request
			String refererKey = generateHash(referer + SERVER_TOKEN);
		}

		if (apiKeyRepository.getApiKey(apiKey) == null) {
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
