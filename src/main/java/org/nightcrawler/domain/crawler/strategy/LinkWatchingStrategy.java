package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

public class LinkWatchingStrategy extends HandlingStrategy {

	private final Consumer<URI> linkHandler;
	private final HandlingStrategy base;

	@VisibleForTesting
	LinkWatchingStrategy(final HandlingStrategy base, final Consumer<URI> linkHandler) {
		this.linkHandler = Preconditions.checkNotNull(linkHandler);
		this.base = Preconditions.checkNotNull(base);
	}

	@Override
	public <T> T forAddress(final Function<URI, T> transformation) {
		return base.forAddress(transformation);
	}

	public HandlingStrategy link(final URI link) {
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

	public static <P> HandlingStrategyBuilder<P> wrap(final HandlingStrategyBuilder<P> builder, final Consumer<URI> linkHandler) {
		return new HandlingStrategyBuilder<P>() {
			@Override
			public HandlingStrategy build(final PageBuilder<P> pageBuilder) {
				return new LinkWatchingStrategy(builder.build(pageBuilder), linkHandler);
			}

			@Override
			public HandlingStrategyBuilder<P> copy(final URI address) {				
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