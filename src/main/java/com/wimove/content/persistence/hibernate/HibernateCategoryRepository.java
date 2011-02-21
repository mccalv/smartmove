/**
 * 
 */
package com.wimove.content.persistence.hibernate;

import java.util.List;

import org.hibernate.criterion.Order;

import com.wimove.content.domain.Category;
import com.wimove.content.persistence.CategoryRepository;

/**
 * @author mccalv
 * 
 */
public class HibernateCategoryRepository extends HibernateBaseRepository
		implements CategoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.CategoryRepository#get(java.lang.String)
	 */
	@Override
	public Category get(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.persistence.CategoryRepository#getCategories()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getCategories() {
		return sessionFactory.getCurrentSession().createCriteria(Category.class).addOrder(Order.asc("category")).list();
	}

}
