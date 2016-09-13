package org.nightcrawler.domain.crawler;

import java.net.URL;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.nightcrawler.UrlUtils;

import com.google.common.collect.ImmutableMap;

public class RedirectPageTest {

	private static final URL LINK_URL = UrlUtils.url("http://localhost:8000/1.html");
	private static final URL PAGE_URL = UrlUtils.url("http://localhost:8000/allSamples.html");

	@Test
	public void shouldBuildPage() {
		// when
		final Page page = RedirectPage.builder().link(LINK_URL).build(PAGE_URL);

		// then
		Assert.assertEquals(ImmutableMap.of("a", PAGE_URL, "rd", LINK_URL), page.render(mapRenderer()));
	}

	private static Renderer<Map<String, Object>> mapRenderer() {
		return new IntrospectingRenderer();
	}

}
