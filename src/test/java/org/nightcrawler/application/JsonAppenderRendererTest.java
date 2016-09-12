package org.nightcrawler.application;

import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class JsonAppenderRendererTest {

	private static final URI LINK_URI = URI.create("http://localhost:8000/1.html");
	private static final URI IMG_URI = URI.create("http://localhost:8000/img.jpg");
	private static final URI PAGE_URI = URI.create("http://localhost:8000/allSamples.html");

	@Test
	public void shouldRenderToJson() {
		// given
		final JsonAppenderRenderer renderer = new JsonAppenderRenderer();
		final JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

		// when
		renderer.address(PAGE_URI).links(ImmutableSet.of(LINK_URI)).resources(ImmutableSet.of(IMG_URI)).build().accept(jsonBuilder);

		// then
		Assert.assertEquals(expectedJson(), jsonBuilder.build());
		;
	}

	private JsonObject expectedJson() {
		return Json.createObjectBuilder()
				.add(PAGE_URI.toString(), Json.createObjectBuilder()
						.add("links", Json.createArrayBuilder().add(LINK_URI.toString()))
						.add("resources", Json.createArrayBuilder().add(IMG_URI.toString())))
				.build();
	}

}
