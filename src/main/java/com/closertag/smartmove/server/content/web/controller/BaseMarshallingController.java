/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 20 Nov 2009
 * mccalv
 * BaseMarshallingController
 * 
 */
package com.closertag.smartmove.server.content.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.closertag.smartmove.server.content.domain.Contact;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.TimeOccurrence;
import com.vividsolutions.jts.geom.Point;
import com.wimove.content.protocol.XmlContact;
import com.wimove.content.protocol.XmlContacts;
import com.wimove.content.protocol.XmlCost;
import com.wimove.content.protocol.XmlCosts;
import com.wimove.content.protocol.XmlGpsPosition;
import com.wimove.content.protocol.XmlGpsPositions;
import com.wimove.content.protocol.XmlItem;
import com.wimove.content.protocol.XmlList;
import com.wimove.content.protocol.XmlTimeOccurencies;
import com.wimove.content.protocol.XmlTimeRange;

/**
 * @author mccalv
 * 
 */
public abstract class BaseMarshallingController {
	public XmlList createXmlListItem(List<Item> items, Point position,
			Locale locale) {

		List<XmlItem> xmlItemList = new ArrayList<XmlItem>();
		for (Item item : items) {
			xmlItemList.add(createXmlItem(item, false, locale));
		}
		XmlList xmlList = new XmlList();

		xmlList.getItem().addAll(xmlItemList);
		return xmlList;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	protected XmlItem createXmlItem(Item item,
			boolean populateFullDependencies, Locale locale) {
		XmlItem xmlItem = new XmlItem();
		xmlItem.setNodeId(item.getItemId());
		xmlItem.setTitle(item.getName());
		xmlItem.setDescription(item.getDescription());
		
		if(item.getMp3()!=null){
			xmlItem.setAudioFile(item.getMp3());
		}

		xmlItem.setDistance(item.getDistance());
		xmlItem.setUrl(item.getWebsite());

		/*
		 * Add the contacts
		 */
		if (populateFullDependencies) {

			/*
			 * Add the costs
			 */
			XmlCosts xmlCosts = new XmlCosts();
			for (Cost cost : item.getCosts()) {
				if (cost.getLocale().equals(locale)) {
					XmlCost xmlCost = new XmlCost();
					xmlCost.setLabel(cost.getLabel());
					xmlCost.setValue(cost.getValue());
					xmlCosts.getCost().add(xmlCost);
				}
			}
			if (!xmlCosts.getCost().isEmpty()) {
				xmlItem.setCosts(xmlCosts);
			}

			XmlContacts xmlContacts = new XmlContacts();
			for (Contact cost : item.getContacts()) {
				if (cost.getLocale().equals(locale)) {

					XmlContact xmlContact = new XmlContact();
					xmlContact.setLabel(cost.getLabel());
					xmlContact.setValue(cost.getValue());
					xmlContacts.getContact().add(xmlContact);
				}
			}
			if (!xmlContacts.getContact().isEmpty()) {
				xmlItem.setContacts(xmlContacts);
			}

		}

		XmlTimeOccurencies xmlTimeOccurencies = new XmlTimeOccurencies();
		// timeOccurencies.getTimeRange().addAll(c);
		for (TimeOccurrence timeOccurrence : item.getTimeOccurrences()) {

			XmlTimeRange xmlTimeRange = new XmlTimeRange();
			xmlTimeRange.setEndDate(timeOccurrence.getEndDate());

			xmlTimeRange.setStartDate(timeOccurrence.getStartDate());
			if(timeOccurrence.getStartDate().compareTo(new Date())>0){
			xmlTimeOccurencies.getTimeRange().add(xmlTimeRange);
			}

		}

		if (!xmlTimeOccurencies.getTimeRange().isEmpty()) {
			xmlItem.setTimeOccurencies(xmlTimeOccurencies);
		}
		XmlGpsPositions gpsPositions = new XmlGpsPositions();
		for (GpsPosition gpsPosition : item.getGpsPositions()) {
			XmlGpsPosition xmlGpsPosition = new XmlGpsPosition();
			xmlGpsPosition.setLat(gpsPosition.getLatitude());
			xmlGpsPosition.setLon(gpsPosition.getLongitude());
			xmlGpsPosition.setAddress(gpsPosition.getAddress());
			xmlGpsPosition.setLocality(gpsPosition.getLocality());

			gpsPositions.getGpsPosition().add(xmlGpsPosition);

		}
		xmlItem.setGpsPositions(gpsPositions);
		return xmlItem;

	}

}
