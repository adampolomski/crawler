package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Set;
import java.util.function.Supplier;

public interface Crawler {

	public <T> T crawl(final URI uri, final Supplier<T> supplier);
	
}
