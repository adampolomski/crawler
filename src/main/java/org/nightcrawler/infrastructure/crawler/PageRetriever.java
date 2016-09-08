package org.nightcrawler.infrastructure.crawler;

import java.net.URI;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

public interface PageRetriever {

	void retrieve(URI uri, Consumer<Page> handler);

}