package org.nightcrawler.infrastructure.crawler.index;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

public class BlockingIndexWrapper<K, V> implements Index<K, V> {

	private static final Logger LOG = LoggerFactory.getLogger(BlockingIndexWrapper.class);
	
	private final Index<K, V> delegate;
	private final Phaser phaser;
	
	@VisibleForTesting
	BlockingIndexWrapper(final Index<K, V> delegate, final Phaser phaser) {		
		this.delegate = delegate;
		this.phaser = phaser;
	}

	@Override
	public Optional<Consumer<V>> aquire(final K key) {
		final Optional<Consumer<V>> writer = delegate.aquire(key);
		writer.ifPresent(c -> {
			phaser.register();
			LOG.debug("Aquired key {}.", key.toString());
		});
		return writer.map(w -> w.andThen(deregister()));
	}

	private Consumer<V> deregister() {		
		return value -> {						
			phaser.arriveAndDeregister();
			LOG.debug("Deregistered {}.", value.toString());
		};
	}

	@Override
	public Set<V> all() {			
		phaser.awaitAdvance(phaser.arriveAndDeregister());		
		return delegate.all();
	}
	
	public static <K, V> BlockingIndexWrapper<K, V> wrap(final Index<K, V> delegate) {
		return new BlockingIndexWrapper<K, V>(delegate, new Phaser(1));
	}

}
