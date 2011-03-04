/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 18 Dec 2009
 * mccalv
 * ApiKeyService
 * 
 */
package com.closertag.smartmove.server.content.service;

import javax.servlet.http.HttpServletRequest;

import com.closertag.smartmove.server.content.exception.WimoveException;

/**
 * @author mccalv
 * 
 */
public interface ApiKeyService {

	/**
	 * Checks is the passed key exists. Otherwise, throws a
	 * {@link WimoveException}
	 * 
	 * @param apiKey
	 */
	void checkValidApiKey(HttpServletRequest req,String apiKey);
}
