/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 7 Jan 2010
 * mccalv
 * RomaWirelesseImport
 * 
 */
package com.wimove.content.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.domain.Gid;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.persistence.ItemRepository;
import com.wimove.content.hibernate.AbstractPrepopulatedHibernateItest;

/**
 * @author mccalv
 * 
 */
public class AtacImport extends AbstractPrepopulatedHibernateItest {

	private static final Gid GID = new Gid("ROMA_WIRELESS");
	Category HOT_SPOT_CATEGORY = new Category("HOT_SPOT");
	@Autowired
	ItemRepository itemRepository;
	private String category;
	private String path = "files/bus_stop.csv";

	@SuppressWarnings("unchecked")
	private List<Item> parseWimoveWirelessDevices() throws Exception {

		// 0 1 2 3 4 5 6 7 8 9 10
		// "ID","CATEGORY","TITLE","TITLE_EN","DESCRIPTION_IT","DESCRIPTION_EN","LAT (WGS84)","LON (WGS84)","ADDRESS","MP3_FILE_IT","MP3_FILE_EN"

		List<Item> returnList = new ArrayList<Item>();
		List<String> lines = FileUtils.readLines(new ClassPathResource(path)
				.getFile());

		for (String line : lines) {

			System.out.println(line);
			String[] chunks = StringUtils.splitPreserveAllTokens(StringUtils
					.remove(line, "\""), ",");

			if (!chunks[0].equals("ID")) {
				Item item = new Item();
				item.setGid(GID);
				String category2 = chunks[1];
				item.setCategory(new Category(category2));

				if (category == null) {
					category = category2;
				}
				item.setItemId(StringUtils.remove(chunks[0], "\""));
				item.getLocalizedItems().add(
						new LocalizedItem(item, Label.Title, Locale.ITALIAN,
								StringUtils.remove(chunks[2], "\"")));
				item.getLocalizedItems().add(
						new LocalizedItem(item, Label.Title, Locale.ITALIAN,
								StringUtils.remove(chunks[3], "\"")));

				// String number = chunks[10].equals("-") ? "" : ", " +
				// chunks[10];
				item.getGpsPositions().add(
						new GpsPosition(item, Float.parseFloat(chunks[6]),
								Float

								.parseFloat(chunks[7]), StringUtils.replace(
										chunks[8], ",Roma", ""), "Roma"));

				returnList.add(item);

			}
		}
		return returnList;

	}

	@Test
	public void testParse() throws Exception {
		List<Item> items = parseWimoveWirelessDevices();
		if (!items.isEmpty()) {
			itemRepository.removeItemFromCategory(items.get(0).getCategory()
					.getCategory());
			for (Item item : items) {
				itemRepository.saveOrUpdate(item);
			}
		}

	}
}
