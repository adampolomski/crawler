package org.nightcrawler.infrastructure.crawler.index;

import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

public class BlockingPageIndexWrapper implements PageIndex {

	private final PageIndex delegate = null;
	private final Phaser phaser = null;
	
	@Override
	public Optional<Consumer<Page>> aquire(final URI uri) {		
		final Optional<Consumer<Page>> writer = delegate.aquire(uri);
		writer.ifPresent(c -> phaser.register());
		return writer;
	}

	@Override
	public Set<Page> all() {
		phaser.arriveAndAwaitAdvance();
		return delegate.all();
	}

}
