/**
 * 
 */
package com.closertag.smartmove.server.content.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.closertag.smartmove.server.content.service.BatchContentsService;
import com.closertag.smartmove.server.content.service.ItemService;
import com.closertag.smartmove.server.service.pearson.PearsonService;

/**
 * @author mccalv
 * 
 */
@ManagedResource(objectName = "bean:name=SmartMove Manager", description = "Wimove Item Jms Manager", log = true, logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "foo", persistName = "bar")
public class JmxItemsMbean {

	private ItemService itemService;
	private BatchContentsService batchContentService;

	private PearsonService pearsonService;

	@ManagedOperation(description = "Remove Items from a Provider")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "gidName", description = "Gid Name") })
	public String removeItems(String gidName) {
		itemService.deleteItemsByGid(gidName);
		return "Removed Attributed of" + gidName;
	}

	@ManagedOperation(description = "Import contents from Pearson")
	@ManagedOperationParameters({
			@ManagedOperationParameter(name = "lat", description = "Lat"),
			@ManagedOperationParameter(name = "lon", description = "lon") })
	public String removeItems(String lat, String lon) {
		try {
			pearsonService.importContentsFromGps(Double.valueOf(lat),
					Double.valueOf(lon));
			return "Contents imported";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	// @ManagedOperation(description = "Reimport all contents")
	public String importAllZetema() {
		batchContentService.importAll();
		return "Content imported";
	}

	// @ManagedOperation(description = "Reimport all csv contents")
	public String importAllCSV() {
		batchContentService.importCsvContents();
		return "Content imported";
	}

	public void dontExposeMe() {
		throw new RuntimeException();
	}

	/*
	 * Spring Setter
	 */
	/**
	 * @param itemService
	 *            the itemService to set
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * @param batchContentService
	 *            the batchContentService to set
	 */
	public void setBatchContentService(BatchContentsService batchContentService) {
		this.batchContentService = batchContentService;
	}

	/**
	 * @param pearsonService
	 *            the pearsonService to set
	 */
	public void setPearsonService(PearsonService pearsonService) {
		this.pearsonService = pearsonService;
	}

}
