package org.nightcrawler.domain.crawler;

import java.net.URI;

public interface Renderer<T> {

	Renderer<T> address(final URI address);

	Renderer<T> links(final Iterable<URI> links);

	Renderer<T> resources(final Iterable<URI> resources);
	
	T build();
}
