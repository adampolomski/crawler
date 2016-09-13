package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.function.Function;

import com.google.common.base.Preconditions;

class BaseStrategy extends HandlingStrategy {

	private final URI address;

	BaseStrategy(final URI address) {
		this.address = Preconditions.checkNotNull(address);
	}

	@Override
	public <T> T forAddress(final Function<URI, T> transformation) {
		return transformation.apply(address);
	}
}