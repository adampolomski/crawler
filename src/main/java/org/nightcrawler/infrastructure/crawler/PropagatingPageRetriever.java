package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.index.Aquireable;

public class PropagatingPageRetriever extends PageRetriever {

	private final PageRetriever delegateRetriever;
	private final Aquireable<URI, Page> index;
	
	public PropagatingPageRetriever(PageRetriever delegateRetriever, Aquireable<URI, Page> index) {
		this.delegateRetriever = delegateRetriever;
		this.index = index;
	}	

	@Override
	public void crawl(final URI uri, final Consumer<Page> handler) {
		index.aquire(uri).ifPresent(writer -> delegateRetriever.crawl(uri,
				propagatingConsumer(handler, uri).andThen(handler.andThen(writer))));
	}

	private Consumer<Page> propagatingConsumer(final Consumer<Page> handler, final URI previous) {
		return page -> {
			page.visitLinks(link -> {
				if (link.getHost().equals(previous.getHost()))
					crawl(link, handler);
			});
		};
	}
}
