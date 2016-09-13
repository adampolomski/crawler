package org.nightcrawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Verify;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Full functional test. Uses the jar to crawl a test website. Data fed by mock HTTP server.
 * 
 * @author Adam Polomski
 *
 */
public class ApplicationTest {

	private static final Optional<File> JAR = Optional.ofNullable(System.getProperty("nightcrawler.jar.path")).map(File::new);

	@Before
	public void checkJar() {
		Assume.assumeTrue(JAR.map(File::exists).orElse(false));
	}

	private HttpServer httpServer() throws IOException {
		final HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/", new PageHandler());
		return server;
	}

	@Test
	public void shouldRespondWithMap() throws IOException, InterruptedException {
		// given
		final HttpServer server = httpServer();
		server.start();

		// when
		final String output = executeCrawler();

		// then
		Assert.assertEquals(pageMap(), json(output));
	}

	private String executeCrawler() throws IOException, InterruptedException {
		final Process ps = Runtime.getRuntime().exec(new String[] { "java", "-jar", JAR.map(File::getAbsolutePath).get(), "http://localhost:8000" });
		Verify.verify(ps.waitFor() == 0);

		try (final Scanner scanner = new Scanner(ps.getInputStream(), "utf-8")) {
			return scanner.useDelimiter("\\Z").next();
		}
	}

	private static class PageHandler implements HttpHandler {

		@Override
		public void handle(final HttpExchange exchange) throws IOException {
			final URI uri = exchange.getRequestURI();
			final String page = page(identifier(uri));
			exchange.sendResponseHeaders(200, page.length());
			try (final OutputStream os = exchange.getResponseBody()) {
				os.write(page.getBytes());
			}
		}

		private String identifier(final URI uri) {
			final String page = uri.getPath().substring(1);
			return page.isEmpty() ? "index.html" : page;
		}

		private String page(final String identifier) throws IOException {
			final InputStream stream = new ClassPathResource("page/" + identifier).getInputStream();
			return IOUtils.toString(stream, Charset.forName("UTF-8"));
		}
	}
	
	private static JsonNode json(final String payload) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(payload);
    }

    private static JsonNode pageMap() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream stream = new ClassPathResource("pageMap.json").getInputStream();
        return mapper.readTree(stream);
    }

}
