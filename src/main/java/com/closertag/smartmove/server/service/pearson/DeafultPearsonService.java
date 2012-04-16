/*
 * @(#)DeafultPearsonService.java     Apr 11, 2012
 *
 * Copyright (c) 2010 Innovation Engineering S.r.l. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.closertag.smartmove.server.service.pearson;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.map.ObjectMapper;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.Extra;
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

	public static enum Tag {
		tg_info, article
	}

	public static final Log LOG = LogFactory
			.getLog(DeafultPearsonService.class);

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.closertag.smartmove.server.service.pearson.PearsonService#
	 * importContentsFromGps(java.lang.Double, java.lang.Double)
	 */
	@Override
	public int importContentsFromGps(Double lat, Double lon) throws Exception {
		int imported=0;
		
		HttpConnectionManager manager = new HttpConnectionManager(PEARSON_URL,
				false);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		JsonNode rootNode = mapper.readValue(
				new String(manager.getByteArrayResourcePost(
						"/eyewitness/london/block.json?lon=" + lon + "&lat="
								+ lat + "&apikey=" + getApikey(), null, null)),
				JsonNode.class);
		java.util.Iterator<JsonNode> it = rootNode.get("list").get("link")
				.getElements();

		while (it.hasNext()) {

			if(getSingleEntry(manager, it.next())){
				imported++;
			}
		}
		return imported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.closertag.smartmove.server.service.pearson.PearsonService#
	 * importContentsFromCategory(java.lang.String)
	 */
	@Override
	public int importContentsFromCategory(String category) throws Exception {
		int imported=0;
		HttpConnectionManager manager = new HttpConnectionManager(PEARSON_URL,
				false);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		JsonNode rootNode = mapper.readValue(
				new String(manager.getByteArrayResourcePost(
						"/eyewitness/london/" + category
								+ "/block.json?apikey=" + getApikey(), null,
						null)), JsonNode.class);
		java.util.Iterator<JsonNode> it = rootNode.get("list").get("link")
				.getElements();

		while (it.hasNext()) {

			if(getSingleEntry(manager, it.next())){
				imported++;
			}

		}
		return imported;
	}

	
	@Override
	public int importSingleEntry(String id) throws Exception {
		HttpConnectionManager manager = new HttpConnectionManager(PEARSON_URL,
				false);
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		JsonNode rootNode = mapper.readValue(
				new String("{\"@id\": \""+id+"\",\"@latitude\": \"fake\",\"@categories\": \"museum\"}"), JsonNode.class);
		getSingleEntry(manager,rootNode);
		return 1;
	}

	/**
	 * Get and save the single entry into the database
	 * 
	 * @param manager
	 * @param it
	 * @throws Exception
	 */
	private boolean getSingleEntry(HttpConnectionManager manager,
			JsonNode entry) throws Exception {
		String id = entry.get("@id").getTextValue();
		/*If a coordinate is missing exit */
		if(entry.get("@latitude")==null){
			return false;
			
		}
		String category = StringUtils.substringBefore(entry.get("@categories").getTextValue(),",");
		
		try {
			
			// Chiamo la singola string per eseguire la query
			// https://api.pearson.com/eyewitness/london/block/EWTG_LONDON097APSHOU_001.json?apikey=d14b9d3c132ba476437046de8b1395a8
			ObjectMapper mapper_item = new ObjectMapper();
			JsonNode itemNode = mapper_item
					.readValue(
							new String(manager.getByteArrayResourcePost(
									"/eyewitness/london/block/" + id
											+ ".json?apikey=" + getApikey(),
									null, null)), JsonNode.class);
			JsonNode block = itemNode.get("block");
			String text = block.get("title").get("#text").getTextValue();

			Item item = new Item();

			item.setGid(new Gid("PEARSON"));
			item.setItemId(id);

			JsonNode tagInfo = block.get("tg_info");
			Category c = new Category(category);
			item.setCategory(c);

			
			if(tagInfo==null){
				return false;
			}
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
				description += " Opening: "
						+ tagInfo.get("opening_info").get("#text")
								.getTextValue();
			}
			if (tagInfo.get("closing_info") != null) {
				description += " Closing: "
						+ tagInfo.get("closing_info").get("#text")
								.getTextValue();
			}
			if (tagInfo.get("additional_info") != null) {
				
				if(!tagInfo.get("additional_info").isArray()){
				description += "Additional info: "
						+ tagInfo.get("additional_info").get("#text")
								.getTextValue();
				}else{
					for (int i =0;i<tagInfo.get("additional_info").size();i++){
						JsonNode node =tagInfo.get("additional_info").get(i);
						if(node.get("#text")!=null){
						description +=" "+node.get("#text").getTextValue();
						}
						}
				}
			}

			if (tagInfo.get("phone") != null) {
				if(tagInfo.get("phone").isArray()){
				 
				 item.setTelephone(tagInfo.get("phone").get(0).get("#text").getTextValue());
				 
				}else{
				

					 item.setTelephone(tagInfo.get("phone").get("#text").getTextValue());
					 
				}
			}

			if (tagInfo.get("tg_data") != null) {
				if (tagInfo.get("tg_data").isArray()){
				for (int i=0;i<tagInfo.get("tg_data").size();i++){
					JsonNode data = tagInfo.get("tg_data").get(i);
					
					Extra e = new Extra();
					e.setItem(item);
					e.setLabel(data.get("@role").getTextValue());
					e.setValue("present");
					item.getExtras().add(e);

				}
				}else{
					Extra e = new Extra();
					e.setItem(item);
					e.setLabel(tagInfo.get("tg_data").get("@role").getTextValue());
					e.setValue("present");
					item.getExtras().add(e);

				}
			}

			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Description, Locale.ENGLISH,
							description));
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Title, Locale.ENGLISH, text));
			if (tagInfo.get("url") != null) {
				item.setWebsite(tagInfo.get("url").get("#text").getTextValue());
			}

			String address = tagInfo.get("address") != null ? tagInfo
					.get("address").get("#text").getTextValue() : null;
			item.getGpsPositions().add(
					new GpsPosition(item, Float.valueOf(tagInfo.get("@lat")
							.getTextValue()), Float.valueOf(tagInfo
							.get("@long").getTextValue()), address, "London"));

			if (LOG.isDebugEnabled()) {

				LOG.debug("Importing content id:" + id + " " + text);

			}
			itemService.saveOrUpdate(item);
			return true;
		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {

				LOG.debug("Error importing content " + e.toString());

			}
			e.printStackTrace();
			throw new Exception(e.toString() +" import dell'id  "+id);
			
		}
	}

	/**
	 * @param itemService
	 *            the itemService to set
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
	 * @param apikey
	 *            the apikey to set
	 */
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	/* (non-Javadoc)
	 * @see com.closertag.smartmove.server.service.pearson.PearsonService#importSingleEntry(java.lang.String)
	 */
	
}
