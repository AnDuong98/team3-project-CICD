package com.smartosc.training.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.OrderDTO;
import com.smartosc.training.service.impl.OrderServiceImpl;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

		@Autowired
		private OrderServiceImpl orderServiceImpl;
		
		@GetMapping()
		public ResponseEntity<Object> listOrder() {
			List<OrderDTO> orders = orderServiceImpl.getOrders();
			return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "done", orders), HttpStatus.OK);
		}
		
		@PostMapping
		public ResponseEntity<Object> create(@RequestBody OrderDTO orderDTO) {
			orderServiceImpl.createOrder(orderDTO);

			return new ResponseEntity<>(new APIResponse<OrderDTO>(HttpStatus.OK.value(), "Create Order success", orderDTO),
					HttpStatus.OK);
		}
	
}
