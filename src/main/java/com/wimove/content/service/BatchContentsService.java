/**
 * 
 */
package com.wimove.content.service;


/**
 * @author mccalv
 *
 */
public interface BatchContentsService {

	
	
	/**
	 * Import all the content on a given folder 
	 */
	void importAll();
	
	/**
	 * Method dedicated to import just the CSV Content
	 */
	void importCsvContents();
	
	/**
	 * Removes all the content not present anymore on the database; 
	 */
	int removeExpiredItems();
	/**
	 * Invokes the TTS service for a given list number of items and created the relative tts file 
	 * @param items
	 * @param locale
	 */
	void createMp3(int howmany);
	
}
