package com.closertag.smartmove.server.content.service.impl.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

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
import com.closertag.smartmove.server.content.service.ParsingService;
import com.closertag.smartmove.server.util.HtmlUtils;

@SuppressWarnings("unchecked")
public class DefaultParsingService implements ParsingService {

	private static final Log LOG = LogFactory
			.getLog(DefaultParsingService.class);
	private static HashMap<String, Category> categories = new HashMap<String, Category>();

	private String gidIdentifier;
	private String csvGidIdentifier;
	/**
	 * Static cache the category table
	 */

	static {

		try {

			File file = new ClassPathResource("ZETEMA_CATEGORIES.csv")
					.getFile();
			List<String> lines = FileUtils.readLines(file);
			for (String line : lines) {
				String[] cat = StringUtils.split(
						StringUtils.replace(line, "\"", ""), "|");

				/*
				 * In this case the element [0] is the Italian category and [1]
				 * the English one
				 */
				Category baseCategory = new Category(cat[0]);
				categories.put(cat[0], baseCategory);
				if (cat.length > 1) {

					Category traslated = new Category(cat[1]);
					traslated.setBase(baseCategory);
					categories.put(cat[1], traslated);

				}

			}
		} catch (IOException e) {
			LOG.error("Impossible to parse the ZETEMA_CATEGORIES file ");
			e.printStackTrace();
		}

	}

	@Transactional
	public List<Item> importXmlFile(File file) throws Exception {

		// File folder = new ClassPathResource(EVENT_FILE_LOCATION).getFile();
		List<Item> items = new ArrayList<Item>();

		SAXBuilder builder = new SAXBuilder(false);
		Document document = null;
		// 8859_1
		document = builder.build(new InputStreamReader(
				new FileInputStream(file), "UTF8"));

		Locale locale;
		Category category;
		Category referenced = categories.get(document.getRootElement()
				.getAttribute("nome").getValue().toLowerCase());
		if (referenced == null) {

			LOG.error("Impossible to fine match for "
					+ document.getRootElement().getAttribute("nome").getValue()
							.toLowerCase());

			throw new Exception("Impossible to find a match "
					+ document.getRootElement().getAttribute("nome").getValue()
							.toLowerCase());
		}
		if (referenced.getBase() == null) {
			locale = Locale.ITALIAN;
			category = referenced;
		} else {

			locale = Locale.ENGLISH;
			category = referenced.getBase();

		}

		List<Element> elements = document.getRootElement().getChildren(
				"elemento");

		int i = 0;
		
		for (Element element : elements) {
			Item item = new Item();
			item.setWebsite(element.getChildText("link"));
			item.setGid(new Gid(gidIdentifier));
			try {
				int numberOfElement = elements.size();
				i++;
				
				String itemId = "ZET_" + element.getAttributeValue("id");
				item.setItemId(itemId);
				item.setCategory(category);
				item.getLocalizedItems().add(
						new LocalizedItem(item, Label.Title, locale, element
								.getChildText("nome")));

				String descrizione = element.getChildText("descrizione");
				if (descrizione != null) {
					item.getLocalizedItems().add(
							new LocalizedItem(item, Label.Description, locale,
									StringEscapeUtils.unescapeHtml(HtmlUtils
											.unescapeHtmlText(descrizione))));
				}

				List<Element> indirizzi = element.getChild("indirizzi")
						.getChildren("indirizzo");
				for (Element indirizzo : indirizzi) {

					item.getGpsPositions().clear();

					item.getGpsPositions().add(
							new GpsPosition(item, Float.parseFloat(indirizzo
									.getAttribute("latitudine").getValue()),
									Float.parseFloat(indirizzo.getAttribute(
											"longitudine").getValue()),
									indirizzo.getText(), "Roma"));

				}

				List<Element> costi = element.getChild("costi").getChildren(
						"costo");
				item.getCosts().clear();
				for (Element costo : costi) {
					Cost cost = new Cost();
					cost.setItem(item);
					cost.setLabel(costo.getAttribute("label").getValue());
					cost.setLocale(locale);
					cost.setValue(costo.getText());
					item.getCosts().add(cost);

				}

				List<Element> contatti = element.getChild("contatti")
						.getChildren("contatto");
				item.getContacts().clear();
				for (Element contatto : contatti) {
					Contact contact = new Contact();
					contact.setItem(item);
					contact.setLabel(contatto.getAttribute("label").getValue());
					contact.setValue(contatto.getText());
					contact.setLocale(locale);
					item.getContacts().add(contact);

				}
				item.getTimeOccurrences().clear();

				boolean isTimeAdded = false;
				if (element.getChild("sedi") != null) {
					List<Element> sedi = element.getChild("sedi").getChildren(
							"sede");
					for (Element sede : sedi) {

						String nome = sede.getChildText("nome");

						if (sede.getChild("rappresentazioni") != null) {
							List<Element> rappresentazioni = sede.getChild(
									"rappresentazioni").getChildren(
									"rappresentazione");

							for (Element rappresentazione : rappresentazioni) {
								TimeOccurrence timeOccurence = new TimeOccurrence();
								timeOccurence.setItem(item);
								timeOccurence.setAddress(nome);
								timeOccurence.setStartDate(new Date(Long
										.parseLong(rappresentazione.getText()
												+ "000")));
								timeOccurence.setEndDate(new Date(Long
										.parseLong(rappresentazione.getText()
												+ "000")));
								item.getTimeOccurrences().add(timeOccurence);
								isTimeAdded = true;

							}
						}

					}
				}
				if (element.getChild("periodo") != null && !isTimeAdded) {

					/*
					 * Chechs if theres is a single time occurence
					 */

					String dataInizio = element.getChild("periodo")
							.getChild("datainizio").getText();
					String dataFine = element.getChild("periodo")
							.getChild("datafine").getText();

					TimeOccurrence timeOccurence = new TimeOccurrence();
					timeOccurence.setItem(item);
					timeOccurence.setStartDate(new Date(Long
							.parseLong(dataInizio + "000")));
					timeOccurence.setEndDate(new Date(Long.parseLong(dataFine
							+ "000")));

					item.getTimeOccurrences().add(timeOccurence);

				}

				if (element.getChild("sedi") != null) {
					List<Element> servizi = element.getChild("sedi")
							.getChildren("sedi");
					item.getExtras().clear();
					for (Element servizio : servizi) {
						Extra extra = new Extra();
						extra.setItem(item);
						extra.setLabel("sede");
						extra.setLocale(locale);
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

				/*
				 * if (item.equals(saved)) { LOG.info("Item " + i + "of (" +
				 * numberOfElement + "), name:" + itemId + " already present");
				 * } else { LOG.info("Saved item " + i + "of (" +
				 * numberOfElement + "), name:" + itemId);
				 * itemRepository.saveOrUpdate(item); }
				 */
				LOG.info("Item " + i + " of (" + numberOfElement + "), name:"
						+ itemId + " (File, " + file.getName()
						+ ")parsed, Locale:" + locale.toString());
				items.add(item);
			} catch (Exception e) {

				LOG.error("Error import item "+item.getGid() + e.toString());
			}

		}
		return items;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wimove.content.service.ParsingService#importCsvFile(java.io.File)
	 */

	public List<Item> importCsvFile(File file) throws Exception {

		// 0 1 2 3 4 5 6 7 8 9 10
		// "ID","CATEGORY","TITLE","TITLE_EN","DESCRIPTION_IT","DESCRIPTION_EN","LAT (WGS84)","LON (WGS84)","ADDRESS","MP3_FILE_IT","MP3_FILE_EN"

		List<Item> returnList = new ArrayList<Item>();
		List<String> lines = FileUtils.readLines(file);
		int numLine = 0;
		for (String line : lines) {

			String[] chunks = StringUtils.splitPreserveAllTokens(
					StringUtils.remove(line, "\""), ",");

			if (!chunks[0].equals("ID")
			// &&numLine>=startLine &&numLine<EndLine
			) {
				Item item = new Item();
				item.setGid(new Gid(csvGidIdentifier));
				String category2 = chunks[1];
				item.setCategory(new Category(category2));

				item.setItemId(category2.charAt(0) + "_"
						+ StringUtils.remove(chunks[0], "\""));
				item.getLocalizedItems().add(
						new LocalizedItem(item, Label.Title, Locale.ITALIAN,
								StringUtils.remove(chunks[2], "\"")));
				if (chunks[3].isEmpty()) {
					item.getLocalizedItems().add(
							new LocalizedItem(item, Label.Title,
									Locale.ENGLISH, StringUtils.remove(
											chunks[3], "\"")));
				} else {
					item.getLocalizedItems().add(
							new LocalizedItem(item, Label.Title,
									Locale.ENGLISH, StringUtils.remove(
											chunks[2], "\"")));
				}

				// String number = chunks[10].equals("-") ? "" : ", " +
				// chunks[10];
				item.getGpsPositions().add(
						new GpsPosition(item, Float.parseFloat(chunks[6]),
								Float

								.parseFloat(chunks[7]), StringUtils.replace(
										chunks[8], ",Roma", ""), "Roma"));

				returnList.add(item);

			}
			numLine++;
		}
		return returnList;

	}

	/**
	 * @param gidIdentifier
	 *            the gidIdentifier to set
	 */
	public void setGidIdentifier(String gidIdentifier) {
		this.gidIdentifier = gidIdentifier;
	}

	/**
	 * @param csvGidIdentifier
	 *            the csvGidIdentifier to set
	 */
	public void setCsvGidIdentifier(String csvGidIdentifier) {
		this.csvGidIdentifier = csvGidIdentifier;
	}

}
