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
public class ProductDTO extends AbstractDTO{
	private Long id;
	private String name;
	private String image;
	private String description;
	private String price;
	private List<CategoryDTO> categoryDTOs;
	

	
	
	
}
