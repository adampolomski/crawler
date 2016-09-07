package org.nightcrawler;

import java.net.URI;

import org.nightcrawler.application.Controller;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	
	public static void main(final String[] args) {
		final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(CrawlerConfig.class);
		ctx.refresh();		
		
		final Controller controller = ctx.getBean(Controller.class);
		System.out.println(controller.generatePageMap(URI.create(args[0])));
		
		ctx.close();
	}

}