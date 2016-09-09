package org.nightcrawler;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.asynchttpclient.DefaultAsyncHttpClient;
import org.nightcrawler.application.Controller;
import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.ConcurrentCrawlerFactory;
import org.nightcrawler.infrastructure.crawler.parser.AsyncParser;
import org.nightcrawler.infrastructure.crawler.parser.PageParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

	private static final URI GOOGLE = URI.create("www.google.com");

	@Bean
	Controller controller() {
		Supplier<Crawler> crawlerFactory = new ConcurrentCrawlerFactory(httpClient(), new AsyncParser(
				parser(), executorService()));
		return new Controller(crawlerFactory);
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newSingleThreadExecutor();
	}

	@Bean
	public DefaultAsyncHttpClient httpClient() {
		return new DefaultAsyncHttpClient();
	}

	private PageParser parser() {
		return new PageParser() {
			
			@Override
			public Page parse(final String content) {
				System.out.println(content);				
				return Page.builder().build(GOOGLE);
			}
		};
	}
}
