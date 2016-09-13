package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;

public abstract class HandlingStrategy {

	public HandlingStrategy link(final URI link) { return this;	};

	public HandlingStrategy resource(final URI link) { return this;	};

	public abstract <T> T forAddress(final Function<URI, T> transformation);

	public void process() {};

	public static <P> HandlingStrategyBuilder<P> builder(final URI address) {
		return new BaseBuilder<>(new BaseStrategy(address), ImmutableSet.builder());
	}
	
	private static class BaseBuilder<P> implements HandlingStrategyBuilder<P> {
		private final BaseStrategy base;
		private final ImmutableSet.Builder<Consumer<P>> handlers;
		
		BaseBuilder(final BaseStrategy base, ImmutableSet.Builder<Consumer<P>> handlers) {
			this.base = base;
			this.handlers = handlers;
		}

		@Override
		public HandlingStrategy build(final PageBuilder<P> pageBuilder) {
			final Consumer<P> combinedHandler = handlers.build().stream().reduce(p -> {}, (a, b)->a.andThen(b));
			return new BuildingStrategy<>(base, pageBuilder, combinedHandler);			
		}

		@Override
		public HandlingStrategyBuilder<P> copy(final URI address) {
			return new BaseBuilder<>(new BaseStrategy(address), ImmutableSet.<Consumer<P>>builder().addAll(handlers.build()));
		}
		
		@Override
		public HandlingStrategyBuilder<P> handler(final Consumer<P> handler) {
			handlers.add(handler);
			return this;
		}
	}

}
