package org.nightcrawler.application;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -2978515657304856318L;

	public ValidationException() {		
	}
	
	public ValidationException(final Throwable e) {
		super(e);
	}
}
