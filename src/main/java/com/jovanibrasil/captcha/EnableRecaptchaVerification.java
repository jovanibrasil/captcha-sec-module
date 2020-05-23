package com.jovanibrasil.captcha;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@ComponentScan("com.jovanibrasil.captcha")
public @interface EnableRecaptchaVerification {}
