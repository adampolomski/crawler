package org.nightcrawler.infrastructure.crawler.index;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.nightcrawler.domain.crawler.index.Index;

import com.google.common.collect.ImmutableSet;

public class BlockingIndexWrapperTest {

	private static final Integer KEY = 1;
	private static final Integer VALUE = 2;

	@Mock
	private Index<Integer, Integer> mockDelegate;

	@Mock
	private Phaser mockPhaser;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotPhase() {
		final BlockingIndexWrapper<Integer, Integer> blockingIndex = blockingIndex();
		Mockito.when(mockDelegate.aquire(KEY)).thenReturn(Optional.empty());
		
		// when
		final Optional<Consumer<Integer>> writer = blockingIndex.aquire(KEY);

		// then
		Assert.assertFalse(writer.isPresent());
		Mockito.verifyZeroInteractions(mockPhaser);
	}
	
	@Test
	public void shouldRegister() {
		// given
		final BlockingIndexWrapper<Integer, Integer> blockingIndex = blockingIndex();			
		Mockito.when(mockDelegate.aquire(KEY)).thenReturn(Optional.of(i -> {}));
		
		// when
		blockingIndex.aquire(KEY).ifPresent(c -> c.accept(VALUE));

		// then			
		Mockito.verify(mockPhaser).register();
		Mockito.verify(mockPhaser).arriveAndDeregister();
	}
	
	@Test
	public void shouldArriveAndWait() {
		final BlockingIndexWrapper<Integer, Integer> blockingIndex = blockingIndex();					
		final ImmutableSet<Integer> indexed = ImmutableSet.of(VALUE);
		
		Mockito.when(mockPhaser.arriveAndDeregister()).thenReturn(2);		
		Mockito.when(mockDelegate.all()).thenReturn(indexed);
		
		// when
		final Set<Integer> retrievedStuff = blockingIndex.all();

		// then			
		Assert.assertEquals(indexed, retrievedStuff);
		Mockito.verify(mockPhaser).awaitAdvance(2);
	}
	
	private BlockingIndexWrapper<Integer, Integer> blockingIndex() {
		return new BlockingIndexWrapper<Integer, Integer>(mockDelegate, mockPhaser);
	}

}
