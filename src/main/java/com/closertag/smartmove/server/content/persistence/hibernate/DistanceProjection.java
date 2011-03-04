/**
 * 
 */
package com.closertag.smartmove.server.content.persistence.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.SimpleProjection;
import org.hibernate.type.Type;

import com.closertag.smartmove.server.constant.SRID;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 *
 */
public class DistanceProjection extends SimpleProjection{
private Point point;
protected final String propertyName;
	
	
	public DistanceProjection(Point point,String propertyName){
		this.propertyName = propertyName;
		this.point = point;
	}
	@Override
	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		return new Type[] { Hibernate.DOUBLE };
 
	}

	@Override
	public String toSqlString(Criteria criteria, int position,
			CriteriaQuery criteriaQuery) throws HibernateException {
		
		StringBuilder fragment = new StringBuilder();
		fragment.append(" distance_sphere(");
		fragment.append(criteriaQuery.getColumn(criteria, propertyName) );
		fragment.append(",geometryfromtext('");
		fragment.append(point.toText());
		fragment.append("',");
		fragment.append(SRID.WGS84_SRID.getSRID());
		fragment.append( "))  ");
		fragment.append(" as y");
		fragment.append(position);
		fragment.append('_');
		return fragment.toString();
	}

	
}
