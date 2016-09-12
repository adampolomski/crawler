package org.nightcrawler.infrastructure.crawler.parser;

import java.net.URI;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupPageParser implements PageParser {

	@Override
	public <P> P parse(final String content, final PageBuilder<P> builder) {
		final Document document = builder.forAddress(uri -> Jsoup.parse(content, uri.toString()));
		hyperlinks(document).forEach(builder::link);		
		Stream.concat(Stream.concat(links(document), images(document)), scripts(document)).forEach(builder::resource);		
		return builder.build();
	}

	private static Stream<URI> hyperlinks(final Document document) {
		return document.select("a").stream().map(uri("abs:href"));
	}

	private static Stream<URI> scripts(final Document document) {
		return document.select("script").stream().map(uri("abs:src"));
	}

	private static Stream<URI> images(final Document document) {
		return document.select("img").stream().map(uri("abs:src"));
	}

	private static Stream<URI> links(final Document document) {
		return document.select("link").stream().map(uri("href"));
	}
	
	private static Function<Element, URI> uri(final String attributeKey) {
		return el -> URI.create(el.attr(attributeKey));
	}

}
