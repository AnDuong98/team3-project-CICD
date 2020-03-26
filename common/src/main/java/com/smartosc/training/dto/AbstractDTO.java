package com.smartosc.training.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractDTO {
	private Date createdDate;
	private Date modifiedDate;
	private int status;

}