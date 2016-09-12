package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Supplier;

import org.asynchttpclient.AsyncHttpClient;
import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.PageRetriever;
import org.nightcrawler.domain.crawler.PropagatingPageRetriever;
import org.nightcrawler.domain.crawler.index.Index;
import org.nightcrawler.infrastructure.crawler.index.BlockingIndexWrapper;
import org.nightcrawler.infrastructure.crawler.index.ConcurrentIndex;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

public class ConcurrentCrawlerFactory implements Supplier<Crawler> {

	private final AsyncHttpClient httpClient;
	private final AsyncParser parser;

	public ConcurrentCrawlerFactory(final AsyncHttpClient httpClient, final AsyncParser parser) {
		this.httpClient = httpClient;
		this.parser = parser;
	}

	@Override
	public Crawler get() {
		return uri -> {
			final Index<URI, Page> index = index();
			retriever(index).crawl(uri);
			return index.all();
		};
	}

	private PageRetriever retriever(final Index<URI, Page> index) {
		return new PropagatingPageRetriever(new AsyncPageRetriever(httpClient, parser), index);		
	}

	private Index<URI, Page> index() {
		return BlockingIndexWrapper.wrap(new ConcurrentIndex<>());		
	}

}
