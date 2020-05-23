package com.jovanibrasil.captcha.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("captcha")
public class CaptchaProperties {

    private String keysite;
    private String keysecret;
	
    public String getKeysite() {
		return keysite;
	}
	public void setKeysite(String keySite) {
		this.keysite = keySite;
	}
	public String getKeysecret() {
		return keysecret;
	}
	public void setKeysecret(String keySecret) {
		this.keysecret = keySecret;
	}
    
}
