package com.jovanibrasil.captcha;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jovanibrasil.captcha.exceptions.InvalidRecaptchaException;
import com.jovanibrasil.captcha.exceptions.ReCaptchaInvalidException;
import com.jovanibrasil.captcha.services.CaptchaService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class RecaptchaTest {
	
	@Autowired SimpleMock targetProxy;
	@Autowired CaptchaService captchaService;
	
	
	@Test
	public void proxyCreated() {
		assertTrue(AopUtils.isAopProxy(targetProxy));
	}

	@Test(expected = Test.None.class)
	public void validateValidToken() throws InvalidRecaptchaException, ReCaptchaInvalidException {		
		String captcha = "valid_captcha_code";
		setRequestAttribute(captcha);
		doNothing().when(captchaService).processResponse(captcha);
		targetProxy.run();

		verify(captchaService).processResponse(captcha);
	}
	
	@Test(expected = Exception.class)
	public void validateInvalidToken() throws InvalidRecaptchaException, ReCaptchaInvalidException {
		String captcha = "invalid_captcha_code";
		setRequestAttribute(captcha);
		doThrow(InvalidRecaptchaException.class).when(captchaService).processResponse(captcha);
		targetProxy.run();
	}

	private void setRequestAttribute(String captcha) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(CaptchaAspect.RECAPTCHA_TOKEN_PARAM_NAME, captcha);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
	
	@Configuration
	@EnableAspectJAutoProxy
	static public class TestContext {

	    @Bean CaptchaAspect aspect() {
	    	return new CaptchaAspect(service());
	    }
	    
	    @Bean CaptchaService service(){
	     return mock(CaptchaService.class);
	    }
	    
	    @Bean SimpleMock target(){
	    	return new SimpleMock();
	    }
	}
	
}
