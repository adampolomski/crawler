package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Binds together a PageBuilder and a Consumer for it's output.
 * 
 * @author Adam Polomski
 *
 */
class BuildingStrategy<P> extends HandlingStrategy {
	private final PageBuilder<P> builder;
	private final Consumer<P> handler;
	private final HandlingStrategy base;

	BuildingStrategy(HandlingStrategy base, PageBuilder<P> builder, Consumer<P> handler) {
		this.builder = builder;
		this.handler = handler;
		this.base = base;
	}

	@Override
	public <T> T forAddress(final Function<URL, T> transformation) {
		return base.forAddress(transformation);
	}

	public HandlingStrategy link(final URL link) {
		base.link(link);
		builder.link(link);
		return this;
	}

	public HandlingStrategy resource(final URI resource) {
		base.resource(resource);
		builder.resource(resource);
		return this;
	}

	public void process() {
		final URL URL = base.forAddress(Function.identity());
		handler.accept(builder.build(URL));
		base.process();
	}
}