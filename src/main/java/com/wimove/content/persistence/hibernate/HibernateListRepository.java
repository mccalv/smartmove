/**
 * 
 */
package com.wimove.content.persistence.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

import com.vividsolutions.jts.geom.Point;
import com.wimove.content.domain.ItemList;
import com.wimove.content.domain.LocalizedItem.Label;
import com.wimove.content.hibernate.ContainsRestriction;
import com.wimove.content.persistence.ListRepository;

/**
 * @author mccalv
 * 
 */
public class HibernateListRepository extends HibernateBaseRepository implements
		ListRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.ListRepository#get(java.io.Serializable)
	 */
	@Override
	public ItemList get(Serializable identifier) {
		return (ItemList) sessionFactory.getCurrentSession().createCriteria(
				ItemList.class).add(
				Restrictions.or(Restrictions.eq("id", identifier), Restrictions
						.eq("name", identifier))).uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.persistence.ListRepository#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemList> getAll(Locale locale) {
		List<ItemList> itemLists = sessionFactory.getCurrentSession()
				.createCriteria(ItemList.class)
				
				//.createAlias("gidZones", "gidZones")
				
				/*
				.createAlias("LocalizedLists",
						"LocalizedLists",JoinFragment.INNER_JOIN).add(
						Restrictions.eq("LocalizedLists.label", Label.Title))
				.add(Restrictions.eq("LocalizedLists.locale", locale))
				.addOrder(Order.asc("LocalizedLists.value"))
				*/
				.list();

		if (itemLists.isEmpty())
			return null;

		for (ItemList itemList : itemLists) {

			//initializeFullDependencies(itemList);
		}
		return itemLists;
	}

	@SuppressWarnings("unchecked")
	public List<ItemList> getListWithin(Point point) {
		String query = "select i,g from ItemList i inner join i.gidZones g ";

		query += "WHERE st_contains(g.polygon, PointFromText('POINT("
				+ point.getX() + " " + point.getY() + ")',4326))  ";
		
		
		
		return sessionFactory
				.getCurrentSession()
				.createCriteria(ItemList.class)
				.createAlias("gidZones", "gidZones")
				.add(
						new ContainsRestriction(point, "gidZones")).list();
					/*
						Restrictions
								.sqlRestriction(
										"st_contains(polygon, PointFromText('POINT(? ?)',4326)) ",
										new Double[] { point.getX(),
												point.getY() },
										new org.hibernate.type.Type[] {
												Hibernate.DOUBLE,
							Hibernate.DOUBLE }))

				// .createQuery(query)
				.list();
				*/

	}

	/**
	 * Initializes all the item depencies.Delegates to Hiberante to fetch the
	 * children
	 * 
	 * @param item
	 */
	private void initializeFullDependencies(ItemList itemList) {
		Hibernate.initialize(itemList.getLocalizedLists());

	}

}
