package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class PageTest {

	private static final URI IMG_URI = URI.create("http://localhost:8000/img.jpg");
	private static final URI PAGE_URI = URI.create("http://localhost:8000/allSamples.html");

	@Test
	public void shouldDropTrailingLinkSlashes() {
		// when
		final Page page = Page.builder(PAGE_URI).link(URI.create("http://localhost:8000/"))
				.resource(IMG_URI).build();

		// then
		Assert.assertEquals(ImmutableMap.of("a", PAGE_URI, 
				"l", ImmutableSet.of(URI.create("http://localhost:8000")),
				"r", ImmutableSet.of(IMG_URI)),
				page.render(mapRenderer()));
	}
	
	@Test
	public void shouldDropTrailingAddressSlashes() {
		// when
		final Page trailingSlashPage = Page.builder(URI.create("http://localhost:8000/")).build();
		final Page nonTralingSlashPage = Page.builder(URI.create("http://localhost:8000")).build();
		
		// then
		Assert.assertTrue(trailingSlashPage.equals(nonTralingSlashPage));
	}
	
	@Test
	public void shouldVisitLinks() {
		// given
		final URI link = URI.create("http://localhost:8000/1.html");
		final Page page = Page.builder(PAGE_URI).link(link).build();

		// when
		final Set<URI> visitedLinks = Sets.newHashSet();
		page.visitLinks(visitedLinks::add);
		
		// then
		Assert.assertEquals(ImmutableSet.of(link), visitedLinks);		
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
		};
	}

}
