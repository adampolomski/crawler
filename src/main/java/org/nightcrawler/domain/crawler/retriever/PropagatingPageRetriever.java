package org.nightcrawler.domain.crawler.retriever;

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
public class PropagatingPageRetriever<P> extends PageRetriever<P> {

	private final PageRetriever<P> delegateRetriever;
	private final Aquireable<URL, P> index;

	public PropagatingPageRetriever(final PageRetriever<P> delegateRetriever, final Aquireable<URL, P> index) {
		this.delegateRetriever = Preconditions.checkNotNull(delegateRetriever);
		this.index = Preconditions.checkNotNull(index);
	}

	@Override
	public void crawl(final URL URL, final HandlingStrategyBuilder<P> strategyBuilder) {		
		index.aquire(URL).ifPresent(writer -> delegateRetriever.crawl(URL,
				delegateStrategyBuilder(strategyBuilder, URL, writer)));
	}

	private HandlingStrategyBuilder<P> delegateStrategyBuilder(final HandlingStrategyBuilder<P> strategyBuilder,
			final URL normalizedURL, final Consumer<P> writer) {
		return LinkWatchingStrategy.wrap(strategyBuilder.copy(normalizedURL).handler(writer), link -> {
			if (link.getHost().equals(normalizedURL.getHost()))
				crawl(link, strategyBuilder.copy(link));			
		});
	}
}
