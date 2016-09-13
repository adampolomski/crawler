package org.nightcrawler.infrastructure.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.PageRetriever;
import org.nightcrawler.domain.crawler.RedirectPage;
import org.nightcrawler.domain.crawler.RegularPage;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

/**
 * Retrieves data with a given HTTP client and uses a specified parser to feed the processing strategy.
 * 
 * @author Adam Polomski
 *
 */
public class AsyncPageRetriever extends PageRetriever {

	private final AsyncHttpClient asyncHttpClient;
	private final AsyncParser asyncParser;

	public AsyncPageRetriever(final AsyncHttpClient asyncHttpClient, final AsyncParser asyncParser) {
		this.asyncHttpClient = asyncHttpClient;
		this.asyncParser = asyncParser;
	}

	@Override
	public void crawl(final URL url, final HandlingStrategyBuilder<Page> strategyBuilder) {
		asyncHttpClient.prepareGet(url.toString()).execute(new AsyncCompletionHandler<Response>() {
			@Override
			public Response onCompleted(final Response response) throws Exception {			
				if (response.getStatusCode() == 301) { // Redirect	
					final HandlingStrategy strategy = strategyBuilder.build(RedirectPage.builder());
					location(url, response).ifPresent(strategy::link);
					strategy.process();
				}
				else {
					asyncParser.parse(response.getResponseBody(), strategyBuilder.build(RegularPage.builder()));
				}
				return response;
			}

			private Optional<URL> location(final URL url, final Response response) {		
				try {
					return Optional.of(new URL(url, response.getHeader("Location")));
				} catch (MalformedURLException e) {
					return Optional.empty();
				}
			}
			
			@Override
			public void onThrowable(final Throwable t) {
				strategyBuilder.build(RegularPage.builder()).process();
				super.onThrowable(t);
			}

		});
	}	
}
