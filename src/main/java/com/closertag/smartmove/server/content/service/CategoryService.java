/**
 * 
 */
package com.closertag.smartmove.server.content.service;

import java.util.List;

import com.closertag.smartmove.server.content.domain.Category;

/**
 * @author mccalv
 * 
 */
public interface CategoryService {

	/**
	 * Return
	 * 
	 * @return
	 */
	List<Category> getCategories();
}
