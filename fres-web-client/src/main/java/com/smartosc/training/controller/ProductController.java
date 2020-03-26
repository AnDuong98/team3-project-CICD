package com.smartosc.training.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;

@Controller
@RequestMapping("/Products")
public class ProductController {

	@Autowired
	RestTemplateService productRestTemplate;
	@Autowired
	ServicesConfig servicesConfig;

	@GetMapping
	public String listAll(Model modelMap) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
		ParameterizedTypeReference<APIResponse<List<ProductDTO>>> reponseType = new ParameterizedTypeReference<APIResponse<List<ProductDTO>>>(){};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		List<ProductDTO> list = productRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("products", list);
		return "Product/product-show";
	}

	
	@GetMapping(value = "/detail/{id}")
	public String show(Model modelMap, @PathVariable("id") Long id) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI())+"/"+id;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		ProductDTO product = productRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("product", product);
		return "Product/product-detail";
	}
	
	@GetMapping(value="/search/{id}")
	public String searchProduct(@RequestParam(name = "id") String searchKey,Model modelMap) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI())+"/searchByName/" + searchKey;
		ParameterizedTypeReference<APIResponse<List<ProductDTO>>> reponseType = new ParameterizedTypeReference<APIResponse<List<ProductDTO>>>(){};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		List<ProductDTO> list = productRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("list", list);
		return "Search";
	}
	
}
