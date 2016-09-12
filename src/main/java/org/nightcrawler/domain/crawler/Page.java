package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.nightcrawler.infrastructure.crawler.parser.PageBuilder;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class Page {	
	private final URI address;
	private final Set<URI> links;
	private final Set<URI> resources;

	private Page(final URI address, final Set<URI> links, final Set<URI> resources) {
		this.address = Objects.requireNonNull(address);
		this.links = Objects.requireNonNull(links);
		this.resources = Objects.requireNonNull(resources);
	}

	public <T> T render(final Renderer<T> renderer) {		
		return renderer.address(address).links(links).resources(resources).build();
	}
	
	public void visitLinks(final Consumer<URI> visitor) {
		links.forEach(visitor);
	}

	@Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Page other = (Page) obj;
        return Objects.equals(this.address, other.address);
    }   

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("address", address).add("links", links).add("resources", resources)
				.toString();
	}

	public static PageBuilder<Page> builder(final URI address) {
		return new Builder(normalize(address));
	}
	
	public static class Builder implements PageBuilder<Page> {
		private final URI address;
		private final ImmutableSet.Builder<URI> links;
		private final ImmutableSet.Builder<URI> resources;
		
		private Builder(final URI address) {
			this.address = Preconditions.checkNotNull(address);
			this.links = ImmutableSet.builder();
			this.resources = ImmutableSet.builder();
		}
				
		@Override
		public <T> T forAddress(final Function<URI, T> transformation) {
			return transformation.apply(address);
		}
		
		@Override
		public Page build() {
			return new Page(address, links.build(), resources.build());
		}
		
		@Override
		public PageBuilder<Page> link(final URI link) {
			links.add(normalize(link));
			return this;
		}
				
		@Override
		public PageBuilder<Page> resource(final URI resource) {
			resources.add(resource);
			return this;
		}
	}
	
	private static URI normalize(final URI uri) {
		return URI.create(uri.toString().replaceAll("/$", ""));
	}
}
