package com.smartosc.training.service;

import java.util.List;

import com.smartosc.training.dto.RoleDTO;

public interface IRoleService {
	public List<RoleDTO> findByUser_UserName(String username);
	public RoleDTO findByName(String username);
}
