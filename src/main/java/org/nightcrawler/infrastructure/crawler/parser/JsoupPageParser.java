package org.nightcrawler.infrastructure.crawler.parser;

import java.net.URI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.Page.Builder;

public class JsoupPageParser implements PageParser {

	@Override
	public Page parse(final String content, final Builder builder) {
		final Document document = builder.forAddress(uri -> Jsoup.parse(content, uri.toString()));
		document.select("a").stream().map(link -> URI.create(link.attr("abs:href"))).forEach(l -> builder.link(l));
		return builder.build();
	}

}
