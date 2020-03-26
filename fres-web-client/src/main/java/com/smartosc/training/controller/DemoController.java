package com.smartosc.training.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.dto.Pagination;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;

@Controller
@RequestMapping(value = "/wines")
public class DemoController {

	@Autowired
	RestTemplateService productRestTemplate;
	@Autowired
	ServicesConfig servicesConfig;

//	@GetMapping("/home")
//	public String home() {
//		return "./products/index";
//	}
//
//	@GetMapping("/login")
//	public String login() {
//		return "./products/login";
//	}
//
//	@GetMapping("/register")
//	public String register() {
//		return "./products/register";
//	}

	// Show shop website
	@GetMapping()
	public String listAllProductsWithPAgination(Model modelMap,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "6") Integer size) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());

		// List products
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/list/") + page + "/"
				+ size;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};
		APIResponse<List<ProductDTO>> list = productRestTemplate.exchangePaging(url, HttpMethod.GET, headers,reponseType);
		Pagination pagination = list.getPagination();
		modelMap.addAttribute("page", pagination);
		modelMap.addAttribute("listproduct", list.getData());

		// List Categories
		String urlCat = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<List<CategoryDTO>>> reponseCat = new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>() {
		};
		List<CategoryDTO> listCat = productRestTemplate.getSomething(urlCat, HttpMethod.GET, headers, null, reponseCat);
		modelMap.addAttribute("listCategories", listCat);

		return "./products/list-product";
	}

	@GetMapping("/category/{id}")
	public String listAllProductsByCat(Model modelMap, @PathVariable("id") Long id,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "9") Integer size) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());

		// List products
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/") + id + "/" + page
				+ "/" + size;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};
		APIResponse<List<ProductDTO>> list = productRestTemplate.exchangePaging(url, HttpMethod.GET, headers,
				reponseType);

		if (list.getData().isEmpty()) {
			modelMap.addAttribute("listproduct", list.getMessage());
		} else {
			Pagination pagination = list.getPagination();
			modelMap.addAttribute("categoryId", id);
			modelMap.addAttribute("page", pagination);
			modelMap.addAttribute("listproduct", list.getData());
		}

		// List Categories
		String urlCat = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<List<CategoryDTO>>> reponseCat = new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>() {
		};
		List<CategoryDTO> listCat = productRestTemplate.getSomething(urlCat, HttpMethod.GET, null, headers, reponseCat);

		modelMap.addAttribute("listCategories", listCat);

		return "./products/list-product-by-cat";
	}

	@GetMapping(value = "/detail/{id}")
	public String showDetail(Model modelMap, @PathVariable("id") Long id) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()) + "/" + id;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		ProductDTO product = productRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("product", product);

		String urlCat = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<List<CategoryDTO>>> reponseCat = new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>() {
		};
		List<CategoryDTO> listCat = productRestTemplate.getSomething(urlCat, HttpMethod.GET, headers, null, reponseCat);
		List<CategoryDTO> selectedCat = new ArrayList<>();
		selectedCat = product.getCategoryDTOs();
		Set<Long> selectedPartsLongSet = selectedCat.stream().map(CategoryDTO::getId).collect(Collectors.toSet());
		modelMap.addAttribute("selectedPartsLongSet", selectedPartsLongSet);
		modelMap.addAttribute("listCat", listCat);

		Long idCat = product.getId();

		// List product by category

		modelMap.addAttribute("idCat", idCat);

		return "./products/product-detail";
	}

	@PostMapping("/search")
	public String searchByName(Model model, @RequestParam("searchKey") String key) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/searchByName/") + key;
		ParameterizedTypeReference<APIResponse<ProductDTO>> reponseType = new ParameterizedTypeReference<APIResponse<ProductDTO>>() {
		};

		// List found products
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		APIResponse<List<ProductDTO>> list = productRestTemplate.exchangePaging(url, HttpMethod.GET, headers,
				reponseType);
		model.addAttribute("listproduct", list.getData());

		// List Categories
		String urlCat = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<List<CategoryDTO>>> reponseCat = new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>() {
		};
		List<CategoryDTO> listCat = productRestTemplate.getSomething(urlCat, HttpMethod.GET, null, headers, reponseCat);
		model.addAttribute("listCategories", listCat);

		return "./products/search";

	}

	/*
	 * @GetMapping("/error") public String error() { return "./error/500"; }
	 */
	@GetMapping("/cart")
	public String showCart() {
		return "./products/product-cart";
	}

	@GetMapping("/checkout")
	public String checkout() {
		return "./products/checkout";
	}

	@GetMapping("/about")
	public String about() {
		return "./products/about";
	}

	@GetMapping("/thanks")
	public String completeOrder() {
		return "./products/thankyou";
	}

}
