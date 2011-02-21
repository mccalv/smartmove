/**
 * 
 */
package com.wimove.content.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.wimove.content.domain.Item;

/**
 * @author mccalv
 *
 */
public interface BatchRepository {
	
	/**
	 * Updates a list of item via JDBC
	 */
	void updateItems(List<Item> items,Locale locale);
	
	
	/**
	 * Removes the items no longer active on the database, i.e: items whit at least one Time
	 */
	int removeExpiredItems();


	List<HashMap<String,String>> getItemsWithoutLocale(int howmany);
	
	
	
	int removeItemsFromCategory(String category);
	
	
	//List<Item>

}
