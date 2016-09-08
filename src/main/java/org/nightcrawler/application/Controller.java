package org.nightcrawler.application;

import java.net.URI;
import java.util.Set;

import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;

public class Controller {

	private final Crawler crawler = null;
	
	public String generatePageMap(final URI uri) {
		final Set<Page> pages = crawler.crawl(uri);			
		return null;		
	}
}
