package com.smartosc.training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartosc.training.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

	List<RoleEntity> findByUsers_Username(String username);

	RoleEntity findByName(String string);
}
