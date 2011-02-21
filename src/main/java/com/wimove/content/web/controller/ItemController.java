package com.wimove.content.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimove.content.domain.Category;
import com.wimove.content.domain.GpsPosition;
import com.wimove.content.domain.Item;
import com.wimove.content.exception.WimoveException;
import com.wimove.content.exception.WimoveException.ExceptionType;
import com.wimove.content.geometry.GeoLocHelper;
import com.wimove.content.persistence.filter.SearchFilter;
import com.wimove.content.service.ApiKeyService;
import com.wimove.content.service.GeocodingService;
import com.wimove.content.service.ItemService;
import com.wimove.content.web.controller.filter.SearchFilterBuilder;
import com.wimove.content.web.view.JaxbView;
import com.wimove.content.web.view.ServiceView;
import com.wimove.service.atac.AtacCalcolaPercorsoService;
import com.wimove.service.atac.AtacTempiAttesaBusService;

/**
 * 
 * @author mccalv
 * @since Sep 1, 2010
 * 
 */
@Controller
@Component
@RequestMapping(value = "/services/get/**")
public class ItemController extends BaseMarshallingController {

	/*
	 * qw33fvvtg5hh
	 */
	@Autowired
	private ItemService itemService;
	@Autowired
	private ApiKeyService apiKeyService;
	@Autowired
	private AtacCalcolaPercorsoService atacCalcolaPercorsoService;

	@Autowired
	private GeocodingService geoCodingService;
	@Autowired
	private AtacTempiAttesaBusService atacTempiAttesaBusService;

	@RequestMapping("GetBusWaitingtime/xml")
	public JaxbView getBusWaitingtime(@RequestParam("api_key") String apyKey,
			@RequestParam("idPalina") Integer idPalina,
			@RequestParam("linea") String linea) throws Exception {

		return new JaxbView(atacTempiAttesaBusService
				.calculateWaitingTimeByIdLine(idPalina, linea));
	}

	@RequestMapping("GetBusesWaitingtime/xml")
	public JaxbView getBusesWaitingtime(@RequestParam("api_key") String apyKey,
			@RequestParam("idPalina") Integer idPalina) throws Exception {

		return new JaxbView(atacTempiAttesaBusService
				.calculateWaitingTimeByIdPalina(idPalina));
	}

	/**
	 * Delegates to the Atac service to calculate the root between two items
	 * <p>
	 * <a href="view-source:http://roma.wimove.it/wimove/services/get/GetRoutesFromItem/xml?api_key=qw33fvvtg5hh&fromItemId=PC_11&toItemId=PC_12&language=it">view-source:http://roma.wimove.it/wimove/services/get/GetRoutesFromItem/xml?api_key=qw33fvvtg5hh&fromItemId=PC_11&toItemId=PC_12&language=it</a>
	 *
	 * @param api_key
	 * @param from_itemId
	 * @param to_itemId
	 * @param language
	 * @return
	 */
	@RequestMapping("GetRoutesFromItem/xml")
	public ServiceView getRoutesFromItem(
			@RequestParam("api_key") String apiKey,
			@RequestParam("fromItemId") String from_itemId,
			@RequestParam("toItemId") String to_itemId,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);

		Item from = itemService.get(from_itemId, new Locale(language));
		Item to = itemService.get(to_itemId, new Locale(language));
		if (from == null || to == null) {

			throw new RuntimeException(
					"The item requested is not present on the System");
		}

		return new ServiceView(atacCalcolaPercorsoService.calcolaPercorso(from
				.getGpsPositions().get(0), to.getGpsPositions().get(0)));
	}

	/**
	 * Calculates the routes between two point. It delegates to Atac service and
	 * mediate between the different Geografical System: WGS84-Gauss Boaga
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetRoutesFromCoord/xml?api_key=qw33fvvtg5hh&fromGps=41.908283,12.479838&toGps=41.836676,12.425983">http://roma.wimove.it/wimove/services/get/GetRoutesFromCoord/xml?api_key=qw33fvvtg5hh&fromItemId=22&fromGps=41.908283,12.479838&toGps=41.836676,12.425983</a>
	 * 
	 * @param apiKey
	 * @param fromGps
	 * @param toGps
	 * @return
	 */
	@RequestMapping("GetRoutesFromCoord/xml")
	public ServiceView getRoutesFromCoord(
			@RequestParam("api_key") String apiKey,
			@RequestParam("fromGps") String fromGps,
			@RequestParam("toGps") String toGps) {

		apiKeyService.checkValidApiKey(apiKey);

		return new ServiceView(atacCalcolaPercorsoService
				.calcolaPercorsoFromPosition(fromGps, toGps));
	}

	/**
	 * Similar to {@link #getRoutesFromCoord(String, String, String)} but accept
	 * a string address. For disambiguation issues, the first is more precise and has to be privileges
	
	 * @param apiKey
	 * @param fromAddress
	 * @param toAddress
	 * @return the xml representation
	 */
	@RequestMapping("GetRoutesFromAddress/xml")
	public ServiceView getRoutesFromAddress(
			@RequestParam("api_key") String apiKey,
			@RequestParam("fromAddress") String fromAddress,
			@RequestParam("toAddress") String toAddress) {

		apiKeyService.checkValidApiKey(apiKey);
		return new ServiceView(atacCalcolaPercorsoService.calcolaPercorso(
				fromAddress, toAddress));
	}

	private String appendRomeSuffix(String add) {

		if (!StringUtils.containsIgnoreCase(add, "rome")) {
			return add + ",Rome";
		}
		return add;
	}

	/**
	 * General api to get a routes around a position or an address. From and To
	 * can be combined
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetRoute/xml?api_key=qw33fvvtg5hh&language=it&fromAddress=via salaria 1&toAddress=via olevano Romano 71">http://roma.wimove.it/wimove/services/get/GetRoute/xml?api_key=qw33fvvtg5hh&language=it&fromAddress=via salaria 1&toAddress=via olevano Romano 71</a>
	 * @param apiKey
	 * @param fromAddress
	 * @param toAddress
	 * @param fromGps
	 * @param toGps
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("GetRoute/xml")
	public ServiceView getRoutes(
			@RequestParam("api_key") String apiKey,
			@RequestParam(value = "fromAddress", required = false) String fromAddress,
			@RequestParam(value = "toAddress", required = false) String toAddress,
			@RequestParam(value = "fromGps", required = false) String fromGps,

			@RequestParam(value = "toGps", required = false) String toGps)
			throws Exception {
		Locale locale = Locale.ITALIAN;
		String fromString = null;
		String toString = null;
		if (fromGps == null) {

			GpsPosition from = geoCodingService.getGpsPosition(
					appendRomeSuffix(fromAddress), locale);
			fromString = from.getLatitude() + "," + from.getLongitude();
		} else {
			fromString = fromGps;
		}

		if (toGps == null) {
			GpsPosition to = geoCodingService.getGpsPosition(
					appendRomeSuffix(toAddress), locale);
			toString = to.getLatitude() + "," + to.getLongitude();
		} else {
			toString = toGps;
		}

		apiKeyService.checkValidApiKey(apiKey);
		return new ServiceView(atacCalcolaPercorsoService
				.calcolaPercorsoFromPosition(fromString, toString));
	}

	/**
	 * Returns the single Item xml representation
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetItemById/xml?api_key=qw33fvvtg5hh&language=it&itemId=PC_11">http://roma.wimove.it/wimove/services/get/GetItemById/xml?api_key=qw33fvvtg5hh&language=it&itemId=PC_11</a>
	 * 
	 * @param apiKey
	 * @param itemId
	 * @param language
	 * 
	 */
	@RequestMapping("GetItemById/xml")
	public JaxbView getItem(@RequestParam("api_key") String apiKey,
			@RequestParam("itemId") String itemId,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);
		Locale locale = new Locale(language);
		Item item = itemService.get(itemId, locale);

		return new JaxbView(createXmlItem(item, true, locale));

	}

	/**
	 * Returns all the items for given list
	 * 
	 * @param apiKey
	 * @param listId
	 * @param language
	 * @return the xml representation
	 */
	@RequestMapping("GetItemByList/xml")
	public JaxbView getItemByList(@RequestParam("api_key") String apiKey,
			@RequestParam("listId") String listId,
			@RequestParam("language") String language,
			@RequestParam(value = "start", required = false) Integer start) {

		apiKeyService.checkValidApiKey(apiKey);
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setListIdentifier(listId);
		searchFilter.setLocale(new Locale(language));
		if (start != null) {
			searchFilter.setStart(start);
		}
		List<Item> item = itemService.getItemsByCriteria(searchFilter);

		return new JaxbView(createXmlListItem(item, null, new Locale(language)));

	}

	/**
	 * Returns items by tags.
	 * 
	 * @param apiKey
	 * @param tags
	 * @param language
	 * @return
	 * @deprecated Refers to the most generat
	 *             {@link #getItemsAroundmeByCriteria }
	 */
	@RequestMapping("GetItemByTags/xml")
	public JaxbView getItemByTags(@RequestParam("api_key") String apiKey,
			@RequestParam("tags") String tags,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);
		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setTags(org.apache.commons.lang.StringUtils.split(tags,
				","));
		Locale locale = new Locale(language);
		searchFilter.setLocale(locale);
		List<Item> item = itemService.getItemsByCriteria(searchFilter);

		return new JaxbView(createXmlListItem(item, null, locale));

	}

	@RequestMapping("GetItemByName/xml")
	public JaxbView getItemByName(@RequestParam("api_key") String apiKey,
			@RequestParam("itemName") String itemName,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);

		Locale locale = new Locale(language);
		Item item = itemService.getItemByName(itemName, locale);

		return new JaxbView(createXmlItem(item, true, locale));

	}

	/**
	 * A quick way to access points around a specific place in a precise radius
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetItemsByGpsPlace/xml?api_key=qw33fvvtg5hh&lat=41.8736&lon=12.4501&radius=5000&language=it">http://roma.wimove.it/wimove/services/get/GetItemsByGpsPlace/xml?api_key=qw33fvvtg5hh&lat=41.8736&lon=12.4501&radius=5000&language=it</a> 
	 * 
	 * @param apiKey
	 * @param lat
	 * @param lon
	 * @param radius
	 * @param language
	 * @return the Xml representation
	 */
	@RequestMapping("GetItemsByGpsPlace/xml")
	public JaxbView getItemsByGpsPlace(@RequestParam("api_key") String apiKey,
			@RequestParam("lat") float lat, @RequestParam("lon") float lon,
			@RequestParam("radius") double radius,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);

		SearchFilter searchFilter = new SearchFilter();
		searchFilter.setPoint(GeoLocHelper.createPoint(lon, lat));
		searchFilter.setRadius(radius);
		searchFilter.setCategory(new Category("musei"));
		Locale locale = new Locale(language);
		searchFilter.setLocale(locale);
		List<Item> item = itemService.getItemsByCriteria(searchFilter);

		return new JaxbView(createXmlListItem(item, GeoLocHelper.createPoint(
				lon, lat), locale));

	}

	/**
	 * It represents the most general API present at the moment on the platform
	 * and combines different search methods. An example could be the following
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetItemsAroundmeByCriteria/xml?api_key=qw33fvvtg5hh&limit=50&start=0&language=it&category=PICCOLI_TURISTI">http://roma.wimove.it/wimove/services/get/GetItemsAroundmeByCriteria/xml?api_key=qw33fvvtg5hh&limit=50&start=0&language=it&category=PICCOLI_TURISTI</a>
	 * 
	 * @param apiKey
	 * @param position
	 * @param language
	 * @param category
	 * @param radius
	 * @param text
	 * @param orderBy Possible values {CREATION_DATA, UPDATE_DATA, PROXIMITY, TITLE, DATE}
	 * @param tag
	 * @param tagNot
	 * @param mashup
	 *            (inactive)
	 * @param startDate
	 * @param endDate
	 * @param filterByOwner
	 * @param withExtension
	 * @param extraFieldsConditions
	 *            (inactive)
	 * @param limit
	 * @param start
	 * @return the xml representation
	 */
	@RequestMapping("GetItemsAroundmeByCriteria/xml")
	public JaxbView getItemsAroundmeByCriteria(
			@RequestParam("api_key") String apiKey,
			@RequestParam(value = "position", required = false) String position,
			@RequestParam(value = "language", required = true) String language,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "tagNot", required = false) String tagNot,
			@RequestParam(value = "mashup", required = false) Boolean mashup,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "filterByOwner", required = false) String filterByOwner,
			@RequestParam(value = "withExtension", required = false) String withExtension,
			@RequestParam(value = "extraFieldsConditions", required = false) String extraFieldsConditions,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "fromAddress", required = false) String fromAddress

	) throws Exception {

		apiKeyService.checkValidApiKey(apiKey);
		Locale locale = new Locale(language);
		if (fromAddress != null && position == null) {
			GpsPosition from = geoCodingService.getGpsPosition(fromAddress,
					locale);
			position = "point:" + from.getLongitude() + ","
					+ from.getLatitude();
		}

		SearchFilter searchFilter = SearchFilterBuilder.buildSearchFilter(
				apiKey, position, language, category, radius, text, null, null,
				null, orderBy, tag, tagNot, mashup, startDate, endDate,
				filterByOwner, withExtension, extraFieldsConditions, limit,
				start);

		searchFilter.setLocale(locale);
		;
		List<Item> items = itemService.getItemsByCriteria(searchFilter);
		for (Item item : items) {
			if (searchFilter.getCentroid() != null) {
				item.setDistance(new Float(GeoLocHelper.calculateMtDistance(
						item.getGpsPositions().get(0).getGeom_point(),
						searchFilter.getCentroid())));
			}
		}
		return new JaxbView(createXmlListItem(items,
				searchFilter.getCentroid(), locale));

	}

	/**
	 * Returns only the items which have a time occurence
	 * <p>
	 * <a href="http://roma.wimove.it/wimove/services/get/GetItemsByTime/xml?api_key=qw33fvvtg5hh&startDate=22/10/2010 00:00&language=it">http://roma.wimove.it/wimove/services/get/GetItemsByTime/xml?api_key=qw33fvvtg5hh&startDate=22/10/2010 00:00&language=it</a>
	 * 
	 * 
	 */
	@RequestMapping("GetItemsByTime/xml")
	public JaxbView getItemsByTime(
			@RequestParam("api_key") String apiKey,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "orderBy", required = false) String orderBy,
			@RequestParam("language") String language) {

		apiKeyService.checkValidApiKey(apiKey);

		SearchFilter searchFilter = new SearchFilter();
		if (orderBy != null) {
			searchFilter.setOrderBy(SearchFilter.Order.valueOf(orderBy));
		}
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			if (startDate != null) {
				searchFilter.setStartDate(df.parse(startDate));
			}
			if (endDate != null) {
				searchFilter.setEndDate(df.parse(endDate));
			}
		} catch (Exception e) {
			throw new WimoveException(ExceptionType.INVALID_DATE_FORMAT, e);
		}
		Locale locale = new Locale(language);
		searchFilter.setLocale(locale);
		;
		List<Item> item = itemService.getItemsByCriteria(searchFilter);

		return new JaxbView(createXmlListItem(item, null, locale));
	}

}
