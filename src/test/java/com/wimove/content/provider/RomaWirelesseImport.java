/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 7 Jan 2010
 * mccalv
 * RomaWirelesseImport
 * 
 */
package com.wimove.content.provider;

import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.wimove.content.domain.Category;
import com.wimove.content.domain.Gid;
import com.wimove.content.domain.GpsPosition;
import com.wimove.content.domain.Item;
import com.wimove.content.domain.LocalizedItem;
import com.wimove.content.domain.LocalizedItem.Label;
import com.wimove.content.hibernate.AbstractPrepopulatedHibernateItest;
import com.wimove.content.persistence.ItemRepository;

/**
 * @author mccalv
 * 
 */
public class RomaWirelesseImport extends AbstractPrepopulatedHibernateItest {

	private static final Gid GID = new Gid("ROMA_WIRELESS");
	Category HOT_SPOT_CATEGORY = new Category("HOT_SPOT");
	@Autowired
	ItemRepository itemRepository;

	@SuppressWarnings("unchecked")
	@Test
	public void parseWimoveWirelessDevices() throws Exception {

		List<String> lines = FileUtils.readLines(new ClassPathResource(
				"files/hot_spot.csv").getFile());
		for (String line : lines) {
			System.out.println(line);
			String[] chunks = StringUtils.split(line, ","); 

			Item item = new Item();
			item.setGid(GID);
			item.setCategory(HOT_SPOT_CATEGORY);
			item.setItemId(StringUtils.remove(chunks[0], "\""));
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Title, Locale.ITALIAN,
							StringUtils.remove(chunks[2], "\"")));
			try {
				
				String number = chunks[10].equals("-")?"":", "+chunks[10];
				item.getGpsPositions().add(
						new GpsPosition(item, Float.parseFloat(chunks[4]),
								Float

								.parseFloat(chunks[5]), 
										chunks[9]+""+number, "Roma"));
			} catch (Exception e) {
				System.out.println("Exception on item " + chunks[0]);
			}

			 itemRepository.saveOrUpdate(item);
		}

	}

}
