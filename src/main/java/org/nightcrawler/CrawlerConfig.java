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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

	@Bean
	Controller controller() {
		Supplier<Crawler> crawlerFactory = new ConcurrentCrawlerFactory(httpClient(),
				new AsyncParser(new JsoupPageParser(), parserExecutorService()));
		return new Controller(crawlerFactory);
	}

	@Bean
	public ExecutorService parserExecutorService() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@Bean
	public DefaultAsyncHttpClient httpClient() {
		return new DefaultAsyncHttpClient(
				new DefaultAsyncHttpClientConfig.Builder().setFollowRedirect(true).build());
	}
}
