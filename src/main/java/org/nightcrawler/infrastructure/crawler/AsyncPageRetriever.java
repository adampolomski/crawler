package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

public class AsyncPageRetriever extends PageRetriever {

	private final AsyncHttpClient asyncHttpClient;
	private final AsyncParser asyncParser;

	public AsyncPageRetriever(final AsyncHttpClient asyncHttpClient, final AsyncParser asyncParser) {
		this.asyncHttpClient = asyncHttpClient;
		this.asyncParser = asyncParser;
	}

	@Override
	public void crawl(final URI uri, final Consumer<Page> handler) {
		asyncHttpClient.prepareGet(uri.toString()).execute(new AsyncCompletionHandler<Response>() {
			@Override
			public Response onCompleted(final Response response) throws Exception {
				asyncParser.parse(response.getResponseBody(), handler);
				return response;
			}

		});
	}	
}
