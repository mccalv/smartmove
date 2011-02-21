/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Dec 2009
 * mccalv
 * SearchFilterBuilder
 * 
 */
package com.wimove.content.web.controller.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.wimove.content.domain.Category;
import com.wimove.content.exception.WimoveException;
import com.wimove.content.exception.WimoveException.ExceptionType;
import com.wimove.content.geometry.GeoLocHelper;
import com.wimove.content.persistence.filter.SearchFilter;

/**
 * Converts all the incoming parameters into a SearchFilter object, passed later
 * on the the persistence layer to search against the database
 * 
 * @author mccalv
 * 
 */
public class SearchFilterBuilder {

	

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");
	private static final String POINT_PREFIX = "point:";
	private static final String AREA_PREFIX = "area:";

	public static SearchFilter buildSearchFilter(

	String apiKey, String position, String language, String category,
			Double radius, String text, Boolean listTypePrimary,
			String listTypeSecondary, String listTypeMenu, String orderBy,
			String tag, String tagNot, Boolean mashup, String startDate,
			String endDate, String filterByOwner, String withExtension,
			String extraFieldsConditions, Integer limit,Integer start

	) {
		SearchFilter searchFilter = new SearchFilter();

		if (StringUtils.indexOf(position, POINT_PREFIX) == 0) {
			searchFilter.setPoint(GeoLocHelper.createPoint(StringUtils
					.substringAfterLast(position, POINT_PREFIX)));

		}
		if (StringUtils.indexOf(position, AREA_PREFIX) == 0) {
			searchFilter.setPolygon(GeoLocHelper
					.createPolygonFromString(StringUtils.substringAfterLast(
							position, AREA_PREFIX)));

		}

		
		if (orderBy != null) {
			searchFilter.setOrderBy(SearchFilter.Order.valueOf(orderBy));
		}
		if (category != null) {
			searchFilter.setCategory(new Category(category));
		}
		if (text != null) {
			searchFilter.setText(text);
		}
		if (filterByOwner != null) {
			searchFilter.setGidIdentifier(filterByOwner);
		}
		if(tag!=null){
			searchFilter.setTags(StringUtils.split(tag,","));
		}
		
		try {
			if (startDate != null) {
				searchFilter.setStartDate(DATE_FORMAT.parse(startDate));
			}
			if (endDate != null) {
				searchFilter.setEndDate(DATE_FORMAT.parse(endDate));
			}
		} catch (Exception e) {
			throw new WimoveException(ExceptionType.INVALID_DATE_FORMAT, e);
		}
		if (radius != null) {
			searchFilter.setRadius(radius);
		}

		if (limit != null) {
			searchFilter.setLimit(limit);
		}
		if (start != null) {
			searchFilter.setStart(start);
		}
		return searchFilter;
	}
}
