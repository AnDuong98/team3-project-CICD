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
import com.smartosc.training.utils.SecurityUtil;
@Service
public class RoleServiceImpl {
	@Autowired
	private RestTemplateServiceImpl restemplateService;
	@Autowired
	private ServicesConfig servicesConfig;
	
	public List<RoleDTO> findByUserName(String username,String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		String url = servicesConfig.getHotsName()+servicesConfig.getRoleAPI();
		return restemplateService.getSomething(url+"/"+username, HttpMethod.GET, headers, null,new ParameterizedTypeReference<APIResponse<List<RoleDTO>>>() {});
	}
	
}
