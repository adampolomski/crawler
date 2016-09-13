package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public final class IntrospectingRenderer implements Renderer<Map<String, Object>> {
	final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

	@Override
	public Renderer<Map<String, Object>> address(final URL address) {
		builder.put("a", address);
		return this;
	}

	@Override
	public Renderer<Map<String, Object>> links(final Iterable<URL> links) {
		builder.put("l", links);
		return this;
	}

	@Override
	public Renderer<Map<String, Object>> resources(final Iterable<URI> resources) {
		builder.put("r", resources);
		return this;
	}

	@Override
	public Map<String, Object> build() {
		return builder.build();
	}

	@Override
	public Renderer<Map<String, Object>> redirect(URL address) {
		builder.put("rd", address);
		return this;
	}
}