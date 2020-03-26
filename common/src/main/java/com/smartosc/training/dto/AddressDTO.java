package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO extends AbstractDTO{
	private Long id;
	private String address;
	private String phone;
	private Boolean gender;
	private int status;
	

	
}
