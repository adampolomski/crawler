package org.nightcrawler;

import org.nightcrawler.application.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

	@Bean
	Controller controller() {
		return new Controller();
	}
}
