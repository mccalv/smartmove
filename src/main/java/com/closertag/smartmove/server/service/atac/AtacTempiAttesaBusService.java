/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 16 Dec 2009
 * mccalv
 * AtacTempiAttesaBusService
 * 
 */
package com.closertag.smartmove.server.service.atac;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.wimove.content.protocol.XmlBusWaitingTime;
import com.wimove.content.protocol.XmlBuswaitingTimes;

/**
 * @author mccalv
 * 
 */
public class AtacTempiAttesaBusService {

	private static final Log LOG = LogFactory
			.getLog(AtacTempiAttesaBusService.class);

	private String url;
	private String token;
	private String method;

	/**
	 * 
	 * @param url
	 * @param token
	 * @param method
	 */
	public AtacTempiAttesaBusService(String url, String token, String method) {

		this.url = url;
		this.token = token;
		this.method = method;
	}

	/**
	 * Performs and XmlRpc request against the ATAC services and convers the
	 * result in an internal XML representation
	 * 
	 * @param idPalina
	 * @return
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */

	public XmlBuswaitingTimes calculateWaitingTimeByIdPalina(int idPalina)
			throws MalformedURLException, XmlRpcException {
		XmlBuswaitingTimes xmlBuswaitingTimes = new XmlBuswaitingTimes();
		Object[] veicoli = getXmlResultObject(idPalina);

		for (Object veicolo : veicoli) {

			xmlBuswaitingTimes.getBusWaitingTime().add(
					createXmlBusWaitingTime(veicolo));
		}
		return xmlBuswaitingTimes;

	}

	/**
	 * @param veicolo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private XmlBusWaitingTime createXmlBusWaitingTime(Object veicolo) {
		XmlBusWaitingTime xmlBusWaitingTime = new XmlBusWaitingTime();
		xmlBusWaitingTime.setLineNumber(((HashMap) veicolo).get("linea")
				.toString());
		xmlBusWaitingTime.setWaitingTime(((HashMap) veicolo)
				.get("tempo_attesa").toString());
		xmlBusWaitingTime.setTerminus(((HashMap) veicolo).get("capolinea")
				.toString());
		xmlBusWaitingTime.setAnnounce(((HashMap) veicolo).get("annuncio")
				.toString());
		xmlBusWaitingTime.setNumberOfStop(((HashMap) veicolo).get(
				"distanza_fermate").toString());
		return xmlBusWaitingTime;
	}

	@SuppressWarnings("unchecked")
	public XmlBuswaitingTimes calculateWaitingTimeByIdLine(int idPalina,
			String idLine) throws MalformedURLException, XmlRpcException {
		XmlBuswaitingTimes xmlBuswaitingTimes = new XmlBuswaitingTimes();
		Object[] veicoli = getXmlResultObject(idPalina);

		for (Object veicolo : veicoli) {
			String line = ((HashMap) veicolo).get("linea").toString();
			if (line.equals(idLine)) {

				xmlBuswaitingTimes.getBusWaitingTime().add(
						createXmlBusWaitingTime(veicolo));
			}
		}
		return xmlBuswaitingTimes;

	}

	@SuppressWarnings("unchecked")
	private Object[] getXmlResultObject(int idPalina)
			throws MalformedURLException, XmlRpcException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

		config.setServerURL(new URL(url));
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		Object[] params = new Object[] { new String(token),
				new Integer(idPalina) };

		Object execute = client.execute(method, params);
		if (LOG.isDebugEnabled()) {

			LOG.debug(execute);
		}
		HashMap result = (HashMap) execute;

		HashMap response = (HashMap) result.get("risposta");
		Object[] veicoli = (Object[]) response.get("veicoli");
		return veicoli;
	}

}
