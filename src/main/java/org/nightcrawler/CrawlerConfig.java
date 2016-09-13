package org.nightcrawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.nightcrawler.application.Controller;
import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.infrastructure.crawler.ConcurrentCrawlerFactory;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;
import org.nightcrawler.infrastructure.crawler.parser.JsoupPageParser;
import org.nightcrawler.infrastructure.crawler.retriever.AsyncCompletionHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

	@Bean
	Controller controller() {
		final AsyncParser asyncParser = new AsyncParser(new JsoupPageParser(), parserExecutorService());
		final AsyncCompletionHandlerFactory completionHandlerFactory = new AsyncCompletionHandlerFactory(asyncParser, parserExecutorService());
		final Supplier<Crawler> crawlerFactory = new ConcurrentCrawlerFactory(httpClient(), completionHandlerFactory);
		return new Controller(crawlerFactory);
	}

	@Bean
	public ExecutorService parserExecutorService() {
		return Executors.newWorkStealingPool();
	}

	@Bean
	public DefaultAsyncHttpClient httpClient() {
		return new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setFollowRedirect(false).build());
	}
}
