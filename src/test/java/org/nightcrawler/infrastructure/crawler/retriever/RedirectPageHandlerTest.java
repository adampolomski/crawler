package org.nightcrawler.infrastructure.crawler.retriever;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nightcrawler.UrlUtils;
import org.nightcrawler.domain.crawler.Page;
import org.nightcrawler.domain.crawler.RedirectPage;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategyBuilder;
import org.nightcrawler.domain.crawler.strategy.IntrospectingPageBuilder;
import org.nightcrawler.domain.crawler.strategy.PageBuilder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


public class RedirectPageHandlerTest {

	private static final URL PAGE_URL = UrlUtils.url("http://www.google.com");

	@Mock
	private AsyncCompletionHandler<Response> mockFallback;

	@Mock
	private Response mockResponse;
	
	@Mock 
	private HandlingStrategyBuilder<Page> mockStrategyBuilder;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldProcessRedirect() throws Exception {
		// given
		final RedirectPageHandler handler = new RedirectPageHandler(mockStrategyBuilder, Executors.newSingleThreadExecutor(), mockFallback);
		final PageBuilder<Map<String, Object>> mapPagebuilder = mapPagebuilder();
		Mockito.when(mockResponse.getStatusCode()).thenReturn(301);
		Mockito.when(mockResponse.getHeader("Location")).thenReturn("http://www.google.com/redirect");
		Mockito.when(mockStrategyBuilder.build(Mockito.any(RedirectPage.RedirectBuilder.class))).thenReturn(fakeStrategy(mapPagebuilder));
		
		// when
		handler.onCompleted(mockResponse);
		
		// then
		Assert.assertEquals(ImmutableMap.of("address", PAGE_URL, "links", ImmutableSet.of("http://www.google.com/redirect")),
				mapPagebuilder.build(PAGE_URL));
	}

	private static HandlingStrategy fakeStrategy(final PageBuilder<Map<String, Object>> mapPagebuilder) {
		return HandlingStrategy.<Map<String, Object>>builder(PAGE_URL).build(mapPagebuilder);
	}
	
	private PageBuilder<Map<String, Object>> mapPagebuilder() {
		return new IntrospectingPageBuilder();
	}
	
	@Test
	public void shouldFallBack() throws Exception {
		// given
		final RedirectPageHandler handler = new RedirectPageHandler(HandlingStrategy.builder(PAGE_URL), Executors.newSingleThreadExecutor(), mockFallback);
		Mockito.when(mockResponse.getStatusCode()).thenReturn(200);
		
		// when
		handler.onCompleted(mockResponse);
		
		// then
		Mockito.verify(mockFallback).onCompleted(mockResponse);
		Mockito.verifyZeroInteractions(mockStrategyBuilder);
	}
}
