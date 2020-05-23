package com.jovanibrasil.captcha.service;

import com.jovanibrasil.captcha.exception.InvalidRecaptchaException;
import com.jovanibrasil.captcha.exception.ReCaptchaInvalidException;

public interface CaptchaService {

	/**
	 * Verifies the reCaptcha response.
	 * 
	 * @param response
	 * @throws InvalidRecaptchaException
	 * @throws ReCaptchaInvalidException
	 */
	void processResponse(String response) throws InvalidRecaptchaException, ReCaptchaInvalidException;
	
	String getReCaptchaSecret();
	String getReCaptchaSite();
	
}
