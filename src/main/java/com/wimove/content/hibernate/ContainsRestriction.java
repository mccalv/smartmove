/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/

package com.wimove.content.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

import com.vividsolutions.jts.geom.Point;
import com.wimove.content.domain.GidZone;
import com.wimove.content.domain.GpsPosition;

/**
 * An implementation of the <code>Criterion</code> interface that implements
 * distance restriction
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class ContainsRestriction implements Criterion {

	/**
	 * Point we have to calculate the distance from
	 */
	private Point geometry;

	private String alias;
	private static final long serialVersionUID = 1L;

	/**
	 * @param point
	 *            Point we have to calculate the distance from
	 * @param distance
	 *            The distance restriction by default use index
	 */
	public ContainsRestriction(Point geometry, String alias) {
		this.geometry = geometry;
		
		this.alias = alias;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.criterion.Criterion#toSqlString(org.hibernate.Criteria,
	 * org.hibernate.criterion.CriteriaQuery)
	 */
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		String[] columnName = criteriaQuery.getColumnsUsingProjection(criteria,
				alias + "." + GidZone.POLYGON_COLUMN_NAME);
		// String columnName = criteriaQuery.getColumn(criteria,
		// );
		StringBuffer result = new StringBuffer(" st_within(").append(" PointFromText('POINT("+geometry
						.getX()+" "+ geometry.getY()+")',4326)  ,").append(columnName[0]+")");
		return result.toString();

	}



	@Override
	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] {  };
	}

}
