package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Set;

@FunctionalInterface
public interface Crawler {

	Set<Page> crawl(final URI uri);

}
