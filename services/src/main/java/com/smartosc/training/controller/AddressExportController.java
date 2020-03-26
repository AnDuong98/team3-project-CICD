package com.smartosc.training.controller;


import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.AddressDTO;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.exception.NotFoundException;
import com.smartosc.training.export.AddressExportService;
import com.smartosc.training.repository.AddressRepository;
import com.smartosc.training.service.impl.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/address")
public class AddressExportController {
    @Autowired
    private AddressExportService addressExportService;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private AddressRepository addressRepository;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ResponseEntity<?> report() {
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Category is created successfully", addressExportService.generateReport()), HttpStatus.CREATED);
    }

    @PostMapping(value = "/import")
    public ResponseEntity<Object> createAddress(@RequestBody @Valid AddressDTO add)
            throws MethodArgumentNotValidException {
        addressService.createAddress(add);
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Address is created successfully", add), HttpStatus.OK);
    }

    @PostMapping(value = "/importTXT")
    public ResponseEntity<Object> createAddressTXT(@RequestBody @Valid AddressDTO add)
            throws MethodArgumentNotValidException {
        addressService.createAddress(add);
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Address is created successfully", add), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllAddresses() throws NotFoundException {
        Collection<AddressDTO> list = addressService.listAll();
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "okay", list), HttpStatus.OK);
    }
}
