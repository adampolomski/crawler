package org.nightcrawler.infrastructure.crawler.parser;

import java.net.URI;
import java.util.function.Function;

public interface PageBuilder<P> {

	<T> T forAddress(Function<URI, T> transformation);

	PageBuilder<P> link(URI link);

	PageBuilder<P> resource(URI resource);
	
	P build();

}