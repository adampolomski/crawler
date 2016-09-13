package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.net.URL;

public interface PageBuilder<P> {
	
	PageBuilder<P> link(URL link);

	PageBuilder<P> resource(URI resource);
	
	P build(URL address);

}