package com.smartosc.training.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.RoleDTO;
import com.smartosc.training.entity.RoleEntity;
import com.smartosc.training.repository.RoleRepository;
import com.smartosc.training.service.IRoleService;
@Service
public class RoleServiceImpl implements IRoleService{
	@Autowired
	private RoleRepository roleRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public List<RoleDTO> findByUser_UserName(String username) {
		List<RoleDTO> results = new ArrayList<>();
		List<RoleEntity> entities = roleRepository.findByUsers_Username(username);
		for (RoleEntity item : entities) {
			RoleDTO roleDTO = modelMapper.map(item, RoleDTO.class);
			results.add(roleDTO);
		}
		return results;
	}

	@Override
	public RoleDTO findByName(String name) {
		RoleEntity results = roleRepository.findByName(name);
		return modelMapper.map(results, RoleDTO.class);
	}
}
