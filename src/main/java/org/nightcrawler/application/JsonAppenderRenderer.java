package org.nightcrawler.application;

import java.net.URI;
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
	public Renderer<Consumer<JsonObjectBuilder>> address(final URI address) {
		this.address = Optional.of(render(address));
		return this;
	}

	@Override
	public Renderer<Consumer<JsonObjectBuilder>> links(final Iterable<URI> links) {
		return addUris("links", links);
	}

	@Override
	public Renderer<Consumer<JsonObjectBuilder>> resources(final Iterable<URI> resources) {
		return addUris("resources", resources);
	}

	private Renderer<Consumer<JsonObjectBuilder>> addUris(final String name, final Iterable<URI> uris) {
		final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		uris.forEach(uri -> arrayBuilder.add(render(uri)));
		value.add(name, arrayBuilder);
		return this;
	}

	@Override
	public Consumer<JsonObjectBuilder> build() {
		return builder -> builder.add(address.orElseThrow(IllegalStateException::new), value);
	}
	
	private static String render(final URI uri) {
		return uri.toString().replaceAll("/$", "");
	}

}
