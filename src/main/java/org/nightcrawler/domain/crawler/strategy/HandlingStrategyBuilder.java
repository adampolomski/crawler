package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.function.Consumer;

public interface HandlingStrategyBuilder<P> {

	HandlingStrategy build(PageBuilder<P> pageBuilder);
	
	HandlingStrategyBuilder<P> copy(final URI address);

	HandlingStrategyBuilder<P> handler(Consumer<P> handler);

}