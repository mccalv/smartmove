/**
 * 
 */
package com.wimove.content.service;

import java.util.Locale;

import com.wimove.content.domain.GpsPosition;

/**
 * @author mccalv
 * 
 */
public interface GeocodingService {
	/**
	 * Get an input string and return a specific output
	 * 
	 * @param query
	 * @return
	 */
	String getQueryOutput(String query, Locale locale) throws Exception ;
	
	
	/**
	 * 
	 * @param query
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	GpsPosition getGpsPosition (String query, Locale locale)throws Exception ;
	

}
