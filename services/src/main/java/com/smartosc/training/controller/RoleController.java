package com.smartosc.training.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.RoleDTO;
import com.smartosc.training.entity.RoleEntity;
import com.smartosc.training.service.IRoleService;

import com.smartosc.training.exception.NotFoundException;

import static com.smartosc.training.exception.NotFoundException.supplier;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {
	
	private static Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private IRoleService roleService;

	
	@GetMapping(value = "/role/{name}")
	public ResponseEntity<?> findByName(@PathVariable("name") String name) 
			throws IllegalArgumentException, NotFoundException {
		List<RoleDTO> role = new ArrayList<>();
		try {
			log.info("Role Name: {}",name);
			role = roleService.findByUser_UserName(name);
		} catch (Exception e) {
			// TODO: handle exception
			throw new NotFoundException(supplier(RoleEntity.class.getSimpleName()).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done" ,role), HttpStatus.OK);
	}

}
