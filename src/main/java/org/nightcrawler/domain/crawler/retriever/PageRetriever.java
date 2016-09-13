package org.nightcrawler.domain.crawler.retriever;

import java.net.URL;

import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;

/**
 * Retrieves page data from a given URL and passes it on according to given strategy.
 * 
 * @author Adam Polomski
 *
 */
public abstract class PageRetriever<P> {

	public abstract void crawl(URL url, HandlingStrategyBuilder<P> strategyBuilder);
	
	public void crawl(final URL url) {
		crawl(url, HandlingStrategy.builder(url));
	}	
}
