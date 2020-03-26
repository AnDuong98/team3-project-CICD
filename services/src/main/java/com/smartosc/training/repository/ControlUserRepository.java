package com.smartosc.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartosc.training.entity.UserEntity;

public interface ControlUserRepository extends JpaRepository<UserEntity, Long>{
	public UserEntity findByUsername(String name);
	
}
