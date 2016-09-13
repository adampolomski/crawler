package org.nightcrawler.infrastructure.crawler.parser;

import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

public interface PageParser {

	void parse(String content, HandlingStrategy strategy);
}
