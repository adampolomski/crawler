package org.nightcrawler.domain.crawler.strategy;

import java.net.URI;
import java.util.Set;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class LinkWatchingStrategyTest {

	private static final URI LINK_URI = URI.create("http://www.google.com");

	@Mock
	private HandlingStrategy mockBaseStrategy;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldConsumeLink() {
		// given
		final Set<URI> processedLinks = Sets.newHashSet();
		final LinkWatchingStrategy strategy = new LinkWatchingStrategy(mockBaseStrategy, processedLinks::add);

		// when
		strategy.link(LINK_URI);

		// then
		Assert.assertTrue(processedLinks.contains(LINK_URI));
		Mockito.verify(mockBaseStrategy).link(LINK_URI);
	}
	
	@Test
	public void shouldDelegateProcessCalls() {
		// given
		final LinkWatchingStrategy strategy = new LinkWatchingStrategy(mockBaseStrategy, l->{});

		// when
		strategy.process();

		// then
		Mockito.verify(mockBaseStrategy).process();
	}
	
	@Test
	public void shouldDelegateResourceCalls() {
		// given
		final LinkWatchingStrategy strategy = new LinkWatchingStrategy(mockBaseStrategy, l->{});

		// when
		strategy.resource(LINK_URI);

		// then
		Mockito.verify(mockBaseStrategy).resource(LINK_URI);
	}
	
	@Test
	public void shouldDelegateForAddressCalls() {
		// given
		final LinkWatchingStrategy strategy = new LinkWatchingStrategy(mockBaseStrategy, l->{});

		// when
		strategy.forAddress(Function.identity());

		// then
		Mockito.verify(mockBaseStrategy).forAddress(Mockito.any());
	}

}
