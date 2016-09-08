package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.infrastructure.crawler.index.PageIndex;

public class PropagatingPageRetriever implements PageRetriever {

	private final PageRetriever delegateRetriever = null;	
	private final PageIndex index = null;

	@Override
	public void retrieve(final URI uri, final Consumer<Page> handler) {
		index.aquire(uri).ifPresent(writer -> {
			delegateRetriever.retrieve(uri, page -> {
				writer.accept(page);
				handler.accept(page);
				page.visitLinks(link -> retrieve(uri, handler));
			});
		});
	}
}
