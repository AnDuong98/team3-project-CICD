package com.smartosc.training.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.utils.SecurityUtil;

@Service
public class UserServiceImpl {
	@Autowired
	private RestTemplateServiceImpl restemplateService;
	@Autowired
	private ServicesConfig servicesConfig;
	
	
	public UserDTO findById(Long id) {
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		return restemplateService.getSomething(url+"/update/"+id, HttpMethod.GET, header, null,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
	public UserDTO findByName(String name,String token) {
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(token);
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		return restemplateService.getSomething(url+"/username/"+name, HttpMethod.GET, header, null,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
	
	public UserDTO updateUserInfo(UserDTO userDTO, Long id) {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url+"/update/"+id, HttpMethod.POST, header, userDTO,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});

	}
	
	public UserDTO registerUser(UserDTO userDTO) {
		String url="http://localhost:8004/register";
		return restemplateService.getSomething(url, HttpMethod.POST, null, userDTO, new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
}
