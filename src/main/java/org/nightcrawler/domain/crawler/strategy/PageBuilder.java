package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;

public interface PageBuilder<P> {
	
	PageBuilder<P> link(URI link);

	PageBuilder<P> resource(URI resource);
	
	P build(URI address);

}