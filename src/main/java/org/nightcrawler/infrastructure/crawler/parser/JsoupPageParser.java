package org.nightcrawler.infrastructure.crawler.parser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

public class JsoupPageParser implements PageParser {

	@Override
	public void parse(final String content, final HandlingStrategy strategy) {
		final Document document = strategy.forAddress(URL -> Jsoup.parse(content, URL.toString()));		
		hyperlinks(document).filter(attr -> !attr.isEmpty()).map(JsoupPageParser::parseUrl).filter(Optional::isPresent).map(Optional::get).forEach(strategy::link);	
		Stream.concat(Stream.concat(links(document), images(document)), scripts(document))
				.filter(attr -> !attr.isEmpty()).map(JsoupPageParser::parseUri).filter(Optional::isPresent).map(Optional::get).forEach(strategy::resource);
		strategy.process();
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
	
	private static Optional<URL> parseUrl(final String url) {		
		try {
			return Optional.of(new URL(url));
		} catch (MalformedURLException e) {
			return Optional.empty();
		}
	}
	
	private static Optional<URI> parseUri(final String path) {		
		try {
			return Optional.of(new URI(path));	
		} catch (URISyntaxException e) {
			return Optional.empty();
		}
	}
}