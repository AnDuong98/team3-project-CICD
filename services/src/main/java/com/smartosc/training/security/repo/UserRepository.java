package com.smartosc.training.security.repo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.smartosc.training.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	
	@Query("select e from UserEntity e where username = :username")
	UserEntity findByUsername(String username);
}