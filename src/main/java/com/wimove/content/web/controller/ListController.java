/**
 * 
 */
package com.wimove.content.web.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimove.content.domain.ItemList;
import com.wimove.content.geometry.GeoLocHelper;
import com.wimove.content.protocol.XmlItemList;
import com.wimove.content.protocol.XmlItemLists;
import com.wimove.content.service.ApiKeyService;
import com.wimove.content.service.ListService;
import com.wimove.content.web.view.JaxbView;

/**
 * @author mccalv
 * 
 */
@Controller
@Component
public class ListController {
	@Autowired
	private ListService listService;
	@Autowired
	private ApiKeyService apiKeyService;

	/**
	 * Returns all the list present on a single location
	 * 
	 * @param api_key
	 * @param language
	 * @return
	 */
	@RequestMapping("/services/get/GetLists/xml")
	public JaxbView getLists(@RequestParam("api_key") String apiKey,
			@RequestParam("language") String language) {
		apiKeyService.checkValidApiKey(apiKey);
		List<ItemList> itemsList = listService.getAllList(new Locale(language));

		return new JaxbView(getXmlList(itemsList));
	}

	/**
	 * Returns all list present on a given point and radius
	 * @param api_key
	 * @param language
	 * @param lat
	 * @param lon
	
	 * @return
	 */
	@RequestMapping("/services/get/GetListsByPosition/xml")
	public JaxbView getListsByPosition(@RequestParam("api_key") String apiKey,
			@RequestParam("language") String language,
			@RequestParam("lat") float lat, @RequestParam("lon") float lon
		) {
			List<ItemList> listsByGeometry = listService.getListsByGeometry(GeoLocHelper.createPoint(lon, lat), new Locale(language));
		return new JaxbView(getXmlList(listsByGeometry));
	}

	private XmlItemLists getXmlList(List<ItemList> itemsList) {
		XmlItemLists xmlItemLists = new XmlItemLists();
		for (ItemList itemList : itemsList) {
			XmlItemList xmlItemList = new XmlItemList();
			xmlItemList.setDescription(itemList.getListDescription());
			xmlItemList.setTitle(itemList.getListName());
			xmlItemList.setId(Long.toString(itemList.getId()));
			xmlItemList.setIdentifier(itemList.getName());
			xmlItemLists.getItemList().add(xmlItemList);
		}

		return xmlItemLists;
	}
}
