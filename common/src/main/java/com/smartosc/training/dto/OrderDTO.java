package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO extends AbstractDTO{
	
	private Long id;
	private String name;
	private Double totalPrice;
	private List<OrderDetailDTO> details;
	private UserDTO userDTOs;


}
