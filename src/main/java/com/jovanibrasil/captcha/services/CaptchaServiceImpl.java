package com.jovanibrasil.captcha.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import com.jovanibrasil.captcha.exceptions.InvalidRecaptchaException;
import com.jovanibrasil.captcha.exceptions.ReCaptchaInvalidException;
import com.jovanibrasil.captcha.properties.CaptchaSettings;

import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.regex.Pattern;

@Service
public class CaptchaServiceImpl implements CaptchaService {

	private static Logger log = LoggerFactory.getLogger(CaptchaServiceImpl.class);

	private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	private CaptchaSettings captchaSettings;
	private RestOperations restTemplate;
	private RecaptchaAttemptService reCaptchaAttemptService;
	private HttpServletRequest httpServletRequest;

	public CaptchaServiceImpl(CaptchaSettings captchaSettings, RecaptchaAttemptService reCaptchaAttemptService,
			RestOperations restTemplate, HttpServletRequest httpServletRequest) {
		this.captchaSettings = captchaSettings;
		this.reCaptchaAttemptService = reCaptchaAttemptService;
		this.httpServletRequest = httpServletRequest;
		this.restTemplate = restTemplate;
	}

	@Override
	public void processResponse(String response) throws InvalidRecaptchaException, ReCaptchaInvalidException {
		if (!responseSanityCheck(response)) {
			log.info("Response contains invalid characters");
			throw new InvalidRecaptchaException("Response contains invalid characters");
		}

		if (reCaptchaAttemptService.isBlocked(getClientIP())) {
			log.info("Client exceeded maximum number of failed attempts");
			throw new InvalidRecaptchaException("Client exceeded maximum number of failed attempts");
		}

		URI verifyUri = URI.create(
				String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
						getReCaptchaSecret(), response, getClientIP()));
		ResponseEntity<GoogleResponse> googleResponse = restTemplate.getForEntity(verifyUri, GoogleResponse.class);
		if (!googleResponse.hasBody()) {
			log.info("Entity response with empty body.");
			throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
		}
		if (!googleResponse.getBody().isSuccess()) {
			if (googleResponse.getBody().hasClientError()) {
				reCaptchaAttemptService.recaptchaFailed(getClientIP());
			}
			log.info("Validation fail.");
			throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
		}
		reCaptchaAttemptService.recaptchaSucceeded(getClientIP());

	}

	@Override
	public String getReCaptchaSite() {
		return captchaSettings.getSite();
	}

	@Override
	public String getReCaptchaSecret() {
		return captchaSettings.getSecret();
	}

	/**
	 * Returns the client IP.
	 * 
	 * @return
	 */
	private String getClientIP() {
		// Get X-Forwarded-For value. The X-Forwarded-For header has the originating IP
		// address.
		final String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
		// If X-Forwarded-For is invalid, get the IP from package. If client is behind some proxy
		// then the IP will be the proxy IP.
		if (xfHeader == null)
			return httpServletRequest.getRemoteAddr();
		return xfHeader.split(",")[0];
	}

	/**
	 * 
	 * Check if response is neither null or length 0 and if matches with the
	 * RESPONSE_PATTERN.
	 * 
	 * @param response
	 * @return
	 */
	private boolean responseSanityCheck(String response) {
		return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
	}

}
