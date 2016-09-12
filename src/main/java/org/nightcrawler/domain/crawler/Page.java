package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.base.MoreObjects;
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

	public static Builder builder(final URI address) {
		return new Builder(address);
	}
	
	public static class Builder {
		private final URI address;
		private final ImmutableSet.Builder<URI> links;
		private final ImmutableSet.Builder<URI> resources;
		
		private Builder(final URI address) {
			this.address = address;
			this.links = ImmutableSet.builder();
			this.resources = ImmutableSet.builder();
		}
		
		public <T> T forAddress(final Function<URI, T> transformation) {
			return transformation.apply(address);
		}

		public Page build() {
			return new Page(address, links.build(), Collections.emptySet());
		}

		public Builder link(final URI link) {
			links.add(link);
			return this;
		}
		
		public Builder resource(final URI resource) {
			resources.add(resource);
			return this;
		}
	}
}
