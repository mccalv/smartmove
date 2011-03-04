/**
 * 
 */
package com.closertag.smartmove.server.content.service;

import java.util.List;
import java.util.Locale;

import com.closertag.smartmove.server.content.domain.ItemList;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 *
 */
public interface ListService {
	
	/**
	 * Return alle the present list order by Locale
	 * @param locale
	 * @return
	 */	
	List<ItemList>getAllList(Locale locale);
	
	
	List<ItemList>getListsByGeometry(Point geometry,Locale locale);
	

}
