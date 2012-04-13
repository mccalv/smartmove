/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 17 Dec 2009
 * mccalv
 * SearchFilterCriteriaBuilder
 * 
 */
package com.closertag.smartmove.server.content.persistence.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.persistence.hibernate.DistanceOrder;
import com.closertag.smartmove.server.content.persistence.hibernate.DistanceProjection;
import com.closertag.smartmove.server.content.persistence.hibernate.DistanceRestriction;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 * 
 */
public class SearchFilterCriteriaBuilder {

	private static final String GPS_POSITIONS_ALIAS = "gpsPositions";

	/**
	 * Builds the HibernateCriteria from a SearchFilter. Don't apply projections
	 * to this list, since they are cleaned in this method call
	 * 
	 * TODO: Remove unused filters
	 * 
	 * @param searchFilter
	 * @return
	 */

	/*
	 * String query = "select cat.name, count(image) " +
	 * "from Category cat, Image image " + "join cat.collection.items item " +
	 * "where item = image.item " + "group by cat.name";
	 */

	@SuppressWarnings("unchecked")
	public static List<Item> buildContentCriteria(Criteria criteria,
			SearchFilter searchFilter) {
		criteria.createAlias("localizedItems", "localizedItems",
				JoinFragment.INNER_JOIN);

		criteria.add(Restrictions.and(Restrictions.eq(

		"localizedItems.locale", searchFilter.getLocale()), Restrictions.eq(
				"localizedItems.label", LocalizedItem.Label.Title)));

		// criteria.createAlias("localizedItems",
		// "description",Criteria.INNER_JOIN);
		/*
		 * criteria.add(Restrictions.and(Restrictions.eq(
		 * "localizedItems.locale",searchFilter.getLocale()), Restrictions.eq(
		 * "localizedItems.label",LocalizedItem.Label.Description.name()) ));
		 */

		criteria.createAlias("gpsPositions", GPS_POSITIONS_ALIAS,
				JoinFragment.INNER_JOIN);

		if (searchFilter.getCategory() != null) {
			criteria.createAlias("category", "category").add(
					Restrictions.eq("category.category", searchFilter
							.getCategory().getCategory()));
		}
		if (searchFilter.getCategory() == null) {
			criteria.createAlias("category", "category")
			
			
			.add(
					Restrictions.not(
						     // replace "id" below with property name, depending on what you're filtering against
						    Restrictions.in("category.category", new String[] {"HOT_SPOT", "BUS_STOP"})
						  ));
					
					
					//Restrictions.ne("category.category", "HOT_SPOT"));
		}
		if (searchFilter.getRadius() != null && searchFilter.getPoint() != null) {
			criteria.add(new DistanceRestriction(searchFilter.getPoint(),
					GPS_POSITIONS_ALIAS, searchFilter.getRadius()));
		}
		if (searchFilter.getPolygon() != null) {
			criteria.add(SpatialRestrictions.within("gpsPositions.geom_point",
					searchFilter.getPolygon()));
		}
		if (searchFilter.getText() != null) {

			criteria.add(Restrictions.ilike("localizedItems" + ".value", "%"
					+ searchFilter.getText() + "%"));
		}
		if (searchFilter.getGidIdentifier() != null) {
			criteria.createAlias("gid", "gid", JoinFragment.INNER_JOIN).add(
					Restrictions.in("gid.identifier", Arrays.asList(StringUtils
							.split(searchFilter.getGidIdentifier(), ","))));
		}
		if (searchFilter.getTags() != null) {
			criteria.createAlias("tags", "tags").add(
					Restrictions.in("tags.tag", Arrays.asList(searchFilter
							.getTags())));
		}

		if (searchFilter.getListIdentifier() != null) {
			criteria.createAlias("itemList", "itemList").add(
					Restrictions.eq("itemList.name", searchFilter
							.getListIdentifier()));

		}

		if (searchFilter.getStartDate() != null) {
			criteria.createAlias("timeOccurrences", "timeOccurrences",
					JoinFragment.INNER_JOIN).add(
					Restrictions.and(Restrictions.ge(
							"timeOccurrences.startDate", searchFilter
								
							
							.getStartDate()), Restrictions.le(
							
											
											"timeOccurrences.endDate", searchFilter
									.getEndDate())));
			// Needed b.o. left join

		}
		/*
		 * if (searchFilter.getStartDate() == null && searchFilter.getEndDate()
		 * == null) {
		 * 
		 * // This in case the item doesn't have a starting date
		 * criteria.createAlias("timeOccurrences", "timeOccurrences",
		 * Criteria.INNER_JOIN).add( Restrictions.or(Restrictions
		 * .isNull("timeOccurrences.startDate"),Restrictions.or(Restrictions
		 * .isNotNull("timeOccurrences.startDate"), Restrictions
		 * .le("timeOccurrences.endDate", new Date())))); // A left ojoin is
		 * needed here
		 * 
		 * }
		 */
		Point isPointSelected = null;
		if (searchFilter.getOrderBy() != null) {
			switch (searchFilter.getOrderBy()) {
			case CREATION_DATA: {
				criteria.addOrder(Order.desc("creationDate"));
				break;
			}
			case UPDATE_DATA: {
				criteria.addOrder(Order.desc("updateDate"));
				break;
			}

			case PROXIMITY: {
				criteria.addOrder(new DistanceOrder(searchFilter.getPoint(),
						true, GPS_POSITIONS_ALIAS));
				isPointSelected = searchFilter.getPoint();
				break;
			}
			case TITLE: {
				// criteria.createAlias("localizedItems", "localizedItems");
				// criteria.addOrder(Order.asc("localizedItems" + ".locale"));
				// criteria.addOrder(Order.desc("localizedItems" + ".label"));
				criteria.addOrder(Order.asc("localizedItems" + ".value"));
				// criteria.add(Projections.groupProperty("itemId"));
				break;
			}

			}
		}

		if (searchFilter.getPoint() != null) {
			criteria.addOrder(new DistanceOrder(searchFilter.getPoint(), true,
					GPS_POSITIONS_ALIAS));
			isPointSelected = searchFilter.getPoint();
		}
		/*
		 * In this case it goes to by the centroid
		 */
		/*
		if (searchFilter.getPolygon() != null) {
			criteria.addOrder(new DistanceOrder(searchFilter.getPolygon()
					.getCentroid(), true, GPS_POSITIONS_ALIAS));
			isPointSelected = searchFilter.getPolygon().getCentroid();
		}
		*/
		if (searchFilter.getLimit() == null) {
			criteria
					.setMaxResults(com.closertag.smartmove.server.constant.Constants.MAX_RESULT_PER_PAGE);
		} else {
			criteria.setMaxResults(searchFilter.getLimit());

		}
		if (searchFilter.getStart() != null) {
			criteria.setFirstResult(searchFilter.getStart());
		}

		ProjectionList proList = Projections.projectionList();
		proList.add(Projections.distinct(Projections.property("itemId")));
		proList.add(Projections.property("itemId"), "itemId");
		//proList.add(Projections.property("category"), "category");
		//proList.add(Projections.property("website"));
		
		proList.add(Projections.property("localizedItems.value"), "title");
		proList.add(Projections.property(GPS_POSITIONS_ALIAS + ".latitude"),
				"lat");
		proList.add(Projections.property(GPS_POSITIONS_ALIAS + ".longitude"),
				"lon");
		proList.add(Projections.property(GPS_POSITIONS_ALIAS + ".address"),
				"address");
		proList.add(Projections.property(GPS_POSITIONS_ALIAS + ".geom_point"),
				"address");

		/*
		if (isPointSelected != null) {
			proList.add(new DistanceProjection(isPointSelected,
					GPS_POSITIONS_ALIAS + ".geom_point"));
		}
		*/

		// proList.add(Projections.property("itemId"), "itemId");
		// proList.add(Projections.alias("category"),"category");
		// proList.add(Projections.property("timeOccurrences"),
		// "timeOccurrences");
		// proList.add(Projections.property("gpsPositions"), "gpsPositions");
		// proList.add(Projections.property("localizedItems"),
		// "localizedItems");
		// proList.add(Projections.groupProperty("id"));

		criteria.setProjection(proList);

		// criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		// criteria
		// .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

		criteria.setCacheable(true);
		// criteria.setC

		List list = criteria.list();
		if (list != null) {
			List<Object[]> results = list;
			List<Item> items = new ArrayList<Item>();
			for (Object[] row : results) {
				Item itemDetached = new Item();
				itemDetached.setItemId((String) row[0]);

				GpsPosition gpsPositions = new GpsPosition();
				gpsPositions.setAddress((String) row[5]);
				gpsPositions.setLatitude((Float) row[3]);
				gpsPositions.setLongitude((Float) row[4]);
				if (isPointSelected != null) {
					gpsPositions.setGeom_point((Point) row[6]);
				}
				itemDetached.getGpsPositions().add(gpsPositions);
				LocalizedItem localizedItem = new LocalizedItem();
				localizedItem.setLabel(Label.Title);
				localizedItem.setLocale(searchFilter.getLocale());
				localizedItem.setValue((String) row[2]);
				itemDetached.getLocalizedItems().add(localizedItem);
				// if(isPointSelected!=null ){
				// itemDetached.setDistance(new
				// Float(GeoLocHelper.calculateMtDistance((Point)
				// row[6],isPointSelected)));
				// System.out.println("Distance :"+GeoLocHelper.calculateMtDistance((Point)
				// row[6],isPointSelected));
				// }
				items.add(itemDetached);

			}

			return items;
		} else {
			return new ArrayList<Item>();
		}
	}

}
