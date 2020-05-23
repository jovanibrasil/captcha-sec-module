package com.jovanibrasil.captcha.property;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaSettings {

	private String site;
    private String secret;

    public CaptchaSettings(CaptchaProperties configuration) {
    	this.site = configuration.getKeysite();
    	this.secret = configuration.getKeysecret();
    }

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
