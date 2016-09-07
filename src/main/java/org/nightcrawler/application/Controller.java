package org.nightcrawler.application;

import java.net.URI;
import java.util.Set;

import org.nightcrawler.domain.Crawler;
import org.nightcrawler.domain.crawler.page.Page;
import org.nightcrawler.domain.crawler.page.PageRepository;

public class Controller {

	private final PageRepository repository = null;
	private final Crawler crawler = null;
	
	public String generatePageMap(final URI url) {
		Set<Page> pages = crawler.crawl(uri);
		
		
		return null;		
	}
}
repository