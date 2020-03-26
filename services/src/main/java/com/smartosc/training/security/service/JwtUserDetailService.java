package com.smartosc.training.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.entity.RoleEntity;
import com.smartosc.training.entity.UserEntity;
import com.smartosc.training.repository.AddressRepository;
import com.smartosc.training.repository.RoleRepository;
import com.smartosc.training.security.repo.UserRepository;

@Service
public class JwtUserDetailService implements UserDetailsService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		UserEntity users = this.userRepository.findByUsername(userName);

		if (users == null|| users.getStatus() == 0) {
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
		
		
		
		List<GrantedAuthority> grantList = new ArrayList<>();
		List<RoleEntity> roleNames = this.roleRepository.findByUsers_Username(userName);
		if (roleNames != null) {
			for (RoleEntity role : roleNames) {
				grantList.add(new SimpleGrantedAuthority(role.getName()));
			}
		}
		
		return new User(users.getUsername(), users.getPassword(), grantList);
	}
	
	public UserEntity save(UserDTO user) {
		UserEntity newUser = new UserEntity(user.getFullname(), user.getEmail(), user.getUsername(), bcryptEncoder.encode(user.getPassword()));
		newUser.setStatus(1);
		AddressEntity addressEntity = addressRepository.save(new AddressEntity());
		Optional<RoleEntity> role = roleRepository.findById(new Long(2));
		if(role.isPresent()) {
			RoleEntity roleEntities = role.get();
			List<RoleEntity> entities = new ArrayList<>();
			entities.add(roleEntities);
			newUser.setRoles(entities);
			newUser.setAddress(addressEntity);
		}
		return userRepository.save(newUser);
	}
}