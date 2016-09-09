package org.nightcrawler.infrastructure.crawler.index;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Consumer;

import com.google.common.annotations.VisibleForTesting;

public class BlockingIndexWrapper<K, V> implements Index<K, V> {

	private final Index<K, V> delegate;
	private final Phaser phaser;
	
	@VisibleForTesting
	BlockingIndexWrapper(final Index<K, V> delegate, final Phaser phaser) {		
		this.delegate = delegate;
		this.phaser = phaser;
	}

	@Override
	public Optional<Consumer<V>> aquire(final K uri) {		
		final Optional<Consumer<V>> writer = delegate.aquire(uri);
		writer.ifPresent(c -> phaser.register());
		return writer.map(w -> wrap(w));
	}

	private Consumer<V> wrap(final Consumer<V> writer) {		
		return value -> {			
			writer.accept(value);
			phaser.arriveAndDeregister();
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
