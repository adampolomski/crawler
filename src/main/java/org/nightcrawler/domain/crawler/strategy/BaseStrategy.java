package org.nightcrawler.domain.crawler.strategy;

import java.net.URL;
import java.util.function.Function;

import com.google.common.base.Preconditions;

/**
 * Base strategy. Fulfills the contract, but does nothing on it's own.
 * 
 * @author Adam Polomski
 *
 */
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