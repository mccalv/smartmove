/**
 * 
 */
package com.wimove.content.protocol;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.wimove.content.utils.JAXBUtils;

/**
 * @author mccalv
 * 
 */
public class JabxBindingTest {
	@SuppressWarnings("unchecked")
	private final static ThreadLocal<HashMap<Class, JAXBContext>> CONTEXT_CACHE = new ThreadLocal<HashMap<Class, JAXBContext>>();

	/**
	 * Simply tests if the {@link JAXB} marshalling is done without exception
	 * and if the output string contains CDATA elements
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBaseJaxb() throws Exception {
		// unmarshal a doc

		/*
		 * A list of elements to be used as CDATA field. It's necessary to api
		 * the namespace
		 */
		String[] cdata = { "http://content.wimove.com/services^title",
				"http://content.wimove.com/services^name",
				"http://content.wimove.com/services^description",
				"http://content.wimove.com/services^address",
				"http://content.wimove.com/services^value" };

		// marshal using the Apache XMLSerializer

		XmlList xmlList = new XmlList();
		xmlList.getDescription().add("èèpoii sdfsdfsd");

		XmlItem xmlItem = new XmlItem();
		xmlItem.setTitle("Title");
		xmlItem.setDescription("description");

		XmlExtra xmlExtra = new XmlExtra();
		xmlExtra.setName("telefono");
		xmlExtra.setValue("3282930943");
		xmlItem.getExtra().add(xmlExtra);

		XmlCost cost = new XmlCost();
		cost.setLabel("biglietto");
		cost.setValue("111.29€");
		XmlCost cost2 = new XmlCost();
		cost2.setLabel("biglietto");
		cost2.setValue("111.29€");

		XmlContact contact = new XmlContact();
		contact.setLabel("telefono");
		contact.setValue("0684579554");

		XmlCosts xmlCosts = new XmlCosts();
		xmlCosts.getCost().add(cost);
		xmlCosts.getCost().add(cost2);

		XmlContacts xmlContacts = new XmlContacts();
		xmlContacts.getContact().add(contact);

		xmlItem.setCosts(xmlCosts);
		xmlItem.setContacts(xmlContacts);

		XmlGpsPosition xmlGpsPosition = new XmlGpsPosition();
		xmlGpsPosition.setLat(1520.55f);
		xmlGpsPosition.setLon(1520.55f);
		xmlGpsPosition.setAddress("via della marmorata,21,roma");
		XmlGpsPositions gpsPositions = new XmlGpsPositions();

		gpsPositions.getGpsPosition().add(xmlGpsPosition);
		xmlItem.setGpsPositions(gpsPositions);

		/*
		 * adding time recurrencies
		 */

		XmlTimeOccurencies timeOccurencies = new XmlTimeOccurencies();
		XmlTimeRange xmlTimeRange = new XmlTimeRange();
		
		
		
		xmlTimeRange.setEndDate(new Date());
		xmlTimeRange.setStartDate(new Date());
		timeOccurencies.getTimeRange().add(xmlTimeRange);
		xmlItem.setTimeOccurencies(timeOccurencies);

		
		
		
		
		XmlTimeRecurrences xmlTimeRecurrences = new XmlTimeRecurrences();
		XmlByDayOfWeek xmlByDayOfWeek = new XmlByDayOfWeek();
 		XmlDayOfTheWeek e = new XmlDayOfTheWeek();
 		e.setValue(new BigInteger("1"));
		xmlByDayOfWeek.getDayOfTheWeek().add(e);
		xmlTimeRecurrences.setByDayOfWeek(xmlByDayOfWeek ); 
		xmlItem.setTimeRecurrences(xmlTimeRecurrences);		
		
		xmlList.getItem().add(xmlItem);
		
		// StringWriter stringWriter = new StringWriter();
		// XMLSerializer serializer = getXMLSerializer();

		String output = JAXBUtils.marshalToString(xmlList, cdata);
		System.out.println(output);
		assertTrue(StringUtils.contains(output, "CDATA"));
	}

	/**
	 * Returns the Jaxb context creating it and adding it to the thread local
	 * cache.
	 * 
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	protected static JAXBContext getContext(Class clazz) throws JAXBException {
		HashMap<Class, JAXBContext> map = CONTEXT_CACHE.get();
		JAXBContext context = null;
		if (map == null) {
			map = new HashMap<Class, JAXBContext>();
			CONTEXT_CACHE.set(map);
		}
		context = map.get(clazz);
		if (context == null) {
			context = JAXBContext.newInstance(new Class[] { clazz });
			map.put(clazz, context);
		}
		return context;
	}

}
