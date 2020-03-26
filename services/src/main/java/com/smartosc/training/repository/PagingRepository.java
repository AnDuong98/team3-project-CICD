package com.smartosc.training.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.smartosc.training.entity.ProductEntity;

@Repository
public interface PagingRepository extends PagingAndSortingRepository<ProductEntity, Long> {

}
