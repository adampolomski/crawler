package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		public Page build(final URI address) {
			return new Page(address, Collections.emptySet(), Collections.emptySet());
		}
	}
}
