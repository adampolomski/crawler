package org.nightcrawler.domain.crawler;

import java.net.URL;

import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

/**
 * Retrieves page data from a given URL and passes it on according to given strategy.
 * 
 * @author Adam Polomski
 *
 */
public abstract class PageRetriever {

	public abstract void crawl(URL url, HandlingStrategyBuilder<Page> strategyBuilder);
	
	public void crawl(final URL url) {
		crawl(url, HandlingStrategy.builder(url));
	}	
}
