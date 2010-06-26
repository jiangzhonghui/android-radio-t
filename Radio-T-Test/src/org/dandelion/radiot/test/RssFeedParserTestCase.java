package org.dandelion.radiot.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;

import org.dandelion.radiot.PodcastItem;
import org.dandelion.radiot.RssFeedParser;
import org.xml.sax.SAXException;

import android.net.Uri;

public class RssFeedParserTestCase extends TestCase {

	private RssFeedParser provider;
	private String feedContent;
	private List<PodcastItem> parsedItems;
	private PodcastItem firstParsedItem;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new RssFeedParser();
		feedContent = "";
	}

	public void testCreateAppropriateNumberOfPodcastItems() throws Exception {
		newFeedItem("");
		newFeedItem("");

		parseRssFeed();

		assertEquals(2, parsedItems.size());
		assertNotNull(parsedItems.get(0));
	}

	public void testExtractingPodcastNumber() throws Exception {
		newFeedItem("<title>Radio 192</title>");

		parseRssFeed();

		assertEquals(192, firstParsedItem.getNumber());
	}

	public void testExtractPodcastDate() throws Exception {
		newFeedItem("<pubDate>Sun, 13 Jun 2010 01:37:22 +0000</pubDate>");

		parseRssFeed();
		String strDate = new SimpleDateFormat("dd.MM.yyyy")
				.format(firstParsedItem.getPubDate());
		assertEquals("13.06.2010", strDate);
	}

	public void testExtractShowNotes() throws Exception {
		newFeedItem("<description><![CDATA[Show notes]]></description>");
		parseRssFeed();
		assertEquals("Show notes", firstParsedItem.getShowNotes());
	}

	public void testExtractPodcastLink() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>");
		parseRssFeed();
		assertEquals(Uri.parse("http://podcast-link"),
				firstParsedItem.getAudioUri());
	}

	public void testSkipNonAudioEnsclosures() throws Exception {
		newFeedItem("<enclosure url=\"http://podcast-link\" type=\"audio/mpeg\"/>"
				+ "<enclosure url=\"http://yet-another-link\" type=\"text/xml\"/>");
		
		parseRssFeed();
		
		assertEquals(Uri.parse("http://podcast-link"),
				firstParsedItem.getAudioUri());
	}

	private void newFeedItem(String itemContent) {
		feedContent = feedContent + "<item>" + itemContent + "</item>";
	}

	private void parseRssFeed() throws SAXException, IOException {
		InputStream stream = new ByteArrayInputStream(getCompleteFeed()
				.getBytes());
		parsedItems = provider.readRssFeed(stream);
		firstParsedItem = parsedItems.get(0);
	}

	private String getCompleteFeed() {
		return "<rss><channel>" + feedContent + "</channel></rss>";
	}
}