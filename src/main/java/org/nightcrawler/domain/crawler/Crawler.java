package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Set;

import org.nightcrawler.domain.crawler.page.Page;

public interface Crawler {

	public Set<Page> crawl(final URI uri);
	
}
