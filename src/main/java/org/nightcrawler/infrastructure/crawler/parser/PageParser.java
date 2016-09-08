package org.nightcrawler.infrastructure.crawler.parser;

import org.nightcrawler.domain.crawler.Page;

@FunctionalInterface
public interface PageParser {

	Page from(String content);
}
