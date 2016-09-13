package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Optional;

import org.nightcrawler.domain.crawler.strategy.PageBuilder;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

public class RedirectPage extends Page {
	private final URI location;

	private RedirectPage(final URI address, final URI location) {
		super(address);
		this.location = Preconditions.checkNotNull(location);		
	}

	public <T> T render(final Renderer<T> renderer) {
		return renderer.address(address).redirect(location).build();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("address", address).add("location", location).toString();
	}
	
	public static PageBuilder<Page> builder() {
		return new RedirectBuilder();
	}

	public static class RedirectBuilder implements PageBuilder<Page> {
		private Optional<URI> location = Optional.empty();		

		@Override
		public Page build(final URI address) {
			return new RedirectPage(address, location.orElse(address));
		}

		@Override
		public PageBuilder<Page> link(final URI link) {
			this.location = Optional.of(link);
			return this;
		}

		@Override
		public PageBuilder<Page> resource(final URI resource) {			
			return this;
		}
	}
}