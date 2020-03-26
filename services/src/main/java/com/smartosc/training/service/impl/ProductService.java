package com.smartosc.training.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.entity.ProductEntity;
import com.smartosc.training.repository.CategoryRepository;
import com.smartosc.training.repository.PagingRepository;
import com.smartosc.training.repository.ProductRepository;
import org.springframework.util.ResourceUtils;

@Service
public class ProductService {

	@Autowired
	private PagingRepository pagingRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository repo;

	private ModelMapper map = new ModelMapper();
	
	private String reportPath;



	//Jasper
	public String generateReport() {
		reportPath = "D://";
		List<ProductEntity> listProduct = repo.findAll();
		try {
			File file = ResourceUtils.getFile("classpath:product.jrxml");
			InputStream input = new FileInputStream(file);


			// Compile the Jasper report from .jrxml to .japser

			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Get your data source

			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(listProduct);

			// Add parameters

			Map<String, Object> parameters = new HashMap<>();

			parameters.put("createdBy", "JavaHelper.org");

			// Fill the report

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);

			// Export the report to a PDF file

			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Product.pdf");

			System.out.println("PDF File Generated !!");

			xlsx(jasperPrint);

			csv(jasperPrint);

			return "Report successfully generated @path= " + reportPath;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();

		}

	}

	private void csv(JasperPrint jasperPrint) throws JRException {

		JRCsvExporter exporter = new JRCsvExporter();

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

		exporter.setExporterOutput(new SimpleWriterExporterOutput(reportPath + "\\Product.csv"));

		SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();

		configuration.setFieldDelimiter(",");

		exporter.setConfiguration(configuration);

		exporter.exportReport();

	}

		// Ref: https://www.programcreek.com/java-api-examples/?class=net.sf.jasperreports.export.SimpleXlsxReportConfiguration&method=setOnePagePerSheet

	private void xlsx(JasperPrint jasperPrint) throws JRException {

		// Exports a JasperReports document to XLSX format. It has character output type and exports the document to a grid-based layout.

		JRXlsxExporter exporter = new JRXlsxExporter();

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "\\Product.xlsx"));

		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();

		configuration.setOnePagePerSheet(true);

		configuration.setRemoveEmptySpaceBetweenColumns(true);

		configuration.setDetectCellType(true);

		exporter.setConfiguration(configuration);

		exporter.exportReport();

	}

	public List<ProductDTO> getAll() {
		List<ProductEntity> productEntities = repo.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		productEntities.forEach(pro -> {
			List<CategoryEntity> categoryEntities = pro.getCategories();
			List<CategoryDTO> categoryDTOs = new ArrayList<>();
			categoryEntities.forEach(cate -> {
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setDescription(cate.getDescription());
				categoryDTO.setId(cate.getId());
				categoryDTO.setImage(cate.getImage());
				categoryDTO.setName(cate.getName());
				categoryDTOs.add(categoryDTO);
			});
			ProductDTO productDTO = new ProductDTO();
			productDTO.setCategoryDTOs(categoryDTOs);
			productDTO.setDescription(pro.getDescription());
			productDTO.setId(pro.getId());
			productDTO.setImage(pro.getImage());
			productDTO.setName(pro.getName());
			productDTO.setPrice(pro.getPrice());
			productDTO.setStatus(pro.getStatus());
			productDTOs.add(productDTO);
		});
		return productDTOs;
	}

	public void save(ProductDTO product) {
		ProductEntity entity = new ProductEntity();
		List<CategoryDTO> categoryDTOs = product.getCategoryDTOs();
		List<CategoryEntity> categoryEntities = new ArrayList<>();
		if(categoryDTOs!=null) {
			categoryDTOs.forEach(c -> {
				CategoryEntity categoryEntity = new CategoryEntity();
				categoryEntity = map.map(c, CategoryEntity.class);
				categoryEntities.add(categoryEntity);
			});
		}
		entity.setCategories(categoryEntities);
		entity.setDescription(product.getDescription());
		entity.setImage(product.getImage());
		entity.setName(product.getName());
		entity.setPrice(product.getPrice());
		entity.setStatus(1);
		repo.save(entity);
	}

	public void update(Long id, ProductDTO product) {
		Optional<ProductEntity> entity1 = repo.findById(id);
		if (entity1.isPresent()) {
			ProductEntity entity = entity1.get();
			List<CategoryDTO> categoryDTOs = product.getCategoryDTOs();
			List<CategoryEntity> categoryEntities = new ArrayList<>();
			categoryDTOs.forEach(c -> {
				CategoryEntity categoryEntity = new CategoryEntity();
				categoryEntity = map.map(c, CategoryEntity.class);
				categoryEntities.add(categoryEntity);
			});
			entity.setCategories(categoryEntities);
			entity.setDescription(product.getDescription());
			entity.setImage(product.getImage());
			entity.setName(product.getName());
			entity.setPrice(product.getPrice());
			entity.setStatus(product.getStatus());
			repo.save(entity);
		}

	}

	public ProductDTO getById(Long id) {
		Optional<ProductEntity> productEntity = repo.findById(id);
		ProductDTO productDTO = new ProductDTO();
		if (productEntity.isPresent()) {
			ProductEntity entity = productEntity.get();
			List<CategoryEntity> categoryEntity = entity.getCategories();
			List<CategoryDTO> categoryDTOs = new ArrayList<>();
			categoryEntity.forEach(p -> {
				CategoryDTO dto = new CategoryDTO();
				dto.setDescription(p.getDescription());
				dto.setId(p.getId());
				dto.setImage(p.getImage());
				dto.setName(p.getName());
				categoryDTOs.add(dto);
			});
			productDTO.setCategoryDTOs(categoryDTOs);
			productDTO.setDescription(entity.getDescription());
			productDTO.setId(entity.getId());
			productDTO.setImage(entity.getImage());
			productDTO.setName(entity.getName());
			productDTO.setPrice(entity.getPrice());
			productDTO.setStatus(entity.getStatus());
		}
		return productDTO;
	}

	public void delete(Long id) {
		ProductEntity entity = repo.findById(id).get();
		repo.delete(entity);
	}

	public ProductEntity getProductByName(String productName) {
		return repo.getByName(productName);
	}

	public List<ProductEntity> getProductByPrice(double price) {
		return repo.getByPrice(price);

	}

	public List<ProductEntity> findByName(String searchkey) {
		return repo.findByName(searchkey);

	}

//	public Page<ProductDTO> getAllProductsByCategory(Long id, Pageable pageable) {
//		Page<ProductEntity> products = repo.getAllProductsByCategory(id, pageable);
//		Page<ProductDTO> listProductDTO = products.map(p -> this.convertProductDTO(p));
//		return listProductDTO;
//	}

	public List<ProductDTO> getAllProductsByPaging(Pageable pageable) {

		Page<ProductEntity> result = pagingRepository.findAll(pageable);
		List<ProductDTO> productDTOs = new ArrayList<>();
		result.forEach(pro -> {
			List<CategoryEntity> categoryEntities = pro.getCategories();
			List<CategoryDTO> categoryDTOs = new ArrayList<>();
			categoryEntities.forEach(cate -> {
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setDescription(cate.getDescription());
				categoryDTO.setId(cate.getId());
				categoryDTO.setImage(cate.getImage());
				categoryDTO.setName(cate.getName());
				categoryDTOs.add(categoryDTO);
			});
			ProductDTO productDTO = new ProductDTO();
			productDTO.setCategoryDTOs(categoryDTOs);
			productDTO.setDescription(pro.getDescription());
			productDTO.setId(pro.getId());
			productDTO.setImage(pro.getImage());
			productDTO.setName(pro.getName());
			productDTO.setPrice(pro.getPrice());
			productDTO.setStatus(pro.getStatus());
			productDTOs.add(productDTO);
		});
		return productDTOs;
	}

	private ProductDTO convertProductDTO(ProductEntity p) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(p.getId());
		productDTO.setName(p.getName());
		productDTO.setStatus(p.getStatus());
		productDTO.setDescription(p.getDescription());
		productDTO.setImage(p.getImage());
		productDTO.setPrice(p.getPrice());
		return productDTO;
	}

	public Page<ProductDTO> getAllPageProduct(Pageable pageable) {
		Page<ProductEntity> products = repo.findAll(pageable);
		Page<ProductDTO> listProductDTO = products.map(p -> this.convertProductDTO(p));
		return listProductDTO;
	}
	
	public Page<ProductDTO> getAllProductsByCategoryId(Long id, Pageable pageable) {
		CategoryEntity category = categoryRepository.findById(id).get();
		Page<ProductEntity> products = repo.findByCategories(category, pageable);
		Page<ProductDTO> listProductDTO = products.map(p -> this.convertProductDTO(p));
		return listProductDTO;
	}

}
