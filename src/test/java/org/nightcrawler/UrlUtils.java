package org.nightcrawler;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Throwables;

public class UrlUtils {

	public static URL url(final String path) {
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}
	}

}
