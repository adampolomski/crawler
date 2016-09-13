package org.nightcrawler.infrastructure.crawler.retriever;

import java.util.function.UnaryOperator;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

abstract class DecoratingPageHandler extends AsyncCompletionHandler<Response> {

	private final AsyncCompletionHandler<Response> fallback;

	abstract Response onCompleted(final Response response, final UnaryOperator<Response> fallback) throws Exception;
	
	DecoratingPageHandler(final AsyncCompletionHandler<Response> fallback) {		
		this.fallback = Preconditions.checkNotNull(fallback);
	}

	@Override
	public final Response onCompleted(final Response response) throws Exception {
		return onCompleted(response, this::fallback);
	}
	
	private Response fallback(final Response response) {
		try {
			return fallback.onCompleted(response);
		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}		
	}
	
	@Override
	public final void onThrowable(Throwable t) {
		fallback.onThrowable(t);
	}
}
