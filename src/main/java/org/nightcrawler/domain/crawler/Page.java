package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Objects;

import com.google.common.base.Preconditions;

public abstract class Page {

	protected final URI address;

	public Page(final URI address) {
		this.address = Preconditions.checkNotNull(address);
	}

	public abstract <T> T render(final Renderer<T> renderer);

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
}
