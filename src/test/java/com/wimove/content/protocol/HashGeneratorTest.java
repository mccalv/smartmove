package com.wimove.content.protocol;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

/**
 * 
 * @author mccalv
 * 
 */
public class HashGeneratorTest {

	@Test
	public void voidTestHashGenerator() throws Exception {
		String plainText = "123456";
		StringBuffer hexString = generateHashCode(plainText);

		System.out.println(generateHashCode("www.zetema.it"));
		System.out.println(generateHashCode("www.atac.it"));
		System.out.println(generateHashCode("www.mccalv.com"));
	//	System.out.print(generateHashCode("www.zetema.it"));
		

	}

	private StringBuffer generateHashCode(String plainText) throws NoSuchAlgorithmException {
		
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
