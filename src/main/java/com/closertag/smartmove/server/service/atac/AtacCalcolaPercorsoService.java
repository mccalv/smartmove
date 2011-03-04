/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 7 Dec 2009
 * mccalv
 * AtacCalcolaPercorsoService
 * 
 */
package com.closertag.smartmove.server.service.atac;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.geometry.ConvertSrid;
import com.closertag.smartmove.server.content.http.HttpConnectionManager;

/**
 * @author mccalv
 * 
 */
public class AtacCalcolaPercorsoService extends HttpConnectionManager {

	
	private static final Log LOG = LogFactory.getLog(AtacCalcolaPercorsoService.class);
	public AtacCalcolaPercorsoService(String url, boolean useTimeouts)
			throws MalformedURLException {
		super(url, useTimeouts);
	}

	private static final String PLACE = "[PLACE]";
	private static final String ADDRESS = "[ADDRESS]";
	private static final String NUMBER = "[NUMBER]";
	private static final String NNP_START = "[NNP_START]";
	private static final String NNP_END = "[NNP_END]";

	private static final String X_START = "[X_START]";
	private static final String X_END = "[X_END]";
	private static final String Y_START = "[Y_START]";
	private static final String Y_END = "[Y_END]";

	private static final String NORM_ADDRESS = "<request_norm><place>" + PLACE
			+ "</place><address>" + ADDRESS + "</address><number>" + NUMBER
			+ "</number></request_norm>";
	private static final String CALCOLA_PERCORSO = "<request_navigate>"
			+ "<command>bestway</command>" + "<place>ROMA</place>"
			+ "<nnp_start>" + NNP_START + "</nnp_start>" + "<nnp_end>"
			+ NNP_END + "</nnp_end>"

			+ "<store_image>no</store_image></request_navigate>";

	private static final String CALCOLA_PERCORSO_COORD = "<request_navigate>"
			+ "<command>bestway</command>" + "<place>ROMA</place>"
			+ "<nnp_start>-1</nnp_start>" + "<nnp_end>-1</nnp_end>"

			+ "<x_start>" + X_START + "</x_start>" + "<y_start >" + Y_START
			+ "</y_start>" + "<x_end>" + X_END + "</x_end>" + "<y_end>"
			+ Y_END + "</y_end>"
			+ "<store_image>no</store_image></request_navigate>";

	private String normalizeAddress(String place, String address, String number) {
		String ind = StringUtils.replace(NORM_ADDRESS, PLACE, place);
		ind = StringUtils.replace(ind, NUMBER, number);
		ind = StringUtils.replace(ind, ADDRESS, address);

		return ind;

	}

	/**
	 * Calcolate path from a specific position
	 * Its expends an in input in the form {lat,lon}
	 * 
	 * @param gpsFrom
	 * @param gpsTo
	 * @return
	 */
	public String calcolaPercorsoFromPosition(String gpsFrom, String gpsTo) {

		String[] from = gpsFrom.split(",");
		String[] to = gpsTo.split(",");

		Double[] gaussFrom = ConvertSrid.fromWgs84ToGaussBoaga(new Double[] {
				Double.parseDouble(from[0]), Double.parseDouble(from[1])

		});
		Double[] gaussto = ConvertSrid.fromWgs84ToGaussBoaga(new Double[] {
				Double.parseDouble(to[0]), Double.parseDouble(to[1])

		});
		String ind = StringUtils.replace(CALCOLA_PERCORSO_COORD, X_START, ""
				+ gaussFrom[0]);
		ind = StringUtils.replace(ind, X_END, "" + gaussto[0]);
		ind = StringUtils.replace(ind, Y_START, "" + gaussFrom[1]);
		ind = StringUtils.replace(ind, Y_END, "" + gaussto[1]);
		if(LOG.isDebugEnabled()){
			LOG.debug(ind);
		}
		
		return getResource("/navigate_xml.asp", null, ind);
	}

	public String calcolaPercorso(GpsPosition from, GpsPosition to) {

		if (!StringUtils.contains(from.getAddress(), (",Roma"))) {
			from.setAddress(from.getAddress() + ",Roma");

		}
		if (!StringUtils.contains(to.getAddress(), (",Roma"))) {
			to.setAddress(to.getAddress() + ",Roma");

		}
		String[] address = from.getAddress().split(",");
		String[] addressTo = to.getAddress().split(",");
		String nnppStart = getNnp(normalizeAddress(from.getLocality(),
				address[0], address[1]));
		
		
		String nnppTo = getNnp(normalizeAddress(to.getLocality(), addressTo[0],
				addressTo[1]));
		
		
		if(LOG.isDebugEnabled()){
			LOG.debug("LOOKING FOR ADDRESS FROM "+nnppStart +" to" + nnppTo);
			
		}
		String ind = StringUtils
				.replace(CALCOLA_PERCORSO, NNP_START, nnppStart);
		ind = StringUtils.replace(ind, NNP_END, nnppTo);

		return getResource("/navigate_xml.asp", null, ind);

	}

	public String calcolaPercorso(String from, String to) {

		if (!StringUtils.contains(from, ",Roma")) {
			from += ",Roma";

		}
		if (!StringUtils.contains(to, ",Roma")) {
			to += ",Roma";

		}
		String[] address = from.split(",");
		String[] addressTo = to.split(",");
		String nnppStart = getNnp(normalizeAddress("Roma", address[0],
				address[1]));
		
		String nnppTo = getNnp(normalizeAddress("Roma", addressTo[0],
				addressTo[1]));
		String ind = StringUtils
				.replace(CALCOLA_PERCORSO, NNP_START, nnppStart);
		ind = StringUtils.replace(ind, NNP_END, nnppTo);

		return getResource("/navigate_xml.asp", null, ind);

	}

	private String getNnp(String xmlSource) {

		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;

		try {
			document = builder.build(new StringReader(

			new String(getByteArrayResource("/norm_xml.asp", null, xmlSource)

			)));
			return document.getRootElement().getChild("nnp").getText();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
