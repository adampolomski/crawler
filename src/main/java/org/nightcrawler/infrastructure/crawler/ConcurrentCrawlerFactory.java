package org.nightcrawler.infrastructure.crawler;

import java.net.URL;
import java.util.function.Supplier;

import org.asynchttpclient.AsyncHttpClient;
import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.index.Index;
import org.nightcrawler.domain.crawler.retriever.PageRetriever;
import org.nightcrawler.domain.crawler.retriever.PropagatingPageRetriever;
import org.nightcrawler.infrastructure.crawler.index.BlockingIndexWrapper;
import org.nightcrawler.infrastructure.crawler.index.ConcurrentIndex;
import org.nightcrawler.infrastructure.crawler.retriever.AsyncCompletionHandlerFactory;
import org.nightcrawler.infrastructure.crawler.retriever.AsyncPageRetriever;

import com.google.common.base.Preconditions;

public class ConcurrentCrawlerFactory implements Supplier<Crawler> {

	private final AsyncHttpClient httpClient;
	private final AsyncCompletionHandlerFactory completionHandlerSupplier;

	public ConcurrentCrawlerFactory(final AsyncHttpClient httpClient, final AsyncCompletionHandlerFactory completionHandlerSupplier) {
		this.httpClient = Preconditions.checkNotNull(httpClient);
		this.completionHandlerSupplier = Preconditions.checkNotNull(completionHandlerSupplier);
	}

	@Override
	public Crawler get() {
		return url -> {
			final Index<URL, Page> index = index();
			retriever(index).crawl(url);
			return index.all();
		};
	}

	private PageRetriever<Page> retriever(final Index<URL, Page> index) {
		return new PropagatingPageRetriever<Page>(new AsyncPageRetriever<Page>(httpClient, completionHandlerSupplier), index);		
	}

	private Index<URL, Page> index() {
		return BlockingIndexWrapper.wrap(new ConcurrentIndex<>());		
	}

}
