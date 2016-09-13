package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.mockito.internal.util.collections.Sets;
import org.nightcrawler.domain.crawler.strategy.PageBuilder;

import com.google.common.collect.Maps;

public final class IntrospectingPageBuilder implements PageBuilder<Map<String, Object>> {
	final Map<String, Object> page = Maps.newHashMap();

	@Override
	public PageBuilder<Map<String, Object>> link(final URL link) {	
		addURL("links", link);
		return this;
	}

	@SuppressWarnings("unchecked")
	private void addURL(final String name, final Object link) {
		((Set<String>)page.computeIfAbsent(name, l->Sets.newSet())).add(link.toString());
	}

	@Override
	public PageBuilder<Map<String, Object>> resource(final URI resource) {
		addURL("resources", resource);
		return this;
	}

	@Override
	public Map<String, Object> build(URL address) {
		page.put("address", address);
		return page;
	}
}