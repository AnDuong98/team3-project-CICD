package com.smartosc.training.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO extends AbstractDTO {
	private Long id;
	private String name;
	private String image;
	private String description;

}
