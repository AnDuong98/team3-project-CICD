package com.smartosc.training.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.service.impl.RestTemplateServiceImpl;

@RestController
public class ProductAPI {
	@Autowired
	private RestTemplateServiceImpl restemplateService;
	
	
	private String url = "http://localhost:8084/product";
	
	private String urlcount = "http://localhost:8084/product/count";
	

	@GetMapping(value = "/product/rest/template")
	public List<ProductDTO> showProduct() {
		return restemplateService.getSomething(url, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<List<ProductDTO>>>() {});
	}
	
	@GetMapping(value = "/product/rest/template/count")
	public int countProduct() {
		return restemplateService.getSomething(urlcount, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<Integer>>() {});
	}
	
	@PostMapping(value = "/product/rest/template")
	public ProductDTO createProduct(@RequestBody ProductDTO product, HttpServletRequest request) {
		//getSomething(url, method, headers, body, reponseType);
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		return restemplateService.getSomething(url, HttpMethod.POST, null, product,new ParameterizedTypeReference<APIResponse<ProductDTO>>() {});
	}
	
	@PutMapping(value = "/product/rest/template")
	public ProductDTO updateProduct(@RequestBody ProductDTO product, HttpServletRequest request) {
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		return restemplateService.getSomething(url, HttpMethod.PUT, header, product,new ParameterizedTypeReference<APIResponse<ProductDTO>>() {});
	}
	
	@DeleteMapping(value = "/product/rest/template")
	public void deleteProduct(@RequestBody Long[] ids, HttpServletRequest request) {
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", request.getHeader("Authorization"));
		restemplateService.getSomething(url, HttpMethod.DELETE, null, ids,new ParameterizedTypeReference<APIResponse<String>>() {});
	}

}
