package com.smartosc.training.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.RoleDTO;
@Service
public class RoleServiceImpl {
	@Autowired
	private RestTemplateServiceImpl restemplateService;
	@Autowired
	private ServicesConfig servicesConfig;
	
	public List<RoleDTO> findByUserName(String username, String token) {
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(token);
		String url = servicesConfig.getHotsName()+servicesConfig.getRoleAPI();
		return restemplateService.getSomething(url+"/"+username, HttpMethod.GET, header, null,new ParameterizedTypeReference<APIResponse<List<RoleDTO>>>() {});
	}
	
//	public UserDTO findById(Long id) {
//		return restemplateService.getSomething(url+"/"+id, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
//	}
//	
//	public int countUser() {
//		return restemplateService.getSomething(urlcount, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<Integer>>() {});
//	}
//	
//	public UserDTO createUser(UserDTO User, HttpServletRequest request) {
//		//getSomething(url, method, headers, body, reponseType);
//		HttpHeaders header = new HttpHeaders();
//		header.add("Authorization", request.getHeader("Authorization"));
//		return restemplateService.getSomething(url, HttpMethod.POST, null, User,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
//	}
//	
//	public UserDTO updateUser(@RequestBody UserDTO User, HttpServletRequest request) {
//		HttpHeaders header = new HttpHeaders();
//		header.add("Authorization", request.getHeader("Authorization"));
//		return restemplateService.getSomething(url, HttpMethod.PUT, header, User,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
//	}
//	
//	public void deleteUser(@RequestBody Long[] ids, HttpServletRequest request) {
//		HttpHeaders header = new HttpHeaders();
//		header.add("Authorization", request.getHeader("Authorization"));
//		restemplateService.getSomething(url, HttpMethod.DELETE, null, ids,new ParameterizedTypeReference<APIResponse<String>>() {});
//	}
}
