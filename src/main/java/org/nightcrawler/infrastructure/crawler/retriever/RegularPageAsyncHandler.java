package org.nightcrawler.infrastructure.crawler.retriever;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.RegularPage;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

class RegularPageAsyncHandler extends AsyncCompletionHandler<Response> {

	private final HandlingStrategyBuilder<Page> strategyBuilder;
	private final AsyncParser asyncParser;
	
	RegularPageAsyncHandler(HandlingStrategyBuilder<Page> strategyBuilder, AsyncParser asyncParser) {
		this.strategyBuilder = strategyBuilder;
		this.asyncParser = asyncParser;
	}

	@Override
	public Response onCompleted(final Response response) throws Exception {
		asyncParser.parse(response.getResponseBody(), strategyBuilder.build(RegularPage.builder()));
		return response;
	}
	
	@Override
	public void onThrowable(final Throwable t) {
		strategyBuilder.build(RegularPage.builder()).process();
		super.onThrowable(t);
	}	
}
