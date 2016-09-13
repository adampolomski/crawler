package org.nightcrawler.domain.crawler.strategy;

import java.net.URL;
import java.util.function.Consumer;

public interface HandlingStrategyBuilder<P> {

	HandlingStrategy build(PageBuilder<P> pageBuilder);
	
	HandlingStrategyBuilder<P> copy(final URL address);

	HandlingStrategyBuilder<P> handler(Consumer<P> handler);

}