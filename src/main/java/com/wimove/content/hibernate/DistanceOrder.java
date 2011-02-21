/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Dec 2009
 * mccalv
 * DistanceOrder
 * 
 */
package com.wimove.content.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

import com.vividsolutions.jts.geom.Point;
import com.wimove.constant.SRID;
import com.wimove.content.domain.GpsPosition;

/**
 * A criteria that sort by distance for better performance you can use
 * {@link ProjectionOrder} if a projection for distance calculation have been
 * added
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class DistanceOrder extends Order {

	/**
	 * Default serial id
	 */
	private static final long serialVersionUID = 7382013976844760232L;

	private Point point;
	private boolean ascending = true;
private String alias  ;
	/**
	 * @param point
	 *            The point from which we calculate the distance
	 * @param ascending
	 *            Whether we sort Ascending
	 */
	public DistanceOrder(Point point, boolean ascending,String alias ) {
		super(null, ascending);
		this.point = point;
		this.ascending = ascending;
		this.alias = alias;
	}

	/**
	 * @param point
	 *            the point from which we calculate the distance, default
	 *            ascending is true
	 */
	public DistanceOrder(Point point) {
		super(null, true);
		this.point = point;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.criterion.Order#toSqlString(org.hibernate.Criteria,
	 * org.hibernate.criterion.CriteriaQuery)
	 */
	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		String aliases[] = criteriaQuery.getColumnsUsingProjection(criteria,  alias+"."+GpsPosition.LOCATION_COLUMN_NAME);
		StringBuilder fragment = new StringBuilder();
		fragment.append(" distance_sphere(");
		fragment.append(aliases[0]);
		fragment.append(",geometryfromtext('");
		fragment.append(point.toText());
		fragment.append("',");
		fragment.append(SRID.WGS84_SRID.getSRID());
		fragment.append(ascending ? ")) asc" : ")) desc");
		return fragment.toString();
	}
	


}
