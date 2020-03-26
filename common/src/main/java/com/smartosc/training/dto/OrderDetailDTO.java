package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO extends AbstractDTO{
	private Long id;
	private int quantity;
	private String price;
	private OrderDTO order;
	private ProductDTO product;

}
