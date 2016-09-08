package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.index.PageIndex;

public class ConcurrentCrawler implements Crawler {

	private final PageRetriever pageRetriever = null;
	
	@Override
	public <T> T crawl(final URI uri, final Supplier<T> supplier) {
		//pageRetriever.retrieve(uri, handler);
		return supplier.get();
	}	
}
