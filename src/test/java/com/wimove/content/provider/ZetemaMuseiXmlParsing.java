/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 24 Nov 2009
 * mccalv
 * ZetemaMuseiXmlParsing
 * 
 */
package com.wimove.content.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.wimove.content.domain.Category;
import com.wimove.content.domain.Contact;
import com.wimove.content.domain.Cost;
import com.wimove.content.domain.Extra;
import com.wimove.content.domain.Gid;
import com.wimove.content.domain.GpsPosition;
import com.wimove.content.domain.Item;
import com.wimove.content.domain.LocalizedItem;
import com.wimove.content.domain.LocalizedItem.Label;
import com.wimove.content.hibernate.AbstractPrepopulatedHibernateItest;
import com.wimove.content.persistence.ItemRepository;
import com.wimove.util.HtmlUtils;

/**
 * @author mccalv
 * 
 */
public class ZetemaMuseiXmlParsing extends AbstractPrepopulatedHibernateItest {

	private static final Gid GID = new Gid("ZETEMA");
	private static final Category CATEGORY = new Category("musei");
	
	@Autowired
	ItemRepository itemRepository;

	@SuppressWarnings("unchecked")
	//@Test
	public void testParsing() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;

		document = builder.build(new InputStreamReader(new FileInputStream(
				new ClassPathResource("files/comune/StralcioMusei.xml")
						.getFile()), "8859_1"));

		List<Element> elements = document.getRootElement().getChildren(
				"elemento");
		int i = 0;
		for (Element element : elements) {
			Item item = new Item();
			i++;
			item.setItemId("ZET_"+i+CATEGORY.getCategory());
			item.setWebsite(element.getChildText("link"));
			item.setGid(GID);
			item.setCategory(CATEGORY);
			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Title, Locale.ITALIAN,
							element.getChildText("nome")));

			item.getLocalizedItems().add(
					new LocalizedItem(item, Label.Description, Locale.ITALIAN,
							HtmlUtils.unescapeHtmlText(element
									.getChildText("descrizione"))));

			List<Element> indirizzi = element.getChild("indirizzi")
					.getChildren("indirizzo");
			for (Element indirizzo : indirizzi) {

				item.getGpsPositions().add(
						new GpsPosition(item, Float.parseFloat(indirizzo
								.getAttribute("latitudine").getValue()), Float
								.parseFloat(indirizzo.getAttribute(
										"longitudine").getValue()), indirizzo
								.getText(),"Roma"));

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
			if (element.getChild("servizi") != null) {
				List<Element> servizi = element.getChild("servizi")
						.getChildren("servizio");
				for (Element servizio : servizi) {
					Extra extra = new Extra();
					extra.setItem(item);
					extra.setLabel("servizi");
					extra.setValue(servizio.getText());
					item.getExtras().add(extra);
				}
			}

			itemRepository.saveOrUpdate(item);
		}

	}
}
