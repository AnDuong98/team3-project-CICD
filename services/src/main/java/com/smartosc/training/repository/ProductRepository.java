package com.smartosc.training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	// JPQL
	@Query("SELECT p.name FROM ProductEntity p where p.id = :productId")
	String getNameById(Long productId);

	@Query("SELECT p FROM ProductEntity p where p.name = :productName")
	ProductEntity getByName(String productName);

	@Query("SELECT p FROM ProductEntity p where p.price = :price")
	List<ProductEntity> getByPrice(double price);

	@Query("FROM ProductEntity p WHERE p.name LIKE %:searchKey%")
	List<ProductEntity> findByName(@Param("searchKey") String searchkey);
	
	
	Page<ProductEntity> findByCategories(CategoryEntity category,Pageable pageable);
}
