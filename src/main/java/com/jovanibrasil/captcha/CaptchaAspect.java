package com.jovanibrasil.captcha;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jovanibrasil.captcha.exceptions.InvalidRecaptchaException;
import com.jovanibrasil.captcha.services.CaptchaService;

@Aspect
@Component
public class CaptchaAspect {

	public static final String RECAPTCHA_TOKEN_PARAM_NAME = "recaptchaResponseToken";
	private static final Logger log = LoggerFactory.getLogger(CaptchaAspect.class);
	
	private CaptchaService captchaService;
	
	public CaptchaAspect(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	/**
	 * Add extra code before the method execution. In this case add the
	 * captcha verification logic.
	 * 
	 * @param joinPoint is the executing method
	 * @return
	 * @throws Throwable
	 */
	@Before("@annotation(Recaptcha)")
	public void verifyCaptcha(JoinPoint joinPoint) throws InvalidRecaptchaException {	
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes()).getRequest();
			String recaptchaResponse = request.getParameter(RECAPTCHA_TOKEN_PARAM_NAME);
			captchaService.processResponse(recaptchaResponse);
			log.info("Captcha successfuly validated.");
		} catch (Exception e) {
			log.info("Error when validating captcha. {}", e.getMessage());
			throw new InvalidRecaptchaException();
		}
		
	}

}
