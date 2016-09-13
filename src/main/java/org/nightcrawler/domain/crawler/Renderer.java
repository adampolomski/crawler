package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.net.URL;

public interface Renderer<T> {

	Renderer<T> address(final URL address);
	
	Renderer<T> redirect(final URL address);

	Renderer<T> links(final Iterable<URL> links);

	Renderer<T> resources(final Iterable<URI> resources);
	
	T build();
}
