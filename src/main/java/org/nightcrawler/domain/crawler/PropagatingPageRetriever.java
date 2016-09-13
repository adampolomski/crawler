package org.nightcrawler.domain.crawler;

import java.net.URL;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.index.Aquireable;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.domain.crawler.strategy.LinkWatchingStrategy;

import com.google.common.base.Preconditions;

/**
 * Propagates crawling to all linked pages.
 * 
 * @author Adam Polomski
 *
 */
public class PropagatingPageRetriever extends PageRetriever {

	private final PageRetriever delegateRetriever;
	private final Aquireable<URL, Page> index;

	public PropagatingPageRetriever(final PageRetriever delegateRetriever, final Aquireable<URL, Page> index) {
		this.delegateRetriever = Preconditions.checkNotNull(delegateRetriever);
		this.index = Preconditions.checkNotNull(index);
	}

	@Override
	public void crawl(final URL URL, final HandlingStrategyBuilder<Page> strategyBuilder) {		
		index.aquire(URL).ifPresent(writer -> delegateRetriever.crawl(URL,
				delegateStrategyBuilder(strategyBuilder, URL, writer)));
	}

	private HandlingStrategyBuilder<Page> delegateStrategyBuilder(final HandlingStrategyBuilder<Page> strategyBuilder,
			final URL normalizedURL, final Consumer<Page> writer) {
		return LinkWatchingStrategy.wrap(strategyBuilder.copy(normalizedURL).handler(writer), link -> {
			if (link.getHost().equals(normalizedURL.getHost()))
				crawl(link, strategyBuilder.copy(link));			
		});
	}
}
