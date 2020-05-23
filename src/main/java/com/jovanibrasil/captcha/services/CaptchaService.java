package com.jovanibrasil.captcha.services;

import com.jovanibrasil.captcha.exceptions.InvalidRecaptchaException;
import com.jovanibrasil.captcha.exceptions.ReCaptchaInvalidException;

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
