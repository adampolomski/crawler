package org.nightcrawler.domain.crawler;

import java.net.URI;
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
	private final Aquireable<URI, Page> index;

	public PropagatingPageRetriever(final PageRetriever delegateRetriever, final Aquireable<URI, Page> index) {
		this.delegateRetriever = Preconditions.checkNotNull(delegateRetriever);
		this.index = Preconditions.checkNotNull(index);
	}

	@Override
	public void crawl(final URI uri, final HandlingStrategyBuilder<Page> strategyBuilder) {
		final URI normalizedUri = normalize(uri);
		index.aquire(normalizedUri).ifPresent(writer -> delegateRetriever.crawl(normalizedUri,
				delegateStrategyBuilder(strategyBuilder, normalizedUri, writer)));
	}

	private HandlingStrategyBuilder<Page> delegateStrategyBuilder(final HandlingStrategyBuilder<Page> strategyBuilder,
			final URI normalizedUri, final Consumer<Page> writer) {
		return LinkWatchingStrategy.wrap(strategyBuilder.copy(normalizedUri).handler(writer), link -> {
			if (link.getHost().equals(normalizedUri.getHost()))
				crawl(link, strategyBuilder.copy(link));
		});
	}

	private static URI normalize(final URI uri) {
		return URI.create(uri.toString().replaceAll("/$", ""));
	}
}
