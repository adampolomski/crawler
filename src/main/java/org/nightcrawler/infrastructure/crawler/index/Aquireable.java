package org.nightcrawler.infrastructure.crawler.index;

import java.util.Optional;
import java.util.function.Consumer;

public interface Aquireable<K, V> {

	Optional<Consumer<V>> aquire(K key);

}