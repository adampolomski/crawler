package org.nightcrawler.application;

import java.net.URI;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.Assert;
import org.junit.Test;
import org.nightcrawler.UrlUtils;

import com.google.common.collect.ImmutableSet;

public class JsonAppenderRendererTest {

	private static final URL LINK_URL = UrlUtils.url("http://localhost:8000/1.html");
	private static final URI IMG_URI = URI.create("http://localhost:8000/img.jpg");
	private static final URL PAGE_URL = UrlUtils.url("http://localhost:8000/allSamples.html");

	@Test
	public void shouldRenderToJson() {
		// given
		final JsonAppenderRenderer renderer = new JsonAppenderRenderer();
		final JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

		// when
		renderer.address(PAGE_URL).links(ImmutableSet.of(LINK_URL)).resources(ImmutableSet.of(IMG_URI)).build().accept(jsonBuilder);

		// then
		Assert.assertEquals(expectedJson(), jsonBuilder.build());
		;
	}

	private JsonObject expectedJson() {
		return Json.createObjectBuilder()
				.add(PAGE_URL.toString(), Json.createObjectBuilder()
						.add("links", Json.createArrayBuilder().add(LINK_URL.toString()))
						.add("resources", Json.createArrayBuilder().add(IMG_URI.toString())))
				.build();
	}

}
