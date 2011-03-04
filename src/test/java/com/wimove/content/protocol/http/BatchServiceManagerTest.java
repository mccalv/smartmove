package com.wimove.content.protocol.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.closertag.smartmove.server.content.service.BatchContentsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
public class BatchServiceManagerTest {

	@Autowired
	BatchContentsService batchContentsService;
	/**
	 * Test for {@link BatchContentsService#createMp3(int)}
	 */
	//@Test
	public void testCreateMp3() {
		batchContentsService.createMp3(2);
	}
	
	
	@Test
	public void testImportAll() {
		batchContentsService.importAll();
	}


}