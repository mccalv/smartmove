/**
 * 
 */
package com.wimove.content.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.closertag.smartmove.server.content.domain.Contact;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.TimeOccurrence;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.geometry.GeoLocHelper;
import com.closertag.smartmove.server.content.persistence.ItemRepository;
import com.vividsolutions.jts.geom.Point;

/**
 * @author mccalv
 * 
 */

public class HibernateItemRepositoryItest extends
		AbstractPrepopulatedHibernateItest {



	private static final Point ROMA = GeoLocHelper.createPoint(12.25006f,
			41.8736f);
	private static final Point SAN_BENEDETTO_DEL_TRONTO = GeoLocHelper
			.createPoint(13.87812f, 42.95152f);

	@SuppressWarnings("unused")
	private static final Point ASCOLI_PICENO = GeoLocHelper.createPoint(
			13.60036f, 42.8608f);

	@Autowired
	ItemRepository itemRepository;



	@Test
	public void saveItem() {
		Item item = new Item();
		// item.setId(2L);
		String nameIt = com.closertag.smartmove.server.util.HtmlUtils
				.unescapeHtmlText("Casa Museo di Giorgio De Chirico");
		String descriptionIt = com.closertag.smartmove.server.util.HtmlUtils
				.unescapeHtmlText("E' stata riaperta nel novembre 1998, in occasione del XX anniversario della morte, la casa in cui l'artista Giorgio De Chirico (1888-1978) ha vissuto con la moglie Isabella Far. L'appartamento occupa gli ultimi tre piani del seicentesco Palazzo dei Borgognoni con vista su Piazza di Spagna, la Scalinata di Trinit&agrave; dei Monti e Villa Medici. Uno scorcio di citt&agrave; questo che ha ispirato l'artista in molte sue opere.</div><div>La Fondazione De Chirico ha ricostruito con grande attenzione e ricerca filologica gli spazi interni secondo l'arredamento originale: lo studio (integro di tutto il necessario per la pittura come i calchi, i materiali, la poltrona), la biblioteca e i sontuosi saloni arricchiti dalle opere che fanno parte della collezione privata");

		String nameEn = com.closertag.smartmove.server.util.HtmlUtils
				.unescapeHtmlText("Museum home of di Giorgio De Chirico");
		String descriptionEn = com.closertag.smartmove.server.util.HtmlUtils
				.unescapeHtmlText("The    De Chirico foundation is situated in the apartment where  Giorgio De Chirico live in Italy, in the Baroque Palazzetto dei Borgognoni in Rome's Piazza di Spagna n°31 which was originally the home of a seventeenth-century Burgundian (Borgognoni) family, painters, the Courtois, and later home to assorted Grand Tourists, including Swiss artists and English writers.In 1947, the ageing metaphysical painter moved into the Palazzetto, living there until his death, aged 90, in 1978.Now the property of the Fondazione Giorgio e Isa De Chirico, formed in 1986 to promote continued interest in the artist's work, the apartment was opened to the public for the second time last june after a lengthy restoration.");

		// String description = "description";
		GpsPosition gpsPosition = new GpsPosition();
		gpsPosition.setItem(item);
		gpsPosition.setLatitude(new Float(ROMA.getY()));
		gpsPosition.setLongitude(new Float(ROMA.getX()));

		Cost cost = new Cost();
		cost.setLabel("intero");
		cost.setValue("5.00€");
		cost.setItem(item);

		Contact contact = new Contact();
		contact.setValue("telefono");
		contact.setLabel("06555655");
		contact.setItem(item);

		item.getCosts().add(cost);
		item.getContacts().add(contact);
		item.getLocalizedItems().add(
				new LocalizedItem(item, Label.Title, Locale.ITALIAN, nameIt));
		item.getLocalizedItems().add(
				new LocalizedItem(item, Label.Title, Locale.ITALIAN,
						descriptionIt));
		item.getLocalizedItems().add(
				new LocalizedItem(item, Label.Title, Locale.ENGLISH, nameEn));
		item.getLocalizedItems().add(
				new LocalizedItem(item, Label.Title, Locale.ENGLISH,
						descriptionEn));

		item.getTimeOccurrences().add(
				new TimeOccurrence(item, new Date(1254065400000L), new Date(
						)));

		gpsPosition.setGeom_point(ROMA);
		item.getGpsPositions().add(gpsPosition);
		itemRepository.saveOrUpdate(item);

	}

	@Test
	public void testRadius() {

		List<Item> poisAroundLocation = itemRepository.getPoisAroundLocation(
				SAN_BENEDETTO_DEL_TRONTO, 200000d);
		System.out.println(poisAroundLocation.size());
	}

}