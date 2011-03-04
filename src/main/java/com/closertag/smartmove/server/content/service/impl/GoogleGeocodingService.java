/**
 * 
 */
package com.closertag.smartmove.server.content.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.closertag.smartmove.server.content.domain.GeoSearch;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.GeoSearch.GeoSearchPk;
import com.closertag.smartmove.server.content.http.HttpConnectionManager;
import com.closertag.smartmove.server.content.persistence.GeoSearchRepository;
import com.closertag.smartmove.server.content.service.GeocodingService;

/**
 * 
 * @author mccalv
 * 
 */
public class GoogleGeocodingService extends HttpConnectionManager implements
		GeocodingService {

	private GeoSearchRepository geoSearchRepository;

	private static final Log LOG = LogFactory
			.getLog(GoogleGeocodingService.class);

	public GoogleGeocodingService(String url, boolean useTimeouts)
			throws MalformedURLException {
		super(url, useTimeouts);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.service.GeocodingService#getQueryOutput(java.lang.
	 * String)
	 */
	@Transactional
	public String getQueryOutput(String query, Locale locale) throws Exception {

		GeoSearch geoSearchSaved = geoSearchRepository.get(normalize(query),
				locale);
		if (geoSearchSaved == null) {
			if (LOG.isInfoEnabled()) {
				LOG
						.info("Entry "
								+ query
								+ " for locale "
								+ locale
								+ " is not present on memory. Opening  a call to Google to get a proper result");
			}

			String geocodingResult = getResource("/xml?address="
					+ URLEncoder.encode(query, "utf-8") + ",roma&region="
					+ locale + "&sensor=false&language=" + locale, null, null);

			/*
			 * Persists the new entry against the db
			 */
			GeoSearchPk pk = new GeoSearchPk(normalize(query), locale);
			GeoSearch geoSearch = new GeoSearch();
			geoSearch.setGeoSearchPk(pk);
			geoSearch.setValue(geocodingResult);
			geoSearch.setSearchDate(new Date());
			geoSearchRepository.saveOrUpdate(geoSearch);

			return geocodingResult;
		} else {
			if (LOG.isInfoEnabled()) {
				LOG.info("Retrieving result for query " + query + " from db");
			}
			return geoSearchSaved.getValue();
		}

	}

	@Transactional(readOnly=true)
	public GpsPosition getGpsPosition(String query, Locale locale)
			throws Exception {
		String outString = getQueryOutput(query, locale);
		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;

		try {
			document = builder.build(new StringReader(

			new String(outString)

			));
			GpsPosition position = new GpsPosition();
			// position.setLatitude(latitude);
			// position.seLongitude()
			org.jdom.Element location = document.getRootElement().getChild(
					"result").getChild("geometry").getChild("location");

			position.setLatitude(Float.valueOf(location.getChild("lat")
					.getText()));
			position.setLongitude(Float.valueOf(location.getChild("lng")
					.getText()));
			return position;

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Removes spaces and set the string lo lower case to case insensitive
	 * comparations
	 * 
	 * @param query
	 * @return
	 */
	private String normalize(String query) {
		return query.toLowerCase().trim();
	}

	/**
	 * @param geoSearchRepository
	 *            the geoSearchRepository to set
	 */
	public void setGeoSearchRepository(GeoSearchRepository geoSearchRepository) {
		this.geoSearchRepository = geoSearchRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.service.GeocodingService#getGpsPosition(java.lang.
	 * String, java.util.Locale)
	 */

}
