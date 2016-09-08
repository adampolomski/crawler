package org.nightcrawler.infrastructure.crawler.index;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

import com.google.common.base.Preconditions;

public class ConcurrentPageIndex implements PageIndex {

	private final Set<URI> acquired = new ConcurrentSkipListSet<>();
	private final Set<Page> indexed = new ConcurrentSkipListSet<>();
	
	@Override
	public Optional<Consumer<Page>> aquire(final URI uri) {
		if (acquired.add(uri)) {
			return Optional.of(page -> indexed.add(page));
		}
		
		return Optional.empty();
	}

	@Override
	public Set<Page> all() {		
		Preconditions.checkState(acquired.size() == indexed.size());
		return Collections.unmodifiableSet(indexed);
	}

}
