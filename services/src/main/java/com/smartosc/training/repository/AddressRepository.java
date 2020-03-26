package com.smartosc.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartosc.training.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
