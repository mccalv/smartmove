package com.wimove.content.protocol;

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

/**
 * A class to test a generation of an hash code using a MD5 digest
 * 
 * @author mccalv
 * 
 */
public class HashGeneratorTest {
	/**
	 * Tests a generation of and Hashset without the error
	 * 
	 * @throws Exception
	 */
	@Test
	public void voidTestHashGenerator() throws Exception {

		assertEquals(32, generateHashCode("www.zetema.it").length());
		assertEquals(32, generateHashCode("www.atac.it").length());
		assertEquals(32, generateHashCode("www.mccalv.com").length());

	}

	
	/**
	 * Generates the StringBuffer of the hashcode for a given String. It uses a MD5 digest.
	 * 
	 * @param plainText
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private StringBuffer generateHashCode(String plainText)
			throws NoSuchAlgorithmException {

		MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
		mdAlgorithm.update(plainText.getBytes());

		byte[] digest = mdAlgorithm.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			plainText = Integer.toHexString(0xFF & digest[i]);

			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}

			hexString.append(plainText);
		}
		return hexString;
	}

}
