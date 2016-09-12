package org.nightcrawler.infrastructure.crawler.parser;

import java.net.URI;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupPageParser implements PageParser {

	@Override
	public <P> P parse(final String content, final PageBuilder<P> builder) {
		final Document document = builder.forAddress(uri -> Jsoup.parse(content, uri.toString()));
		hyperlinks(document).map(URI::create).forEach(builder::link);
		Stream.concat(Stream.concat(links(document), images(document)), scripts(document))
				.filter(attr -> !attr.isEmpty()).map(URI::create).forEach(builder::resource);
		return builder.build();
	}

	private static Stream<String> hyperlinks(final Document document) {
		return document.select("a").stream().map(e -> e.attr("abs:href"));
	}

	private static Stream<String> scripts(final Document document) {
		return document.select("script").stream().map(e -> e.attr("abs:src"));
	}

	private static Stream<String> images(final Document document) {
		return document.select("img").stream().map(e -> e.attr("abs:src"));
	}

	private static Stream<String> links(final Document document) {
		return document.select("link").stream().map(e -> e.attr("href"));
	}		
}