package org.nightcrawler.application;

import java.net.URI;
import java.util.Set;
import java.util.function.Supplier;

import org.nightcrawler.domain.crawler.Crawler;
import org.nightcrawler.domain.crawler.Page;

import com.google.common.base.Preconditions;

public class Controller {

	private final Supplier<Crawler> crawlerFactory;

	public Controller(final Supplier<Crawler> crawlerFactory) {
		this.crawlerFactory = Preconditions.checkNotNull(crawlerFactory);
	}

	public String generatePageMap(final URI uri) {
		final Set<Page> pages = crawlerFactory.get().crawl(uri);
		return render(pages);
	}

	private String render(final Set<Page> pages) {
		final StringBuffer buffer = new StringBuffer("{");
		buffer.append(pages.size());
		pages.stream().forEach(p -> buffer.append("a"));
		buffer.append("}");
		return buffer.toString();
	}
}
