/**
 * 
 */
package com.wimove.content.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Point;
import com.wimove.content.domain.ItemList;
import com.wimove.content.domain.LocalizedList;
import com.wimove.content.persistence.ListRepository;
import com.wimove.content.service.ListService;

/**
 * @author mccalv
 * 
 */
public class DefaultListService implements ListService {

	private ListRepository listRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.service.ListService#getAllList(java.util.Locale)
	 */
	@Transactional(readOnly = true)
	public List<ItemList> getAllList(Locale locale) {
		List<ItemList> itemLists = listRepository.getAll(locale);
		for (ItemList itemList : itemLists) {
			populateLocalizedValues(locale, itemList);
		}
		return itemLists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.service.ListService#getListsByGeometry(com.vividsolutions
	 * .jts.geom.Geometry, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ItemList> getListsByGeometry(Point geometry, Locale locale) {
		List<ItemList> itemLists = listRepository.getListWithin(geometry);
		
		for (ItemList itemList : itemLists) {
			populateLocalizedValues(locale, itemList);
		}
		Collections.sort(itemLists);
		return itemLists;
	}

	private void populateLocalizedValues(Locale locale, ItemList item) {
		List<LocalizedList> localizedItems = item.getLocalizedLists();
		for (LocalizedList localizedItem : localizedItems) {
			if (localizedItem.getLabel().equals(LocalizedList.Label.Title)
					&& localizedItem.getLocale().equals(locale)) {
				item.setListName(localizedItem.getValue());
			}
			if (localizedItem.getLabel()
					.equals(LocalizedList.Label.Description)
					&& localizedItem.getLocale().equals(locale)) {
				item.setListDescription(localizedItem.getValue());
			}
		}
	}

	/**
	 * @param listRepository
	 *            the listRepository to set
	 */
	public void setListRepository(ListRepository listRepository) {
		this.listRepository = listRepository;
	}
}
