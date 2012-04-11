/*
 * @(#)DeafultPearsonService.java     Apr 11, 2012
 *
 * Copyright (c) 2010 Innovation Engineering S.r.l. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.closertag.smartmove.server.service.pearson;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.Gid;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.http.HttpConnectionManager;
import com.closertag.smartmove.server.content.service.ItemService;

/**
 * 
 * @author mccalv
 * @since Apr 11, 2012
 *
 */
public class DeafultPearsonService implements PearsonService {
	public static final String PEARSON_URL = "https://api.pearson.com/";
	private ItemService itemService;
	private String apikey;
	

	/* (non-Javadoc)
	 * @see com.closertag.smartmove.server.service.pearson.PearsonService#importContentsFromGps(java.lang.Double, java.lang.Double)
	 */
	@Override
	public void importContentsFromGps(Double lat, Double lon)throws Exception {
		
		HttpConnectionManager manager = new HttpConnectionManager(PEARSON_URL,
				false);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		JsonNode rootNode = mapper.readValue(
				new String(manager.getByteArrayResourcePost(
						"/eyewitness/london/block.json?lon=-"+lon+"&lat="+lat+"&apikey="
								+ getApikey(), null, null)), JsonNode.class);
		java.util.Iterator<JsonNode> it = rootNode.get("list").get("link")
				.getElements();

		while (it.hasNext()) {
			JsonNode entry = it.next();
			System.out.println(entry.get("@id"));
			// Chiamo la singola string per eseguire la query
			// https://api.pearson.com/eyewitness/london/block/EWTG_LONDON097APSHOU_001.json?apikey=d14b9d3c132ba476437046de8b1395a8
			ObjectMapper mapper_item = new ObjectMapper();
			String id = entry.get("@id").getTextValue();
			JsonNode itemNode = mapper_item.readValue(
					new String(manager.getByteArrayResourcePost(
							"/eyewitness/london/block/" + id + ".json?apikey="
									+ getApikey(), null, null)), JsonNode.class);
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
	}


	/**
	 * @param itemService the itemService to set
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}


	/**
	 * Getter for apikey.
	 *
	 * @return the apikey.
	 */
	public String getApikey() {
		return apikey;
	}


	/**
	 * @param apikey the apikey to set
	 */
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}


	
	
	
	

}
