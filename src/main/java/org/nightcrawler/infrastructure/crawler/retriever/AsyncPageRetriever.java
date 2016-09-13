package org.nightcrawler.infrastructure.crawler.retriever;

import java.net.URL;
import java.util.function.Function;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.retriever.PageRetriever;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;

/**
 * Retrieves data with a given HTTP client.
 * 
 * @author Adam Polomski
 *
 */
public class AsyncPageRetriever<P> extends PageRetriever<P> {

	private final AsyncHttpClient asyncHttpClient;
	private final Function<HandlingStrategyBuilder<P>, AsyncHandler<Response>> asyncCompletionHandlerSupplier;

	public AsyncPageRetriever(final AsyncHttpClient asyncHttpClient, final Function<HandlingStrategyBuilder<P>, AsyncHandler<Response>> asyncCompletionHandlerSupplier) {
		this.asyncHttpClient = asyncHttpClient;
		this.asyncCompletionHandlerSupplier = asyncCompletionHandlerSupplier;
	}

	@Override
	public void crawl(final URL url, final HandlingStrategyBuilder<P> strategyBuilder) {
		asyncHttpClient.prepareGet(url.toString()).execute(asyncCompletionHandlerSupplier.apply(strategyBuilder));
	}	
}
