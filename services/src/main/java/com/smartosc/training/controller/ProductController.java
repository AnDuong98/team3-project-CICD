package com.smartosc.training.controller;

import static com.smartosc.training.exception.InvalidSearchParamException.supplier;
import static com.smartosc.training.exception.NotFoundException.supplier;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.Pagination;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.entity.ProductEntity;
import com.smartosc.training.exception.InvalidSearchParamException;
import com.smartosc.training.exception.NotFoundException;
import com.smartosc.training.service.impl.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @GetMapping("/report")
    public ResponseEntity<?> empReport() {
        service.generateReport();
        return new ResponseEntity(new APIResponse<>(HttpStatus.OK.value(), "done", ""), HttpStatus.OK);
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<Object> listProduct() throws NotFoundException {
        List<ProductDTO> products = service.getAll();
        /*
         * if (products.isEmpty()) { throw new NotFoundException("Product(s) is empty");
         * }
         */
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "done", products), HttpStatus.OK);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getProductByPrice(@PathVariable(name = "id") Long productId)
            throws NotFoundException {
        ProductDTO product = service.getById(productId);
        if (product.getId() == null) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName(), productId).get().getMessage());
        }
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "done", product), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductDTO product)
            throws MethodArgumentNotValidException, NoHandlerFoundException {
        service.save(product);
        return new ResponseEntity<>(new APIResponse<ProductDTO>(HttpStatus.OK.value(), "Create success", product),
                HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductDTO product)
            throws NotFoundException {
        ProductDTO pro = service.getById(id);
        if (pro.getId() == null) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName(), id).get().getMessage());
        }
        service.update(id, product);

        return new ResponseEntity<>(
                new APIResponse<ProductDTO>(HttpStatus.OK.value(), "Product is update successfully", product),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") Long id) throws Exception {
        ProductDTO pro = service.getById(id);

        if (pro.getId() == null) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName(), id).get().getMessage());
        }
        service.delete(id);
        return new ResponseEntity<>(
                new APIResponse<ProductEntity>(HttpStatus.OK.value(), "Product is deleted successfully"),
                HttpStatus.OK);
    }

    @GetMapping(value = "/getProductByPrice/{price}")
    public ResponseEntity<APIResponse<List<ProductEntity>>> getProductByPrice(
            @PathVariable(name = "price") double price) throws Exception {
        List<ProductEntity> products = service.getProductByPrice(price);
        if (products.isEmpty()) {
            throw new NotFoundException("Product(s) is empty");
        }
        return new ResponseEntity<>(
                new APIResponse<List<ProductEntity>>(HttpStatus.OK.value(), "getProductByPrice successfully", products),
                HttpStatus.OK);
    }

    // Search By Name
    @GetMapping(value = "/searchByName/{name}")
    public ResponseEntity<Object> searchByName(@PathVariable(name = "name") String searchkey) throws Exception {
        List<ProductEntity> listP = new ArrayList<>();
        listP = service.findByName(searchkey);
        if (listP.isEmpty()) {
            throw new InvalidSearchParamException(supplier("name", searchkey).get().getMessage());
        }
        List<ProductDTO> list = new ArrayList<>();
        for (ProductEntity p : listP) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(p.getName());
            productDTO.setImage(p.getImage());
            productDTO.setPrice(p.getPrice());
            productDTO.setDescription(p.getDescription());
            productDTO.setCreatedDate(p.getCreateddate());
            productDTO.setModifiedDate(p.getModifieddate());
            productDTO.setStatus(p.getStatus());
            list.add(productDTO);
        }
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "List Result Search :", list),
                HttpStatus.OK);

    }

    // Pagination - ThanhTTT
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> getAllProductsByPage(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size)
            throws MissingServletRequestParameterException, NotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        List<ProductDTO> list = service.getAllProductsByPaging(pageable);
        if (list.isEmpty()) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName()).get().getMessage());
        }
        Page<ProductDTO> pages = new PageImpl<ProductDTO>(list, pageable, list.size());
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "List A", pages), HttpStatus.OK);

    }

    @GetMapping(value = "/list/{page}/{size}")
    public ResponseEntity<Object> pagination(@PathVariable("page") Integer page, @PathVariable("size") Integer size)
            throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        APIResponse<List<ProductDTO>> responseData = new APIResponse<>();
        Page<ProductDTO> productPage = service.getAllPageProduct(pageable);
        if (productPage.isEmpty()) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName()).get().getMessage());
        }
        responseData.setStatus(HttpStatus.OK.toString());
        responseData.setMessage("get all page successful");
        if (productPage != null && !CollectionUtils.isEmpty(productPage.getContent())) {
            responseData.setData(productPage.getContent());
            Pagination metaData = new Pagination(page, size, productPage.getTotalPages(),
                    productPage.getTotalElements());
            responseData.setPagination(metaData);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);

    }

    @GetMapping(value = "/{id}/{page}/{size}")
    public ResponseEntity<?> getAllProductByCategory(@PathVariable(name = "id") Long id,
                                                     @PathVariable(name = "page") Integer page,
                                                     @PathVariable(name = "size") Integer size)
            throws MissingServletRequestParameterException, NotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        APIResponse<List<ProductDTO>> responseData = new APIResponse<>();
        Page<ProductDTO> productPage = service.getAllProductsByCategoryId(id, pageable);
        if (productPage.isEmpty()) {
            throw new NotFoundException(supplier(ProductEntity.class.getSimpleName()).get().getMessage());
        }
        responseData.setStatus(HttpStatus.OK.toString());
        responseData.setMessage("get all page successful");

        if (productPage != null && !CollectionUtils.isEmpty(productPage.getContent())) {
            responseData.setData(productPage.getContent());
            Pagination metaData = new Pagination(page, size, productPage.getTotalPages(),
                    productPage.getTotalElements());
            responseData.setPagination(metaData);
        } else {
            APIResponse<List<ProductDTO>> responseData1 = new APIResponse<>();
            return new ResponseEntity<>(responseData1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);

    }

}
