package org.nightcrawler.domain.crawler;

import java.net.URL;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nightcrawler.UrlUtils;
import org.nightcrawler.domain.crawler.index.Aquireable;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

public class PropagatingPageRetrieverTest {

	private static final URL PAGE_URL = UrlUtils.url("http://localhost:8000");

	@Mock
	private PageRetriever mockDelegateRetriever;

	@Mock
	private Aquireable<URL, Page> mockIndex;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPropagateIfAcquired() {
		// given
		final PropagatingPageRetriever retriever = new PropagatingPageRetriever(mockDelegateRetriever, mockIndex);
		
		Mockito.when(mockIndex.aquire(PAGE_URL)).thenReturn(Optional.of(p -> {}));		
		
		// when
		retriever.crawl(PAGE_URL, HandlingStrategy.builder(PAGE_URL));		
		
		// then
		Mockito.verify(mockDelegateRetriever).crawl(Mockito.eq(PAGE_URL), Mockito.any());;				
	}
	
	@Test
	public void shouldNotPropagateIfNotAcquired() {
		// given
		final PropagatingPageRetriever retriever = new PropagatingPageRetriever(mockDelegateRetriever, mockIndex);
		Mockito.when(mockIndex.aquire(PAGE_URL)).thenReturn(Optional.empty());

		// when
		retriever.crawl(PAGE_URL, HandlingStrategy.builder(PAGE_URL));

		// then
		Mockito.verifyZeroInteractions(mockDelegateRetriever);
	}

}
