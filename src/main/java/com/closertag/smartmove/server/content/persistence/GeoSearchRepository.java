/**
 * 
 */
package com.closertag.smartmove.server.content.persistence;

import java.util.Locale;

import com.closertag.smartmove.server.content.domain.GeoSearch;

/**
 * @author mccalv
 * 
 */
public interface GeoSearchRepository {

	/**
	 * Returns the spefic {@link GeoSearch} objec for the given locale
	 * 
	 * @param query
	 * @param locale
	 * @return
	 */
	GeoSearch get(String query, Locale locale);

	/**
	 * Saves or updates a Search against the Db
	 * 
	 * @param geoSearch
	 */
	void saveOrUpdate(GeoSearch geoSearch);
}
