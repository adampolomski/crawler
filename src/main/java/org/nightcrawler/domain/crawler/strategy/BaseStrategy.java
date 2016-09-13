package org.nightcrawler.domain.crawler.strategy;

import java.net.URL;
import java.util.function.Function;

import com.google.common.base.Preconditions;

class BaseStrategy extends HandlingStrategy {

	private final URL address;

	BaseStrategy(final URL address) {
		this.address = Preconditions.checkNotNull(address);
	}

	@Override
	public <T> T forAddress(final Function<URL, T> transformation) {
		return transformation.apply(address);
	}
}