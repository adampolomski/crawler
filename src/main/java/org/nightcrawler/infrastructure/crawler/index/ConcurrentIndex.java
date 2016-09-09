package org.nightcrawler.infrastructure.crawler.index;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;

public class ConcurrentIndex<K, V> implements Index<K, V> {

	private final Set<K> acquired = Collections.newSetFromMap(new ConcurrentHashMap<K, Boolean>());
	private final Set<V> indexed = Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>());

	@Override
	public Optional<Consumer<V>> aquire(final K key) {
		if (acquired.add(key)) {
			return Optional.of(value -> Preconditions.checkState(indexed.add(value)));
		}

		return Optional.empty();
	}

	@Override
	public Set<V> all() {		
		return Collections.unmodifiableSet(indexed);
	}

}
