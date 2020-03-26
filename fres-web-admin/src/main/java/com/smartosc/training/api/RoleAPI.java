package com.smartosc.training.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.RoleDTO;
import com.smartosc.training.service.impl.RoleServiceImpl;

@RestController
public class RoleAPI {
	@Autowired
	private RoleServiceImpl roleService;

	@GetMapping(value = "/admin/rolename")
	public List<RoleDTO> showRole() {
		return roleService.findByUserName("admin", "null");
	}
	
//	@GetMapping(value = "/admin/Role/{id}")
//	public RoleDTO findById(@PathVariable("id") Long id) {
//		return RoleService.findById(id);
//	}
//	
//	@GetMapping(value = "/admin/Role/count")
//	public int countRole() {
//		return restemplateService.getSomething(urlcount, HttpMethod.GET, null, null,new ParameterizedTypeReference<APIResponse<Integer>>() {});
//	}
//	
//	@PostMapping(value = "/admin/Role")
//	public RoleDTO createRole(@ModelAttribute("model") RoleDTO Role, HttpServletRequest request) {
//		return RoleService.createRole(Role, request);
//	}
//	
//	@PutMapping(value = "/admin/Role")
//	public RoleDTO updateRole(@RequestBody RoleDTO Role, HttpServletRequest request) {
//		HttpHeaders header = new HttpHeaders();
//		header.add("Authorization", request.getHeader("Authorization"));
//		return restemplateService.getSomething(url, HttpMethod.PUT, header, Role,new ParameterizedTypeReference<APIResponse<RoleDTO>>() {});
//	}
//	
//	@DeleteMapping(value = "/admin/Role")
//	public void deleteRole(@RequestBody Long[] ids, HttpServletRequest request) {
//		HttpHeaders header = new HttpHeaders();
//		header.add("Authorization", request.getHeader("Authorization"));
//		restemplateService.getSomething(url, HttpMethod.DELETE, null, ids,new ParameterizedTypeReference<APIResponse<String>>() {});
//	}

}
