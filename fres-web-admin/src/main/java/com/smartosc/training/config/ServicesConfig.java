package com.smartosc.training.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
@Getter@Setter
public class ServicesConfig {
	private String hotsName;

	private String productAPI;
	
	private String categoryAPI;
	
	private String userAPI;
	
	private String roleAPI;
	
	private String token;

	private String insertBatchAPI;

}