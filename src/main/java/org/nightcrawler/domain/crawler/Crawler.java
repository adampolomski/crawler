package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Set;

public interface Crawler {

	public Set<Page> crawl(final URI uri);
	
}
