package com.smartosc.training.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ServicesConfig {
	private String hotsName;
	
	private String token;
	
	private String authenticate;
	
	private String roleAPI;
	
	private String userAPI;

	private String productAPI;

	private String categoryAPI;
	
	private String orderAPI;
	
	
	
	
	
	public String getOrderAPI() {
		return orderAPI;
	}

	public void setOrderAPI(String orderAPI) {
		this.orderAPI = orderAPI;
	}

	public String getUserAPI() {
		return userAPI;
	}

	public void setUserAPI(String userAPI) {
		this.userAPI = userAPI;
	}

	public String getCategoryAPI() {
		return categoryAPI;
	}

	public void setCategoryAPI(String categoryAPI) {
		this.categoryAPI = categoryAPI;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRoleAPI() {
		return roleAPI;
	}

	public void setRoleAPI(String roleAPI) {
		this.roleAPI = roleAPI;
	}

	public String getAuthenticate() {
		return authenticate;
	}

	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}

	public String getHotsName() {
		return hotsName;
	}

	public void setHotsName(String hotsName) {
		this.hotsName = hotsName;
	}

	public String getProductAPI() {
		return productAPI;
	}

	public void setProductAPI(String productAPI) {
		this.productAPI = productAPI;
	}

}