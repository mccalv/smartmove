/**
 * 
 */
package com.closertag.smartmove.server.content.persistence;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.closertag.smartmove.server.content.domain.Item;

/**
 * A repository for batch inserts and updates
 * 
 * @author mccalv
 * 
 */
public interface BatchRepository {

	/**
	 * Updates a list of item via JDBC
	 */
	void updateItems(List<Item> items, Locale locale);

	/**
	 * Removes the items no longer active on the database, i.e: items whit at
	 * least one Time
	 */
	int removeExpiredItems();

	List<Map<String, Object>> getItemsWithoutLocale(int howmany);

	int removeItemsFromCategory(String category);

}
