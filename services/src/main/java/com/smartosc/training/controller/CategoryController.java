package com.smartosc.training.controller;


import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.exception.InvalidSearchParamException;
import com.smartosc.training.exception.NotFoundException;
import com.smartosc.training.repository.CategoryRepository;
import com.smartosc.training.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static com.smartosc.training.exception.InvalidSearchParamException.supplier;
import static com.smartosc.training.exception.NotFoundException.supplier;

@RestController
@RequestMapping(value = "/categories")

public class CategoryController {

	private ServletContext servletContext;

	@Autowired
	private CategoryService catService;
	@Autowired
	private CategoryRepository repo;

	@GetMapping
	@PermitAll
	public ResponseEntity<?> getCategory() throws NotFoundException {

		Collection<CategoryDTO> list = catService.getCategories();
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "okay", list), HttpStatus.OK);
	}

	@GetMapping("/paging")
	  public ResponseEntity<?> listCategory(
	      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
	      @RequestParam(name = "size", required = false, defaultValue = "3") Integer size,
	      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) 
	    		  throws NotFoundException, MissingServletRequestParameterException {
	    Sort sortable = null;
	    if (sort.equals("ASC")) {
	      sortable = Sort.by("name").ascending();
	    }
	    if (sort.equals("DESC")) {
	      sortable = Sort.by("name").descending();
	    }
	    Pageable pageable = PageRequest.of(page, size, sortable);
	    if (pageable.isUnpaged()) {
			throw new NotFoundException(supplier(Page.class.getSimpleName()).get().getMessage());
		}
	    return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "page",repo.findAll(pageable)), HttpStatus.OK);
	}	

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getCatById(@PathVariable("id") Long id) 
			throws NotFoundException {
		Optional<CategoryEntity> cate = repo.findById(id);
		if (!cate.isPresent()) {
			throw new NotFoundException(supplier(CategoryEntity.class.getSimpleName(), id).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "okay", catService.show(id)),
				HttpStatus.OK);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO)
			throws MethodArgumentNotValidException, NotFoundException {
		Optional<CategoryEntity> cate = repo.findById(categoryDTO.getId());
		if (!cate.isPresent()) {
			throw new NotFoundException(supplier(CategoryEntity.class.getSimpleName(),categoryDTO.getId() ).get().getMessage());
		}
		catService.updateCategory(categoryDTO);
		return new ResponseEntity<>( new APIResponse<>(HttpStatus.OK.value(), "Category is updated successfully", categoryDTO), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deleteCategory(@PathVariable("id") Long id) throws NotFoundException 
			{
		Optional<CategoryEntity> cate = repo.findById(id);
		if (!cate.isPresent()) {
			throw new NotFoundException(supplier(CategoryEntity.class.getSimpleName(), id).get().getMessage());
		}
		catService.deleteCategory(id);
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Category is deleted successfully", cate),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryDTO cat) 
			throws MethodArgumentNotValidException {
		catService.createCategory(cat);
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Category is created successfully", cat),
				HttpStatus.OK);
	}

	@GetMapping(value = "/search/{id}")
	public ResponseEntity<?> searchByCategory(@PathVariable(name ="id") String searchKey){
	Collection<CategoryDTO> list = catService.searchByCat(searchKey);
	if (list.isEmpty()) {
		throw new InvalidSearchParamException(supplier("id", searchKey).get().getMessage());
	}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"Category is created successfully", list), HttpStatus.CREATED);
	}

	@GetMapping(value = "/report")
	public ResponseEntity<?> generatedReport() {
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"Category is created successfully", catService.generatedReport()), HttpStatus.CREATED);
	}

	@PostMapping(value = "/import")
	public void importExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
		catService.importExcelFile(file);
	}

	@PostMapping(value = "/importTXT")
	public void importTxtFile(@RequestParam("file") MultipartFile file) throws IOException{
		catService.importTXTFile(file);
	}

}
