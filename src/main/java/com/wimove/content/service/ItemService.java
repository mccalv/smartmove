/**
 * 
 */
package com.wimove.content.service;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.wimove.content.domain.GpsPosition;
import com.wimove.content.domain.Item;
import com.wimove.content.persistence.filter.SearchFilter;

/**
 * The business logic to hadle the {@link Item}s
 * 
 * @author mccalv
 * 
 */
public interface ItemService {

	/**
	 * Returns the single item by its id
	 * 
	 * @param id
	 * @param locale
	 * @return
	 */
	Item get(Serializable id, Locale locale);
	
	
	void deleteItemsByGid(String GID);

	/**
	 * Persists a single Item
	 * 
	 * @param item
	 */
	void saveOrUpdate(Item item);

	/**
	 * Return a single item by its name
	 * 
	 * @param itemName
	 * @param locale
	 * @return
	 */
	Item getItemByName(String itemName, Locale locale);

	/**
	 * Returns a list of {@link Item} around a specific {@link GpsPosition}
	 * 
	 * @param gpsPosition
	 * @param radius
	 * @return
	 */
	List<Item> getPoisAroundLocation(Float lat, Float Long, double radius,
			Locale locale);

	/**
	 * 
	 * @param searchFilter
	 * @return
	 */
	List<Item> getItemsByCriteria(SearchFilter searchFilter);

	
}
