package org.nightcrawler.application;

import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.nightcrawler.domain.crawler.Renderer;

public class JsonAppenderRenderer implements Renderer<Consumer<JsonObjectBuilder>> {

	private final JsonObjectBuilder value = Json.createObjectBuilder();
	private Optional<String> address = Optional.empty();
	
	@Override
	public Renderer<Consumer<JsonObjectBuilder>> address(final URL address) {
		this.address = Optional.of(render(address));
		return this;
	}
	
	@Override
	public Renderer<Consumer<JsonObjectBuilder>> redirect(final URL address) {
		value.add("redirect", render(address));
		return this;
	}

	@Override
	public Renderer<Consumer<JsonObjectBuilder>> links(final Iterable<URL> links) {
		return addPaths("links", links);
	}

	@Override
	public Renderer<Consumer<JsonObjectBuilder>> resources(final Iterable<URI> resources) {
		return addPaths("resources", resources);
	}

	private Renderer<Consumer<JsonObjectBuilder>> addPaths(final String name, final Iterable<?> urls) {
		final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		urls.forEach(path -> arrayBuilder.add(path.toString()));
		value.add(name, arrayBuilder);
		return this;
	}

	@Override
	public Consumer<JsonObjectBuilder> build() {
		return builder -> builder.add(address.orElseThrow(IllegalStateException::new), value);
	}
	
	private static String render(final URL url) {
		return url.toString();
	}
}
