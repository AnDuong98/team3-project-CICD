package com.smartosc.training.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.ProductDTO;
@Service
public class ProductServiceImpl {
	@Autowired
	private RestTemplateServiceImpl restemplateService;
	
	
	private String url = "http://localhost:8000/product";
	
	private String urlcount = "http://localhost:8000/product/count";
	

	public List<ProductDTO> showProduct() {
		return restemplateService.getSomething(url, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<List<ProductDTO>>>() {});
	}
	
	public int countProduct() {
		return restemplateService.getSomething(urlcount, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<Integer>>() {});
	}
	
	public ProductDTO createProduct(@RequestBody ProductDTO product, HttpServletRequest request) {
		//getSomething(url, method, headers, body, reponseType);
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		return restemplateService.getSomething(url, HttpMethod.POST, null, product,new ParameterizedTypeReference<APIResponse<ProductDTO>>() {});
	}
	
	public ProductDTO updateProduct(@RequestBody ProductDTO product, HttpServletRequest request) {
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		return restemplateService.getSomething(url, HttpMethod.PUT, header, product,new ParameterizedTypeReference<APIResponse<ProductDTO>>() {});
	}
	
	public void deleteProduct(@RequestBody Long[] ids, HttpServletRequest request) {
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		restemplateService.getSomething(url, HttpMethod.DELETE, null, ids,new ParameterizedTypeReference<APIResponse<String>>() {});
	}
}
