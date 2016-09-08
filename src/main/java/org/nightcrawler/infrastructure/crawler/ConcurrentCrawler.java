package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.index.PageIndex;

public class ConcurrentCrawler implements Crawler {

	private final Phaser phaser = null;
	private final PageIndex index = null;
	private final AsyncPageRetriever worker = null;
	
	@Override
	public Set<Page> crawl(final URI uri) {
		submit(uri);
		phaser.arriveAndAwaitAdvance();
		return index.all();
	}

	private void submit(final URI uri) {	
		index.aquire(uri).ifPresent(writer -> {
			phaser.register();
			worker.submit(uri, p -> handle(p, writer));			
		});		
	}

	private void handle(final Page page, final Consumer<Page> writer) {
		writer.accept(page);
		page.visitLinks( link -> submit(link) );
		phaser.arrive();
	}
}
