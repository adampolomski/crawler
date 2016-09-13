package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class RegularPageTest {

	private static final URI LINK_URI = URI.create("http://localhost:8000/1.html");
	private static final URI IMG_URI = URI.create("http://localhost:8000/img.jpg");
	private static final URI PAGE_URI = URI.create("http://localhost:8000/allSamples.html");

	@Test
	public void shouldBuildPage() {
		// when
		final Page page = RegularPage.builder().link(LINK_URI)
				.resource(IMG_URI).build(PAGE_URI);

		// then
		Assert.assertEquals(ImmutableMap.of("a", PAGE_URI, 
				"l", ImmutableSet.of(LINK_URI),
				"r", ImmutableSet.of(IMG_URI)),
				page.render(mapRenderer()));
	}

	private static Renderer<Map<String, Object>> mapRenderer() {
		return new Renderer<Map<String, Object>>() {

			final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

			@Override
			public Renderer<Map<String, Object>> address(final URI address) {
				builder.put("a", address);
				return this;
			}

			@Override
			public Renderer<Map<String, Object>> links(final Iterable<URI> links) {
				builder.put("l", links);
				return this;
			}

			@Override
			public Renderer<Map<String, Object>> resources(final Iterable<URI> resources) {
				builder.put("r", resources);
				return this;
			}

			@Override
			public Map<String, Object> build() {
				return builder.build();
			}

			@Override
			public Renderer<Map<String, Object>> redirect(URI address) {
				builder.put("rd", address);
				return this;
			}
		};
	}

}
