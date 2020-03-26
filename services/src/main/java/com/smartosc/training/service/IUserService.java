package com.smartosc.training.service;

import java.util.List;

import com.smartosc.training.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import com.smartosc.training.dto.UserDTO;

public interface IUserService{
	public UserDTO save(UserDTO UserDTO);
	public void delete(Long[] ids);
	public List<UserDTO> findAll(Pageable page);
	public List<UserDTO> findAll();
	public UserDTO findById(long id);
	public int totalItem();
	public UserDTO findByUserame(String name);
	public String generateReport();
	public List<UserDTO> registerAlot(List<UserDTO> userDTOS);
}