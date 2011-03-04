/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 24 Nov 2009
 * mccalv
 * ZetemaMuseiXmlParsing
 * 
 */
package com.wimove.content.service;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.closertag.smartmove.server.content.persistence.filter.SearchFilter;
import com.closertag.smartmove.server.content.service.ItemService;

/**
 * It parses data coming from a different source. At this stage, data are not
 * provided by a relative XSD schema
 * 
 * @author mccalv
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class ItemServiceTest {


	@Autowired
	ItemService itemService;

	@Test
	public void testSearchByFiter() throws Exception {

		SearchFilter searchFilter  = new SearchFilter();
		searchFilter.setLocale(Locale.ITALIAN);
		searchFilter.setOrderBy(SearchFilter.Order.TITLE);
		itemService.getItemsByCriteria(searchFilter);
		
	}

}
