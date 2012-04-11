/*
 * @(#)PearsonReaderText.java     Apr 11, 2012
 *
 * Copyright (c) 2010 Innovation Engineering S.r.l. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.matchpoint.smartmove.content.pearson;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.Gid;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.http.HttpConnectionManager;
import com.closertag.smartmove.server.content.service.ItemService;
import com.closertag.smartmove.server.service.pearson.PearsonService;

/**
 * 
 * @author mccalv
 * @since Apr 11, 2012
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class PearsonReaderText {

	public static final String API_KEY = "d14b9d3c132ba476437046de8b1395a8";
	public static final String PEARSON_URL = "https://api.pearson.com/";

	@Autowired
	ItemService itemService;

	
	
	@Autowired 
	PearsonService pearsonService;
	
	
	@Test
	public void testPearsonService() throws Exception{
		
		pearsonService.importContentsFromGps(51.497866d, 0.164739d);
	} 
	
	/**
	 * <Pre>
	 * {
	 *    "block":{
	 *       "@parent":"EWTG_LONDON097APSHOU",
	 *       "title":{
	 *          "#text":"Apsley House"
	 *       },
	 *       "tg_info":{
	 *          "@id":"EWTG_LONDON097APSHOU_001",
	 *          "@lat":"51.50331",
	 *          "@long":"-0.151641",
	 *          "address":{
	 *             "#text":"Hyde Park Corner W1"
	 *          },
	 *          "map_ref":{
	 *             "#text":"12 D4"
	 *          },
	 *          "phone":{
	 *             "#text":"020 7499 5676"
	 *          },
	 *          "transport":{
	 *             "@role":"underground_railway",
	 *             "@keyref":"EWTG_LONDONKEYMAI_001",
	 *             "#text":"Hyde Park Corner"
	 *          },
	 *          "opening_info":{
	 *             "@keyref":"EWTG_LONDONKEYMAI_023",
	 *             "#text":"Apr–Oct: 11am–5pm Wed–Sun & bank hols; Nov–Mar: 11am–4pm Wed–Sun & bank hols"
	 *          },
	 *          "closing_info":{
	 *             "@keyref":"EWTG_LONDONKEYMAI_025",
	 *             "#text":"24–26 Dec, 1 Jan"
	 *          },
	 *          "admission_charge":{
	 *             "@keyref":"EWTG_LONDONKEYMAI_021",
	 *             "#text":"joint ticket with Wellington Arch available"
	 *          },
	 *          "tg_data":[
	 *             {
	 *                "@role":"photography",
	 *                "@keyref":"EWTG_LONDONKEYMAI_007"
	 *             },
	 *             {
	 *                "@role":"audio_tours",
	 *                "@keyref":"EWTG_LONDONKEYMAI_011"
	 *             },
	 *             {
	 *                "@role":"guided_tours",
	 *                "@keyref":"EWTG_LONDONKEYMAI_010",
	 *                "#text":"pre-booked only"
	 *             }
	 *          ],
	 *          "url":{
	 *             "#text":"www.english-heritage.org.uk"
	 *          }
	 *       }
	 *    }
	 * }
	 * @throws Exception
	 */
	//@Test
	public void getEntryRss() throws Exception {

		
		HttpConnectionManager manager = new HttpConnectionManager(PEARSON_URL,
				false);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		JsonNode rootNode = mapper.readValue(
				new String(manager.getByteArrayResourcePost(
						"/eyewitness/london/block.json?lon=-0.164739&lat=51.497866&apikey="
								+ API_KEY, null, null)), JsonNode.class);
		java.util.Iterator<JsonNode> it = rootNode.get("list").get("link")
				.getElements();

		while (it.hasNext()) {
			JsonNode entry = it.next();
			System.out.println(entry.get("@id"));
			// Chiamo la singola string per eseguire la query
			// https://api.pearson.com/eyewitness/london/block/EWTG_LONDON097APSHOU_001.json?apikey=d14b9d3c132ba476437046de8b1395a8
			ObjectMapper mapper_item = new ObjectMapper();
			String id = entry.get("@id").getTextValue();
			JsonNode itemNode = mapper.readValue(
					new String(manager.getByteArrayResourcePost(
							"/eyewitness/london/block/" + id + ".json?apikey="
									+ API_KEY, null, null)), JsonNode.class);
			JsonNode block = itemNode.get("block");
			String text = block.get("title").get("#text").getTextValue();

			System.out.println(text);
			Item item = new Item();
			Category c = new Category("pearson");
			item.setCategory(c);
			item.setGid(new Gid("PEARSON"));
			item.setItemId(id);

			JsonNode tagInfo = block.get("tg_info");
			Cost cost = new Cost();
			if (tagInfo.get("admission_charge") != null) {
				cost.setItem(item);
				cost.setLocale(Locale.ENGLISH);
				cost.setValue(tagInfo.get("admission_charge").getTextValue());
				item.setWebsite(tagInfo.get("url").get("#text").getTextValue());
				item.getCosts().add(cost);
			}
			
			String description = text;
			if (tagInfo.get("opening_info") != null) {
			 description += "Opening:"
					+ tagInfo.get("opening_info").get("#text").getTextValue();
			}
			if (tagInfo.get("closing_info") != null) {
				description += "Closing: "
						+ tagInfo.get("closing_info").get("#text")
								.getTextValue();
			}
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Description, Locale.ENGLISH,
							description));
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Title, Locale.ENGLISH, text));

			item.getGpsPositions().add(
					new GpsPosition(item, Float.valueOf(tagInfo.get("@lat")
							.getTextValue()), Float.valueOf(tagInfo.get("@long")
							.getTextValue()), tagInfo.get("address")
							.get("#text").getTextValue(), "London"));

			itemService.saveOrUpdate(item);

		}

		// JSON.parse(new
		// String(manager.getByteArrayResourcePost("eyewitness/london/block.json",
		// null, params));

	}

}
