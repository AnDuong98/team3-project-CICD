package com.smartosc.training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author AnDuong
 *
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIResponse<T> {
	private String status;
	private String message;
	private T data;
	private Pagination pagination;

	public APIResponse(int status, String message) {
		this.status = Integer.toString(status);
		this.message = message;
	}

	public APIResponse(int status, String message, T data) {
		this.status = Integer.toString(status);
		this.message = message;
		this.data = data;
	}

	public APIResponse(String status, String message, Pagination pagination) {
		this.status = status;
		this.message = message;
		this.pagination = pagination;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
