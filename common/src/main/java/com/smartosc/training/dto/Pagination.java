package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagination {
	/**
	 * @author ThanhTTT
	 *
	 * */
	private int pageNumber;
	private int size;
	private int totalPages;
	private long totalElements;


}
