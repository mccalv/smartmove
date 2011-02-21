/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 24 Nov 2009
 * mccalv
 * ZetemaMuseiXmlParsing
 * 
 */
package com.wimove.content.provider;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.wimove.content.service.ParsingService;

/**
 * It parses data coming from a different source. At this stage, data are not
 * provided by a relative XSD schema
 * 
 * @author mccalv
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
 
public class CopyOfZetemaImportXmlParsingTest {  

	private static final String EVENT_FILE_LOCATION = "snippet/";

	@Autowired
	ParsingService parsingService;

	@SuppressWarnings("unchecked")
	@Test
	public void testParsing() throws Exception {

		// File folder = new ClassPathResource(EVENT_FILE_LOCATION).getFile();
		Collection<File> listFiles = FileUtils.listFiles(new ClassPathResource(
				EVENT_FILE_LOCATION).getFile(), new String[] { "xml" }, false);
		for (File file : listFiles) {
			//if (file.getName().equals("eventi-e-spettacoli_20100216.xml")) {
				parsingService.importXmlFile(file);
			//}
		}
	}

}
