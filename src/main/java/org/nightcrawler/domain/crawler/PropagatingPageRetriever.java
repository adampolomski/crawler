package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.index.Aquireable;

import com.google.common.base.Preconditions;

public class PropagatingPageRetriever extends PageRetriever {

	private final PageRetriever delegateRetriever;
	private final Aquireable<URI, Page> index;
	
	public PropagatingPageRetriever(final PageRetriever delegateRetriever, final Aquireable<URI, Page> index) {
		this.delegateRetriever = Preconditions.checkNotNull(delegateRetriever);
		this.index = Preconditions.checkNotNull(index);
	}	

	@Override
	public void crawl(final URI uri, final Consumer<Page> handler) {
		final URI normalizedUri = normalize(uri);
		index.aquire(normalizedUri).ifPresent(writer -> delegateRetriever.crawl(normalizedUri,
				propagatingConsumer(handler, normalizedUri).andThen(handler.andThen(writer))));
	}

	private Consumer<Page> propagatingConsumer(final Consumer<Page> handler, final URI previous) {
		return page -> {
			page.visitLinks(link -> {
				if (link.getHost().equals(previous.getHost()))
					crawl(link, handler);
			});
		};
	}
	
	private static URI normalize(final URI uri) {
		return URI.create(uri.toString().replaceAll("/$", ""));
	}
}
