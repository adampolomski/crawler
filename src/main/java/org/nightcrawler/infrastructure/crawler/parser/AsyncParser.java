package org.nightcrawler.infrastructure.crawler.parser;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

public class AsyncParser {

	private final PageParser delegate;
	private final ExecutorService executorService;
	
	public AsyncParser(final PageParser delegate, final ExecutorService executorService) {
		this.delegate = Objects.requireNonNull(delegate);
		this.executorService = Objects.requireNonNull(executorService);
	}

	public void from(final String content, final Consumer<Page> handler) {
		executorService.execute(() -> handler.accept(delegate.from(content)));
	}
}
