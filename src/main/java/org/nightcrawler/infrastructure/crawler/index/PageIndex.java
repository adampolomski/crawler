package org.nightcrawler.infrastructure.crawler.index;

import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.nightcrawler.domain.crawler.Page;

public interface PageIndex {

	Optional<Consumer<Page>> aquire(URI uri);

	Set<Page> all();

}
