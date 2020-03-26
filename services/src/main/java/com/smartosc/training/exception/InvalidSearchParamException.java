package com.smartosc.training.exception;

import java.util.function.Supplier;

public class InvalidSearchParamException extends RuntimeException {

	private static final long serialVersionUID = -4493869365064754463L;
	
	public InvalidSearchParamException(String message) {
		super(message);
	}
	
	public static Supplier<InvalidSearchParamException> supplier(final String param, final Object value){
		return () -> new InvalidSearchParamException(String.format("Value '%s' is not valid for parameter '%s'", value, param));
	}

}
