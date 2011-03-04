/*
 * @(#)RomeRssReaderTest.java     Mar 3, 2011
 *
 * Copyright (c) 2010 Innovation Engineering S.r.l. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.matchpoint.smartmove.content.rss;

import java.net.URL;
import java.util.Iterator;

import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * 
 * @author mccalv
 * @since Mar 3, 2011
 * 
 */
public class RomeRssReaderTest {
	/**
	 * 
	 */
	private static final String THISISLONDON_CO_UK_THEATRE_RSS = "http://www.thisislondon.co.uk/theatre/rss/?ito=1588";

	@Test
	public void testRssRead() throws Exception {

		URL feedSource = new URL(THISISLONDON_CO_UK_THEATRE_RSS);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));

		Iterator<SyndEntry> entryIter = feed.getEntries().iterator();
		while (entryIter.hasNext()) {
			SyndEntry entry = entryIter.next();
			String description  = entry.getDescription().getValue();
			entry.getPublishedDate();
			String title = entry.getTitle();
			System.out.println(title);
			System.out.println(description);

		}

	}
}