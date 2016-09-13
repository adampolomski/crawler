package org.nightcrawler.infrastructure.crawler.parser;

import java.util.concurrent.Executor;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

import com.google.common.base.Preconditions;

/**
 * Wraps parser with asynchronous behavior.
 * 
 * @author Adam Polomski
 *
 */
public class AsyncParser {

	private final PageParser delegate;
	private final Executor executor;
	
	public AsyncParser(final PageParser delegate, final Executor executorService) {
		this.delegate = Preconditions.checkNotNull(delegate);
		this.executor = Preconditions.checkNotNull(executorService);
	}

	public <P> void parse(final String content, final HandlingStrategy strategy) {
		executor.execute(() -> delegate.parse(content, strategy));
	}
}
