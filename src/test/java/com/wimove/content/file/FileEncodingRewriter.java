package com.wimove.content.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.junit.Test;

/**
 * @author mccalv
 * 
 */

public class FileEncodingRewriter {

	private static final String LATIN1_ENCODED_FILE = "";
	private static final String UTF8_ENCODED_FILE = "";

	/**
	 * Rewrites a LATIN1 file to UTF-8
	 */
	@Test
	public void testEncodingRewrite() throws Exception {
		new FileReader(new File(LATIN1_ENCODED_FILE));
	
		File file = new File(UTF8_ENCODED_FILE);
		
		FileOutputStream fOutPutStream = new FileOutputStream(file);
		
	
	}
}
