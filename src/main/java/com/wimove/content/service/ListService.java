/**
 * 
 */
package com.wimove.content.service;

import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Point;
import com.wimove.content.domain.ItemList;

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
