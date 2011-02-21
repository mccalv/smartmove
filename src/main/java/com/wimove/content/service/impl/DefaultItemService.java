/**
 * 
 */
package com.wimove.content.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.wimove.content.domain.Item;
import com.wimove.content.domain.LocalizedItem;
import com.wimove.content.geometry.GeoLocHelper;
import com.wimove.content.persistence.ItemRepository;
import com.wimove.content.persistence.filter.SearchFilter;
import com.wimove.content.service.ItemService;

/**
 * @author mccalv
 * 
 */
public class DefaultItemService implements ItemService {

	public static final Log LOG = LogFactory.getLog(DefaultItemService.class);

	private ItemRepository itemRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.service.ItemService#get(java.lang.Long)
	 */
	@Transactional
	public Item get(Serializable id, Locale locale) {
		Item item = itemRepository.get(id);
		populateLocalizedValues(locale, item);
		return item;
	}

	@Transactional(readOnly = true)
	public Item getItemByName(String itemName, Locale locale) {
		Item item = itemRepository.getByName(itemName, locale);
		
	
		populateLocalizedValues(locale, item);
		return item;
	}

	/**
	 * Populates the localized value
	 * 
	 * @param locale
	 * @param item
	 */
	private void populateLocalizedValues(Locale locale, Item item) {

		if (item.getLocalizedItems() != null) {
			List<LocalizedItem> localizedItems = item.getLocalizedItems();
			for (LocalizedItem localizedItem : localizedItems) {
				if (localizedItem.getLabel().equals(LocalizedItem.Label.Title)
						&& localizedItem.getLocale().equals(locale)) {
					item.setName(localizedItem.getValue());
				}
				if (localizedItem.getLabel().equals(
						LocalizedItem.Label.Description)
						&& localizedItem.getLocale().equals(locale)) {
					item.setDescription(localizedItem.getValue());
				}
				if (localizedItem.getLabel().equals(LocalizedItem.Label.Mp3)
						&& localizedItem.getLocale().equals(locale)) {
					item.setMp3(localizedItem.getValue());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.service.ItemService#saveOrUpdate(com.wimove.content
	 * .domain.Item)
	 */
	@Transactional
	public void saveOrUpdate(Item item) {
		itemRepository.saveOrUpdate(item);
	}

	/*
	 * Spring setters
	 */

	/**
	 * @param itemRepository
	 *            the itemRepository to set
	 */
	public void setItemRepository(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Transactional(readOnly = true)
	public List<Item> getPoisAroundLocation(Float lat, Float lon,
			double radius, Locale locale) {
		List<Item> items = itemRepository.getPoisAroundLocation(GeoLocHelper
				.createPoint(lon, lat), radius);
		for (Item item : items) {
			populateLocalizedValues(locale, item);

		}
		return items;

	}

	@Transactional(readOnly = true)
	public List<Item> getItemsByCriteria(SearchFilter searchFilter) {
		List<Item> items = itemRepository.getItemsByCriteria(searchFilter);
		for (Item item : items) {
			populateLocalizedValues(searchFilter.getLocale(), item);

		}
		return items;
	}

	@Transactional
	public void deleteItemsByGid(String GID) {
		itemRepository.removeItemFromGid(GID);

	}

}
