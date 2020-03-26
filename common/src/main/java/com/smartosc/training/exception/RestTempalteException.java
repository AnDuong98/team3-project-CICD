package com.smartosc.training.exception;

/**
 * @author AnDuong
 */
public class RestTempalteException extends RuntimeException {
	
	private static final long serialVersionUID = -3242126885270683621L;

	public RestTempalteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestTempalteException(String message) {
		super(message);
	}

	public RestTempalteException(String message, Throwable cause) {
		super(message, cause);
	}

}
