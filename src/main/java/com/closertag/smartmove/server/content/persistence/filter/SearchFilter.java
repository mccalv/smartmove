/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 17 Dec 2009
 * mccalv
 * SearchFilter
 * 
 */
package com.closertag.smartmove.server.content.persistence.filter;

import java.util.Date;
import java.util.Locale;

import com.closertag.smartmove.server.content.domain.Category;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author mccalv
 * 
 */
public class SearchFilter {

	private Double radius;
	private Date startDate;
	private Date endDate;
	private Category category;
	private Point point;
	private Polygon polygon;
	private Integer limit;
	private Order orderBy;
	private Locale locale;	
	private String text;
	private String gidIdentifier;
	private String[] tags;
	private String listIdentifier;
	private Integer start;

	
	
	
	/**
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * @return the listIdentifier
	 */
	public String getListIdentifier() {
		return listIdentifier;
	}

	/**
	 * @param listIdentifier the listIdentifier to set
	 */
	public void setListIdentifier(String listIdentifier) {
		this.listIdentifier = listIdentifier;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public static enum Order {
		CREATION_DATA, UPDATE_DATA, PROXIMITY, TITLE, DATE
	}

	/**
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		return polygon;
	}

	/**
	 * @param polygon
	 *            the polygon to set
	 */
	public void setPolygon(Polygon polygon) {

		this.polygon = polygon;
	}



	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(Double radius) {
		this.radius = radius;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the enddDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param enddDate
	 *            the enddDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point
	 *            the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the gidIdentifier
	 */
	public String getGidIdentifier() {
		return gidIdentifier;
	}

	/**
	 * @param gidIdentifier
	 *            the gidIdentifier to set
	 */
	public void setGidIdentifier(String gidIdentifier) {
		this.gidIdentifier = gidIdentifier;
	}

	public Point getCentroid() {
		if (point != null) {
			return point;
		}
		if (polygon != null) {
			return polygon.getCentroid();
		}
		return null;

	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the orderBy
	 */
	public Order getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(Order orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
