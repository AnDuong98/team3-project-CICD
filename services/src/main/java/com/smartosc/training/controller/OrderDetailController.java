package com.smartosc.training.controller;

import java.io.IOException;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import com.smartosc.training.service.impl.ExportReport;
import com.smartosc.training.service.impl.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.service.impl.OrderDetailServiceImpl;

@RestController
@RequestMapping(value = "/orderDetails")
public class OrderDetailController {

	private static final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

	@Autowired
	private OrderDetailServiceImpl orderDetailServiceImpl;

	@Autowired
	private ExportReport exportReport;

	@Autowired
	private FileStorageService fileStorageService;
	
	
	@GetMapping()
	@PermitAll
	public ResponseEntity<Object> listOrderDetail() {
		List<OrderDetailDTO> orderDetails = orderDetailServiceImpl.getOrderDetails();
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "done", orderDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "/create",method =  RequestMethod.POST)
	public ResponseEntity<Object> create(@RequestBody OrderDetailDTO orderDetailDTO) {
		orderDetailServiceImpl.createOrderDetail(orderDetailDTO);
		return new ResponseEntity<>(new APIResponse<OrderDetailDTO>(HttpStatus.OK.value(), "Create Order success", orderDetailDTO),
				HttpStatus.OK);
	}

	@GetMapping("/report/{fileName:.+}")
	public ResponseEntity<Resource> downloadReport(@PathVariable String fileName, HttpServletRequest request) throws Exception {

		exportReport.generateReport();

		Resource resource = fileStorageService.loadFileAsResource(fileName);
		String contentType = null;

		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType.toString()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}


}
