package org.nightcrawler.infrastructure.crawler.parser;

import java.util.concurrent.ExecutorService;

import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

import com.google.common.base.Preconditions;

public class AsyncParser {

	private final PageParser delegate;
	private final ExecutorService executorService;
	
	public AsyncParser(final PageParser delegate, final ExecutorService executorService) {
		this.delegate = Preconditions.checkNotNull(delegate);
		this.executorService = Preconditions.checkNotNull(executorService);
	}

	public <P> void parse(final String content, final HandlingStrategy strategy) {
		executorService.execute(() -> delegate.parse(content, strategy));
	}
}
