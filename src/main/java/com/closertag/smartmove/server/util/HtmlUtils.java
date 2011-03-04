/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Nov 2009
 * mccalv
 * HtmlUtils
 * 
 */
package com.closertag.smartmove.server.util;

/**
 * @author mccalv
 * 
 */
public class HtmlUtils {
	/**
	 * Removes all the Html tag from a given string and then turns HTML
	 * character references into their plain text UNICODE equivalent
	 * 
	 * @param htmlString
	 */
	public static String unescapeHtmlText(String htmlString) {

		String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
		return org.springframework.web.util.HtmlUtils
				.htmlUnescape(noHTMLString);
	}
}
