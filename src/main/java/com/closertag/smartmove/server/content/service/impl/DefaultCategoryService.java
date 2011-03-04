/**
 * 
 */
package com.closertag.smartmove.server.content.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.persistence.CategoryRepository;
import com.closertag.smartmove.server.content.service.CategoryService;

/**
 * @author mccalv
 * 
 */
public class DefaultCategoryService implements CategoryService {

	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<Category> getCategories() {
		return categoryRepository.getCategories();
	}

	/**
	 * @param categoryRepository
	 *            the categoryRepository to set
	 */
	public void setCategoryRepository(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

}
