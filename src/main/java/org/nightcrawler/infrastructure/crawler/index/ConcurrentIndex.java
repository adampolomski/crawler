package org.nightcrawler.infrastructure.crawler.index;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ConcurrentIndex<K, V> implements Index<K, V> {

	private static final Logger LOG = LoggerFactory.getLogger(ConcurrentIndex.class);
	
	private final Set<K> acquired = Collections.newSetFromMap(new ConcurrentHashMap<K, Boolean>());
	private final Set<V> indexed = Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>());

	@Override
	public Optional<Consumer<V>> aquire(final K key) {
		if (acquired.add(key)) {
			return Optional.of(value -> {	
				LOG.debug("Indexing object {}.", value);
				Preconditions.checkState(indexed.add(value), "Page already indexed " + key);
			});
		}

		return Optional.empty();
	}

	@Override
	public Set<V> all() {		
		return Collections.unmodifiableSet(indexed);
	}

}
