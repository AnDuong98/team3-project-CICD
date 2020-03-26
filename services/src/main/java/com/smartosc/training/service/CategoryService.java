package com.smartosc.training.service;

import java.io.IOException;
import java.util.Collection;

import com.smartosc.training.dto.CategoryDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
	public abstract void createCategory(CategoryDTO category);
	public abstract void updateCategory(CategoryDTO category);
	public abstract void deleteCategory(Long id);
	public abstract Collection<CategoryDTO> getCategories();
	public abstract Collection<CategoryDTO> searchByCat(String searchKey);
	CategoryDTO show(Long id);
	//Import & export file Excel, xtx
	String generatedReport();
	public Collection<CategoryDTO> importExcelFile(MultipartFile file) throws IOException;
	public Collection<CategoryDTO> importTXTFile(MultipartFile file) throws IOException;
}
