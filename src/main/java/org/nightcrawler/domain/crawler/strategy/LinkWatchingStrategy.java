package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

public class LinkWatchingStrategy extends HandlingStrategy {

	private final Consumer<URL> linkHandler;
	private final HandlingStrategy base;

	@VisibleForTesting
	LinkWatchingStrategy(final HandlingStrategy base, final Consumer<URL> linkHandler) {
		this.linkHandler = Preconditions.checkNotNull(linkHandler);
		this.base = Preconditions.checkNotNull(base);
	}

	@Override
	public <T> T forAddress(final Function<URL, T> transformation) {
		return base.forAddress(transformation);
	}

	public HandlingStrategy link(final URL link) {
		base.link(link);
		linkHandler.accept(link);
		return this;
	}

	public HandlingStrategy resource(final URI resource) {
		base.resource(resource);
		return this;
	}

	public void process() {
		base.process();
	}

	public static <P> HandlingStrategyBuilder<P> wrap(final HandlingStrategyBuilder<P> builder, final Consumer<URL> linkHandler) {
		return new HandlingStrategyBuilder<P>() {
			@Override
			public HandlingStrategy build(final PageBuilder<P> pageBuilder) {
				return new LinkWatchingStrategy(builder.build(pageBuilder), linkHandler);
			}

			@Override
			public HandlingStrategyBuilder<P> copy(final URL address) {				
				return wrap(builder.copy(address), linkHandler);
			}

			@Override
			public HandlingStrategyBuilder<P> handler(final Consumer<P> handler) {
				builder.handler(handler);
				return this;
			}
		};
	}
}