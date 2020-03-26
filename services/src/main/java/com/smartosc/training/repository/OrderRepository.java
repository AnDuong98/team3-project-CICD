package com.smartosc.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartosc.training.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
	
}
