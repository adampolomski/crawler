package org.nightcrawler.application;

class ValidationException extends Exception {

	private static final long serialVersionUID = -2978515657304856318L;

	ValidationException() {		
	}
	
	ValidationException(final Throwable e) {
		super(e);
	}
}
