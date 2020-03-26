package com.smartosc.training.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartosc.training.entity.CategoryEntity;

@Repository
public interface CategoryRepository
		extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {
	@Query("FROM CategoryEntity c WHERE c.name LIKE %:searchKey%")
	Collection<CategoryEntity> findByName(@Param("searchKey") String searchKey);
	
	
	Optional<CategoryEntity> findById(Long id);
}
