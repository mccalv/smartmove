/**
 * 
 */
package com.closertag.smartmove.server.content.persistence;

import java.util.List;

import com.closertag.smartmove.server.content.domain.Category;

/**
 * @author mccalv
 * 
 */
public interface CategoryRepository {

	/**
	 *Returns a single category by its identifier
	 * 
	 * @param identifier
	 * @return
	 */
	Category get(String identifier);

	/**
	 * Returns the complete list of the categories present on the database
	 * 
	 * @return
	 */
	List<Category> getCategories();
}
