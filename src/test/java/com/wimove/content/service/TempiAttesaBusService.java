/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 15 Dec 2009
 * mccalv
 * TempiAttesaBusService
 * 
 */
package com.wimove.content.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

/**
 * @author mccalv
 * 
 */
public class TempiAttesaBusService {
	//
	// $m = new xmlrpcmsg('paline.Veicoli', array (
	// new xmlrpcval("turismo-UCM65k2","string"),
	// new xmlrpcval($_GET['id_palina'], "int")
	@SuppressWarnings("unchecked")
	@Test
	public void testTempiAttesaServerRpc() throws MalformedURLException, XmlRpcException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(
				"http://127.0.0.1:8080/wimove/fake/tempiAttesaBus"));
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		Object[] params = new Object[] { new String("turismo-UCM65k2"),
				new Integer("70100") };
		HashMap result = (HashMap) client.execute("paline.Veicoli", params);
		HashMap x = (HashMap) result.get("risposta");
		Object[] veicoli = (Object[]) x.get("veicoli");
		for (Object veicolo : veicoli) {
			//System.out.println(veicolo);
			Object x2 = ((HashMap) veicolo).get("linea");
			System.out.println(x2);
			System.out.println(((HashMap) veicolo).get("tempo_attesa"));
			System.out.println(((HashMap) veicolo).get("capolinea"));
			System.out.println(((HashMap) veicolo).get("annuncio"));
			System.out.println(((HashMap) veicolo).get("distanza_fermate"));
			
			System.out.println(veicolo);
		}

	}
}
