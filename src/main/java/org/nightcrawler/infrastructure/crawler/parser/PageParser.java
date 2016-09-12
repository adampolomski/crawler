package org.nightcrawler.infrastructure.crawler.parser;

@FunctionalInterface
public interface PageParser {

	<P> P parse(String content, PageBuilder<P> builder);
}
