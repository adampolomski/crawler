package org.nightcrawler.infrastructure.crawler.retriever;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.RedirectPage;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;

import com.google.common.base.Preconditions;

class RedirectPageHandler extends DecoratingPageHandler {

	private final HandlingStrategyBuilder<Page> strategyBuilder;
	private final Executor executor;

	RedirectPageHandler(final HandlingStrategyBuilder<Page> strategyBuilder, final Executor executorService, final AsyncCompletionHandler<Response> fallback) {
		super(fallback);
		this.strategyBuilder = Preconditions.checkNotNull(strategyBuilder);
		this.executor = Preconditions.checkNotNull(executorService);
	}

	@Override
	Response onCompleted(final Response response, final UnaryOperator<Response> fallback) throws Exception {
		final int statusCode = response.getStatusCode();
		if (statusCode == 301 || statusCode == 302) { // Redirect
			final HandlingStrategy strategy = strategyBuilder.build(RedirectPage.builder());
			executor.execute(() -> parseRedirect(response, strategy));
		} else {
			return fallback.apply(response);
		}
		return response;
	}

	private static void parseRedirect(final Response response, final HandlingStrategy strategy) {
		final URL baseUrl = strategy.forAddress(Function.identity());
		location(baseUrl, response).ifPresent(strategy::link);
		strategy.process();
	}
	
	private static Optional<URL> location(final URL url, final Response response) {
		try {
			return Optional.of(new URL(url, response.getHeader("Location")));
		} catch (final MalformedURLException e) {
			return Optional.empty();
		}
	}

}
