package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends AbstractDTO{

	private Long id;
	private String username;
	private String password;
	private String fullname;
	private String email;
	private int status;
	private AddressDTO address;
	private Long addressId;

}
