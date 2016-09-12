package org.nightcrawler.infrastructure.crawler.parser;

import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.Page.Builder;

@FunctionalInterface
public interface PageParser {

	Page parse(String content, Builder builder);
}
