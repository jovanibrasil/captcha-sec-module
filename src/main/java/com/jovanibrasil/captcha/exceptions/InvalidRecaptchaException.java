package com.jovanibrasil.captcha.exceptions;

public class InvalidRecaptchaException extends Exception {

	private static final long serialVersionUID = -1861699434138111027L;

	public InvalidRecaptchaException(String message) {
		super(message);
	}

	public InvalidRecaptchaException() {
		super();
	}

}
