/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 24 Nov 2009
 * mccalv
 * ZetemaMuseiXmlParsing
 * 
 */
package com.wimove.content.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.domain.Contact;
import com.closertag.smartmove.server.content.domain.Cost;
import com.closertag.smartmove.server.content.domain.Extra;
import com.closertag.smartmove.server.content.domain.Gid;
import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.Tag;
import com.closertag.smartmove.server.content.domain.TimeOccurrence;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.persistence.ItemRepository;
import com.closertag.smartmove.server.util.HtmlUtils;
import com.wimove.content.hibernate.AbstractPrepopulatedHibernateItest;

/**
 * It parses data coming from a different source. 
 * At this stage, data are not provided by a relative XSD schema
 * @author mccalv
 * 
 */
public class ZetemaFolderXmlParsing extends AbstractPrepopulatedHibernateItest {

	private static final String EVENT_FILE_LOCATION = "files/export";
	private static final Gid GID = new Gid("ZETEMA");
		
	@Autowired
	ItemRepository itemRepository;

	@SuppressWarnings("unchecked")
	@Test
	public void testParsing() throws JDOMException, IOException {
		
		//File folder  = new ClassPathResource(EVENT_FILE_LOCATION).getFile();
		Collection<File> listFiles = FileUtils.listFiles(new ClassPathResource(EVENT_FILE_LOCATION).getFile(),new String[]{"xml"}, false);
		for(File file:listFiles){
		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;

		document = builder.build(new InputStreamReader(new FileInputStream(
				file), "8859_1"));
		Category category = new Category(document.getRootElement().getAttribute("nome").getValue().toLowerCase());
		List<Element> elements = document.getRootElement().getChildren(
				"elemento");

		int i = 0;
		for (Element element : elements) {
			i++;
			Item item = new Item();
			item.setWebsite(element.getChildText("link"));
			item.setGid(GID);
			item.setItemId("ZET_"+element.getAttributeValue("id"));
			item.setCategory(category);
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Title, Locale.ITALIAN,
							element.getChildText("nome")));

			String descrizione = element
					.getChildText("descrizione");
			if(descrizione!=null){
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Description, Locale.ITALIAN,
							HtmlUtils.unescapeHtmlText(descrizione)));
			}
			
			
			List<Element> indirizzi = element.getChild("indirizzi")
					.getChildren("indirizzo");
			for (Element indirizzo : indirizzi) {

				item.getGpsPositions().add(
						new GpsPosition(item, Float.parseFloat(indirizzo
								.getAttribute("latitudine").getValue()), Float
								.parseFloat(indirizzo.getAttribute(
										"longitudine").getValue()), indirizzo
								.getText(), "Roma"));

			}

			List<Element> costi = element.getChild("costi")
					.getChildren("costo");
			for (Element costo : costi) {
				Cost cost = new Cost();
				cost.setItem(item);
				cost.setLabel(costo.getAttribute("label").getValue());
				cost.setValue(costo.getText());
				item.getCosts().add(cost);

			}

			List<Element> contatti = element.getChild("contatti").getChildren(
					"contatto");
			for (Element contatto : contatti) {
				Contact contact = new Contact();
				contact.setItem(item);
				contact.setLabel(contatto.getAttribute("label").getValue());
				contact.setValue(contatto.getText());
				item.getContacts().add(contact);

			}
			if (element.getChild("periodo") != null) {
				String dataInizio = element.getChild("periodo").getChild(
						"datainizio").getText();
				String dataFine = element.getChild("periodo").getChild(
						"datafine").getText();

				TimeOccurrence timeOccurence = new TimeOccurrence();
			//	timeOccurence.setItem(item);
				timeOccurence.setStartDate(new Date(Long.parseLong(dataInizio
						+ "000")));
				timeOccurence.setEndDate(new Date(Long.parseLong(dataFine
						+ "000")));

				item.getTimeOccurrences().add(timeOccurence);

			}
			
			if (element.getChild("sedi") != null) {
				List<Element> servizi = element.getChild("sedi")
						.getChildren("sedi");
				for (Element servizio : servizi) {
					Extra extra = new Extra();
					extra.setItem(item);
					extra.setLabel("sede");
					extra.setValue(servizio.getChild("nome").getText());
					item.getExtras().add(extra);
				}
			}
			
			if (element.getChild("keywords") != null) {
				List<Element> keywords = element.getChild("keywords")
						.getChildren("keyword");
				for (Element keyword : keywords) {
					item.getTags().add(new Tag(keyword.getText()));
				}
			}

			itemRepository.saveOrUpdate(item);
		}}

	}
}
