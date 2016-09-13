package org.nightcrawler.domain.crawler;

import java.net.URI;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nightcrawler.domain.crawler.index.Aquireable;
import org.nightcrawler.domain.crawler.strategy.HandlingStrategy;

public class PropagatingPageRetrieverTest {

	private static final URI PAGE_URI = URI.create("http://localhost:8000");

	@Mock
	private PageRetriever mockDelegateRetriever;

	@Mock
	private Aquireable<URI, Page> mockIndex;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPropagateIfAcquired() {
		// given
		final PropagatingPageRetriever retriever = new PropagatingPageRetriever(mockDelegateRetriever, mockIndex);
		
		Mockito.when(mockIndex.aquire(PAGE_URI)).thenReturn(Optional.of(p -> {}));		
		
		// when
		retriever.crawl(PAGE_URI, HandlingStrategy.builder(PAGE_URI));		
		
		// then
		Mockito.verify(mockDelegateRetriever).crawl(Mockito.eq(PAGE_URI), Mockito.any());;				
	}
	
	@Test
	public void shouldNotPropagateIfNotAcquired() {
		// given
		final PropagatingPageRetriever retriever = new PropagatingPageRetriever(mockDelegateRetriever, mockIndex);
		Mockito.when(mockIndex.aquire(PAGE_URI)).thenReturn(Optional.empty());

		// when
		retriever.crawl(PAGE_URI, HandlingStrategy.builder(PAGE_URI));

		// then
		Mockito.verifyZeroInteractions(mockDelegateRetriever);
	}

}
