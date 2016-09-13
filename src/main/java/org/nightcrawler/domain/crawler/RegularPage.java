package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import org.nightcrawler.domain.crawler.strategy.PageBuilder;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

public class RegularPage extends Page {
	private final Set<URI> links;
	private final Set<URI> resources;

	private RegularPage(final URI address, final Set<URI> links, final Set<URI> resources) {
		super(address);
		this.links = Objects.requireNonNull(links);
		this.resources = Objects.requireNonNull(resources);
	}

	public <T> T render(final Renderer<T> renderer) {
		return renderer.address(address).links(links).resources(resources).build();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("address", address).add("links", links).add("resources", resources).toString();
	}
	
	public static PageBuilder<Page> builder() {
		return new RegularBuilder();
	}

	public static class RegularBuilder implements PageBuilder<Page> {
		private final ImmutableSet.Builder<URI> links;
		private final ImmutableSet.Builder<URI> resources;

		private RegularBuilder() {
			this.links = ImmutableSet.builder();
			this.resources = ImmutableSet.builder();
		}

		@Override
		public Page build(final URI address) {
			return new RegularPage(address, links.build(), resources.build());
		}

		@Override
		public PageBuilder<Page> link(final URI link) {
			links.add(link);
			return this;
		}

		@Override
		public PageBuilder<Page> resource(final URI resource) {
			resources.add(resource);
			return this;
		}
	}
}