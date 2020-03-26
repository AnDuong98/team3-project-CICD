package com.smartosc.training.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.AddressDTO;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.entity.UserEntity;
import com.smartosc.training.repository.ControlUserRepository;

@Service
public class ControlUserService {
	@Autowired
	private ControlUserRepository controlUserRepository;

	private ModelMapper map = new ModelMapper();

	public void updateUser(Long id, UserDTO userDTO) {
		Optional<UserEntity> entity1 = controlUserRepository.findById(id);
		if(entity1.isPresent()) {
			UserEntity userEntity = entity1.get();
			AddressDTO dto = userDTO.getAddress();
			
			AddressEntity addressEntity = userEntity.getAddress();
			
			addressEntity.setAddress(dto.getAddress());
			addressEntity.setGender(dto.getGender());
			addressEntity.setPhone(dto.getPhone());
		
			
			userEntity.setAddress(addressEntity);
			userEntity.setFullName(userDTO.getFullname());
			userEntity.setEmail(userDTO.getEmail());
			
			
			controlUserRepository.save(userEntity);
		}
		
	}

	public UserDTO getUserById(Long id) {
		Optional<UserEntity> user = controlUserRepository.findById(id);
		UserDTO userDTO = new UserDTO();
		if (user.isPresent()) {
			UserEntity userEntity = user.get();
			AddressEntity addressEntity = userEntity.getAddress();
			AddressDTO addressDTO = map.map(addressEntity, AddressDTO.class);

			userDTO.setAddress(addressDTO);
			userDTO.setEmail(userEntity.getEmail());
			userDTO.setFullname(userEntity.getFullName());
			userDTO.setId(userEntity.getId());
			userDTO.setUsername(userEntity.getUsername());
			userDTO.setPassword(userEntity.getPassword());
		}
		return userDTO;
	}

}
