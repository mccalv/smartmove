/**
 * 
 */
package com.wimove.content.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.wimove.content.service.BatchContentsService;
import com.wimove.content.service.ItemService;

/**
 * @author mccalv
 * 
 */
@ManagedResource(objectName = "bean:name=wimoveItemsBeans", description = "Wimove Item Jms Manager", log = true, logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "foo", persistName = "bar")
public class JmxItemsMbean {


	private ItemService itemService;
	private BatchContentsService batchContentService;



	@ManagedOperation(description = "Remove Item Gid")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "gidName", description = "Gid Name") })
	public String removeItems(String gidName) {
		itemService.deleteItemsByGid(gidName);
		return "Removed Attributed of" + gidName;
	}

	@ManagedOperation(description = "Reimport all items from Zetema")
	public String importAllZetema() {
		batchContentService.importAll();
		return "Content imported";
	}
	

	@ManagedOperation(description = "Reimport all csv items from Atac")
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
	 * @param batchContentService the batchContentService to set
	 */
	public void setBatchContentService(BatchContentsService batchContentService) {
		this.batchContentService = batchContentService;
	}

	

}
