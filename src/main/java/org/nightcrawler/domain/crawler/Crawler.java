package org.nightcrawler.domain.crawler;

import java.net.URL;
import java.util.Set;

@FunctionalInterface
public interface Crawler {

	Set<Page> crawl(final URL uri);

}
