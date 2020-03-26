package com.smartosc.training.service.impl;

import java.util.List;

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
	

	public List<UserDTO> showUser() {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url, HttpMethod.GET, header, null,new ParameterizedTypeReference<APIResponse<List<UserDTO>>>() {});
	}
	
	public UserDTO findById(Long id) {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url+"/"+id, HttpMethod.GET, header, null,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
	
	public int countUser() {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI()+"/count";
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url, HttpMethod.GET, header, header,new ParameterizedTypeReference<APIResponse<Integer>>() {});
	}
	
	public UserDTO createUser(UserDTO userDTO) {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url, HttpMethod.POST, header, userDTO,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
	
	public UserDTO updateUser(UserDTO User) {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url, HttpMethod.PUT, header, User,new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}
	
	public void deleteUser(Long[] ids) {
		String url = servicesConfig.getHotsName() + servicesConfig.getUserAPI();
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getJWTToken());
		restemplateService.getSomething(url, HttpMethod.DELETE, header, ids,new ParameterizedTypeReference<APIResponse<String>>() {});
	}

	public String exportExecl(){
		String url = servicesConfig.getHotsName() +"/report";
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url,HttpMethod.GET,header,"Export Sucess!!",new ParameterizedTypeReference<APIResponse<String>>() {});
	}

	public UserDTO registerUser(UserDTO userDTO) {
		String url=servicesConfig.getHotsName()+"/register";
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return restemplateService.getSomething(url, HttpMethod.POST, header, userDTO, new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}

	public List<UserDTO> importAll(List<UserDTO> userDTOS){
		String url=servicesConfig.getHotsName()+"/addUser";
		HttpHeaders header = new HttpHeaders();
		header.setBearerAuth(SecurityUtil.getJWTToken());
		return (List<UserDTO>) restemplateService.getSomething(url, HttpMethod.POST, header, userDTOS, new ParameterizedTypeReference<APIResponse<UserDTO>>() {});
	}

}
