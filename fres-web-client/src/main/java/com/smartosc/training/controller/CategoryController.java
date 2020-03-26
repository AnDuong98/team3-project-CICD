package com.smartosc.training.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;


@Controller
@RequestMapping("/Categories")
public class CategoryController {

	@Autowired
	RestTemplateService categoryRestTemplate;

	@GetMapping
	public String listAll(Model modelMap) {
		String url = "http://localhost:8000/categories";
		ParameterizedTypeReference<APIResponse<List<CategoryDTO>>> reponseType = new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>(){};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		List<CategoryDTO> list = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("categories", list);
		return "Category/category-show";
	}

	
	@GetMapping(value = "/detail/{id}")
	public String show(Model modelMap, @PathVariable("id") Long id) {
		String url = "http://localhost:8000/categories/" + id;
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		CategoryDTO category = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("category", category);
		return "Category/category-detail";
	}
	


}
