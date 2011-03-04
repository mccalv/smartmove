/*
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.geosearch")
public class GeoSearch {

	public GeoSearch() {

	}

	/**
	 * @return the geoSearchPk
	 */
	public GeoSearchPk getGeoSearchPk() {
		return geoSearchPk;
	}

	/**
	 * @param geoSearchPk
	 *            the geoSearchPk to set
	 */
	public void setGeoSearchPk(GeoSearchPk geoSearchPk) {
		this.geoSearchPk = geoSearchPk;
	}

	@Id
	private GeoSearchPk geoSearchPk;

	@Column(name = "search_date")
	private Date searchDate;

	@Column(name = "value")
	private String value;

	/**
	 * @return the searchDate
	 */
	public Date getSearchDate() {
		return searchDate;
	}

	/**
	 * @param searchDate
	 *            the searchDate to set
	 */
	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	@Embeddable
	public static class GeoSearchPk implements Serializable {
		
		private static final long serialVersionUID = 5677162366295999378L;

		public GeoSearchPk(String query, Locale locale) {

			this.query = query;
			this.locale = locale;
		}

		
		public GeoSearchPk() {
		}
		
		
		
		@Column(name = "query")
		private String query;

		@Column(name = "locale")
		@Type(type = "locale")
		private Locale locale;

		/**
		 * @return the locale
		 */
		public Locale getLocale() {
			return locale;
		}

		/**
		 * @param locale
		 *            the locale to set
		 */
		public void setLocale(Locale locale) {
			this.locale = locale;
		}

		/**
		 * @return the query
		 */
		public String getQuery() {
			return query;
		}

		/**
		 * @param query
		 *            the query to set
		 */
		public void setQuery(String query) {
			this.query = query;
		}

	}
	
}

