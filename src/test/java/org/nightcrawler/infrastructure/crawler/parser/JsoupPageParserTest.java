package org.nightcrawler.infrastructure.crawler.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.nightcrawler.UrlUtils;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;
import org.nightcrawler.domain.crawler.strategy.PageBuilder;
import org.springframework.core.io.ClassPathResource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class JsoupPageParserTest {

	private static final URL PAGE_URL = UrlUtils.url("http://localhost:8000/allSamples.html");

	@Test
	public void shouldParsePage() throws IOException {
		// given
		final JsoupPageParser parser = new JsoupPageParser();
		final PageBuilder<Map<String, Object>> mapPagebuilder = mapPagebuilder(PAGE_URL);
		
		// when		
		parser.parse(content(PAGE_URL), HandlingStrategy.<Map<String, Object>>builder(PAGE_URL).build(mapPagebuilder));

		// then
		Assert.assertEquals(
				ImmutableMap.of("address", PAGE_URL, "links",
						ImmutableSet.of("http://localhost:8000/", "http://localhost:8000/2.html", "http://www.google.com"),
						"resources",
						ImmutableSet.of("http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css",
								"ios-app://305343404/tumblr/x-callback-url/blog?blogName=tomblomfield",
								"http://assets.tumblr.com/assets/scripts/tumblelog_post_message_queue.js?_v=cf140c0f1704d59bad98d3be7adddfde",
								"http://www.gravatar.com/avatar/c833be5582482777b51b8fc73e8b0586?s=128&d=identicon&r=PG")),
				mapPagebuilder.build(PAGE_URL));

	}

	private PageBuilder<Map<String, Object>> mapPagebuilder(final URL pageURL) {
		return new PageBuilder<Map<String, Object>>(){

			final Map<String, Object> page = Maps.newHashMap();						

			@Override
			public PageBuilder<Map<String, Object>> link(final URL link) {	
				addURL("links", link);
				return this;
			}

			@SuppressWarnings("unchecked")
			private void addURL(final String name, final Object link) {
				((Set<String>)page.computeIfAbsent(name, l->Sets.newSet())).add(link.toString());
			}

			@Override
			public PageBuilder<Map<String, Object>> resource(final URI resource) {
				addURL("resources", resource);
				return this;
			}

			@Override
			public Map<String, Object> build(URL address) {
				page.put("address", pageURL);
				return page;
			}};
	}

	private String content(final URL identifier) throws IOException {
		final InputStream stream = new ClassPathResource("page/" + identifier.getPath()).getInputStream();
		return IOUtils.toString(stream, Charset.forName("UTF-8"));
	}
}
