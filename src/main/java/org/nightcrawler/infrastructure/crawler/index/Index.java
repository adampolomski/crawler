package org.nightcrawler.infrastructure.crawler.index;

import java.util.Set;

public interface Index<K, V> extends Aquireable<K, V> {

	Set<V> all();

}
