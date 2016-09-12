package org.nightcrawler.infrastructure.crawler.parser;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.Page.Builder;

import com.google.common.base.Preconditions;

public class AsyncParser {

	private final PageParser delegate;
	private final ExecutorService executorService;
	
	public AsyncParser(final PageParser delegate, final ExecutorService executorService) {
		this.delegate = Preconditions.checkNotNull(delegate);
		this.executorService = Preconditions.checkNotNull(executorService);
	}

	public void parse(final String content, final Builder builder, final Consumer<Page> handler) {
		executorService.execute(() -> handler.accept(delegate.parse(content, builder)));
	}
}
