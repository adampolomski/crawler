package org.nightcrawler.infrastructure.crawler.retriever;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;

import com.google.common.base.Preconditions;

public class AsyncCompletionHandlerFactory implements Function<HandlingStrategyBuilder<Page>, AsyncHandler<Response>> {

	private final AsyncParser asyncRegularParser;
	private final ExecutorService redirectExecutorService;
	
	public AsyncCompletionHandlerFactory(final AsyncParser asyncRegularParser, final ExecutorService redirectExecutorService) {
		this.asyncRegularParser = Preconditions.checkNotNull(asyncRegularParser);
		this.redirectExecutorService = Preconditions.checkNotNull(redirectExecutorService);
	}


	@Override
	public AsyncHandler<Response> apply(final HandlingStrategyBuilder<Page> strategyBuilder) {
		final RegularPageAsyncHandler regularHandler = new RegularPageAsyncHandler(strategyBuilder, asyncRegularParser);
		return new RedirectPageHandler(strategyBuilder, redirectExecutorService, regularHandler);
	}

}
