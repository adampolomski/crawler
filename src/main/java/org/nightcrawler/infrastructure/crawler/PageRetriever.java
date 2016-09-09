package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

public abstract class PageRetriever {

	public abstract void crawl(URI uri, Consumer<Page> handler);
	
	public void crawl(final URI uri) {
		crawl(uri, p -> {});
	}
	
}
