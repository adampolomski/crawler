package org.nightcrawler.domain.crawler;

import java.net.URI;

import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

/**
 * Retrieves page data from a given URI and passes it on according to given strategy.
 * 
 * @author Adam Polomski
 *
 */
public abstract class PageRetriever {

	public abstract void crawl(URI uri, HandlingStrategyBuilder<Page> strategyBuilder);
	
	public void crawl(final URI uri) {
		crawl(uri, HandlingStrategy.builder(uri));
	}	
}
