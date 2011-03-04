/**
 * 
 */
package com.closertag.smartmove.server.content.persistence.hibernate;

import java.util.Locale;

import org.hibernate.criterion.Restrictions;

import com.closertag.smartmove.server.content.domain.GeoSearch;
import com.closertag.smartmove.server.content.persistence.GeoSearchRepository;

/**
 * @author mccalv
 * 
 */
public class HibernateGeoSearchRepository extends HibernateBaseRepository
		implements GeoSearchRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.GeoSearchRepository#get(java.lang.String,
	 * java.util.Locale)
	 */
	public GeoSearch get(String query, Locale locale) {
		return (GeoSearch) sessionFactory.getCurrentSession().createCriteria(
				GeoSearch.class).add(Restrictions.eq("geoSearchPk.query", query.toLowerCase().trim())).add(
				Restrictions.eq("geoSearchPk.locale", locale)).uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.persistence.GeoSearchRepository#saveOrUpdate(com.wimove
	 * .content.domain.GeoSearch)
	 */
	public void saveOrUpdate(GeoSearch geoSearch) {
		sessionFactory.getCurrentSession().merge(geoSearch);
	}

}
