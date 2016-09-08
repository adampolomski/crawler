package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

public class AsyncPageRetriever implements PageRetriever {

	private final AsyncHttpClient asyncHttpClient = null;
	private final AsyncParser asyncParser = null;

	@Override
	public void retrieve(final URI uri, final Consumer<Page> handler) {
		asyncHttpClient.prepareGet(uri.toString()).execute(new AsyncCompletionHandler<Response>() {
			@Override
			public Response onCompleted(final Response response) throws Exception {
				asyncParser.from(response.getResponseBody(), handler);
				return response;
			}

		});
	}
}
