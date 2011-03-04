/**
 * 
 */
package com.closertag.smartmove.server.content.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.persistence.filter.SearchFilter;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 * 
 */
public interface ItemRepository {

	/**
	 * Returns a list of {@link Item} around a specific {@link GpsPosition}
	 * 
	 * @param gpsPosition
	 * @param radius
	 * @return
	 */
	List<Item> getPoisAroundLocation(Point point, double radius);

	/**
	 * Returns a list of {@link Item} around a specific {@link GpsPosition}
	 * 
	 * @param gpsPosition
	 * @param radius
	 * @return
	 */
	List<Item> getItemsByCriteria(SearchFilter searchFilter);
	
	
	int removeItemFromGid(String gid);	
	
	
	int removeItemFromCategory(String category);	
	
	/**
	 * Saves a single Item
	 */
	void saveOrUpdate(Item item);

	/**
	 * Returns the single Item by its database identifier or legacy identifier
	 * 
	 * @param id
	 * @return
	 */
	Item get(Serializable id);

	/**
	 * Returns a single item for its {@link LocalizedItem.Label.Title} in the
	 * given {@link Locale}
	 * 
	 * @param itemName
	 * @param locale
	 * @return
	 */
	Item getByName(String itemName, Locale locale);

}
