package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.PageRetriever;
import org.nightcrawler.domain.crawler.RedirectPage;
import org.nightcrawler.domain.crawler.RegularPage;
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
	public void crawl(final URI uri, final HandlingStrategyBuilder<Page> strategyBuilder) {
		asyncHttpClient.prepareGet(uri.toString()).execute(new AsyncCompletionHandler<Response>() {
			@Override
			public Response onCompleted(final Response response) throws Exception {			
				if (response.getStatusCode() == 301) { // Redirect	
					strategyBuilder.build(RedirectPage.builder()).link(location(uri, response)).process();
				}
				else {
					asyncParser.parse(response.getResponseBody(), strategyBuilder.build(RegularPage.builder()));
				}
				return response;
			}

			private URI location(final URI uri, final Response response) {
				return uri.resolve(URI.create(response.getHeader("Location")));
			}
			
			@Override
			public void onThrowable(final Throwable t) {
				strategyBuilder.build(RegularPage.builder()).process();
				super.onThrowable(t);
			}

		});
	}	
}
