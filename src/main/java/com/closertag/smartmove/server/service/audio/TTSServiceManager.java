/**
 * 
 */
package com.closertag.smartmove.server.service.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.domain.LocalizedItem;
import com.closertag.smartmove.server.content.domain.LocalizedItem.Label;
import com.closertag.smartmove.server.content.http.HttpConnectionManager;
import com.closertag.smartmove.server.content.persistence.filter.SearchFilter;
import com.closertag.smartmove.server.content.service.ItemService;

/**
 * @author mccalv
 * 
 */

public class TTSServiceManager extends HttpConnectionManager {
	private static final Log LOG = LogFactory.getLog(TTSServiceManager.class);

	public static enum Voice {
		Paola, Kate
	}

	private static final String ID = "[ID]";
	private static final String VOCE = "[VOCE]";
	private static final String TESTO = "[TESTO]";
	private ItemService itemService;

	private String mp3Folder;
	private String tmpFolder;
	/*
	 * <form action="http://atacmobile.imnet.it:8081/Wimove/code/AppManager.php"
	 * method="post" enctype="multipart/form-data"> <input type="file"
	 * name="uploadfile"> <input type="hidden" name="MAX_FILE_SIZE"
	 * value="10000"> <input type="submit" value="Invia il file"> </form>
	 */

	private static String XML_TTS_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dati><id>"
			+ ID
			+ "</id> <voce>"
			+ VOCE
			+ "</voce><testo><![CDATA["
			+ TESTO
			+ "]]></testo></dati>";

	public TTSServiceManager(String url, boolean useTimeouts)
			throws MalformedURLException {

		super(url, useTimeouts);
	}

	public InputStream getMp3(String id, String text, String voice) {
		String ind = StringUtils.replace(XML_TTS_REQUEST, ID, id);
		ind = StringUtils.replace(ind, VOCE, voice);
		ind = StringUtils.replace(ind, TESTO, text);
		return getResourceInputStream("/Wimove/Code/AppManager.php", ind);
	}

	public InputStream generateMp3FromServer(String id, String text, Voice voice) {
		String ind = StringUtils.replace(XML_TTS_REQUEST, ID, id);
		ind = StringUtils.replace(ind, VOCE, voice.name());
		ind = StringUtils.replace(ind, TESTO, text);

		try {

			synchronized (this) {
				File file = new File(tmpFolder + "/" + id + ".xml");
				FileUtils.writeStringToFile(file, ind, "utf-8");
				return getPostFileResponse("/Wimove/Code/AppManager.php", file,
						"uploadfile", "text/xml");
			}

		} catch (Exception e) {
			LOG.error(e.toString());
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// @Transactional
	public void generateItemTTS(String id, Locale locale)
			throws FileNotFoundException, IOException {
		Item item = itemService.get(id, locale);
		boolean hasBeenCreated = false;
		List<LocalizedItem> lcs = item.getLocalizedItems(locale);
		for (LocalizedItem lc : lcs) {
			if (lc.getLabel().equals(Label.Description)) {
				/*
				 * Creates a file content representing the mp3
				 */
				if (StringUtils.isNotEmpty(lc.getValue())) {
					String fileName = id + "_" + locale.toString() + ".mp3";
					String name = mp3Folder + "/" + fileName;
					// if (!new File(name).exists()) {

					Voice voice = locale.equals(Locale.ENGLISH) ? Voice.Kate
							: Voice.Paola;
					synchronized (this) {
						if (!new File(name).exists()) {
							InputStream mp3 = generateMp3FromServer("" + id, lc
									.getValue(), voice);

							IOUtils.copy(mp3, new FileOutputStream(name));
						}
						LocalizedItem localizedItem = new LocalizedItem(item,
								Label.Mp3, locale, fileName);

						item.getLocalizedItems().add(localizedItem);
						itemService.saveOrUpdate(item);

						LOG
								.info("Creating MP3 for file "
										+ name
										+ " . Adding the localized entry to the database");
						return;
					}
					// }else{
					// LOG.error("File "+name+" already exists");
					// throw new
					// RuntimeException("File "+name+" already exists");

					// }
				}
			}
		}

	}

	/*
	 * SELECT count(id) as total,category From item group by (category) order by
	 * total
	 */
	public void generateAllMp3Files(String category, Locale locale)
			throws FileNotFoundException, IOException {

		SearchFilter searchFilter = new SearchFilter();
		// searchFilter.setCategory(new Category(category));

		searchFilter.setLocale(locale);
		// searchFilter.setGidIdentifier("ZETEMA");
		;
		for (Item item : itemService.getItemsByCriteria(searchFilter)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Generating tts for file " + item.getItemId()
						+ ", locale " + locale);
			}
			generateItemTTS(item.getItemId(), locale);
			// generateItemTTS(item.getItemId(), Locale.ENGLISH);

		}
	}

	/**
	 * @param itemService
	 *            the itemService to set
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * @param mp3Folder
	 *            the mp3Folder to set
	 */
	public void setMp3Folder(String mp3Folder) {
		this.mp3Folder = mp3Folder;
	}

	/**
	 * @param tmpFolder
	 *            the tmpFolder to set
	 */
	public void setTmpFolder(String tmpFolder) {
		this.tmpFolder = tmpFolder;
	}

}
