/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 4 Jan 2010
 * mccalv
 * CreateUniqueIdentifierTest
 * 
 */
package com.wimove.content.identifier;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

/**
 * @author mccalv
 * 
 */
public class CreateUniqueIdentifierTest {

	private static final int NUMBER_OF_ATTEMPTS = 10000;

	@Test
	public void testUniqueIdentifier() {

		List<String> randomUUIDS = new ArrayList<String>();

		for (int i = 0; i < NUMBER_OF_ATTEMPTS; i++) {
			randomUUIDS.add(UUID.randomUUID().toString());
		}

		for (String randomUUID : randomUUIDS) {

			/*
			 * The single identifier is present just once on the list (itself)
			 * or never
			 */
			assertTrue(getNumberOfOccurrencies(randomUUIDS, randomUUID) <= 1);

		}

	}

	private int getNumberOfOccurrencies(List<String> randomUUIDS, String randomUUID) {
		int num = 0;
		for (String randomUUIDList : randomUUIDS) {

			if (randomUUID.equals(randomUUIDList))
				num++;

		}
		return num;
	}

}
