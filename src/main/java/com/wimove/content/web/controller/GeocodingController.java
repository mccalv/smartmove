/**
 * 
 */
package com.wimove.content.web.controller;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimove.content.domain.GpsPosition;
import com.wimove.content.geometry.ConvertSrid;
import com.wimove.content.protocol.XmlGpsPosition;
import com.wimove.content.protocol.XmlGpsPositions;
import com.wimove.content.service.GeocodingService;
import com.wimove.content.web.view.JaxbView;
import com.wimove.content.web.view.ServiceView;

/**
 * @author mccalv
 * 
 */
@Controller
@Component
public class GeocodingController extends BaseMarshallingController {
	@Autowired
	GeocodingService geocodingService;

	/**
	 * It delegates to the Google Search Service to Geocode the request and
	 * returns the exact Google answer
	 * <p>
	 * <a href="http://roma.wimove.it/wimove//services/get/Geocoding/xml?api_key=qw33fvvtg5hh&query=via%20olevano%20romano,71&language=it"
	 * > http://roma.wimove.it/wimove//services/get/Geocoding/xml?api_key=
	 * qw33fvvtg5hh&query=via%20olevano%20romano,71&language=it </a>
	 * 
	 * @param apiKey
	 * @param language
	 * @param query
	 * @return
	 */
	@RequestMapping("/services/get/Geocoding/xml")
	public ServiceView getQuery(@RequestParam("api_key") String apiKey,
			@RequestParam("query") String query,
			@RequestParam("language") String language) {

		try {
			return new ServiceView(geocodingService.getQueryOutput(query,
					new Locale(language)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Returns a gps position for a given address
	 * <p>
	 * <a href="view-source:http://roma.wimove.it/wimove/services/get/Geocoding/getGps/xml?api_key=qw33fvvtg5hh&query=via%20olevano%20romano,71&language=it"
	 * >view-source:http://roma.wimove.it/wimove/services/get/Geocoding/getGps/
	 * xml?api_key=qw33fvvtg5hh&query=via%20olevano%20romano,71&language=it</a>
	 * 
	 * @param apiKey
	 * @param query
	 * @param language
	 * @return
	 */
	@RequestMapping("/services/get/Geocoding/getGps/xml")
	public JaxbView getGps(@RequestParam("api_key") String apiKey,
			@RequestParam("query") String query,
			@RequestParam("language") String language) {

		try {
			GpsPosition gpsPosition = geocodingService.getGpsPosition(query,
					new Locale(language));
			XmlGpsPosition position = new XmlGpsPosition();
			position.setLat(gpsPosition.getLatitude());
			position.setLon(gpsPosition.getLongitude());
			// position.getAddress(gpsPosition.getAddress());

			return new JaxbView(position);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("/services/get/Geocoding/convertCoord/xml")
	public JaxbView convertCoord(@RequestParam("api_key") String apiKey,
			@RequestParam("query") String query,
			@RequestParam("language") String language) {
		XmlGpsPositions xmlGpsPositions = new XmlGpsPositions();

		String[] splits = StringUtils.split(query, "|");

		for (String string : splits) {

			String[] latLon = StringUtils.split(string, ",");
			if (latLon.length == 2) {

				Double[] gaussFrom = ConvertSrid
						.fromWgs84ToGaussBoaga(new Double[] {
								Double.parseDouble(latLon[0]),
								Double.parseDouble(latLon[1])

						});

				XmlGpsPosition position = new XmlGpsPosition();
				position.setLat(new Float(gaussFrom[1]));
				position.setLon(new Float(gaussFrom[0]));
				xmlGpsPositions.getGpsPosition().add(position);

			}
			
		}
		return new JaxbView(xmlGpsPositions);
	}

}
