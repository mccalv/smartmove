/**
 * 
 */
package com.closertag.smartmove.server.content.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.closertag.smartmove.server.content.domain.ItemList;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 * 
 */
public interface ListRepository {

	/**
	 * Returns the single ItemList by its identifier by its id or its string
	 * identifier
	 * 
	 * @param identifier
	 * @return
	 */
	ItemList get(Serializable identifier);

	
	/**
	 * Returns all the list present on the database
	 * @return
	 */
	List<ItemList> getAll(Locale locale);
	
	
	
	/**
	 * Returns all the list present for a given geometry0
	 * @return
	 */
	List<ItemList> getListWithin(Point geometry);
	
	
	
}
