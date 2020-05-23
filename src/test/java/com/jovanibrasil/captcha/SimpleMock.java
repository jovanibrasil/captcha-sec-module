package com.jovanibrasil.captcha;

import com.jovanibrasil.captcha.aspect.Recaptcha;

public class SimpleMock {
	@Recaptcha
	public void run() {
		System.out.println("Running ...");
	}
}
