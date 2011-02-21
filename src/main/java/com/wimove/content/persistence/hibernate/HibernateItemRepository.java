/**
 * 
 */
package com.wimove.content.persistence.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.vividsolutions.jts.geom.Point;
import com.wimove.content.domain.Item;
import com.wimove.content.domain.LocalizedItem.Label;
import com.wimove.content.hibernate.DistanceRestriction;
import com.wimove.content.persistence.ItemRepository;
import com.wimove.content.persistence.filter.SearchFilter;
import com.wimove.content.persistence.filter.SearchFilterCriteriaBuilder;

/**
 * @author mccalv
 * 
 */
public class HibernateItemRepository extends HibernateBaseRepository implements
		ItemRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.ItemRepository#getPoisAroundLocation(com
	 * .wimove.content.domain.GpsPosition)
	 */
	@SuppressWarnings("unchecked")
	public List<Item> getPoisAroundLocation(Point point, double radius) {
		Criteria testCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Item.class);

		testCriteria.createAlias("gpsPositions", "gpsPositions").add(
				new DistanceRestriction(point, "gpsPositions", radius));

		return setFetchMode(testCriteria).list();
	}

	private Criteria setFetchMode(Criteria criteria) {
		//criteria.setFetchMode("gpsPositions", FetchMode.JOIN).setFetchMode(
		//"localizedItems", FetchMode.);
		criteria.setCacheable(true);
		
		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.ItemRepository#saveOrUpdate(com.wimove
	 * .content.domain.Item)
	 */
	public void saveOrUpdate(Item item) {
		sessionFactory.getCurrentSession().merge(item);
	}

	public Item get(Serializable id) {

		Criteria createCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Item.class);
		if (id instanceof String) {
			createCriteria.add(Restrictions.eq("itemId", id));

		}

		if (id instanceof Long) {
			createCriteria.add(Restrictions.eq("id", id));

		}
		Item item = (Item) setFetchMode(createCriteria).uniqueResult();

		/*
		 * Initialized the object dependencies
		 */
		if (item == null)
			return item;

		initializeFullDependencies(item);
		return item;
	}

	/**
	 * Initializes all the item depencies
	 * 
	 * @param item
	 */
	private void initializeFullDependencies(Item item) {

		Hibernate.initialize(item.getContacts());
		Hibernate.initialize(item.getCosts());
		Hibernate.initialize(item.getExtras());
		Hibernate.initialize(item.getLocalizedItems());
		Hibernate.initialize(item.getGpsPositions());
		Hibernate.initialize(item.getTimeOccurrences());
	}

	@SuppressWarnings("unchecked")
	public Item getByName(String itemName, Locale locale) {

		List<Item> items = setFetchMode(
				sessionFactory.getCurrentSession().createCriteria(Item.class)
						.createAlias("localizedItems", "localizedItems").add(
								Restrictions.eq("localizedItems.label",
										Label.Title)).add(
								Restrictions
										.eq("localizedItems.locale", locale))
						.add(
								Restrictions.ilike("localizedItems.value",
										itemName))).list();

		if (items.isEmpty())
			return null;
		Item item = items.get(0);
		initializeFullDependencies(item);
		return item;
	}

	
	public List<Item> getItemsByCriteria(SearchFilter searchFilter) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Item.class,"item");
		return 
				SearchFilterCriteriaBuilder.buildContentCriteria(criteria,
						searchFilter);
	}

	public int removeItemFromGid(String gid) {

		String hql = "delete from Item where gid.identifier = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("name", gid);
		return query.executeUpdate();

	}

	/* (non-Javadoc)
	 * @see com.wimove.content.persistence.ItemRepository#removeItemFromCategory(java.lang.String)
	 */
	
	public int removeItemFromCategory(String category) {
		String hql = "delete from Item where category.category = :category";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("category", category);
		return query.executeUpdate();
	}
}
