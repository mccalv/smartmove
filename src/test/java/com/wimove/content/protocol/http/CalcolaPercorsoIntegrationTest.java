package com.wimove.content.protocol.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wimove.content.domain.GpsPosition;
import com.wimove.service.atac.AtacCalcolaPercorsoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
public class CalcolaPercorsoIntegrationTest {

	

	@Autowired
	AtacCalcolaPercorsoService atacCalcolaPercorsoService;

	@Test
	public void testCalcolaPercorso() {
		String calcolaPercorso = atacCalcolaPercorsoService.calcolaPercorso(
				new GpsPosition("via Olevano Romano,71","Roma"), 
				new GpsPosition("Piazza di Spagna, 26","Roma")
				
		);
		System.out.println(calcolaPercorso);
	}

}