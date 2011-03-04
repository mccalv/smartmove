/**
 * 
 */
package com.closertag.smartmove.server.content.service;

import java.io.File;
import java.util.List;

import com.closertag.smartmove.server.content.domain.Item;

/**
 * @author mccalv
 * 
 */
public interface ParsingService {

	/**
	 * Import a single file from a locatoion and persists it to the database;
	 * 
	 * @param file
	 */
	List<Item> importXmlFile(File file) throws Exception;

	/**
	 * Imports all the contents from a Csv file formatted in the following
	 * format.
	 * <p>
	 * "ID","CATEGORY","TITLE","TITLE_EN","DESCRIPTION_IT","DESCRIPTION_EN",
	 * "LAT (WGS84)","LON (WGS84)","ADDRESS","MP3_FILE_IT","MP3_FILE_EN"
	 * <p>
	 * Items are therefore persisted on the platform
	 * 
	 * @param file
	 * @throws Exception
	 */
	List<Item> importCsvFile(File file) throws Exception;

}
