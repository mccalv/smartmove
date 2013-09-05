package com.wimove.content.protocol.http;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.closertag.smartmove.server.content.domain.GpsPosition;
import com.closertag.smartmove.server.service.atac.AtacCalcolaPercorsoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
public class CalcolaPercorsoIntegrationTest {

	@Autowired
	AtacCalcolaPercorsoService atacCalcolaPercorsoService;

	/**
	 * Integration class for italian Atac backend service con calculate a route
	 * from two point
	 */
	@Test
	public void testCalcolaPercorso() {
		String calcolaPercorso = atacCalcolaPercorsoService.calcolaPercorso(
				new GpsPosition("via Olevano Romano,71", "Roma"),
				new GpsPosition("Piazza di Spagna, 26", "Roma")

		);
		System.out.println(calcolaPercorso);
		assertNotNull(calcolaPercorso);
	}

}