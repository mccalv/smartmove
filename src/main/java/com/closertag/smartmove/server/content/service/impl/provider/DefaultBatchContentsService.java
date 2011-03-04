/**
 * 
 */
package com.closertag.smartmove.server.content.service.impl.provider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.closertag.smartmove.server.content.domain.Item;
import com.closertag.smartmove.server.content.persistence.BatchRepository;
import com.closertag.smartmove.server.content.service.BatchContentsService;
import com.closertag.smartmove.server.content.service.ParsingService;
import com.closertag.smartmove.server.service.audio.TTSServiceManager;

/**
 * @author mccalv
 * 
 */
@SuppressWarnings("unchecked")
public class DefaultBatchContentsService implements BatchContentsService {

	private static final Log LOG = LogFactory
			.getLog(DefaultBatchContentsService.class);

	private String folderPath;
	private String backupFolder;
	private String csvfolderPath;

	private ParsingService parsingService;
	private BatchRepository batchRepository;
	private TTSServiceManager ttsServiceManager;

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.service.ImportService#importAll()
	 */

	public void importAll() {

		File sourceFolder = new File(folderPath);
		Collection<File> listFiles = FileUtils.listFiles(sourceFolder,
				new String[] { "xml" }, false);

		Object[] files = listFiles.toArray();
		int i = 0;
		List<File> processedFiles = new ArrayList<File>();
		while (i < files.length) {
			File f = (File) files[i];
			try {
				List<Item> items = parsingService.importXmlFile(f);
				if (items.size() > 0) {
					/*
					 * the items need to have at least one localized string
					 */
					batchRepository.updateItems(items, items.get(0)
							.getLocalizedItems().get(0).getLocale());

				}
				processedFiles.add(f);
				/*
				 * Moved the generated file to the backup folder
				 */
				FileUtils.moveFile(f, new File(backupFolder + "bckup_"
						+ new java.util.Date().getTime() + "_" + f.getName()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wimove.content.service.BatchContentsService#importCsvContents()
	 */

	public void importCsvContents() {
		File sourceFolder = new File(csvfolderPath);
		Collection<File> listFiles = FileUtils.listFiles(sourceFolder,
				new String[] { "csv" }, false);

		for (File csvFile : listFiles) {
			try {
				LOG.info("Parsing File "+csvFile.getName() + " from line 1 to line 2000");
				 persisteItemFromCsvFile(csvFile);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private List<Item> persisteItemFromCsvFile(File csvFile) throws Exception,
			IOException {
		List<Item> items = parsingService.importCsvFile(csvFile);
		if (!items.isEmpty()) {
			
			//int n = batchRepository.removeItemsFromCategory(category);
			//LOG.info("Removed " + n + " elements of category: " + category+". Starting new import.");
			//batchRepository.updateItems(items, Locale.ITALIAN);
			batchRepository.updateItems(items, null);
			/*
			 * Moved the generated file to the backup folder
			 */
			FileUtils.moveFile(csvFile, new File(backupFolder + "bckup_"
					+ new java.util.Date().getTime() + "_" + csvFile.getName()));
		}
		return items;
	}

	public int removeExpiredItems() {

		int n = batchRepository.removeExpiredItems();
		LOG.info("Removed " + n + " non active contents");
		return n;

	}

	@Override
	public void createMp3(int howmany) {
		// batchImportRep
		// TODO Auto-generated method stub
		 List<Map<String, Object>> itemsWithoutLocale = batchRepository
				.getItemsWithoutLocale(howmany);
		for (Map<String, Object> map : itemsWithoutLocale) {

			String item_id = (String)map.get("item_id");
			try {
				Locale locale = new Locale((String)map.get("locale"));
				ttsServiceManager.generateItemTTS(item_id, locale);

			} catch (Exception e) {
				LOG.error("Error trying to create Mp3 for file " + item_id);

			}
		}
	}

	/*
	 * Spring setter
	 */

	/**
	 * @param folderPath
	 *            the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * @param csvfolderPath
	 *            the csvfolderPath to set
	 */
	public void setCsvfolderPath(String csvfolderPath) {
		this.csvfolderPath = csvfolderPath;
	}

	/**
	 * @param parsingService
	 *            the parsingService to set
	 */
	public void setParsingService(ParsingService parsingService) {
		this.parsingService = parsingService;
	}

	/**
	 * @param batchRepository
	 *            the batchRepository to set
	 */
	public void setBatchRepository(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

	/**
	 * @param backupFolder
	 *            the backupFolder to set
	 */
	public void setBackupFolder(String backupFolder) {
		this.backupFolder = backupFolder;
	}

	/**
	 * @param ttsServiceManager
	 *            the ttsServiceManager to set
	 */
	public void setTtsServiceManager(TTSServiceManager ttsServiceManager) {
		this.ttsServiceManager = ttsServiceManager;
	}


}
